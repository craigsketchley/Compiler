package IntermediateLanguage;

public class Register
{
	
	public int register; //the register number
	
	/**
	 * Constructor
	 * @param register number
	 */
	public Register(int register)
	{
		this.register = register;
	}

	/**
	 * Copy constructor
	 * @param register to copy
	 */
	public Register(Register register)
	{
		this.register = register.register;
	}

	@Override
	public String toString()
	{
		return "r" + register;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other == null)
		{
			//cannot match null
			return false;
		}
	    if (other == this)
	    {
	    	//same object
	    	return true;
	    }
	    if (!(other instanceof Register))
	    {
	    	//can't compare with anything but Registers
	    	return false;
	    }
	    //compare the register values
		return ( ((Register)other).register == this.register);
	}
	
	@Override
	public int hashCode()
	{
		return register;
    }

}
