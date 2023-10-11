import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Chess {
    public static void main(String[] args) {
	if (args.length != 2) {
	    System.out.println("Usage: java Chess layout moves");
	}
	Piece.registerPiece(new KingFactory());
	Piece.registerPiece(new QueenFactory());
	Piece.registerPiece(new KnightFactory());
	Piece.registerPiece(new BishopFactory());
	Piece.registerPiece(new RookFactory());
	Piece.registerPiece(new PawnFactory());
	Board.theBoard().registerListener(new Logger());

	// args[0] is the layout file name
	// args[1] is the moves file name
	// Put your code to read the layout file and moves files
	// here.
	String layout = args[0];
	String moves = args[1];


	// these arrays list the allowed rows, columns, colors, and pieces
	List<String> cols = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
	List<String> rows = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");
	List<String> colors = Arrays.asList("w", "b");
	List<String> pieces = Arrays.asList("k", "q", "n", "b", "r", "p");

	// try reading the given layout file. FileNotFoundException is a checked exception but we are catching it and throwing an unchecked exception
	try{
		File file = new File(layout);
		Scanner lines = new Scanner(file);

		// this will keep track of positions listed in the file. we do not allow multiple positions
		ArrayList<String> pos = new ArrayList<String>();

		while(lines.hasNextLine()){
			String line = lines.nextLine();

			// disallow xn=cp1 and xn=cp2
			if (line.charAt(0) == '#' || line.isEmpty()){
				continue;
			}
			else if (pos.contains(line.substring(0, 2))){
				throw new RuntimeException("File contains multiple position assignments.");
			}
			else if (line.length() == 5){
				// we know there are five characters 
				char char0 = line.charAt(0);
				char char1 = line.charAt(1);
				char char2 = line.charAt(2);
				char char3 = line.charAt(3);
				char char4 = line.charAt(4);

				// check that the five characters match the required format
				if (cols.contains(String.valueOf(char0)) && 
				rows.contains(String.valueOf(char1)) &&  
				char2 == '=' && 
				colors.contains(String.valueOf(char3)) && 
				pieces.contains(String.valueOf(char4))){
					Piece p = Piece.createPiece(String.valueOf(char3) + String.valueOf(char4));
					Board.theBoard().addPiece(p, String.valueOf(char0) + String.valueOf(char1));
					pos.add(line.substring(0, 2));
					continue;
				}
			}
			else{
				throw new RuntimeException("File contains an invalid layout format. Must be of the form: xn=cp");
			}
		}
		lines.close();
	}
	catch(FileNotFoundException e){
		throw new RuntimeException("File not found!");
	}

	// try reading the moves file
	try{
		File file = new File(moves);
		Scanner lines = new Scanner(file);

		while(lines.hasNextLine()){
			String line = lines.nextLine();
			
			if (line.charAt(0) == '#' || line.isEmpty()){
				continue;
			}
			else if (line.length() == 5){
				char char0 = line.charAt(0);
				char char1 = line.charAt(1);
				char char2 = line.charAt(2);
				char char3 = line.charAt(3);
				char char4 = line.charAt(4);

				if (cols.contains(String.valueOf(char0)) &&
				rows.contains(String.valueOf(char1)) && 
				char2 == '-' &&
				cols.contains(String.valueOf(char3)) &&
				rows.contains(String.valueOf(char4))){
					Board.theBoard().movePiece(String.valueOf(char0) + String.valueOf(char1), String.valueOf(char3) + String.valueOf(char4));
				}
			}
			else{
				throw new RuntimeException("File contains an invalid move format. Must be of the form: xn-ym");
			}
		}
		lines.close();
	}
	catch(FileNotFoundException e){
		throw new RuntimeException("File not found!");
	}

	// Leave the following code at the end of the simulation:
	System.out.println("Final board:");
	Board.theBoard().iterate(new BoardPrinter());
    }
}