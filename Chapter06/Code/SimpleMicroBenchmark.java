
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
 
public class SimpleMicroBenchmark {
	public static Long buildTimeForArrayList = 0l;
	public static Long buildTimeForLinkedList = 0l;
	
    public static void main(String[] args) {
        System.out.println("Building an ArrayList with 10,000 elements" );
        System.out.println("--------build test----------" );
        List<Integer> arrayList = buildList("ArrayList",new ArrayList<Integer>(), 10000 );
        List<Integer> linkedList = buildList("LinkedList",new LinkedList<Integer>(), 10000 );
        System.out.println("ArrayList vs LinkedList Build time: " +
                ((double)buildTimeForArrayList/(double)buildTimeForLinkedList));
        System.out.println("--------end of build test----------" );
        
 
        System.out.println("--------iterate test----------" );
        Long arrayListIterate = iterateOverList(arrayList);
        Long linkedListIterate = iterateOverList(linkedList);
        System.out.println("arrayListIterate : " + arrayListIterate);
        System.out.println("linkedListIterate : " + linkedListIterate);
        System.out.println("ArrayList vs LinkedList Iterate time: " +
                          ((double)arrayListIterate/(double)linkedListIterate));
        System.out.println("--------end of iterate test----------" );
    }
 
    private static List<Integer> buildList(String type, List<Integer> list, Integer size){
    	Long start = System.nanoTime(); 
    	for(int i=0; i<10000; i++) {
        	list.add(i);
        }
    	Long end = System.nanoTime();
    	Long diff = end-start;
    	System.out.println("Build Time for " + type + " : " + diff);
    	
    	if(type.equals("ArrayList")){
    		buildTimeForArrayList = diff;
    	}else if(type.equals("LinkedList")){
    		buildTimeForLinkedList = diff;
    	}
    	return list;
    }
    private static Long iterateOverList(List<Integer> list) {
    	int counter = 0;
        Long start = System.nanoTime();      
        for(int i=0; i<list.size(); i++) {
        	counter += list.get(i);
        }     
        Long end = System.nanoTime();
        return end-start;
    }
 
}