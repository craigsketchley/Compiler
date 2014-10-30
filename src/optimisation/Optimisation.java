package optimisation;

import genKillFramework.GenKill;
import CFG.ControlFlowGraph;
import IntermediateLanguage.*;

public abstract class Optimisation
{
	public Program optimise(Program p)
	{
		Program result = new Program();
		for(Function f : p.functions)
		{
			ControlFlowGraph cfg = new ControlFlowGraph(f);
			cfg = runOptimisation(cfg);
			result.functions.add(cfg.convertToFunction());
		}
		return result;
	}
	
	public abstract ControlFlowGraph runOptimisation (ControlFlowGraph cfg);
}
