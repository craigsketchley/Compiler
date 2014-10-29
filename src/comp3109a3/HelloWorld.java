package comp3109a3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import IntermediateLanguage.*;

public class HelloWorld {

	public static void main(String[] args) {
		
		System.out.println("Hello, World!");

		if(args.length > 2) {
			System.out.println("Too many arguments.");
			return;
		}
		
		if(args.length == 0) {
			System.out.println("No input file specified.");
			return;
		}

		Program program = Parser.parse(args[0]);
		System.out.println(program);
		
		if(args.length == 1) {
			System.out.println("No output file specified.");
			return;
		}

		try {
			File outfile = new File(args[1]);
			PrintWriter outstream = new PrintWriter(outfile);
			outstream.println(program);
			outstream.flush();
			outstream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
	}

}
