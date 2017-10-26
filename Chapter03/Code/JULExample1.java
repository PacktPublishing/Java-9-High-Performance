import java.util.logging.*;

public class JULExample1 {
   
   private static final Logger logger = Logger.getLogger(JULExample1.class.getName());
 
   public static void main(String[] args) {
      logger.info("Info logging level example");  
      try {
    	 Object o = null;
    	 if(o == null){
    		 logger.log(Level.WARNING, "Object is null");
    	 }
    	 o.toString();  //this line is going to throw a null pointer exception
      } catch (Exception ex){
         logger.log(Level.SEVERE, ex.getMessage(), ex);
      }
   }
}