public class HeapAndStack {
    private static Long counter = 0l;
    public HeapAndStack() {
         ++counter;
     }
     
     public static void main(String[] args) {
         String intro = "Call myAwesomeMethod";
         HeapAndStack has = new HeapAndStack();
         System.out.println(intro);
         LookupPrimeNumbers lpn = new LookupPrimeNumbers();
         has.myAwesomeMethod(lpn);
     }
     
     private void myAwesomeMethod(LookupPrimeNumbers lpnParameter) {
         String message = "Let's generate some prime numbers";
         int start = 1;
         int end = 10000;
         System.out.println(message);
         lpnParameter.lookupPrime(start, end);
     }
     
     public static Long getCounter(){
         return counter;
     }
 }