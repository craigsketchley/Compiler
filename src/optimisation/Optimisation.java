package optimisation;

import CFG.ControlFlowGraph;
import IntermediateLanguage.*;

public abstract class Optimisation
{
	public Program optimise(Program p) throws Exception
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
	
	public abstract void runOptimisation (ControlFlowGraph cfg) throws Exception;
}
