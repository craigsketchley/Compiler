package comp3109a3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import IntermediateLanguage.*;

public class HelloWorld
{

	public static void main(String[] args)
	{
		
		System.out.println("Hello, World!");

		if(args.length > 2)
		{
			System.out.println("Too many arguments.");
			return;
		}
		
		if(args.length == 0)
		{
			System.out.println("No input file specified.");
			return;
		}

		Program program = Parser.parse(args[0]);
		System.out.println(program);

		/*
		// Example use case for the Optimiser Class.
		program = Optimiser.optimise(program, new UnreachableCodeOptimisation());
		
		program = Optimiser.optimise(program, new DeadCodeEliminationOptimisation());
		
		program = Optimiser.optimise(program, new RedundantLoadsOptimisation());
		*/

		if(args.length == 1)
		{
			System.out.println("No output file specified.");
			return;
		}
		
		//output file was specified, print the program into the file
		try
		{
			//create the file and directory structure
			File outfile = new File(args[1]);
			outfile.getParentFile().mkdirs();
			
			//stream the program into the file
			PrintWriter outstream = new PrintWriter(outfile);
			outstream.println(program);
			outstream.flush();
			outstream.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}
	}
}
