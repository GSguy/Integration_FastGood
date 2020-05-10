package acs.boundaries;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActionBoundary {
	
	private String actionId;
	private String type;
	private Map<String, String> element;
	private Date createdTimestamp;
	private Map<String, String> invokedBy;
    private Map<String,Object> actionAttributes;
    
	public ActionBoundary() {
		super();
		element = new HashMap<String,String>();
		invokedBy = new HashMap<String,String>();
		actionAttributes = new HashMap<String,Object>();
	}
	

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimeStamp) {
		this.createdTimestamp = createdTimeStamp;
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

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionID) {
		this.actionId = actionID;
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