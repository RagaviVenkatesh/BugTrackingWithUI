package model;

public class Developer {
	
	private int developerId;
    private String name;
    private String email;
    
    public Developer() {
		super();
	}
	
	public Developer(int developerId, String name, String email) {
		super();
		this.developerId = developerId;
		this.name = name;
		this.email = email;
	}
	public int getDeveloperId() {
		return developerId;
	}
	public void setDeveloperId(int developerId) {
		this.developerId = developerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
    
    

}
