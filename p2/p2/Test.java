public class Test {

    // Run "java -ea Test" to run with assertions enabled (If you run
    // with assertions disabled, the default, then assert statements
    // will not execute!)

    public static void test1() {
	Board b = Board.theBoard();
	Piece.registerPiece(new PawnFactory());
	Piece p = Piece.createPiece("bp");
	b.addPiece(p, "a3");
	assert b.getPiece("a3") == p;
    }

    public static void test2(){
        Board b = Board.theBoard();
        Piece.registerPiece(new PawnFactory());
        Piece p = Piece.createPiece("bp");
        b.addPiece(p, "a3");
        
        // text that blank board spaces return null before and after a move
        assert b.getPiece("a3") == p;
        assert b.getPiece("a4") == null;
        b.movePiece("a3", "a4");
        assert b.getPiece("a3") == null;
        assert b.getPiece("a4") == p;
        b.clear();
        assert b.getPiece("a4") == null;
    }

    
    public static void main(String[] args) {
	test1();
    }

}