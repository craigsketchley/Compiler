package IntermediateLanguage;

public class IfStatement implements Statement {
	
	public String id;
	public int block_true;
	
	IfStatement(String id, int block_number) {
		this.id = id;
		this.block_true = block_number;
	}

}
