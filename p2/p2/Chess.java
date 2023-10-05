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

	// try reading the given file
	try{
		File file = new File(layout);
		Scanner lines = new Scanner(file);

		// this will keep track of positions listed in the file. we do not allow multiple positions
		ArrayList<String> pos = new ArrayList<String>();

		// these arrays list the allowed rows, columns, colors, and pieces
		List<String> rows = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
		List<String> cols = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");
		List<String> colors = Arrays.asList("w", "b");
		List<String> pieces = Arrays.asList("k", "q", "n", "b", "r", "p");

		while(lines.hasNextLine()){
			String line = lines.nextLine();
			char char0 = line.charAt(0);
			char char1 = line.charAt(1);
			char char2 = line.charAt(2);
			char char3 = line.charAt(3);
			char char4 = line.charAt(4);

			if (line.charAt(0) == '#' || line.isEmpty()){
				continue;
			}
			else if (pos.contains(line.substring(0, 1))){
				throw new RuntimeException("File contains multiple position assignments");
			}
			else if(rows.contains(String.valueOf(char0)) && 
			 		cols.contains(String.valueOf(char1)) &&
					char2 == '=' &&
					colors.contains(String.valueOf(char3)) &&
					pieces.contains(String.valueOf(char4))){
				pos.add(line.substring(0, 1));
				continue;
			}
		}
		lines.close();
	}
	catch(FileNotFoundException e){
		System.out.println("Cannot find file.");
	}

	// Leave the following code at the end of the simulation:
	System.out.println("Final board:");
	Board.theBoard().iterate(new BoardPrinter());
    }
}