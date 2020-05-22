package acs.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.boundaries.ElementBoundary;
import acs.dal.ElementDao;
import acs.dal.LastIdValue;
import acs.dal.LastValueDao;
import acs.data.ElementEntity;
import acs.data.ElementEntityConverter;

@Service
public class ElementServiceWithDB implements RelationalElementService {
	
	private ElementDao elementDao; // DAO = Data Access Object 
	private ElementEntityConverter elementEntityConverter;
	private LastValueDao lastValueDao;
	
	@Autowired
	public ElementServiceWithDB(ElementDao elementDao, LastValueDao lastValueDao) {
		this.elementDao = elementDao;
		this.lastValueDao = lastValueDao;
	}
	
	@Autowired
	public void setEntityConverter(ElementEntityConverter elementEntityConverter) {
		this.elementEntityConverter = elementEntityConverter;
	}

	
	@Override
	@Transactional //(readOnly = false)
	public ElementBoundary create(String managerEmail, ElementBoundary newElement) {
		
		this.checkIfManagerEmailExist(managerEmail); // TODO complete this check
		
		if (managerEmail == null || managerEmail == "")
			throw new RuntimeException("Email not exist. New elements must have a creator email");
		else
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
	public ElementBoundary update(String mangerEmail, String elementid, ElementBoundary update) {
		this.checkIfManagerEmailExist(mangerEmail);
		
		ElementBoundary existeElement = this.getSpecificElement(mangerEmail, elementid);
		
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

		this.checkIfUserEmailExist(userEmail); // TODO check why this is userEmail and not adminEmail		
		List<ElementBoundary> rv = new ArrayList<>();
		
		// SELECT * FROM MESSAGES
		Iterable<ElementEntity> content = this.elementDao.findAll();
		
		for (ElementEntity msg : content) {
			rv.add(this.elementEntityConverter.convertFromEntity(msg));
		}
		
		// On SUCCESS - commit transaction
		// On Error - rollback transaction
		return rv;
	}

	@Override
	@Transactional (readOnly = true)
	public ElementBoundary getSpecificElement(String userEmail, String elementId) {
		this.checkIfUserEmailExist(userEmail); // TODO check why this is userEmail and not adminEmail
		
		// SELECT * FROM MESSAGES WHERE ID=? 
		Optional<ElementEntity> entity = this.elementDao.findById(Long.parseLong(elementId));
		
		if (entity.isPresent()) {
			return this.elementEntityConverter.convertFromEntity(entity.get());
		} else {
			throw new EntityNotFoundException("could not find message for id: " + elementId);
		}
	}

	@Override
	@Transactional //(readOnly = false)
	public void deleteAllElements(String adminEmail) {
		this.checkIfManagerEmailExist(adminEmail); // TODO check why this is userEmail and not adminEmail
		this.elementDao.deleteAll();
	}
	
	
	@Override
	@Transactional
	public void addElementToParent(String parentId, String childrenId,String managerEmail) {
		this.checkIfManagerEmailExist(managerEmail);
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
	public Set<ElementBoundary> getChildrens(String parentId,String userEmail) {
		this.checkIfUserEmailExist(userEmail);
		ElementEntity parent = this.elementDao
				.findById(Long.parseLong(this.elementEntityConverter.toEntityId(parentId)))
				.orElseThrow(()->new EntityNotFoundException("could not find parent element with id: " + parentId));

		return parent
				.getChildrens()
				.stream() 
				.map(this.elementEntityConverter::convertFromEntity) 
				.collect(Collectors.toSet());
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<ElementBoundary> getParents(String childrenId,String userEmail) {
		this.checkIfUserEmailExist(userEmail);
		ElementEntity children = this.elementDao
				.findById(Long.parseLong(this.elementEntityConverter.toEntityId(childrenId)))
				.orElseThrow(()->new EntityNotFoundException ("could not find  element with id: " + childrenId));
		
		return children
				.getParents()
				.stream() 
				.map(this.elementEntityConverter::convertFromEntity) //
				.collect(Collectors.toSet());
	}
	
	public Boolean checkIfManagerEmailExist(String adminEmail) {
		return true;// TODO STUB
		//else throw new  EntityNotFoundException ("could not find any  user with  email : " + adminEmail);
	}
	public Boolean checkIfUserEmailExist(String userEmail) {
		return true; // TODO STUB	
		//else throw new  EntityNotFoundException ("could not find any  user with  email : " + userEmail);
	}

}
