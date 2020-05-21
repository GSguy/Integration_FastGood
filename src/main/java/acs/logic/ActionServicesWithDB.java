package acs.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import acs.boundaries.ActionBoundary;
import acs.dal.ActionDao;
import acs.dal.ElementDao;
import acs.dal.LastIdValue;
import acs.dal.LastValueDao;
import acs.dal.UserDao;
import acs.data.ActionEntity;
import acs.data.ActionEntityConverter;
import acs.data.ElementEntity;
import acs.data.UserEntity;
import acs.data.UserRole;

@Service
public class ActionServicesWithDB implements ActionServiceUpgraded {

	private ActionDao actionDao; // DAO = Data Access Object 
	private ActionEntityConverter actionEntityConverter;
	private LastValueDao lastValueDao;
	private UserDao userDao;
	private ElementDao elementDao;
	@Autowired
	public ActionServicesWithDB(ActionDao actionDao, LastValueDao lastValueDao,UserDao userDao,ElementDao elementDao) {
		this.actionDao = actionDao;
		this.lastValueDao = lastValueDao;
		this.userDao=userDao;
		this.elementDao=elementDao;
	}
	
	
	@Autowired
	public void setActionEntityConverter(ActionEntityConverter actionEntityConverter) {
		this.actionEntityConverter = actionEntityConverter;
	}
	
	
	@Override
	@Transactional //(readOnly = false)
	public Object invokeAction(ActionBoundary action) {
		String elementId=action.getElement().get("elementId");
		String email=action.getInvokedBy().get("email");
		if(elementId==null|| email==null) {
			throw new RuntimeException("cannot invoke action without element id or inovkedby");
		}
		
		UserEntity user=this.userDao.findOneByEmail(email);
		if(user==null) {
			throw new  EntityNotFoundException ("could not find any  user with  email : " + email);
		}
		if(user.getRole()!=UserRole.PLAYER) {
			throw new RuntimeException("only users with player role can invoke actions");
		}
		ElementEntity element=this.elementDao.findOneByElementIdAndActive(Long.parseLong(elementId), true);
		if(element==null) {
			throw new EntityNotFoundException("could not find active Element for id : " + elementId);
		}
		// create new tupple in the idValue table with a non-used id
		LastIdValue idValue = this.lastValueDao.save(new LastIdValue());
		ActionEntity entity = this.actionEntityConverter.toEntity(action);
		// set Server fields
		entity.setActionID(idValue.getLastId()); //create new ID - Not consider user ID INPUT
		this.lastValueDao.delete(idValue);// cleanup redundant data
		
		entity.setCreatedTimeStamp( entity.getCreatedTimeStamp() != null ? entity.getCreatedTimeStamp() : new Date() );
		
		entity = this.actionDao.save(entity); // UPSERT:  SELECT  -> UPDATE / INSERT
		
    	return this.actionEntityConverter.convertFromEntity(entity);
	}

	
	@Override
	@Transactional (readOnly = true) // have database handle race conditions
	public List<ActionBoundary> getAllActions(String adminEmail) {
		// ON INIT - create new Transaction
	
		if(!GlobalUtilites.checkIfAdminEmailExist(adminEmail, userDao)){ 
			throw new RuntimeException("User don't have ligit permissions");
		}
		
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
	@Transactional (readOnly = true) // have database handle race conditions
	public List<ActionBoundary> getAllActions(String adminEmail, int page, int size) {
		
		if(!GlobalUtilites.checkIfAdminEmailExist(adminEmail, userDao)){ 
			throw new RuntimeException("User don't have ligit permissions");
		}
			
			return this.actionDao.findAll(
					 PageRequest.of(page, size, Direction.ASC, "ActionID"))
					.getContent()
					.stream()
					.map(this.actionEntityConverter::convertFromEntity)
					.collect(Collectors.toList())
					;
			
	}
	
	@Override
	@Transactional //(readOnly = false)
	public void deleteAllActions(String adminEmail) {
		if(!GlobalUtilites.checkIfAdminEmailExist(adminEmail, userDao)){ 
			throw new RuntimeException("User don't have ligit permissions");
		}
		this.actionDao.deleteAll();
	}





}