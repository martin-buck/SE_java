import java.util.*;

public class King extends Piece {
    public King(Color c) {
        this.color = c;
    }
    // implement appropriate methods
    public String toString() {
        // Do we need to throw an exception if a bad string is given?
        if (this.color() == Color.BLACK){
            return "bk";
        }
        else {
            return "wk";
        }
    }

    public List<String> moves(Board b, String loc) {
	throw new UnsupportedOperationException();
    }

}