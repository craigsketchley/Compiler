package IntermediateLanguage;

public class Register{
	
	public int register;
	
	public Register(int register) {
		this.register = register;
	}
	
	@Override
	public String toString() {
		return "r" + register;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Register))return false;
	    Register otherReg = (Register)other;
	
		return (otherReg.register == this.register);
	}
	
	@Override
	public int hashCode()
	{
		return register;
    }

}
