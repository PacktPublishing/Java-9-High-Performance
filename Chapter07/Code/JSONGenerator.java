
import java.util.*;
import javax.json.Json;
import javax.json.stream.*;

public class JSONGenerator {

	public static void main(String[] args) {

		Map<String, Object> properties = new HashMap<String, Object>(1);
		properties.put(JsonGenerator.PRETTY_PRINTING, true);
		JsonGeneratorFactory jsonGeneratorFactory = Json.createGeneratorFactory(properties);
		JsonGenerator jsonGenerator = jsonGeneratorFactory.createGenerator(System.out);

		jsonGenerator.writeStartObject();
		jsonGenerator.writeStartArray("clients");
		
		Client client = buildClientObject();
		
		createCustomerJSON(jsonGenerator, client);
		
		jsonGenerator.writeEnd();
		jsonGenerator.close();
	}
	
	private static Client buildClientObject(){
		Client client = new Client();
		Address address = new Address();
		PhoneNumber phoneNumber = new PhoneNumber();
		
		client.setName("Mayur Ramgir");
		client.setCompanyName("Zonopact, Inc.");
		client.setContacted(true);
		
		address.setStreet("867 Boylston Street");
		address.setCity("Boston");
		address.setState("MA");
		address.setCountry("USA");
		
		phoneNumber.setNumber("855-966-6722");
		phoneNumber.setExtension("111");
		phoneNumber.setMobile("111-111-1111");
		
		client.setAddress(address);
		client.setPhoneNumber(phoneNumber);
		
		return client;
		
	}
	
	private static JsonGenerator createCustomerJSON(JsonGenerator jsonGenerator, Client client){
		jsonGenerator.writeStartObject();
			jsonGenerator.write("name", client.getName());
			jsonGenerator.write("company", client.getCompanyName());
				jsonGenerator.writeStartObject("address");
				jsonGenerator.write("street", client.getAddress().getStreet());
				jsonGenerator.writeNull("street2");
				jsonGenerator.write("city", client.getAddress().getCity());
				jsonGenerator.write("state", client.getAddress().getState());
				jsonGenerator.write("contacted", true);
				jsonGenerator.writeEnd();
				jsonGenerator.writeStartArray("phone-numbers");
					jsonGenerator.writeStartObject();
					jsonGenerator.write("number", client.getPhoneNumber().getNumber());
					jsonGenerator.write("extension", client.getPhoneNumber().getExtension());
					jsonGenerator.writeEnd();
					
					jsonGenerator.writeStartObject();
					jsonGenerator.write("mobile", client.getPhoneNumber().getMobile());
					jsonGenerator.writeEnd();
				jsonGenerator.writeEnd();
			jsonGenerator.writeEnd();
		jsonGenerator.writeEnd();
		return jsonGenerator;
	}
	
	
	
	
	
	

}