package acs.logic;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acs.boundaries.ActionBoundary;
import acs.data.ActionEntity;
import acs.data.ActionEntityConverter;

@Service
public class ActionServicesMockup implements ActionService {

	private Map<String, ActionEntity> database; 
	private ActionEntityConverter actionEntityConverter;
	private AtomicReference<String> actionId;
		
	public ActionServicesMockup() {
		
	}
	
	@PostConstruct
	public void init() {
		// create thread safe list
		this.database = Collections.synchronizedMap(new TreeMap<>());
		this.actionId = new AtomicReference<String>();
	}
	
	@Autowired
	public void setActionEntityConverter(ActionEntityConverter actionEntityConverter) {
		this.actionEntityConverter = actionEntityConverter;
	}
	
	@Override
	public Object invokeAction(ActionBoundary action) {
		// TODO Auto-generated method stub
		ActionEntity entity = this.actionEntityConverter.toEntity(action);
		
		if (entity.getActionID() == "" || entity.getActionID() == null)
			entity.setActionID(actionId.get()); //create new ID
		else
			entity.setActionID(action.getActionID());
		
		entity.setCreatedTimeStamp( entity.getCreatedTimeStamp() != null ? entity.getCreatedTimeStamp() : new Date() );
		
		//to fix:
		//if (this.database.get(entity.getActionID()) == null)
		//	this.database.put(entity.getActionID(), entity);
    		
    	return this.actionEntityConverter.convertFromEntity(entity);
	}

	@Override
	public List<ActionBoundary> getAllActions(String adminEmail) {
		checkAdminEmailIsExist(adminEmail);
		return this.database // Map of ActionEntity with String key
				.values() // Collection ofElementEntity 
				.stream() // Stream of ElementEntity
				.map(entity->this.actionEntityConverter.convertFromEntity(entity)) // Stream of ElementBoundary
				.collect(Collectors.toList()); // List<ElementBoundary>
	}

	@Override
	public void deleteAllActions(String adminEmail) {
		checkAdminEmailIsExist(adminEmail);
		this.database.clear();
	}

	private void checkAdminEmailIsExist(String adminEmail) {
		// TODO to check if admin email is exist
	}

}