package IntermediateLanguage;

import java.util.List;
import java.util.ArrayList;

public class Block {

	public List<Statement> statements;
	public int id;
	
	public Block(int id) {
		this.id = id;
		this.statements = new ArrayList<Statement>();
	}	
}
