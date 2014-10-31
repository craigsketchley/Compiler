package optimisation;

import genKillFramework.LoadVariableAnalysis;
import intermediateLanguage.Register;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lattice.Lattice;
import cfg.ControlFlowGraph;
import cfg.Node;

/**
 * Applies the redundant load optimisation.
 * 
 * @see LoadVariableAnalysis for details of the gen / kill / meet / transfer
 * 
 * Once we have the analysis data, we look for uses of registers that
 * we can safely rewrite. For each statement, we look at all referenced
 * registers. If one of these registers corresponds to a register in the
 * analysis data stored for this node, then we look for other registers
 * holding the same data.
 * 
 * We select the register with the lowest id, which holds the variable.
 * Justification: A nice heuristic to maximise the optimisations would
 * be to use the oldest register, since this should cover the most uses.
 * However, identifying this in a language which supports looping is
 * non-trivial. By consistently choosing the register with lowest id,
 * which is likely to be the oldest, we're reasonably likely to get some
 * optimisations.
 * 
 * The register is rewritten, if a suitable register was found.
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
			
			//Get the set of referenced registers, these are the ones we might change
			Set<Register> referencedReg = new HashSet<Register>(n.getInstruction().getReferencedRegisters());

			//Get the dataFlow info for this node (just for convenience)
			HashMap<Register, Lattice<String>> dataFlowNode = dataFlowInfo.get(n);
			
			//For each register referenced by the instruction
			for(Register reg : referencedReg)
			{
				//See if this register is known to be in a specific state
				if( dataFlowNode.containsKey(reg) &&
						(dataFlowNode.get(reg).getState() == Lattice.State.KNOWN) )
				{
					Lattice<String> value = dataFlowNode.get(reg);
					/* See if any other registers are in the same state. If so,
					 * choose the one with lowest ID. See longer description above
					 * for a justification of this choice
					 */
					Register rewriteReg = reg;
					for(Register other : dataFlowNode.keySet())
					{
						if(dataFlowNode.get(other).equals(value))
						{
							if(other.register < rewriteReg.register)
							{
								rewriteReg = other;
							}
						}
					}
					//If we chose a different register
					if(rewriteReg != reg)
					{
						//Then we can rewrite the statement to remove a redundant load!
						n.getInstruction().rewriteReferencedRegisters(reg, rewriteReg);
					}
				}
			}
		}
	}	
}
