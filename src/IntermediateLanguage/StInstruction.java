package IntermediateLanguage;

public class StInstruction implements Instruction {
	
	public String id;
	public int register;
	
	public StInstruction(String id, int register) {
		this.id = id;
		this.register = register;
	}

}
