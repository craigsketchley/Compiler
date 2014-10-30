package optimisation;

import genKillFramework.LiveVariableAnalysis;

import java.util.Iterator;
import java.util.List;

import CFG.ControlFlowGraph;
import CFG.Node;
import IntermediateLanguage.Register;

public class DeadCodeEliminationOptimisation extends Optimisation
{

	@Override
	public ControlFlowGraph runOptimisation(ControlFlowGraph cfg) throws Exception
	{
		LiveVariableAnalysis liveVariableAnalyser = new LiveVariableAnalysis();
		liveVariableAnalyser.analyse(cfg);
		List<Node> nodeList = cfg.bfs(true);
	
		Iterator<Node> it = nodeList.iterator();
		while(it.hasNext())
		{
			Node n = it.next();		
			if(n.isSentinel())
			{
				continue;		
			}
			
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
