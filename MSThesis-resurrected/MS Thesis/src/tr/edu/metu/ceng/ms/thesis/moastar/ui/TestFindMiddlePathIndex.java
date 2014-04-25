package tr.edu.metu.ceng.ms.thesis.moastar.ui;

public class TestFindMiddlePathIndex {
	
	public static void main(String[] args) {
		int a = (int) Math.ceil(1 / 2); //expected 0
		int b = (int) Math.ceil(2 / 2); //expected 1
		int c = (int) Math.ceil(3 / 2); //expected 2
		int d = (int) Math.ceil(4 / 2);
		int e = (int) Math.ceil(5 / 2);
		
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
		System.out.println(d);
		System.out.println(e);
	}

}
