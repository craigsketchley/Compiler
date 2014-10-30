package optimisation;

import IntermediateLanguage.Program;

public class Optimiser
{
	/*Uses a command/template pattern*/
	private Optimiser(){};
	
	public static Program optimise(Program p, Optimisation opt) throws Exception
	{
		return opt.optimise(p);
	}
}
