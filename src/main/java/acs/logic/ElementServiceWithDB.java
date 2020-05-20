package acs.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.boundaries.ElementBoundary;
import acs.dal.ElementDao;
import acs.dal.LastIdValue;
import acs.dal.LastValueDao;
import acs.dal.UserDao;
import acs.data.ElementEntity;
import acs.data.ElementEntityConverter;
import acs.data.UserEntity;
import acs.data.UserRole;

@Service
public class ElementServiceWithDB implements ElementServiceRelational {
	
	private ElementDao elementDao; // DAO = Data Access Object 
	private ElementEntityConverter elementEntityConverter;
	private LastValueDao lastValueDao;
	private UserDao userDao;
	@Autowired
	public ElementServiceWithDB(ElementDao elementDao, LastValueDao lastValueDao,UserDao userDao) {
		this.elementDao = elementDao;
		this.lastValueDao = lastValueDao;
		this.userDao=userDao;
	}
	
	@Autowired
	public void setEntityConverter(ElementEntityConverter elementEntityConverter) {
		this.elementEntityConverter = elementEntityConverter;
	}

	
	@Override
	@Transactional //(readOnly = false)
	public ElementBoundary create(String managerEmail, ElementBoundary newElement) {
		checkIfUserEmailExist(managerEmail);
		if(!checkIfManagerEmailExist(managerEmail)){ 
			throw new RuntimeException("you don't have permission to create new element");
		}

		
		newElement.getCreatedBy().put("email", managerEmail); 

		ElementEntity entity = this.elementEntityConverter.toEntity(newElement);
		
		// create new tupple in the idValue table with a non-used id
		LastIdValue idValue = this.lastValueDao.save(new LastIdValue());

		entity.setElementId(idValue.getLastId()); // use newly generated id
		this.lastValueDao.delete(idValue); // cleanup redundant data
		
		entity.setCreatedTimeStamp(new Date());


		entity = this.elementDao.save(entity); // UPSERT:  SELECT  -> UPDATE / INSERT
		
		return this.elementEntityConverter.convertFromEntity(entity);
	}

	@Override
	@Transactional //(readOnly = false)
	public ElementBoundary update(String managerEmail, String elementid, ElementBoundary update) {
		checkIfUserEmailExist(managerEmail);

		if(!checkIfManagerEmailExist(managerEmail)){ 
			throw new RuntimeException("you don't have permission to update  element");
		}
		
		ElementBoundary existeElement = this.getSpecificElement(managerEmail, elementid);
		
		if(update.getActive() != null) {
			existeElement.setActive(update.getActive());
		}
		if(update.getElementAttributes() != null) {
			existeElement.setElementAttributes(update.getElementAttributes());
		}
		if(update.getLocation() != null) {
			existeElement.setLocation(update.getLocation());
		}
		if(update.getName() != null) {
			existeElement.setName(update.getName());
		}
		if(update.getType() != null) {
			existeElement.setType(update.getType());
		}
		
		this.elementDao.save(this.elementEntityConverter.toEntity(existeElement));
		
		return existeElement;
	}

	@Override
	@Transactional (readOnly = true)
	public List<ElementBoundary> getAll(String userEmail) {
		
		checkIfUserEmailExist(userEmail);
		Iterable<ElementEntity> content;
		List<ElementBoundary> rv = new ArrayList<>();
		if(checkIfManagerEmailExist(userEmail)) {
		 content = this.elementDao.findAll();
		}
		else {
			content=this.elementDao.findAllByActive(true);
		}
		for (ElementEntity msg : content) {
			rv.add(this.elementEntityConverter.convertFromEntity(msg));
		}
		
		// On SUCCESS - commit transaction
		// On Error - rollback transaction
		return rv;
	}
	
	@Override
	@Transactional (readOnly = true) //oshri
	public List<ElementBoundary> getAll(String userEmail, int size, int page) {
		checkIfUserEmailExist(userEmail); 	
		
		return this.elementDao.findAll(
				 PageRequest.of(page, size, Direction.ASC, "elementId"))
				.getContent()
				.stream()
				.map(this.elementEntityConverter::convertFromEntity)
				.collect(Collectors.toList())
				;
	}
	
