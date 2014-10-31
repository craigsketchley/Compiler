package optimisation;

import genKillFramework.LiveVariableAnalysis;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import CFG.ControlFlowGraph;
import CFG.Node;
import IntermediateLanguage.Register;

/**
 * Applies the dead code elimination optimisation. This uses Live Variable
 * Analysis to determine dead code.
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 */
public class DeadCodeEliminationOptimisation extends Optimisation
{
	
	@Override
	public void runOptimisation(ControlFlowGraph cfg)
	{
		LiveVariableAnalysis liveVariableAnalyser = new LiveVariableAnalysis(cfg);
		Map<Node, Set<Register>> liveVariableInfo = liveVariableAnalyser.analyse();
		List<Node> nodeList = cfg.getAllNodes();
		
		
		Iterator<Node> it = nodeList.iterator();
		while(it.hasNext())
		{
			Node n = it.next();
			if(n.isSentinel())
			{
				continue;
			}
			
			/*Removes assignments to registers that are not used in the program*/
			Register assignedReg = n.getInstruction().getAssignedRegister();
			if(assignedReg != null)
			{
				if(!liveVariableInfo.get(n).contains(assignedReg))
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
