package acs.data;

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
		boundary.setActionAttributes(entity.getActionAttributes());
		boundary.setElement(entity.getElement());
		boundary.setInvokedBy(entity.getInvokedBy());

		return boundary;
	}
	
	
	public ActionEntity toEntity (ActionBoundary boundary) {
		ActionEntity entity = new ActionEntity();
		
		entity.setActionAttributes(boundary.getActionAttributes());
		entity.setActionID(this.toEntityId(boundary.getActionID()));
		entity.setCreatedTimeStamp(boundary.getCreatedTimeStamp());
		entity.setType(boundary.getType());
		entity.setElement(boundary.getElement());
		entity.setInvokedBy(boundary.getInvokedBy());
		
		return entity;
	}
	
	public Long toEntityId(String id) {
		if (id != null) {
			return Long.parseLong(id);
		}else {
			return null;
		}
	}

	public String fromEntityId(Long id) {
		if (id != null) {
			return id.toString();
		}else {
			return null;
		}
	}
}