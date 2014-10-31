package optimisation;

import genKillFramework.LiveVariableAnalysis;
import intermediateLanguage.Register;

import java.util.Map;
import java.util.Set;

import cfg.ControlFlowGraph;
import cfg.Node;

/**
 * Applies the dead code elimination optimisation. This uses Live Variable
 * Analysis to determine dead code. See inline comments to understand the
 * optimisation steps.
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
		// Initiate a Live Variable Analysis using the given cfg.
		LiveVariableAnalysis liveVariableAnalyser = new LiveVariableAnalysis(cfg);
		Map<Node, Set<Register>> liveVariableInfo = liveVariableAnalyser.analyse();
		
		// Iterate through all cfg nodes to apply optimisation based on analysis info.
		for (Node n : cfg.getAllNodes())
		{
			if(n.isSentinel())
			{
				// Don't try and optimise a start/end sentinal node in the cfg.
				continue;
			}
			
			// Gets the registers assigned in this node instruction.
			Register assignedReg = n.getInstruction().getAssignedRegister();
			if(assignedReg != null)
			{
				// If the current node's live variable analysis information
				// does not contain the assigned register for that node, then
				// it is dead code, so remove the node/instruction from cfg.
				if(!liveVariableInfo.get(n).contains(assignedReg))
				{
					try {
						cfg.removeNode(n);
					} catch (Exception e) {
						System.out.println("DEV ERROR: Something went wrong removing a node.");
						e.printStackTrace();
						System.exit(-1);
					}
				}
			}					
		}	
	}	
	
}
