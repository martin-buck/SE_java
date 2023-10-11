import java.util.*;

public class Test {

    // Run "java -ea Test" to run with assertions enabled (If you run
    // with assertions disabled, the default, then assert statements
    // will not execute!)

    public static void testPawn(){
        Board b = Board.theBoard();
        Piece.registerPiece(new PawnFactory());
        Piece p = Piece.createPiece("wp");
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

    public static void testKing(){
        // initialize board and listeners
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new KingFactory());
        Piece p = Piece.createPiece("bk");
        b.addPiece(p, "a3");
        b.registerListener(new Logger());
       
        // move the king around
        assert b.getPiece("a3") == p;
        assert b.getPiece("a4") == null;
        b.movePiece("a3", "a4");
        assert b.getPiece("a3") == null;
        assert b.getPiece("a4") == p;

        // try an illegal move and to move out-of-bounds
        try {
            b.movePiece("a4", "a6");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }
        try {
            b.movePiece("a4", "c4");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }
        try {
            b.movePiece("a4", "i4");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid finishing position.");
        }

        // clear board 
        b.clear();
        assert b.getPiece("a4") == null;

        // try moving to an occupied space of both black and white
        Piece p1 = Piece.createPiece("wk");
        Piece p2 = Piece.createPiece("bk");
        b.addPiece(p, "a3");
        try {
            b.addPiece(p1, "a3");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid position or position is occupied.");
        }
        b.addPiece(p1, "a4");
        b.movePiece("a3", "a4");
        assert b.getPiece("a3") == null;
        assert b.getPiece("a4") == p;
        b.addPiece(p2, "a3");
        try {
            b.movePiece("a4", "a3");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }

        // removing a listener should stop print statements
        b.removeAllListeners();
        b.movePiece("a3", "b2");

        // try moving a piece to the same spot
        try {
            b.movePiece("b2", "b2");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }

    }

    // NEED TO TEST PIECES IN PATH
    public static void testRook(){
        // initialize board and listeners
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new RookFactory());
        Piece p = Piece.createPiece("br");
        b.addPiece(p, "a3");
        b.registerListener(new Logger());
       
        // move the rook around
        assert b.getPiece("a3") == p;
        assert b.getPiece("a4") == null;
        b.movePiece("a3", "a8");
        assert b.getPiece("a3") == null;
        assert b.getPiece("a8") == p;

        // try an illegal move and to move out-of-bounds
        try {
            b.movePiece("a8", "b7");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }
        try {
            b.movePiece("a8", "h1");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }
        try {
            b.movePiece("a8", "a9");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid finishing position.");
        }

        // clear board 
        b.clear();
        assert b.getPiece("a4") == null;

        // try moving to an occupied space of both black and white
        Piece p1 = Piece.createPiece("wr");
        Piece p2 = Piece.createPiece("br");
        b.addPiece(p, "a3");
        try {
            b.addPiece(p1, "a3");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid position or position is occupied.");
        }
        b.addPiece(p1, "a4");
        b.movePiece("a3", "a4");
        assert b.getPiece("a3") == null;
        assert b.getPiece("a4") == p;
        b.addPiece(p2, "a3");
        assert b.getPiece("a3") == p2;
        try {
            b.movePiece("a4", "a3");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }

        // try moving a piece that is blocked
        try {
            b.movePiece("a4", "a2");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }

        // removing a listener should stop print statements
        b.removeAllListeners();
        b.movePiece("a3", "a1");

        // try moving a piece to the same spot
        try {
            b.movePiece("a1", "a1");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }

        b.clear();
        b.addPiece(p, "d8");
        b.movePiece("d8", "f8");
        b.movePiece("f8", "d8");
    }

    public static void testBishop(){
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new BishopFactory());
        Piece p = Piece.createPiece("wb");
        b.addPiece(p, "a8");
        b.registerListener(new Logger());
       
        // move the bishop around
        assert b.getPiece("a3") == null;
        assert b.getPiece("a8") == p;
        b.movePiece("a8", "h1");
        assert b.getPiece("a8") == null;
        assert b.getPiece("h1") == p;

        // try an illegal move and to move out-of-bounds
        try {
            b.movePiece("h1", "h2");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }
        try {
            b.movePiece("h1", "g1");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }
        try {
            b.movePiece("h1", "a9");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid finishing position.");
        }

        // clear board 
        b.clear();
        assert b.getPiece("h1") == null;

        // try moving to an occupied space of both black and white
        Piece p1 = Piece.createPiece("bb");
        Piece p2 = Piece.createPiece("wb");
        b.addPiece(p, "a3");
        try {
            b.addPiece(p1, "a3");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid position or position is occupied.");
        }
        b.addPiece(p1, "b4");
        b.movePiece("a3", "b4");
        assert b.getPiece("a3") == null;
        assert b.getPiece("b4") == p;
        b.addPiece(p2, "a3");
        try {
            b.movePiece("b4", "a3");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }

        // try moving a piece that is blocked
        try {
            b.movePiece("a3", "c5");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }

        // removing a listener should stop print statements
        b.removeAllListeners();
        b.movePiece("a3", "c1");

        // try moving a piece to the same spot
        try {
            b.movePiece("c1", "c1");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }

    }

    public static void testQueen(){
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new QueenFactory());
        Piece p = Piece.createPiece("wq");
        b.addPiece(p, "a8");
        b.registerListener(new Logger());
       
        // move the bishop around
        assert b.getPiece("a3") == null;
        assert b.getPiece("a8") == p;
        b.movePiece("a8", "h1");
        assert b.getPiece("a8") == null;
        assert b.getPiece("h1") == p;

        // try an illegal move and to move out-of-bounds
        try {
            b.movePiece("h1", "f4");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }
        try {
            b.movePiece("h1", "g3");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }
        try {
            b.movePiece("h1", "a9");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid finishing position.");
        }

        // clear board 
        b.clear();
        assert b.getPiece("h1") == null;

        // try moving to an occupied space of both black and white
        Piece p1 = Piece.createPiece("bb");
        Piece p2 = Piece.createPiece("wq");
        b.addPiece(p, "a3");
        try {
            b.addPiece(p1, "a3");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid position or position is occupied.");
        }
        b.addPiece(p1, "b4");
        b.movePiece("a3", "b4");
        assert b.getPiece("a3") == null;
        assert b.getPiece("b4") == p;
        b.addPiece(p2, "a3");
        try {
            b.movePiece("b4", "a3");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }

        // removing a listener should stop print statements
        b.removeAllListeners();
        try {
            b.movePiece("a3", "c5");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }

        // try moving a piece to the same spot
        try {
            b.movePiece("a3", "a3");
            assert false;
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Invalid move for this piece.");
        }
    }

    public static void testKnight(){
        Board b = Board.theBoard();
        b.clear();
        Piece.registerPiece(new KnightFactory());
        Piece p = Piece.createPiece("wn");
        b.addPiece(p, "h8");
        b.registerListener(new Logger());

        b.movePiece("h8", "g6");
    }

    public static void main(String[] args) {
	    testPawn();
        testKing();
        testRook();
        testBishop();
        testQueen();
        testKnight();
    }

}