import intermediateLanguage.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import cfg.*;
import optimisation.*;

public class IntermediateCodeOptimiser
{
	private static int NUM_POSSIBLE_OPTIMISATIONS = 3;
	private static Program inputProgram; 
	private static String outputFile;
	private static Set<OptFlag> opts; 
	
	public static enum OptFlag
	{
		UNREACHABLE, DEAD_CODE, REDUNDANT_LOAD
	}
	
	public static void main(String[] args)
	{

		//First Argument is the intermediate code file to optimise
		/* $> IntermediateCodeOptimiser inputFile -o outputFile -O1
		 * possible 
		 */
		opts = new HashSet<OptFlag>();
		
		if(args.length == 0)
		{
			System.out.println("No input file specified.\n");
			printHelpText();
			return;
		}

		if(args.length > NUM_POSSIBLE_OPTIMISATIONS + 2)
		{
			System.out.println("Too many arguments.\n");
			return;
		}

		for(int i=0; i < args.length; ++i)
		{
			if(i == 0)
			{
				inputProgram = Parser.parse(args[0]);
			}
			else
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
					case "-output":
						if(i+1 >= args.length)
						{
							System.out.println("No output file not specified.\n");
							printHelpText();
							System.exit(0);
						}
						outputFile = args[++i]; //skips reading the next argument
						break;
					case "-o":
						if(i+1 >= args.length)
						{
							System.out.println("No output file not specified.\n");
							printHelpText();
							System.exit(0);
						}
						outputFile = args[++i];
						break;
					default:
						//throw an error if command in unrecognised 
						System.out.println(String.format("Command unrecognised: %s\n", args[i]));
						printHelpText();
						System.exit(0);
				}
			}
		}

		// Run the specified optimisations and generate the output optimized code
		
		// TODO: Currently run in any arbitrary order.
		for (OptFlag o : opts)
		{
			switch (o)
			{
				case UNREACHABLE:
					// TODO: Would be nice to encapsulate unreachable into an Optimisation?
					Program output = new Program();
					for (Function f : inputProgram.functions)
					{
						ControlFlowGraph cfg = new ControlFlowGraph(f);
						cfg.removeUnreachableCode();
						output.functions.add(cfg.convertToFunction());
					}
					inputProgram = output;
					break;
				case DEAD_CODE:
					inputProgram = Optimiser.optimise(inputProgram, new DeadCodeEliminationOptimisation());
					break;
				case REDUNDANT_LOAD:
					inputProgram = Optimiser.optimise(inputProgram, new RedundantLoadOptimisation());
					break;
			}
		}
		
		//output file was specified, print the program into the file
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
			System.out.println(String.format("Unable to write to output file: %s", outputFile));
			printHelpText();
			System.exit(0);
		}
	}
	
	public static void printHelpText()
	{
		String toPrint = new StringBuilder()
        .append("OVERVIEW: Intermediate Language Compiler Optimiser\n\n")
        .append("USAGE: IntermediateCodeOptimiser inFile -o[utput] outFile [-O1 -O2 -O3]\n\n")
        .append("OPTIMISATIONS:\n")
        .append("\t-O \tAll Optimisations\n")
        .append("\t-O1\tUnreachable Code Removal\n")
        .append("\t-O2\tDead Code Elimination\n")
        .append("\t-O3\tRedundant Load Elimination\n")
        .toString();
		
		System.out.print(toPrint);
	}
}
