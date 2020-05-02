package acs.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActionEntity {
	
	private Long actionID;
	private String type;
	private Map<String, String> element;
	private Date createdTimeStamp;
	private Map<String, String> invokedBy;
    private Map<String, Object> actionAttributes;
    
	public ActionEntity() {
		element = new HashMap<String,String>();
		invokedBy = new HashMap<String,String>();
		actionAttributes = new HashMap<String,Object>();
	}

	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public Map<String, String> getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(Map<String, String> invokedBy) {
		this.invokedBy = invokedBy;
	}

	public Map<String, Object> getActionAttributes() {
		return actionAttributes;
	}

	public void setActionAttributes(Map<String,Object> actionAttributes) {
		this.actionAttributes = actionAttributes;
	}

	public Long getActionID() {
		return actionID;
	}

	public void setActionID(Long actionID) {
		this.actionID = actionID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public Map<String, String> getElement() {
		return element;
	}

	public void setElement(Map<String, String> element) {
		this.element = element;
	}
	
}