package acs.data;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import acs.boundaries.ElementBoundary;

@Component
public class ElementEntityConverter {
	
	// Use Jackson for JSON marshalling/unmarshalling
	private ObjectMapper jackson;
	 
	
	@PostConstruct
	public void setup() {
		this.jackson = new ObjectMapper();
	}

	
	public ElementBoundary convertFromEntity (ElementEntity entity) {
		ElementBoundary boundary = new ElementBoundary();
		
		boundary.setActive(entity.getActive());
		boundary.setCreatedTimestamp(entity.getCreatedTimeStamp());
		
		if (entity.getElementId()!=null)
			boundary.setElementId(this.fromEntityId(entity.getElementId().toString()));
		else
			boundary.setElementId(null);
		
		boundary.setLocation(entity.getLocation());
		boundary.setName(entity.getName());
		boundary.setType(entity.getType());

		try {
			boundary.setCreatedBy(
					this.jackson.readValue(
							entity.getCreatedBy(), // JSON
							Map.class));
	
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			boundary.setElementAttributes(
					this.jackson.readValue(
							entity.getElementAttributes(), // JSON
							Map.class));
	
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return boundary;
	}
	
	
	public ElementEntity toEntity (ElementBoundary boundary) {
		
		ElementEntity entity = new ElementEntity();
		
		if ((boundary.getActive() == null)) throw new RuntimeException("Active param can't be null");
		entity.setActive(boundary.getActive());
		
		entity.setCreatedTimeStamp(boundary.getCreatedTimeStamp());
		entity.setLocation(boundary.getLocation());
		
		if (boundary.getElementId() != null)
			entity.setElementId(Long.parseLong(this.fromEntityId(boundary.getElementId())));
		else
			entity.setElementId(null);
		
		if (boundary.getName() == null || boundary.getName() == "") throw new RuntimeException("Element Name Cannot be null");
		entity.setName(boundary.getName());
		
		if (boundary.getType() == null) throw new RuntimeException("Element Type Cannot be null");
		entity.setType(boundary.getType());
				
		try {
			entity.setCreatedBy(
					this.jackson.writeValueAsString(boundary.getCreatedBy()));	
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			entity.setElementAttributes(
					this.jackson.writeValueAsString(boundary.getElementAttributes()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return entity;
	}
	 
	
	public String toEntityId(String id) {
		if (id != null) {
			return id;
		}else {
			return null;
		}
	}

	
	public String fromEntityId(String id) {
		if (id != null) {
			return id.toString();
		}else {
			return null;
		}
	}
	 
}
