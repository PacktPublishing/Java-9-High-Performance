public class UserIdentity {
	 private String location;
	 private String device;
	 
	public UserIdentity(String location, String device){
		this.location = location;
		this.device = device;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	   
}