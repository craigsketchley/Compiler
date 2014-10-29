package IntermediateLanguage;

import java.util.List;
import java.util.ArrayList;

public class Function {

	public List<Block> blocks;
	public String id;
	public List<String> vars;
	
	public Function(String id, List<String> vars) {
		this.id = id;
		this.vars = vars;
		this.blocks = new ArrayList<Block>(); //add blocks later
	}	
}
