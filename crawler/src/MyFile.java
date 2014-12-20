import java.io.*;

public class MyFile {
	
	private BufferedWriter fW;
	
	public MyFile(String fileName) throws IOException {
		fW = new BufferedWriter(new FileWriter(fileName));
	}
	
	public void flush() throws IOException {
		this.fW.flush();
	}
	
	public void writeln(Object obj) throws IOException {
		String line = obj.toString();
		fW.write(line,0,line.length());
		fW.newLine();
	}
	
	public void close() throws IOException {
		if (fW != null)
			fW.close();
	}
}
