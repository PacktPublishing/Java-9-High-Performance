import javassist.*;

public class Metaspace {
	static ClassPool cp = ClassPool.getDefault();

	public static void main(String[] args) throws Exception{
		for (int i = 0; ; i++) { 
			Class c = cp.makeClass("Java 9" + i).toClass();
		}
	}
}