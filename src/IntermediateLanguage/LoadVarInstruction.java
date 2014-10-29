package IntermediateLanguage;

public class LoadVarInstruction implements Instruction {
	
	public String register;
	public int num;
	
	LoadVarInstruction(String register, int num) {
		this.register = register;
		this.num = num;
	}

}
