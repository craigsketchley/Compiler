package optimisation;

import genKillFramework.GenKill;
import genKillFramework.LiveVariableAnalysis;

import java.util.List;

import CFG.ControlFlowGraph;
import CFG.Node;
import IntermediateLanguage.Register;

public class DeadCodeEliminationOptimisation extends Optimisation
{

	@Override
	public ControlFlowGraph runOptimisation(ControlFlowGraph cfg)
	{
		GenKill gk = new GenKill(cfg, new LiveVariableAnalysis());
		gk.analyseBackward();
		
		List<Node> nodeList = cfg.bfs(true);
		for(Node n : nodeList)
		{
			Register assignedReg = n.getInstruction().getAssignedRegister();
			if(assignedReg != null)
			{
				if(!n.getOut().contains(assignedReg))
				{
					cfg.removeNode(n);
				}
			}
					
		}
		
		return cfg;
	}
	
	
	
}
