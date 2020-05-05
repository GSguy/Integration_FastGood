package acs.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.boundaries.ActionBoundary;
import acs.dal.ActionDao;
import acs.data.ActionEntity;
import acs.data.ActionEntityConverter;

@Service
public class ActionServicesWithDB implements ActionService {

	private ActionDao actionDao; // DAO = Data Access Object 
	private ActionEntityConverter actionEntityConverter;
		
	@Autowired
	public ActionServicesWithDB(ActionDao actionDao) {
		this.actionDao = actionDao;
	}
	
	@Autowired
	public void setActionEntityConverter(ActionEntityConverter actionEntityConverter) {
		this.actionEntityConverter = actionEntityConverter;
	}
	
	@Override
	@Transactional //(readOnly = false)
	public Object invokeAction(ActionBoundary action) {
		ActionEntity entity = this.actionEntityConverter.toEntity(action);
		
		String newId = UUID.randomUUID().toString();
		
		// set Server fields
		entity.setActionID(newId); //create new ID - Not consider user ID INPUT
		entity.setCreatedTimeStamp( entity.getCreatedTimeStamp() != null ? entity.getCreatedTimeStamp() : new Date() );
		
		entity = this.actionDao.save(entity); // UPSERT:  SELECT  -> UPDATE / INSERT
    	return this.actionEntityConverter.convertFromEntity(entity);
	}

	@Override
	@Transactional (readOnly = true) // have database handle race conditions
	public List<ActionBoundary> getAllActions(String adminEmail) {
		// ON INIT - create new Transaction
	
		checkAdminEmailIsExist(adminEmail); // TODO complete this check
		
		List<ActionBoundary> rv = new ArrayList<>();
		
		// SELECT * FROM MESSAGES
		Iterable<ActionEntity> content = this.actionDao.findAll();
		for (ActionEntity msg : content) {
			rv.add(this.actionEntityConverter.convertFromEntity(msg));
		}
			
		// On SUCCESS - commit transaction
		// On Error - rollback transaction
		return rv;
		
	}

	@Override
	@Transactional //(readOnly = false)
	public void deleteAllActions(String adminEmail) {
		checkAdminEmailIsExist(adminEmail); // TODO complete this check
		this.actionDao.deleteAll();
	}

	private void checkAdminEmailIsExist(String adminEmail) {
		// TODO to check if admin email is exist
	}

}