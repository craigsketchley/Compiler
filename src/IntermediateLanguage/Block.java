package IntermediateLanguage;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class Block {

	public int id;
	public List<Instruction> instructions;
	
	public Block(int id) {
		this.id = id;
		this.instructions = new ArrayList<Instruction>();
	}
	
	@Override
	public String toString() {
		String padding = "";
		if(id < 10) padding = "  ";
		else if(id < 100) padding = " ";
		Iterator<Instruction> it = instructions.iterator();
		StringBuilder output = new StringBuilder(String.format("(%d%s%s", id, padding, it.next()));
		while(it.hasNext()) {
			output.append(String.format("\n        %s", it.next()));
		}
		output.append(" )");
		return output.toString();
	}
	
}
