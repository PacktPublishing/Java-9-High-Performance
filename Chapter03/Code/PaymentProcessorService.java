import org.apache.log4j.Logger;

public class PaymentProcessorService {
	private Logger logger = Logger.getLogger(PaymentProcessorService.class);

	public boolean process(Payment payment) {
		logger.info("Processing payment for " + payment.getSender() + " amount " + payment.getAmount());
		// process payment here
		return true;
	}

}