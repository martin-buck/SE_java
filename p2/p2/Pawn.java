import java.util.*;

public class Pawn extends Piece {
    public Pawn(Color c) {
        this.color = c;
    }
    // implement appropriate methods

    public String toString() {
	// Do we need to throw an exception if a bad string is given?
        if (this.color() == Color.BLACK){
            return "bp";
        }
        else {
            return "wp";
        }
    }

    public List<String> moves(Board b, String loc) {
	throw new UnsupportedOperationException();
    }

}