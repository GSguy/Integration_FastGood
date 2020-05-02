package acs.logic;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
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

	private Map<Long, ActionEntity> database; 
	private ActionEntityConverter actionEntityConverter;
	private AtomicLong nextId;
		
	public ActionServicesMockup() {
		
	}
	
	@PostConstruct
	public void init() {
		// create thread safe list
		this.database = Collections.synchronizedMap(new TreeMap<>());
		this.nextId = new AtomicLong(0L);
	}
	
	@Autowired
	public void setActionEntityConverter(ActionEntityConverter actionEntityConverter) {
		this.actionEntityConverter = actionEntityConverter;
	}
	
	@Override
	public Object invokeAction(ActionBoundary action) {
		ActionEntity entity = this.actionEntityConverter.toEntity(action);
		// set Server fields
		entity.setActionID(nextId.incrementAndGet()); //create new ID - Not consider user ID INPUT
		entity.setCreatedTimeStamp( entity.getCreatedTimeStamp() != null ? entity.getCreatedTimeStamp() : new Date() );
		this.database.put(entity.getActionID(), entity);
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