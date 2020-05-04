package acs.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.boundaries.ElementBoundary;
import acs.dal.ActionDao;
import acs.dal.ElementDao;
import acs.data.ElementEntity;
import acs.data.ElementEntityConverter;

@Service
public class ElementServiceWithDB implements ElementService {
	
	private ElementDao elementDao; // DAO = Data Access Object 
	private ElementEntityConverter elementEntityConverter;
	
	@Autowired
	public ElementServiceWithDB(ElementDao elementDao) {
		this.elementDao = elementDao;
	}
	
	@Autowired
	public void setEntityConverter(ElementEntityConverter elementEntityConverter) {
		this.elementEntityConverter = elementEntityConverter;
	}

	
	@Override
	@Transactional //(readOnly = false)
	public ElementBoundary create(String managerEmail, ElementBoundary newElement) {
		this.checkIfManagerEmailExist(managerEmail); // TODO complete this check
		
		newElement.getCreatedBy().put("managerEmail", managerEmail); // TODO  why we need setCreatedBy
		ElementEntity entity = this.elementEntityConverter.toEntity(newElement);
		
		String newId = UUID.randomUUID().toString();

		entity.setCreatedTimeStamp(new Date());
		entity.setElementId(newId);
		
		entity = this.elementDao.save(entity); // UPSERT:  SELECT  -> UPDATE / INSERT
		
		return this.elementEntityConverter.convertFromEntity(entity);
	}

	@Override
	@Transactional //(readOnly = false)
	public ElementBoundary update(String mangerEmail, String elementid, ElementBoundary update) {
		this.checkIfManagerEmailExist(mangerEmail);
		
		ElementBoundary existeElement = this.getSpecificElement(mangerEmail, elementid);
		boolean dirty = false;
		
		if(update.getActive() != null) {
			existeElement.setActive(update.getActive());
			dirty = true;
		}
		if(update.getElementAttributes() != null) {
			existeElement.setElementAttributes(update.getElementAttributes());
			dirty = true;
		}
		if(update.getLocation() != null) {
			existeElement.setLocation(update.getLocation());
			dirty = true;
		}
		if(update.getName() != null) {
			existeElement.setName(update.getName());
			dirty = true;
		}
		if(update.getType() != null) {
			existeElement.setType(update.getType());
			dirty = true;
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
		Optional<ElementEntity> entity = this.elementDao.findById(elementId);
		
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
	
	public void checkIfManagerEmailExist(String adminEmail) {
		// TODO STUB
	}
	public void checkIfUserEmailExist(String userEmail) {
		// TODO STUB
	}

}
