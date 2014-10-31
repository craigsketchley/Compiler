package optimisation;

import intermediateLanguage.*;
import cfg.ControlFlowGraph;

/**
 * Defines the Optimisation class. Any type of optimisation must be a subclass
 * of Optimisation. Uses the template pattern.
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 */
public abstract class Optimisation
{
	/**
	 * Given a program, this will return a new program with the optimisation
	 * applied by any subclass.
	 * 
	 * @param p the program required to optimise
	 * @return the program after optimisation has been applied
	 */
	public final Program optimise(Program p)
	{
		Program result = new Program();
		for(Function f : p.functions)
		{
			ControlFlowGraph cfg = new ControlFlowGraph(f);
			runOptimisation(cfg);
			result.functions.add(cfg.convertToFunction());
		}
		return result;
	}
	
	/**
	 * Must be implemented by any subclass. Holds the details of the optimisation specific operations.
	 * 
	 * @param cfg the control flow graph to modify with the optimisation
	 */
	protected abstract void runOptimisation(ControlFlowGraph cfg);
}
