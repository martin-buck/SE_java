import java.util.*;

abstract public class Piece {
    // all subclasses should inherit and see these fields
    protected Color color;
    // we can't put static variables inside methods in Java - it will raise an error at compile time
    // HashMap will not take a primite as a key
    protected static HashMap<Character, PieceFactory> mapChar = new HashMap<Character, PieceFactory>();

    public static void registerPiece(PieceFactory pf) {
        mapChar.put(pf.symbol(), pf);
    }

    public static Piece createPiece(String name) {
        // index zero stores color and index one stores name of piece
        Character name_color = name.charAt(0);
        Character name_piece = name.charAt(1);
        
        // do we need to check for bad colors?
        if (name_color.equals('w')){
            return mapChar.get(name_piece).create(Color.WHITE);
        }
        else{
            return mapChar.get(name_piece).create(Color.BLACK);
        }
    }

    public Color color() {
	// You should write code here and just inherit it in
	// subclasses. For this to work, you should know
	// that subclasses can access superclass fields.
	    return this.color;
    }

    abstract public String toString();

    abstract public List<String> moves(Board b, String loc);
}