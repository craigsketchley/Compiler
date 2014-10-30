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
	public void runOptimisation(ControlFlowGraph cfg)
	{
		LiveVariableAnalysis liveVariableAnalyser = new LiveVariableAnalysis(cfg);
		liveVariableAnalyser.analyse();
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
					try {
						cfg.removeNode(n);
					} catch (Exception e) {
						System.out.println("Something went wrong when trying to remove a node.");
						e.printStackTrace();
						System.exit(-1);
					}
				}
			}					
		}	
	}	
	
}
