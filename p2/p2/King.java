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

    // Do we need to throw an exception if a bad location is given?
    public List<String> moves(Board b, String loc) {
        List<String> validMoves = new ArrayList<String>();
        List<String> cols_list = Arrays.asList(Board.cols);
        String loc_col = loc.substring(0,1);
        String loc_row = loc.substring(1, 2);
        
        // translate loc into the array coordinates
        int col = cols_list.indexOf(loc_col);
        int row = Board.rows_trans.indexOf(loc_row);

        for (int i = -1; i < 2; i++){
            for (int j = -1; j < 2; j++){
                int new_col = col + i;
                int new_row = row + j;

                // perhaps don't need to do all this checking (in getPiece)
                Boolean row_bound = (new_row >= 0 && new_row < 8);
                Boolean col_bound = (new_col >= 0 && new_col < 8) ;
                Boolean center = (new_col == col && new_row == row);
                Boolean valid_move = (row_bound && col_bound && !center);
                
                // if we are in bounds on the chess board
                if (valid_move){
                    String new_col_str = cols_list.get(new_col);
                    String new_row_str = Board.rows_trans.get(new_row);
                    String new_loc_str = new_col_str + new_row_str;
                    
                    // check to see if the position to move to is not occupied or is occupied by a piece of opposite color
                    Boolean valid_pos = b.getPiece(new_loc_str) == null || (b.getPiece(new_loc_str).color() != this.color());
                    if (valid_pos){
                        validMoves.add(new_loc_str);
                    }
                }
            }
        }

        return validMoves;
    }

}