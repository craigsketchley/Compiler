package optimisation;

import genKillFramework.ConstantFoldingAnalysis;
import intermediateLanguage.BinOpInstruction;
import intermediateLanguage.Instruction;
import intermediateLanguage.LcInstruction;
import intermediateLanguage.Register;

import java.util.HashMap;
import java.util.HashSet;
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
				Register assignedReg = instruction.getAssignedRegister();
				
				//Determine the State of the referenced Registers
				//If they don't appear in the Data Flow Info then it is implied to be Undefined/BOTTOM
				Lattice<Integer> refRegLeftLattice = new Lattice<Integer>(Lattice.State.BOTTOM); 
				Lattice<Integer> refRegRightLattice = new Lattice<Integer>(Lattice.State.BOTTOM);
				if(dataFlowInfo.get(n).containsKey(refRegLeft))
				{
					refRegLeftLattice = dataFlowInfo.get(n).get(refRegLeft);
				}
				if(dataFlowInfo.get(n).containsKey(refRegRight))
				{
					refRegRightLattice = dataFlowInfo.get(n).get(refRegRight);
				}
				
				if(refRegLeftLattice.isStateKnown() && refRegRightLattice.isStateKnown())
				{
					//If both registers have known constant values then we replace the 
					//binary operation with a load constant into the same assigned register
					int leftVal = refRegLeftLattice.getValue();
					int rightVal = refRegRightLattice.getValue();
					int calculatedVal;
					try
					{
						calculatedVal = ((BinOpInstruction) instruction).calculateValue(leftVal, rightVal);
						LcInstruction lcInsturction = new LcInstruction(assignedReg, calculatedVal);
						n.setInstruction(lcInsturction);
					} catch (Exception e)
					{
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
			else
			{
				continue;
			}
		}
	}	
}
