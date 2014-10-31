package optimisation;

import cfg.ControlFlowGraph;

/**
 * Applies the unreachable code optimisation. Removes all unreachable code from
 * the provided ControlFlowGraph.
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 */
public class UnreachableCodeOptimisation extends Optimisation {

	@Override
	public void runOptimisation(ControlFlowGraph cfg) {
		// Simply call the method available on the cfg.
		cfg.removeUnreachableCode();
	}

}
