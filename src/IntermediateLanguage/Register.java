package IntermediateLanguage;

public class Register {
	
	public int register;
	
	public Register(int register) {
		this.register = register;
	}
	
	public String toString() {
		return "r" + register;
	}
	
	public int equals(Register other) {
		return other.register - register;
	}
	
	public int equals(int other) {
		return other - register;
	}

}
