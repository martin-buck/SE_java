import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Board {

    // two private fields that can only be accessed in this class
    private Piece[][] pieces = new Piece[8][8];
    // private static field can only be accessed by static methods in this class
    private static Board board;
    // this will convert chess coordinates to coordinates in pieces
    private static HashMap<String, Integer> indeces = new HashMap<String, Integer>();
    final static String[] cols = {"a", "b", "c", "d", "e", "f", "g", "h"};
    final static List<String> rows_trans = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8");

    // initiate a listener
    private HashSet<BoardListener> listeners = new HashSet<BoardListener>();

    // empty private constructor that when called below will contain the above two private fields
    private Board() {}
    
    // this static method will initialized a single Board instance or return existing board instance
    public static Board theBoard() {
        if (board == null){
            // fill out the indeces map and initialize new board
            for (int i = 0; i < 8; i++){
                indeces.put(cols[i], i);
            }
            board = new Board();
            return board;
        }
        else{
            return board;
        }
    }

    // Returns piece at given loc or null if no such piece
    // exists
    public Piece getPiece(String loc) {
        // col should be in the range a-h and row should be in the range 1-8
        String col = loc.substring(0,1);
        String row = loc.substring(1, 2);

        // if the input loc is invalid throw an exception
        if (indeces.get(col) == null || rows_trans.indexOf(row) == -1){
            throw new RuntimeException("Invalid position.");
        }

        // otherwise return null or the piece at loc
        return this.pieces[indeces.get(col)][rows_trans.indexOf(row)];
    }

    public void addPiece(Piece p, String loc) {
        String col = loc.substring(0,1);
        String row = loc.substring(1, 2);

        if (indeces.get(col) == null || rows_trans.indexOf(row) == -1 || this.getPiece(loc) != null){
            throw new RuntimeException("Invalid position or position is occupied.");
        }

        this.pieces[indeces.get(col)][rows_trans.indexOf(row)] = p;
    }

    public void movePiece(String from, String to) {
        String from_col = from.substring(0,1);
        String from_row = from.substring(1, 2);

        String to_col = to.substring(0,1);
        String to_row = to.substring(1, 2);

        // throw exceptions if positions are invalid or there is no piece to move
        if (indeces.get(from_col) == null || rows_trans.indexOf(from_row) == -1 || this.getPiece(from) == null){
            throw new RuntimeException("Invalid starting position or there is no piece in this position.");
        }
        if (indeces.get(to_col) == null || rows_trans.indexOf(to_row) == -1){
            throw new RuntimeException("Invalid finishing position.");
        }

        // check if move is valid
        Piece p = this.getPiece(from);
        Color p_color = p.color();
        List<String> moves = p.moves(Board.theBoard(), from);
        if (moves.contains(to)){
            // notify listeners depending on the state of the 'to' position
            Piece captured = this.getPiece(to);
            if (captured == null){
                for (BoardListener l : listeners){
                    l.onMove(from, to, p);
                }
            } 
            else if (captured.color() != p_color){
                for (BoardListener l : listeners){
                    l.onMove(from, to, p);
                    l.onCapture(p, captured);
                }
            }

            // makes the position that the piece started at empty/null and set 'to' position to have the piece there
            this.pieces[indeces.get(from_col)][rows_trans.indexOf(from_row)] = null;
            this.pieces[indeces.get(to_col)][rows_trans.indexOf(to_row)] = null;
            this.addPiece(p, to);
        }
        else{
            throw new RuntimeException("Invalid move for this piece.");
        }
    }

    public void clear() {
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                this.pieces[i][j] = null;
            }
        }
    }

    // Because listeners is a set, we cannot register the same listener twice
    public void registerListener(BoardListener bl) {
        this.listeners.add(bl);
    }

    // remove() returns false if the object was not present in the set
    public void removeListener(BoardListener bl) {
	    this.listeners.remove(bl);
    }

    // clear() removes all elements form the set
    public void removeAllListeners() {
	    this.listeners.clear();
    }

    public void iterate(BoardInternalIterator bi) {
	    for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                bi.visit(cols[i] + rows_trans.get(j), this.pieces[i][j]);
            }
        }
    }
}