import javassist.*;

public class PermGen {
	public static void main(String[] args) throws Exception {
		for(long i=0;;i++){
			PermGen pg = new PermGen();
			pg.createClass("Java 9 "+i);
		}
	}

	public Class createClass(String name) throws Exception {
		ClassPool cp = ClassPool.getDefault();
		return cp.makeClass(name).toClass();
	}
}