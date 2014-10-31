package comp3109a3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import IntermediateLanguage.Parser;
import IntermediateLanguage.Program;

public class IntermediateCodeOptimiser
{
	private static int NUM_POSSIBLE_OPTIMISATIONS = 3;
	private static Program inputProgram; 
	private static String outputFile;
	private static ArrayList<OptFlag> opts; 
	
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
		opts = new ArrayList<OptFlag>();
		
		if(args.length == 0)
		{
			System.out.println("No input file specified.");
			System.out.println("Available optimisations / usage."); //TODO
			return;
		}

		if(args.length > NUM_POSSIBLE_OPTIMISATIONS + 2)
		{
			System.out.println("Too many arguments.");
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
							throw new Exception("Output File not specified");
						outputFile = args[++i]; //skips reading the next argument
					case "-o":
						if(i+1 >= args.length)
							throw new Exception("Output File not specified");
						outputFile = args[++i]; 
					default:
						//throw and error if command in unrecognised 
						throw new Exception("Command unrecognised");
						break; 
				}
			}
		}

		/*Run the specifed optimisations and generate the output optimized code*/

		
		//output file was specified, print the program into the file
		try
		{
			//create the file and directory structure
			File outfile = new File(outputFile);
			outfile.getParentFile().mkdirs();
			
			//stream the program into the file
			PrintWriter outstream = new PrintWriter(outfile);
			outstream.println(program);
			outstream.flush();
			outstream.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	public void printHelpText()
	{
		String toPrint = new StringBuilder()
        .append("*******************HELPTEXT*************************\n")
        .append("IntermediateCodeOptimiser inputFile -o[utput] outputFile [-O1 -O2 -O3 ...]\n")
        .append("--Available Optimisations--\n")
        .append("-O1 : Unreachable Code Removal\n")
        .append("-O2 : Dead Code Elimination\n")
        .append("-O3 : Redundant Load Elimination\n")
        .toString();
		
		System.out.print(toPrint);
	}
}
