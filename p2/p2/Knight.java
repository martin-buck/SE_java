import java.util.*;

public class Knight extends Piece {
    public Knight(Color c) {
        this.color = c;
    }

    // implement appropriate methods
    public String toString() {
        // Do we need to throw an exception if a bad string is given?
        if (this.color() == Color.BLACK){
            return "bn";
        }
        else {
            return "wn";
        }
    }

    public List<String> moves(Board b, String loc) {
	throw new UnsupportedOperationException();
    }

}