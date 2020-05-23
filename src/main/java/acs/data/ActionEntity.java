package acs.data;

import java.util.Date;

//JPA imports:
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Actions")
public class ActionEntity {
	
	/*	Action Boundary:
	private String actionID;
	private String type;
	private Map<String, String> element;
	private Date createdTimeStamp;
	private Map<String, String> invokedBy;
    private Map<String,Object> actionAttributes;
	 */
	
	private Long actionID;
	private String type;
	private String element; // Map -> String
	private Date createdTimeStamp;
	private String invokedBy; // Map -> String
    private String actionAttributes; // Map -> String
    
	public ActionEntity() {

	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public String getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(String invokedBy) {
		this.invokedBy = invokedBy;
	}

	@Lob
	public String getActionAttributes() {
		return actionAttributes;
	}

	public void setActionAttributes(String actionAttributes) {
		this.actionAttributes = actionAttributes;
	}

	@Id
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

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}
	
}
