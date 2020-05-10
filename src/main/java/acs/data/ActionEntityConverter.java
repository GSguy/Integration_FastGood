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
		boundary.setCreatedTimestamp(entity.getCreatedTimeStamp());
		boundary.setType(entity.getType());
		
		if (entity.getActionID() != null)
			boundary.setActionId(this.fromEntityId(entity.getActionID().toString()));
		else
			boundary.setActionId(null);
	
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
		
		entity.setCreatedTimeStamp(boundary.getCreatedTimestamp());
		
		if(boundary.getActionId()!=null)
			entity.setActionID(Long.parseLong(this.toEntityId(boundary.getActionId())));
		else
			entity.setActionID(null);
		
		if (boundary.getType() == null) throw new RuntimeException("Action Type Cannot be null");
		entity.setType(boundary.getType());
				
		// marshalling
		if (boundary.getActionAttributes().size() == 0) throw new RuntimeException("Action Attributes Cannot be empty");
		try {
			entity.setActionAttributes(
					this.jackson
						.writeValueAsString(
								boundary.getActionAttributes()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		if (boundary.getElement().size() == 0) throw new RuntimeException("Action element Cannot be empty");
		// marshalling
		try {
			entity.setElement(
					this.jackson
						.writeValueAsString(
								boundary.getElement()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		if (boundary.getInvokedBy().size() == 0) throw new RuntimeException("Action invokedBy Cannot be empty");
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
			return id;
		}else {
			return null;
		}
	}
	
}