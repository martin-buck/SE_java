import java.util.*;

public class Rook extends Piece {
    public Rook(Color c) {
        this.color = c;
    }
    // implement appropriate methods

    public String toString() {
        // Do we need to throw an exception if a bad string is given?
        if (this.color() == Color.BLACK){
            return "br";
        }
        else {
            return "wr";
        }
    }

    public List<String> moves(Board b, String loc) {
	throw new UnsupportedOperationException();
    }

}