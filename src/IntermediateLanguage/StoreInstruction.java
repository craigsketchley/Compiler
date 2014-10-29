package IntermediateLanguage;

public class StoreInstruction implements Instruction {
	
	public String id;
	public String register;
	
	StoreInstruction(String id, String register) {
		this.id = id;
		this.register = register;
	}

}
