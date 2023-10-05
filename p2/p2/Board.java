import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.management.RuntimeErrorException;

public class Board {

    // two private fields that can only be accessed in this class
    private Piece[][] pieces = new Piece[8][8];

    // private static field can only be accessed by static methods in this class
    private static Board board;

    // this will convert chess coordinates to coordinates in pieces
    private static HashMap<String, Integer> indeces = new HashMap<>();
    private final static String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private final static List<String> positions = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7");

    // empty private constructor that when called below will contain the above two private fields
    private Board() {}
    
    // this static method will initialized a single Board instance or return existing board instance
    public static Board theBoard() {
        if (board == null){
            // fill out the indeces map and initialize new board
            for (int i = 0; i < 8; i++){
                indeces.put(alphabet[i], i);
            }
            return new Board();
        }
        else{
            return board;
        }
    }

    // Returns piece at given loc or null if no such piece
    // exists
    public Piece getPiece(String loc) {
        String col = loc.substring(0,0);
        String row = loc.substring(1, 1);

        // if the input loc is invalid throw and exception
        if (indeces.get(col) == null || positions.indexOf(row) == -1){
            throw new RuntimeException("Invalid position.");
        }

        // otherwise return null or the piece at loc
        return this.pieces[indeces.get(col)][positions.indexOf(row)];
    }

    public void addPiece(Piece p, String loc) {
        String col = loc.substring(0,0);
        String row = loc.substring(1, 1);

        if (indeces.get(col) == null || positions.indexOf(row) == -1 || this.getPiece(loc) != null){
            throw new RuntimeException("Invalid position or position is occupied.");
        }

        this.pieces[indeces.get(col)][positions.indexOf(row)] = p;
    }

    public void movePiece(String from, String to) {
        String from_col = from.substring(0,0);
        String from_row = from.substring(1, 1);

        String to_col = to.substring(0,0);
        String to_row = to.substring(1, 1);

        // throw exceptions if positions are invalid or there is no piece to move
        if (indeces.get(from_col) == null || positions.indexOf(from_row) == -1 || this.getPiece(from) == null){
            throw new RuntimeException("Invalid starting position or there is no piece in this position.");
        }
        if (indeces.get(to_col) == null || positions.indexOf(to_row) == -1){
            throw new RuntimeException("Invalid finishing position");
        }

        // check if move is valid
        Piece p = this.getPiece(from);
        List<String> moves = p.moves(Board.theBoard(), from);
        if (moves.contains(to)){
            this.pieces[indeces.get(from_col)][positions.indexOf(from_row)] = null;
            this.pieces[indeces.get(to_col)][positions.indexOf(to_row)] = null;
            this.pieces[indeces.get(to_col)][positions.indexOf(to_row)] = p;
        }
        else{
            throw new RuntimeException("Invalid move.");
        }
    }

    public void clear() {
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                this.pieces[i][j] = null;
            }
        }
    }

    public void registerListener(BoardListener bl) {
	throw new UnsupportedOperationException();
    }

    public void removeListener(BoardListener bl) {
	throw new UnsupportedOperationException();
    }

    public void removeAllListeners() {
	throw new UnsupportedOperationException();
    }

    public void iterate(BoardInternalIterator bi) {
	throw new UnsupportedOperationException();
    }
}