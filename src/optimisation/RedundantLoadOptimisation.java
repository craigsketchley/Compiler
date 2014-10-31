package optimisation;

import genKillFramework.LoadVariableAnalysis;

import java.util.HashMap;
import java.util.HashSet;
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
					//See if any other registers are in the same state.
					//If so, select the one with lowest ID? TODO: better analysis!
					Register rewriteReg = reg;
					for(Register other : dataFlowNode.keySet())
					{
//						System.out.println(String.format(
//								"comparing %s=%s, %s=%s, %s",
//								reg, value, other, dataFlowNode.get(other),
//								dataFlowNode.get(other).equals(value)
//								));
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
					//if(rewriteReg.register != reg.register)
					{
						n.getInstruction().rewriteReferencedRegisters(reg, rewriteReg);
					}
				}
			}
		}
		System.out.println("***PRINTING MAP***");
		for(Node k : cfg.getAllNodes())
		{
			System.out.println(String.format("Node: %s - Map: %s", k, dataFlowInfo.get(k)));
		}

	}	
}
