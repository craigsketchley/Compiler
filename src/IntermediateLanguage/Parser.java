package IntermediateLanguage;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Parser {
	
	public final static Pattern ID = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
	//public final static Pattern NUM = Pattern.compile("-?[0-9]+"); just use nextInt
	public final static Pattern REG = Pattern.compile("r[1-9][0-9]*");
	public final static String OP_STR =
			"(lc)|(ld)|(st)|(add)|(sub)|(mul)|(div)|(lt)|(gt)|(eq)|(br)|(ret)|(call)";
	public final static Pattern INSTRUCTION = Pattern.compile(
			String.format("\\s*\\( (%s) [^)]+", OP_STR)); //excludes trailing ) !
	
	public static class ParseException extends RuntimeException {
		public ParseException(String string) {
			super(string);
		}

		private static final long serialVersionUID = -8438735493626198339L;
	}
	
	public static Program parse(String path) {
		
		String code = null;
		try {
			code = new String(Files.readAllBytes(Paths.get(path)), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		code = code.replaceAll("\\b", " "); //put spaces between ( and characters
		code = code.replaceAll("\\s+", " "); //replace all whitespace+ with single spaces
		
		
		Scanner scan = new Scanner(code);

		scan.useDelimiter(" ");
		scan.next("\\("); //open the program
		Program program = new Program();
		while(scan.hasNext("\\(")) {
			program.functions.add(parseFunction(scan));
			scan.useDelimiter(" ");
		}
		scan.next("\\)"); //should be a closing )
		if(scan.hasNext())
			throw new ParseException("Data in file after end of Program");
		
		return program;
	}
	
	public static Function parseFunction(Scanner scan) {

		scan.next("\\(");
		String id = scan.next(ID);
		scan.next("\\(");
		ArrayList<String> args = new ArrayList<String>();
		while(scan.hasNext(ID)) {
			args.add(scan.next(ID));
		}
		scan.next("\\)");
		
		Function function = new Function(id, args);
		
		//System.out.println("Parsed function head: " + function);
		while(scan.hasNext("\\(")) {
			function.blocks.add(parseBlock(scan));
		}
		scan.next("\\)");

		//System.out.println("Parsed function: " + function);
		return function;
	}
	
	public static Block parseBlock(Scanner scan) {
		scan.next("\\(");
		Block block = new Block(scan.nextInt());
		
		scan.useDelimiter("\\)");
		while(scan.hasNext(INSTRUCTION)) {
			scan.useDelimiter(" ");
			scan.next("\\(");
			String op = scan.next();
			Instruction instr = null;
			switch(op) {
			case "ret": instr = new RetInstruction(parseReg(scan)); break;
			case "ld": instr = new LdInstruction(parseReg(scan), parseId(scan)); break;
			case "lc": instr = new LcInstruction(parseReg(scan), scan.nextInt()); break;
			case "st": instr = new StInstruction(parseId(scan), parseReg(scan)); break;
			case "br": instr = new BrInstruction(parseReg(scan), scan.nextInt(), scan.nextInt()); break;
			case "call": 
				Register dest = parseReg(scan);
				String id = scan.next(ID);
				ArrayList<Register> args = new ArrayList<Register>();
				while(scan.hasNext(REG)) {
					args.add(parseReg(scan));
				}
				instr = new CallInstruction(dest, id, args); break;
			default: instr = new BinOpInstruction(op, parseReg(scan), parseReg(scan), parseReg(scan));
			//default: throw new ParseException("Instruction type not recognised: " + op);
			}
			block.instructions.add(instr);
			//System.out.println("following instr: " + scan.next());
			scan.next("\\)");
			scan.useDelimiter("\\)");
		}
		scan.useDelimiter(" ");
		scan.next("\\)");
		//System.out.println("Parsed block: " + block);
		return block;
		
	}
	
	public static String parseId(Scanner scan) {
		if(scan.hasNext(ID)) {
			return scan.next(ID);
		}
		throw new ParseException("Syntax error scanning ID");
	}

	public static Register parseReg(Scanner scan) {
		if(scan.hasNext(REG)) {
			return new Register(Integer.parseInt(scan.next(REG).substring(1)));
		}
		throw new ParseException("Syntax error scanning REG");
	}

}
