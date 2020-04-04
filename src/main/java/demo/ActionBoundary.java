package demo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActionBoundary {
	
	private String actionID;
	private String type;
	private Map<String, String> element = new HashMap<String,String>();;
	private Date createdTimeStamp;
	private Map<String, String> invokedBy = new HashMap<String,String>();;
    private Map<String,Object> actionAttributes = new HashMap<String,Object>();;
    
	public ActionBoundary() {
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

	public void setInvokedBy(String invokedBy) {
		this.invokedBy.put("email", invokedBy);
	}

	public Map<String, Object> getActionAttributes() {
		return actionAttributes;
	}

	public void setActionAttributes(String key ,Object value) {
		this.actionAttributes.put(key, value);
	}

	public String getActionID() {
		return actionID;
	}

	public void setActionID(String actionID) {
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

	public void setElement(String element) {
		this.element.put("elementId", element);
	}
	
}