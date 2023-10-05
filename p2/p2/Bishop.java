import java.util.*;

public class Bishop extends Piece {
    public Bishop(Color c) { 
        this.color = c;
    }
    // implement appropriate methods

    public String toString() {
        // Do we need to throw an exception if a bad string is given?
        if (this.color() == Color.BLACK){
            return "bb";
        }
        else {
            return "wb";
        }
    }

    public List<String> moves(Board b, String loc) {
	throw new UnsupportedOperationException();
    }

}