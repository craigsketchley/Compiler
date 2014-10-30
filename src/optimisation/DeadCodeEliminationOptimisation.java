package optimisation;

import genKillFramework.LiveVariableAnalysis;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import CFG.ControlFlowGraph;
import CFG.Node;
import IntermediateLanguage.Register;

public class DeadCodeEliminationOptimisation extends Optimisation
{

	@Override
	public void runOptimisation(ControlFlowGraph cfg) throws Exception
	{
		LiveVariableAnalysis liveVariableAnalyser = new LiveVariableAnalysis(cfg);
		Map<Node, Set<Register>> dataFlowInfo = liveVariableAnalyser.analyse();
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
				if(!dataFlowInfo.get(n).contains(assignedReg))
				{
					cfg.removeNode(n);
				}
			}					
		}	
	}
	
	
	
}
