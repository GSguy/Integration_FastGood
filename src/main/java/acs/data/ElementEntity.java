package acs.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import acs.boundaries.Location;

public class ElementEntity {

    private Long elementId;
    private String type;
    private String name;
    private Boolean active;
    private Date createdTimeStamp;
    private String createdBy; // MAP->String
    private Location location;
    private String elementAttributes; // MAP->String
    
    public ElementEntity() {

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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getElementAttributes() {
		return elementAttributes;
	}

	public void setElementAttributes(String elementAttributes) {
		this.elementAttributes = elementAttributes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getElementId() {
		return elementId;
	}

	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}
}
