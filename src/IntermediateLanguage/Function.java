package IntermediateLanguage;

import java.util.List;
import java.util.ArrayList;

public class Function {

	public String id;
	public List<String> args;
	public List<Block> blocks;
	
	public Function(String id, List<String> args) {
		this.id = id;
		this.args = args;
		this.blocks = new ArrayList<Block>(); //add blocks later
	}

	public String toString() {
		String output = String.format("(%s (%s)\n    ", id, args.toString());
		for(Block b : blocks) {
			output += b.toString() + "\n    ";
		}
		return output + " )";
	}
	
}
