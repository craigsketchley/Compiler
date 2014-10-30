package optimisation;

import genKillFramework.LoadVariableAnalysis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import CFG.ControlFlowGraph;
import CFG.Node;
import IntermediateLanguage.Register;
import Lattice.Lattice;

/**
 * Applies the redundant load optimisation.
 */
public class RedundantLoadOptimisation extends Optimisation
{

	@Override
	public void runOptimisation(ControlFlowGraph cfg)
	{
		LoadVariableAnalysis loadVariableAnalyser = new LoadVariableAnalysis(cfg);
		Map<Node, HashMap<Register, Lattice<String>>> dataFlowInfo = loadVariableAnalyser.analyse();
		List<Node> nodeList = cfg.bfs(true);
	
		Iterator<Node> it = nodeList.iterator();
		while(it.hasNext())
		{
			Node n = it.next();
			if(n.isSentinel())
			{
				continue;
			}
			
//			Register assignedReg = n.getInstruction().getAssignedRegister();
//			if(assignedReg != null)
//			{
//				if(!dataFlowInfo.get(n).contains(assignedReg))
//				{
//					try {
//						cfg.removeNode(n);
//					} catch (Exception e) {
//						System.out.println("Something went wrong when trying to remove a node.");
//						e.printStackTrace();
//						System.exit(-1);
//					}
//				}
//			}					
		}	
	}	
	
}