	@Override
	@Transactional (readOnly = true)
	public ElementBoundary getSpecificElement(String userEmail, String elementId) {
		checkIfUserEmailExist(userEmail); 		
		// SELECT * FROM MESSAGES WHERE ID=? 
		Optional<ElementEntity> OptionalEntity;
		ElementEntity entity = null;
		if(checkIfManagerEmailExist(userEmail)) {
			OptionalEntity = this.elementDao.findById(Long.parseLong(elementId));
			if (OptionalEntity.isPresent()) {
				entity=OptionalEntity.get();
			}
		}
		else {
			entity = this.elementDao.findOneByElementIdAndActive(Long.parseLong(elementId),true);

		}
		if (entity!=null) {
			return this.elementEntityConverter.convertFromEntity(entity);
		} else {
			throw new EntityNotFoundException("could not find Element for id: " + elementId);
		}
	}

	@Override
	@Transactional //(readOnly = false)
	public void deleteAllElements(String adminEmail) {
		checkIfUserEmailExist(adminEmail); 		
		checkIfManagerEmailExist(adminEmail); 
		this.elementDao.deleteAll();
	}
	
	
	@Override
	@Transactional
	public void addElementToParent(String parentId, String childrenId,String managerEmail) {
		checkIfUserEmailExist(managerEmail); 		
		checkIfManagerEmailExist(managerEmail);
		if (parentId != null && parentId.equals(childrenId)) {
			throw new RuntimeException("elements cannot add themselves");
		}
		
		ElementEntity parent = this.elementDao
			.findById(Long.parseLong(this.elementEntityConverter.toEntityId(parentId)))
			.orElseThrow(()->new EntityNotFoundException("could not find parent element with id: " + parentId));

		ElementEntity children = this.elementDao
				.findById(Long.parseLong(this.elementEntityConverter.toEntityId(childrenId)))
				.orElseThrow(()->new EntityNotFoundException ("could not find  children element with id: " + childrenId));

		
		parent.addChildren(children);
		
		this.elementDao.save(parent);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getChildrens(String parentId,String userEmail, int size, int page) {
		checkIfUserEmailExist(userEmail);
		ElementEntity parent = this.elementDao
				.findById(Long.parseLong(this.elementEntityConverter.toEntityId(parentId)))
				.orElseThrow(()->new EntityNotFoundException("could not find parent element with id: " + parentId));
			
		/*return this.elementDao.
				findByChildrensId(parent.getElementId(),
				PageRequest.of(page, size, Direction.ASC, "elementId"))
				//.getContent()
				.stream() 
				.map(this.elementEntityConverter::convertFromEntity) 
				.collect(Collectors.toList()); */
		return null;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getParents(String childrenId,String userEmail, int size, int page) {
		checkIfUserEmailExist(userEmail);
		ElementEntity children = this.elementDao
				.findById(Long.parseLong(this.elementEntityConverter.toEntityId(childrenId)))
				.orElseThrow(()->new EntityNotFoundException ("could not find  element with id: " + childrenId));
	
		return null;
	/*	return this.elementDao.
				findByParentsId(children.getElementId(),
				 PageRequest.of(page, size, Direction.ASC, "elementId"))
				//.getContent()
				.stream() 
				.map(this.elementEntityConverter::convertFromEntity) 
				.collect(Collectors.toList());

*/
	}
	
	public Boolean checkIfManagerEmailExist(String adminEmail) {
		UserEntity user=this.userDao.findOneByEmail(adminEmail);
		if(user.getRole()==UserRole.MANAGER) {
		return true;
		}
		else{
			return false;
		}
		
	}
	public Boolean checkIfUserEmailExist(String userEmail) {
		UserEntity user=this.userDao.findOneByEmail(userEmail);
		if(user==null) {
			throw new  EntityNotFoundException ("could not find any  user with  email : " + userEmail);
		}
		else return true;
		

		}

}
