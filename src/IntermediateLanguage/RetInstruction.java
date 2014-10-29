package IntermediateLanguage;

public class RetInstruction implements Instruction {
	
	public int register;
	
	public RetInstruction(int register) {
		this.register = register;
	}

}
