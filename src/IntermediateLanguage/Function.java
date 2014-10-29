package IntermediateLanguage;

import java.util.Iterator;
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

	@Override
	public String toString() {
		
		String argString = args.toString().replace(", ", " ");
		argString = argString.substring(1, argString.length() - 1);

		Iterator<Block> it = blocks.iterator();
		StringBuilder output = new StringBuilder(String.format("(%s (%s)\n    %s", id, argString, it.next()));
		while(it.hasNext()) {
			output.append(String.format("\n    %s", it.next()));
		}
		output.append(" )");
		return output.toString();
	}
}
