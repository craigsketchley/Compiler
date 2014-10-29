package IntermediateLanguage;

import java.util.List;
import java.util.ArrayList;

public class Block {

	public int id;
	public List<Instruction> statements;
	
	public Block(int id) {
		this.id = id;
		this.statements = new ArrayList<Instruction>();
	}	
}
