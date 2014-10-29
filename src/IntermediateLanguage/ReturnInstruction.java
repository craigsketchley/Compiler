package IntermediateLanguage;

public class ReturnInstruction implements Instruction {
	
	public String register;
	
	ReturnInstruction(String register) {
		this.register = register;
	}

}
