
import java.util.ArrayList;
import java.util.List;

public class KillProcess {
	
	public static void main(String[] args){
		List<int[]> myArrayList = new ArrayList();
		KillProcess kp = new KillProcess();
		kp.processKillLoop(myArrayList);
	}
	
	public void processKillLoop(List<int[]> l){
		for (int i = 1; ; i++) {
			try{
				l.add(new int[Integer.MAX_VALUE-3]);
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
}
