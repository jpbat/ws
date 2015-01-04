
public class Test {
	
	public static void main(String[] args) {
		String a = "(1991–1999)";
		String aux = a.split("–")[1];
		System.out.println(aux.substring(0, aux.length()-1));
		
		aux = "(1991–1999)".split("–")[1].split("\\–")[1];
		System.out.println(Integer.parseInt(aux.substring(0, aux.length() - 1)));
	}

}
