public class ArraySizeExceedsLimit {
	public static void main(String[] args){
		ArraySizeExceedsLimit arr = new ArraySizeExceedsLimit();
		arr.arraySizeChecker();
	}

	public void arraySizeChecker(){
		int[] myIntArray = new int[Integer.MAX_VALUE-1];
	}
}