import java.io.FileInputStream;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
 
public class JSONStream {
 
    public static void main(String[] args) throws Exception  {
        try {
        	FileInputStream json = new FileInputStream("/Users/ramgirm/clients.json");
            JsonParser jsonParser = Json.createParser(json);
            
            Event event = null;
           
            System.out.println("------------Address----------");
            event = parseElement(event, jsonParser, "address");
            printData(event, jsonParser);
            
     
        }catch(Exception e){
        	// Handle the exception logic here
        }
    }
    
    private static Event parseElement(Event event, JsonParser jsonParser, String element) {
    	while(jsonParser.hasNext()) {
            event = jsonParser.next();
            if(event == Event.KEY_NAME && element.equals(jsonParser.getString())) {
                event = jsonParser.next();
                break;
            }
        }
    	return event;
		
	}

	private static void printData(Event event, JsonParser jsonParser){
    	while(event != Event.END_OBJECT) {
            switch(event) {
                case KEY_NAME: {
                    System.out.print(jsonParser.getString());
                    System.out.print(" = ");
                    break;
                }
                case VALUE_FALSE: {
                    System.out.println(false);
                    break;
                }
                case VALUE_NULL: {
                    System.out.println("null");
                    break;
                }
                case VALUE_NUMBER: {
                    if(jsonParser.isIntegralNumber()) {
                        System.out.println(jsonParser.getInt());
                    } else {
                        System.out.println(jsonParser.getBigDecimal());
                    }
                   break;
                }
                case VALUE_STRING: {
                    System.out.println(jsonParser.getString());
                    break;
                }
                case VALUE_TRUE: {
                    System.out.println(true);
                    break;
                }
                default: {
                }
            }
            event = jsonParser.next();
        }
    }
 
}