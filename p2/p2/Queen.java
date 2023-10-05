import java.util.*;

public class Queen extends Piece {
    public Queen(Color c) {
        this.color = c;
    }
    // implement appropriate methods

    public String toString() {
        // Do we need to throw an exception if a bad string is given?
        if (this.color() == Color.BLACK){
            return "bq";
        }
        else {
            return "wq";
        }
    }

    public List<String> moves(Board b, String loc) {
	throw new UnsupportedOperationException();
    }

}