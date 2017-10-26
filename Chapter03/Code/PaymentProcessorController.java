import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

public class PaymentProcessorController {
	private static Logger logger = Logger.getLogger(PaymentProcessorController.class);
	
	public static void main(String[] args){
		Payment payment = mockPaymentObject();
		UserIdentity ui = mockUserIdentity();
		PaymentProcessorService pps = new PaymentProcessorService();
		MDC.put("location", ui.getLocation());
        MDC.put("device", ui.getDevice());
		pps.process(payment);
		MDC.clear();
	}

	private static UserIdentity mockUserIdentity() {
		UserIdentity ui = new UserIdentity("New York", "iPhone 8");
		return ui;
	}

	private static Payment mockPaymentObject() {
		Payment pay= new Payment("Mayur Ramgir", 100);
		return pay;
	}
	
}
