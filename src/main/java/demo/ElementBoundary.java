package demo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ElementBoundary {
	
    private String elementId;
    private String type;
    private String name;
    private Boolean active;
    private Date createdTimeStamp;
    private HashMap<String,String> createdBy = new HashMap<String,String>();
    private Location location;
    private HashMap<String,Object> elementAttributes = new HashMap<String,Object>();
    
    public ElementBoundary() {
	}

	public ElementBoundary(String managerEmail, String elementId) {
	    super();
	    setElementId(elementId);
	    setCreatedBy(managerEmail);
	}
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public Map<String, String> getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy.put("email", createdBy);
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Map<String, Object> getElementAttributes() {
		return elementAttributes;
	}

	public void setElementAttributes(String key ,Object value) {
		this.elementAttributes.put(key, value);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

}
