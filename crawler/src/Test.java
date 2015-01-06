
public class Test {
	
	public static void main(String[] args) {
		
		for (int i = 0; i < 50; i++) {
			System.out.print("Working: " + 100 * + i / 1000 + "% (" +  (i + 1) + "/" + 
					+ 1000 + ")\r");
		}
		
		for (int i = 51; i < 1000; i += 50) {
			for (int j = 0; j < 50; j++) {
				System.out.print("Working: " + 100 * (i + j) / 1000 + "% (" +  (i + j) + "/" + 
						+ 1000 + ")\r");	
			}
		}
	}

}
