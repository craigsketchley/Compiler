package IntermediateLanguage;

public class IfElseStatement implements Statement {
	
	public String id;
	public int block_true;
	public int block_false;
	
	IfElseStatement(String id, int block_true, int block_false) {
		this.id = id;
		this.block_true = block_true;
		this.block_false = block_false;
	}

}
