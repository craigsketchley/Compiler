package genKillFramework;

import java.util.HashMap;
import java.util.Map;

import CFG.ControlFlowGraph;
import CFG.Node;
import IntermediateLanguage.*;
import Lattice.Lattice;

public class ConstantFoldingAnalysis extends DataFlowAnalysis<Map<Register, Lattice<Integer>>>
{

	public ConstantFoldingAnalysis(ControlFlowGraph cfg)
	{
		super(cfg);
	}

	@Override
	public Map<Register, Lattice<Integer>> gen(Node n)
	{
		/* Constant folding does not use Gen / Kill framework */ 
		return null;
	}

	@Override
	public Map<Register, Lattice<Integer>> kill(Node n)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Register, Lattice<Integer>> meet(Node n)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateMeet(Map<Register, Lattice<Integer>> map, Node n)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<Register, Lattice<Integer>> transfer(Node n)
	{
		Map<Register, Lattice<Integer>> result = new HashMap<Register, Lattice<Integer>>();
		Map<Register, Lattice<Integer>> inMap = in.get(n);
		
		
		/*Dependant on the Instruction type at the Node*/
		
		Instruction instruction = n.getInstruction();
		Register assignedReg = instruction.getAssignedRegister();
	
		Lattice<Integer> assignedRegValue = new Lattice<Integer>(Lattice.State.BOTTOM);
		
		//Start analysis with the existing Data Flow Information
		result.putAll(inMap);
		if(assignedReg == null)
		{
			//No changes in the Data flow information
			return result;
		}
		else
		{
			//Instruction assigned a register
			
			//If the instruction is a load constant then define the assigned Register
			//Regardless of Lattice State
			if(instruction instanceof LcInstruction)
			{
				Lattice<Integer> constant = new Lattice<Integer>(
													((LcInstruction) instruction).getConstant());
				result.put(assignedReg, constant);
				return result;
			}
			else if(instruction instanceof BinOpInstruction)
			{
				//We have to merge the results of the referenced registers
				Register refRegLeft = instruction.getReferencedRegisters().get(0);
				Register refRegRight = instruction.getReferencedRegisters().get(1);
				
				//Determine the State of the referenced Registers
				//If they don't appear in the Data Flow Info then it is implied to be Undefined/BOTTOM
				Lattice<Integer> refRegLeftLattice = new Lattice<Integer>(Lattice.State.BOTTOM); 
				Lattice<Integer> refRegRightLattice = new Lattice<Integer>(Lattice.State.BOTTOM);
				if(inMap.containsKey(refRegLeft))
				{
					refRegLeftLattice = inMap.get(refRegLeft);
				}
				if(inMap.containsKey(refRegRight))
				{
					refRegRightLattice = inMap.get(refRegRight);
				}
				
				
				if(refRegLeftLattice.isStateBottom() 
						|| refRegRightLattice.isStateBottom())
				{
					//Monotonicity : If any of the references are unknown then result is BOTTOM
					assignedRegValue.setStateBottom();
				}
				else if(refRegLeftLattice.isStateTop()
						|| refRegRightLattice.isStateTop())
				{
					//If any of the references are TOP states then result is TOP
					assignedRegValue.setStateTop();
				}
				else
				{
					//Both reference registers are KNOWN
					try
					{
						int val = ((BinOpInstruction) instruction).calculateValue(refRegLeftLattice.getValue(), 
																refRegRightLattice.getValue());
						assignedRegValue.setValue(val);
					} 
					catch (Exception e)
					{
						e.printStackTrace();
						System.exit(-1);
					}
				}
			}
			else
			{
				/*Instruction relies on information outside of the function e.g. call or ld*/
				//Monotonicty : assigned Value should be TOP
				assignedRegValue.setStateTop();
			}
		}
		
		result.put(assignedReg, assignedRegValue);
		return result;
	}

	@Override
	public Map<Node, Map<Register, Lattice<Integer>>> analyse()
	{
		// TODO Auto-generated method stub
		return null;
	}



}
