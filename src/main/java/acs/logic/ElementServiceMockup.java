package acs.logic;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acs.boundaries.ElementBoundary;
import acs.data.ElementEntity;
import acs.data.ElementEntityConverter;

//@Service
public class ElementServiceMockup implements ElementService {
	
	private Map<String, ElementEntity> database; 
	private ElementEntityConverter elementEntityConverter;
	private AtomicLong nextId;
	
	public ElementServiceMockup() {
		
	}
	
	@Autowired
	public void setEntityConverter(ElementEntityConverter elementEntityConverter) {
		this.elementEntityConverter = elementEntityConverter;
	}
	
	@PostConstruct
	public void init() {
		// create thread safe list
		this.database = Collections.synchronizedMap(new TreeMap<>()); // TODO sort by which key ?
		this.nextId = new AtomicLong(0L);
	}
	
	@Override
	public ElementBoundary create(String managerEmail, ElementBoundary newElement) {
		this.checkIfManagerEmailExist(managerEmail); 
		Long newId = nextId.incrementAndGet();
		
		newElement.getCreatedBy().put("managerEmail", managerEmail); // TODO  why we need setCreatedBy
		ElementEntity entity =
				this.elementEntityConverter
					.toEntity(newElement);
		
		entity.setCreatedTimeStamp(new Date());
		entity.setElementId(newId);
		
		this.database
			.put(newId.toString(), entity);
		
		return this.elementEntityConverter
				.convertFromEntity(entity);
	}

	@Override
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
		
		if(dirty) {
			this.database
			.put(
					this.elementEntityConverter.toEntityId(elementid),
					this.elementEntityConverter.toEntity(existeElement));
		}
		return existeElement;
	}

	@Override
	public List<ElementBoundary> getAll(String userEmail) {
		this.checkIfUserEmailExist(userEmail); // TODO check why this is userEmail and not adminEmail
		return this.database // Map of ElementEntity with Long key
				.values() // Collection ofElementEntity 
				.stream() // Stream of ElementEntity
				.map(entity->this.elementEntityConverter.convertFromEntity(entity)) // Stream of ElementBoundary
				.collect(Collectors.toList()); // List<ElementBoundary>
	}

	@Override
	public ElementBoundary getSpecificElement(String userEmail, String elementId) {
		this.checkIfUserEmailExist(userEmail); // TODO check why this is userEmail and not adminEmail
		ElementEntity entity =  this.database
				.get(this.elementEntityConverter.toEntityId(elementId));
		
		if (entity != null) {
			return
				this.elementEntityConverter
					.convertFromEntity(
						entity);
		} else {
			throw new EntityNotFoundException("could not find message for id: " + elementId);
		}
	}

	@Override
	public void deleteAllElements(String adminEmail) {
		this.checkIfManagerEmailExist(adminEmail); // TODO check why this is userEmail and not adminEmail
		this.database.clear();
	}
	
	public void checkIfManagerEmailExist(String adminEmail) {
		// TODO STUB
	}
	public void checkIfUserEmailExist(String userEmail) {
		// TODO STUB
	}


}
