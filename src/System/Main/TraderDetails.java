package System.Main;

public class TraderDetails {
	private String username;
	private String password;
	private Integer id;
	
	public TraderDetails(String username, String password, Integer id) {
		this.username = username;
		this.password = password;
		this.id = id;
	}
	public TraderDetails(String username, String password) {
		this.username = username;
		this.password = password;
		this.id = hashCode();
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public Integer getId() {
		return id;
	}
	@Override
	public boolean equals(Object o) {
		if(this.getUsername().equals(( (TraderDetails) o).getUsername()))
			return true;
		return false;
	}
	public boolean checkPassword(String password) {
		if(this.getPassword().equals(password))
			return true;
		return false;
	}

}