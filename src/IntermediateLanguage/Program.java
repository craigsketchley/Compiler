package IntermediateLanguage;

import java.util.List;
import java.util.ArrayList;

public class Program {

	public List<Function> functions;
	
	public Program() {
		functions = new ArrayList<Function>();
	}
	
	public String toString() {
		String output = "( ";
		for(Function f : functions) {
			output += f.toString() + "\n  ";
		}
		return output + " )";
	}
	
}
