package optimisation;

import intermediateLanguage.Program;

/**
 * Optimiser offers a static method to optimise a program.
 * 
 * It cannot be instantiated.
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 */
public class Optimiser
{
	/**
	 * Hiding the default constructor.
	 */
	private Optimiser(){};
	
	/**
	 * Given a Program and Optimisation, this will return a new program with
	 * the given optimisation applied.
	 *  
	 * @param p
	 * @param opt
	 * @return an optimised program
	 */
	public static Program optimise(Program p, Optimisation opt)
	{
		return opt.optimise(p);
	}
}
