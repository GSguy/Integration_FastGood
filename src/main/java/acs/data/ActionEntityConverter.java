package acs.data;

import java.util.Map;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import acs.boundaries.ActionBoundary;

@Component
public class ActionEntityConverter {
	
	private ObjectMapper jackson; 	// Use Jackson for JSON marshalling/unmarshalling
	 
	@PostConstruct
	public void setup() {
		this.jackson = new ObjectMapper();
	}

	
	public ActionBoundary convertFromEntity (ActionEntity entity) {
		ActionBoundary boundary = new ActionBoundary();
		boundary.setCreatedTimeStamp(entity.getCreatedTimeStamp());
		boundary.setActionID(this.fromEntityId(entity.getActionID()));
		boundary.setType(entity.getType());

		// unmarshalling
		try {
			boundary.setActionAttributes(
					this.jackson.readValue(
							entity.getActionAttributes(),
							Map.class)
							);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		// unmarshalling
		try {
			boundary.setElement(
					this.jackson.readValue(
							entity.getElement(),
							Map.class)
							);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		// unmarshalling
		try {
			boundary.setInvokedBy(
					this.jackson.readValue(
							entity.getInvokedBy(),
							Map.class)
							);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		return boundary;
	}
	
	
	public ActionEntity toEntity (ActionBoundary boundary) {
		ActionEntity entity = new ActionEntity();
		
		entity.setActionID(this.toEntityId(boundary.getActionID()));
		entity.setCreatedTimeStamp(boundary.getCreatedTimeStamp());
		entity.setType(boundary.getType());
				
		// marshalling
		try {
			entity.setActionAttributes(
					this.jackson
						.writeValueAsString(
								boundary.getActionAttributes()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		// marshalling
		try {
			entity.setElement(
					this.jackson
						.writeValueAsString(
								boundary.getElement()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		// marshalling
		try {
			entity.setInvokedBy(
					this.jackson
						.writeValueAsString(
								boundary.getInvokedBy()));
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