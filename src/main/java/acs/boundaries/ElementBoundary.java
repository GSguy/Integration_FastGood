package acs.boundaries;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ElementBoundary {
	
    private String elementId;
    private String type;
    private String name;
    private Boolean active;
    private Date createdTimeStamp;
    private Map<String,String> createdBy;
    private Location location;
    private Map<String,Object> elementAttributes;
    
    public ElementBoundary() {
		super();
    	createdBy = new HashMap<String,String>();
    	elementAttributes = new HashMap<String,Object>();
	}

	public ElementBoundary(String managerEmail, String elementId) {
	    this(); //call empty constructor
	    setElementId(elementId);
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

	public void setCreatedBy(Map<String, String> createdBy) {
		this.createdBy = createdBy;
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

	public void setElementAttributes(Map<String, Object> elementAttributes) {
		this.elementAttributes = elementAttributes;
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

	@Override
	public String toString() {
		return "ElementBoundary{" +
				"elementId='" + elementId + '\'' +
				", type='" + type + '\'' +
				", name='" + name + '\'' +
				", active=" + active +
				", createdTimeStamp=" + createdTimeStamp +
				", createdBy=" + createdBy +
				", location=" + location +
				", elementAttributes=" + elementAttributes +
				'}';
	}
}
