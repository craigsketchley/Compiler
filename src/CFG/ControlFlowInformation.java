package CFG;

import genKillFramework.Optimisation;

public interface ControlFlowInformation
{
	public void addInfo(ControlFlow);
	
	public void deleteInfo();
	
	public void clearInfo();
}
