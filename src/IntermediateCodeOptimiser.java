import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import cfg.*;
import optimisation.*;
import intermediateLanguage.*;

/**
 * Command line interface for the optimiser
 * 
 * The main entry point to the program. This contains the `main` method and
 * parses the command line arguments to optimise the provided input file into
 * an output file, according to the optimisation flags.
 *
 * OVERVIEW: Intermediate Language Compiler Optimiser
 * USAGE: IntermediateCodeOptimiser inFile [-o[utput] outFile] [-O1 -O2 -O3]
 * OPTIMISATIONS:
 * 	-O   All Optimisations
 * 	-O1  Unreachable Code Removal
 * 	-O2  Dead Code Elimination
 * 	-O3  Redundant Load Elimination
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 */
public class IntermediateCodeOptimiser
{
	private static int NUM_POSSIBLE_OPTIMISATIONS = 3;
	private static int maxIterations = 3;
	private static Program inputProgram = null; 
	private static String inputFile = null;
	private static String outputFile = null;
	private static Set<OptFlag> opts; 

	/**
	 * Enumeration type to specify the type of optimisation required.
	 */
	public static enum OptFlag
	{
		UNREACHABLE, DEAD_CODE, REDUNDANT_LOAD
	}
	
	/**
	 * The main entry point to the program.
	 * 
	 * @param args The command line arguments.
	 */
	public static void main(String[] args)
	{
		//Parse the command line arguments
		parseArguments(args);
		
		//Parse the input file
		inputProgram = Parser.parse(inputFile);

		//Run the specified optimisations
		runOptimisations();
		
		//Write the result to a file
		writeOutput();
	}

	/**
	 * Print some usage information
	 */
	public static void printUsageInformation()
	{
		String helpText = new StringBuilder()
        .append("OVERVIEW: Intermediate Language Compiler Optimiser\n\n")
        .append("USAGE: IntermediateCodeOptimiser inFile [-o[utput] outFile] [-O1 -O2 -O3]\n\n")
        .append("OPTIMISATIONS:\n")
        .append("\t-O \tAll Optimisations\n")
        .append("\t-O1\tUnreachable Code Removal\n")
        .append("\t-O2\tDead Code Elimination\n")
        .append("\t-O3\tRedundant Load Elimination\n")
        .toString();
		
		System.out.print(helpText);
	}
	
	/**
	 * Print some usage information then exit
	 * @param status an exit status code
	 */
	public static void exitGracefully(int status)
	{
		printUsageInformation();
		System.exit(status);
	}
	
	/**
	 * Parse a list of command line arguments
	 * @param args the command line arguments
	 */
	public static void parseArguments(String[] args)
	{
		if(args.length > 0)
		{
			//The input file is required as the first argument
			inputFile = args[0];
			
		}
		else
		{
			exitGracefully(0);
		}
		
		if(args.length > NUM_POSSIBLE_OPTIMISATIONS + 2)
		{
			System.out.println("Too many arguments.\n");
			return;
		}

		//Parse any remaining arguments
		opts = new HashSet<OptFlag>();
		for(int i = 1; i < args.length; ++i)
		{
			switch(args[i])
			{
			case "-O":
				opts.add(OptFlag.UNREACHABLE);
				opts.add(OptFlag.DEAD_CODE);
				opts.add(OptFlag.REDUNDANT_LOAD);
				break;
			case "-O1":
				opts.add(OptFlag.UNREACHABLE);
				break;
			case "-O2":
				opts.add(OptFlag.DEAD_CODE);
				break;
			case "-O3":
				opts.add(OptFlag.REDUNDANT_LOAD);
				break;
			case "-output": case "-o":
				//-o requires another argument, check it exists
				if(i+1 >= args.length)
				{
					System.out.println("No output file specified.\n");
					exitGracefully(0);
				}
				//consume the next argument as output file name
				outputFile = args[++i];
				break;
			case "-max": case "-iterations":
				//-max requires another argument, check it exists
				if(i+1 >= args.length)
				{
					System.out.println("-max argument requires a number.\n");
					exitGracefully(0);
				}
				//consume the next argument as maximum iterations
				maxIterations = Integer.parseInt(args[++i]);
				break;
			default:
				//exit with an error if the argument is unrecognised 
				System.out.println(String.format("Argument unrecognised: %s\n", args[i]));
				exitGracefully(0);
			}
		}
	}
	
	/**
	 * Run the selected optimisations.
	 * The optimisations are run in a particular order:
	 * 		Unreachable code is removed first, so we don't waste time on it
	 * 		Redundant loads are removed next
	 * 		Dead code is removed last, so it can remove freshly killed loads
	 * The optimisation can be set to run for several iterations, in case
	 * one optimisation makes others available.
	 */
	public static void runOptimisations()
	{
		//Run the optimisations at most maxIterations times
		for(int i = 0; i < maxIterations; ++i)
		{
			//Store the string representation of the program
			String programCode = inputProgram.toString();
	
			//Run the specified optimisations
			if(opts.contains(OptFlag.UNREACHABLE))
			{
				inputProgram = Optimiser.optimise(inputProgram, new UnreachableCodeOptimisation());
			}
			if(opts.contains(OptFlag.REDUNDANT_LOAD))
			{
				inputProgram = Optimiser.optimise(inputProgram, new RedundantLoadOptimisation());
			}
			if(opts.contains(OptFlag.DEAD_CODE))
			{
				inputProgram = Optimiser.optimise(inputProgram, new DeadCodeEliminationOptimisation());
			}
	
			//Stop iterating early, if the program didn't change
			if(programCode.equals(inputProgram.toString()))
			{
				break;
			}
		}
	}
	
	/**
	 * Write the optimised program to stdout or a file, if specified
	 */
	public static void writeOutput()
	{
		//if no output file was specified, print to stdout
		if(outputFile == null)
		{
			System.out.println(inputProgram);
			return;
		}

		//an output file was specified, print the program into the file
		try
		{
			//create the file and directory structure
			File file = new File(outputFile);
			file.getParentFile().mkdirs();
			
			//stream the program into the file
			PrintWriter outstream = new PrintWriter(file);
			outstream.println(inputProgram);
			outstream.flush();
			outstream.close();
		} 
		catch (FileNotFoundException e)
		{
			//Could not write the output, show an error
			System.out.println(String.format(
					"Unable to write to output file: %s", outputFile));
			exitGracefully(-1);
		}
	}
}