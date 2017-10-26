public class EscapeAnalysisTest {
     public static void main(String[] args) {
           System.out.println("Begin");
           for (long i = 0; i < 100000000l; i++) {
               HeapAndStack has = new HeapAndStack();
           }
           System.out.println("End of Test " + HeapAndStack.getCounter());
       }
     
 }