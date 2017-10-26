import java.io.IOException;
import java.util.logging.*;
 
public class JULHandlerExample {
   private static final Logger logger = Logger.getLogger(JULHandlerExample.class.getName());
 
   public static void main(String[] args) throws IOException {
      Handler fileHandler = new FileHandler("/Users/ramgirm/handlerexample.log", true);  //create a handler 
      logger.addHandler(fileHandler);	//add the handler to the logger
      logger.setLevel(Level.INFO);
 
      logger.info("Before try block");
      try {
    	 Object o = null;
     	 if(o == null){
     		 logger.log(Level.WARNING, "Object is null");
     	 }
     	 o.toString();  //this line is going to throw a null pointer exception
      } catch (Exception ex){
         logger.log(Level.SEVERE, ex.getMessage(), ex);
      }
      
      fileHandler.flush();
      fileHandler.close();
   }
}