
import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONTreeExample {

	public static void main(String[] args) {

		try {

			ObjectMapper mapper = new ObjectMapper();

			JsonNode root = mapper.readTree(new File("/Users/ramgirm/clients.json"));

			JsonNode clientsNode = root.path("clients");
			if (clientsNode.isArray()) {
				for (JsonNode clientNode : clientsNode) {
					String name = clientNode.path("name").asText();
					String company = clientNode.path("company").asText();
					
					JsonNode addressNode = clientNode.path("address");
					String street = addressNode.path("street").asText();
					String city = addressNode.path("city").asText();
					String state = addressNode.path("state").asText();
					
					System.out.println("name : " + name);
					System.out.println("company name : " + company);
					System.out.println("street : " + street);
					System.out.println("city : " + city);
					System.out.println("state : " + state);
					
					JsonNode phoneNumbersNodes = clientNode.path("phone-numbers");
					if (phoneNumbersNodes.isArray()) {
						for (JsonNode phoneNode : phoneNumbersNodes) {
							
							if (!phoneNode.path("number").isMissingNode()) {
								String phoneNumber = phoneNode.path("number").asText();
								String extension = phoneNode.path("extension").asText();
								System.out.println("phone number : " + phoneNumber);
								System.out.println("extension : " + extension);
							}
							
							if (!phoneNode.path("mobile").isMissingNode()) {
								String mobile = phoneNode.path("mobile").asText();
								System.out.println("mobile : " + mobile);
							}
							
						}
					}
					
				}
			}

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}