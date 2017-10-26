
public class Payment {
    private String sender;
    private Long amount;
     
    public Payment(String sender, long amount) {
         this.sender = sender;
        this.amount = amount;
    }
     
    public String getSender() {
        return sender;
    }
 
	public Long getAmount() {
	    return amount;
	}
}