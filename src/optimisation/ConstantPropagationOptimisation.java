package optimisation;

import genKillFramework.ConstantFoldingAnalysis;
import intermediateLanguage.BinOpInstruction;
import intermediateLanguage.Instruction;
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

public class ConstantPropagationOptimisation extends Optimisation
{
	/**
	 * 
	 * Replaces any instructions that redundantly recalculates a known constant value 
	 * with a load constant operation using that value. Currently only handles Binary Operations such as add, mul, lt, eq etc.
	 * Information about constant valued registers are calculated using a Constant Propagation Analysis.
	 * 
	 * @param cfg control flow graph to optimise
	 * @see ConstantPropagationAnalysis
	 */
	@Override
	public void runOptimisation(ControlFlowGraph cfg)
	{
		ConstantFoldingAnalysis ConstantFoldingAnalyser = new ConstantFoldingAnalysis(cfg);
		Map<Node, Map<Register, Lattice<Integer>>> dataFlowInfo = ConstantFoldingAnalyser.analyse();
		
		List<Node> nodeList = cfg.getAllNodes();
		
		for(Node n : nodeList)
		{
			if(n.isSentinel())
			{
				continue;
			}
			
			//Propagating a constant depends on the instruction type. 
			//Handles only Binary Operations since all other instructions cannot propagate a constanst
			Instruction instruction = n.getInstruction();
			if(instruction instanceof BinOpInstruction)
			{
				Register refRegLeft = instruction.getReferencedRegisters().get(0);
				Register refRegRight = instruction.getReferencedRegisters().get(1);
				
				//Determine the State of the referenced Registers
				//If they don't appear in the Data Flow Info then it is implied to be Undefined/BOTTOM
				Lattice<Integer> refRegLeftLattice = new Lattice<Integer>(Lattice.State.BOTTOM); 
				Lattice<Integer> refRegRightLattice = new Lattice<Integer>(Lattice.State.BOTTOM);
				if(dataFlowInfo.containsKey(refRegLeft))
				{
					refRegLeftLattice = dataFlowInfo.get(n).get(refRegLeft);
				}
				if(dataFlowInfo.containsKey(refRegRight))
				{
					refRegRightLattice = dataFlowInfo.get(n).get(refRegRight);
				}
				
				if(refRegLeftLattice.isKnown() && refRegRightLattice.isKnown())
				
			}
			else
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

}
