package acs.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Elements")
public class ElementEntity {
	
	/*	ElementBoundary :
    private String elementId;
    private String type;
    private String name;
    private Boolean active;
    private Date createdTimeStamp;
    private Map<String,String> createdBy;
    private Location location;
    private Map<String,Object> elementAttributes;
	 */
	
    private String elementId;
    private String type;
    private String name;
    private Boolean active;
    private Date createdTimeStamp;
    private String createdBy; // MAP->String
    private Location location;
    private String elementAttributes; // MAP->String
    private Set<ElementEntity> childrens;
    private Set<ElementEntity> parents; 
    
    
    public ElementEntity() {
    	childrens=new HashSet<>();
    	parents=new HashSet<>();

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

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	@Lob
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Embedded
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Lob
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

	@Id
	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="parent_child_relationship",joinColumns = @JoinColumn(name = "parent_id"), 
			  inverseJoinColumns = @JoinColumn(name = "children_id"))
	public Set<ElementEntity> getParents() {
		return parents;}
	

	public void setChildrens(Set<ElementEntity> childrens) {
		this.childrens = childrens;
	}
	
	@ManyToMany(mappedBy = "parents", fetch = FetchType.LAZY)
	public Set<ElementEntity> getChildrens() {
		return childrens;
	}
	
	public void setParents(Set<ElementEntity> parents) {
		this.parents = parents;
	}
	
	public void addParent(ElementEntity parent) {
		this.parents.add(parent);
	}
	
	public void addChildren(ElementEntity children) {
		this.childrens.add(children);
		children.addParent(this);
	}

}
