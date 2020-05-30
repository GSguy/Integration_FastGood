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
	public ElementServiceWithDB(ElementDao elementDao, LastValueDao lastValueDao, UserDao userDao) {
		this.elementDao = elementDao;
		this.lastValueDao = lastValueDao;
		this.userDao = userDao;
	}

	@Autowired
	public void setEntityConverter(ElementEntityConverter elementEntityConverter) {
		this.elementEntityConverter = elementEntityConverter;
	}

	@Override
	@Transactional // (readOnly = false)
	public ElementBoundary create(String managerEmail, ElementBoundary newElement) {

		if (!GlobalUtilites.checkIfManagerEmailExist(managerEmail, userDao)) {
			throw new RuntimeException("User don't have ligit permissions");
		}

		newElement.getCreatedBy().put("email", managerEmail);
		ElementEntity entity = this.elementEntityConverter.toEntity(newElement);

		// create new tupple in the idValue table with a non-used id
		LastIdValue idValue = this.lastValueDao.save(new LastIdValue());

		entity.setElementId(idValue.getLastId()); // use newly generated id
		this.lastValueDao.delete(idValue); // cleanup redundant data

		entity.setCreatedTimeStamp(new Date());

		entity = this.elementDao.save(entity); // UPSERT: SELECT -> UPDATE / INSERT

		return this.elementEntityConverter.convertFromEntity(entity);
	}

	@Override
	@Transactional // (readOnly = false)
	public ElementBoundary update(String managerEmail, String elementid, ElementBoundary update) {

		if (!GlobalUtilites.checkIfManagerEmailExist(managerEmail, userDao)) {
			throw new RuntimeException("User don't have ligit permissions");
		}

		ElementBoundary existeElement = this.getSpecificElement(managerEmail, elementid);

		if (update.getActive() != null) {
			existeElement.setActive(update.getActive());
		}
		if (update.getElementAttributes() != null) {
			existeElement.setElementAttributes(update.getElementAttributes());
		}
		if (update.getLocation() != null) {
			existeElement.setLocation(update.getLocation());
		}
		if (update.getName() != null) {
			existeElement.setName(update.getName());
		}
		if (update.getType() != null) {
			existeElement.setType(update.getType());
		}

		this.elementDao.save(this.elementEntityConverter.toEntity(existeElement));

		return existeElement;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAll(String userEmail) {

		GlobalUtilites.checkIfUserEmailExistWithError(userEmail, userDao);
		Iterable<ElementEntity> content;
		List<ElementBoundary> rv = new ArrayList<>();
		if (GlobalUtilites.checkIfAdminEmailExist(userEmail, userDao)) {
			content = this.elementDao.findAll();
		} else {
			content = this.elementDao.findAllByActive(true);
		}
		for (ElementEntity msg : content) {
			rv.add(this.elementEntityConverter.convertFromEntity(msg));
		}

		// On SUCCESS - commit transaction
		// On Error - rollback transaction
		return rv;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getAll(String userEmail, int page, int size) {
		GlobalUtilites.checkIfUserEmailExistWithError(userEmail, userDao);

		if (GlobalUtilites.checkIfAdminEmailExist(userEmail, userDao)) {
			return this.elementDao.findAll(PageRequest.of(page, size, Direction.ASC, "elementId")).getContent().stream()
					.map(this.elementEntityConverter::convertFromEntity).collect(Collectors.toList());
		} else { // user permissions
			return this.elementDao.findAllByActive(true, PageRequest.of(page, size, Direction.ASC, "elementId"))
					.getContent().stream().map(this.elementEntityConverter::convertFromEntity)
					.collect(Collectors.toList());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ElementBoundary getSpecificElement(String userEmail, String elementId) {
		GlobalUtilites.checkIfUserEmailExistWithError(userEmail, userDao);
		// SELECT * FROM MESSAGES WHERE ID=?
		Optional<ElementEntity> OptionalEntity;
		ElementEntity entity = null;
		if (GlobalUtilites.checkIfAdminEmailExist(userEmail, userDao)) {
			OptionalEntity = this.elementDao.findById(Long.parseLong(elementId));
			if (OptionalEntity.isPresent()) {
				entity = OptionalEntity.get();
			}
		} else {
			entity = this.elementDao.findOneByElementIdAndActive(Long.parseLong(elementId), true);

		}
		if (entity != null) {
			return this.elementEntityConverter.convertFromEntity(entity);
		} else {
			throw new EntityNotFoundException("could not find Element for id: " + elementId);
		}
	}

	@Override
	@Transactional // (readOnly = false)
	public void deleteAllElements(String adminEmail) {
		GlobalUtilites.checkIfUserEmailExistWithError(adminEmail, userDao);
		GlobalUtilites.checkIfAdminEmailExist(adminEmail, userDao);
		this.elementDao.deleteAll();
	}

	@Override
	@Transactional // (readOnly = false)
	public void addElementToParent(String parentId, String childrenId, String managerEmail) {
		GlobalUtilites.checkIfManagerEmailExist(managerEmail, userDao);
		if (parentId != null && parentId.equals(childrenId)) {
			throw new RuntimeException("elements cannot add themselves");
		}

		ElementEntity parent = this.elementDao
				.findById(Long.parseLong(this.elementEntityConverter.toEntityId(parentId)))
				.orElseThrow(() -> new EntityNotFoundException("could not find parent element with id: " + parentId));

		ElementEntity children = this.elementDao
				.findById(Long.parseLong(this.elementEntityConverter.toEntityId(childrenId))).orElseThrow(
						() -> new EntityNotFoundException("could not find  children element with id: " + childrenId));

		parent.addChildren(children);

		this.elementDao.save(parent);
	}

	@Override
	@Transactional(readOnly = true)
	public Set<ElementBoundary> getChildrens(String parentId, String userEmail) {
		GlobalUtilites.checkIfUserEmailExistWithError(userEmail, userDao);
		ElementEntity parent = this.elementDao
				.findById(Long.parseLong(this.elementEntityConverter.toEntityId(parentId)))
				.orElseThrow(() -> new EntityNotFoundException("could not find parent element with id: " + parentId));

		return parent.getChildrens().stream().map(this.elementEntityConverter::convertFromEntity)
				.collect(Collectors.toSet());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getChildrens(String parentId, String userEmail, int page, int size) {
		GlobalUtilites.checkIfUserEmailExistWithError(userEmail, userDao);
		ElementEntity parent = this.elementDao
				.findById(Long.parseLong(this.elementEntityConverter.toEntityId(parentId)))
				.orElseThrow(() -> new EntityNotFoundException("could not find parent element with id: " + parentId));

		if (GlobalUtilites.checkIfAdminEmailExist(userEmail, userDao)) {
			return this.elementDao
					.findByChildrensElementId(parent.getElementId(),
							PageRequest.of(page, size, Direction.ASC, "elementId"))
					// .getContent()
					.stream().map(this.elementEntityConverter::convertFromEntity).collect(Collectors.toList());
		} else {
			return this.elementDao
					.findByChildrensElementIdAndActive(parent.getElementId(), true,
							PageRequest.of(page, size, Direction.ASC, "elementId"))
					// .getContent()
					.stream().map(this.elementEntityConverter::convertFromEntity).collect(Collectors.toList());

		}
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<ElementBoundary> getParents(String childrenId, String userEmail) {
		GlobalUtilites.checkIfUserEmailExistWithError(userEmail, userDao);
		ElementEntity children = this.elementDao
				.findById(Long.parseLong(this.elementEntityConverter.toEntityId(childrenId)))
				.orElseThrow(() -> new EntityNotFoundException("could not find  element with id: " + childrenId));

		return children.getParents().stream().map(this.elementEntityConverter::convertFromEntity) //
				.collect(Collectors.toSet());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementBoundary> getParents(String childrenId, String userEmail, int page, int size) {
		ElementEntity children = this.elementDao
				.findById(Long.parseLong(this.elementEntityConverter.toEntityId(childrenId)))
				.orElseThrow(() -> new EntityNotFoundException("could not find  element with id: " + childrenId));
		if (GlobalUtilites.checkIfAdminEmailExist(userEmail, userDao)) {
			return this.elementDao
					.findByParentsElementId(children.getElementId(),
							PageRequest.of(page, size, Direction.ASC, "elementId"))
					.getContent().stream().map(this.elementEntityConverter::convertFromEntity)
					.collect(Collectors.toList());
		} else {
			return this.elementDao
					.findByParentsElementIdAndActive(children.getElementId(), true,
							PageRequest.of(page, size, Direction.ASC, "elementId"))
					.getContent().stream().map(this.elementEntityConverter::convertFromEntity)
					.collect(Collectors.toList());
		}

	}

	@Override
	public List<ElementBoundary> getElementsByType(String type, String userEmail, int page, int size) {
		GlobalUtilites.checkIfUserEmailExistWithError(userEmail, userDao);
		if (GlobalUtilites.checkIfAdminEmailExist(userEmail, userDao)) {
			return this.elementDao.findByType(type, PageRequest.of(page, size, Direction.ASC, "elementId")).getContent()
					.stream().map(this.elementEntityConverter::convertFromEntity).collect(Collectors.toList());
		} else { // user permissions
			return this.elementDao
					.findByTypeAndActive(type, true, PageRequest.of(page, size, Direction.ASC, "elementId"))
					.getContent().stream().map(this.elementEntityConverter::convertFromEntity)
					.collect(Collectors.toList());
		}
	}

	@Override
	public List<ElementBoundary> getElementsByName(String name, String userEmail, int page, int size) {
		GlobalUtilites.checkIfUserEmailExistWithError(userEmail, userDao);
		if (GlobalUtilites.checkIfAdminEmailExist(userEmail, userDao)) {
			return this.elementDao.findByName(name, PageRequest.of(page, size, Direction.ASC, "elementId")).getContent()
					.stream().map(this.elementEntityConverter::convertFromEntity).collect(Collectors.toList());
		} else { // user permissions
			return this.elementDao
					.findByNameAndActive(name, true, PageRequest.of(page, size, Direction.ASC, "elementId"))
					.getContent().stream().map(this.elementEntityConverter::convertFromEntity)
					.collect(Collectors.toList());
		}
	}

}
