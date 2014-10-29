package IntermediateLanguage;

public class BranchInstruction implements Instruction {
	
	public String register;
	public int blockTrue;
	public int blockFalse;
	
	BranchInstruction(String register, int block_true, int block_false) {
		this.register = register;
		this.blockTrue = block_true;
		this.blockFalse = block_false;
	}

}
