package IntermediateLanguage;

public class BrInstruction implements Instruction {
	
	public int register;
	public int blockTrue;
	public int blockFalse;
	
	public BrInstruction(int register, int block_true, int block_false) {
		this.register = register;
		this.blockTrue = block_true;
		this.blockFalse = block_false;
	}

	public String toString() {
		return String.format("(br r%d %d %d)", register, blockTrue, blockFalse);
	}

}
