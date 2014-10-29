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
}
