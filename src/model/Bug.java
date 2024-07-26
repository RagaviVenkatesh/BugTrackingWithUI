package model;

import java.sql.Timestamp;
import java.util.Date;

public class Bug {
	
	private int bugId;
    private String description;
    private int categoryId;
    private int assignedTo;
    private String status;
    private Date createdDate;
    private Date resolvedDate;
    
    public Bug() {
	
	}
    
	public Bug(int bugId, String description, int categoryId, int assignedTo, String status, Date createdDate,
			String resolvedDate) {
	
		this.bugId = bugId;
		this.description = description;
		this.categoryId = categoryId;
		this.assignedTo = assignedTo;
		this.status = status;
		this.createdDate = createdDate;
		this.resolvedDate = (resolvedDate == null)? null :Timestamp.valueOf(resolvedDate);
	}

	public int getBugId() {
		return bugId;
	}

	public void setBugId(int bugId) {
		this.bugId = bugId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(int assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(Date resolvedDate) {
		this.resolvedDate = resolvedDate;
	}
    
}
