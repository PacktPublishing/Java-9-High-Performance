
public class Client{
		private String name;
		private String companyName;
		private String email;
		private Address address;
		private PhoneNumber phoneNumber;
		private Boolean contacted;
		
		public Address getAddress() {
			return address;
		}
		public void setAddress(Address address) {
			this.address = address;
		}
		public PhoneNumber getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(PhoneNumber phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		public String getName(){
			return name;
		}
		public void setName(String name){
			this.name = name;
		}
		public String getCompanyName(){
			return companyName;
		}
		public void setCompanyName(String companyName){
			this.companyName = companyName;
		}
		public String getEmail(){
			return email;
		}
		public void setEmail(String email){
			this.name = email;
		}
		public Boolean getContacted() {
			return contacted;
		}
		public void setContacted(Boolean contacted) {
			this.contacted = contacted;
		}
}