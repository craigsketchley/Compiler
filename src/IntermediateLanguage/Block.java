package IntermediateLanguage;

import java.util.List;
import java.util.ArrayList;

public class Block {

	public int id;
	public List<Instruction> instructions;
	
	public Block(int id) {
		this.id = id;
		this.instructions = new ArrayList<Instruction>();
	}
	
	public String toString() {
		String output = String.format("(%d  ", id);
		for(Instruction i : instructions) {
			output += i.toString() + "\n        ";
		}
		return output + ")";
	}
	
}
