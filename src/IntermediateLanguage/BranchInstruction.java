package IntermediateLanguage;

public class BranchInstruction implements Instruction {
	
	public String register;
	public int block_true;
	public int block_false;
	
	BranchInstruction(String register, int block_true, int block_false) {
		this.register = register;
		this.block_true = block_true;
		this.block_false = block_false;
	}

}
