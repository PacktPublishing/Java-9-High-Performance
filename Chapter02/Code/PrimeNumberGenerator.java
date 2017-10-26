public class PrimeNumberGenerator {
	
	public static void main(String[] args){
		//System.out.println(" Method 1");
		//lookupPrime(1, 1000000);
		//System.out.println(" Method 2");
		//lookupPrime2(1, 1000000);
		System.out.println(" Method 3");
		lookupPrime3(1, 1000000);
	}
	
	public static void lookupPrime(int start, int end) {
        
        for (int i = start; i <= end; i++)         
          {
            int primeCheckCounter = 0;
            for(int j=i; j>=1; j--) {
                if(i%j == 0) {
                    primeCheckCounter =  primeCheckCounter + 1;
                }
            }
            if(primeCheckCounter == 2){
                System.out.print(i + " ");
            }
          }    
    }
	
	public static void lookupPrime2(int start, int end) {
        
        for (int i = start; i <= end; i++)         
          {
            int primeCheckCounter = 0;
            for(int j=i;2*j>=1;j--) {
                if(i%j == 0) {
                    primeCheckCounter =  primeCheckCounter + 1;
                }
            }
            if(primeCheckCounter == 2){
                System.out.print(i + " ");
            }
          }
    }
	
	public static void lookupPrime3(int start, int end) {

		for (int i = start; i <= end; i++)         
        {
          int primeCheckCounter = 0;
          for(int j=i;(int)Math.sqrt(j)>=1;j --) {
              if(i%j == 0) {
                  primeCheckCounter =  primeCheckCounter + 1;
              }
          }
          if(primeCheckCounter == 2){
              System.out.print(i + " ");
          }
        }
		
        
    }

}
