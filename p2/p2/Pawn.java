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
        List<String> validMoves = new ArrayList<String>();
        List<String> cols_list = Arrays.asList(Board.cols);
        String loc_col = loc.substring(0,1);
        String loc_row = loc.substring(1, 2);
        
        // translate loc into the array coordinates
        int col = cols_list.indexOf(loc_col);
        int row = Board.rows_trans.indexOf(loc_row);
        int forward_black = -1;
        int forward_white = 1;
        int forward;

        if (this.color() == Color.BLACK ){
            forward = forward_black;
            // check if this is the first move for the pawn and intervening spaces are vacant
            if (loc_row.equals("7") && b.getPiece(loc_col + "6") == null && b.getPiece(loc_col + "5") == null){
                validMoves.add(loc_col + "5");
            }
        }
        else{
            forward = forward_white;
            if (loc_row.equals("2") && b.getPiece(loc_col + "3") == null && b.getPiece(loc_col + "4") == null){
                validMoves.add(loc_col + "4");
            }
        }

        // move forward one space if possible
        int new_col = col;
        int new_row = row + forward;

        Boolean row_bound = (new_row >= 0 && new_row < 8);
        Boolean col_bound = (new_col >= 0 && new_col < 8) ;
        Boolean valid_move = (row_bound && col_bound);
        
        if (valid_move){
            String new_col_str = cols_list.get(new_col);
            String new_row_str = Board.rows_trans.get(new_row);
            String new_loc_str = new_col_str + new_row_str;
            Boolean valid_pos = b.getPiece(new_loc_str) == null;
            if (valid_pos){
                validMoves.add(new_loc_str);
            }
        }

        // check if the forward diagonal positions are occupied by a piece of opposite color
        int new_col_diag1 = col + forward;
        int new_row_diag1 = row + forward;

        Boolean row_bound_diag1 = (new_row_diag1 >= 0 && new_row_diag1 < 8);
        Boolean col_bound_diag1 = (new_col_diag1 >= 0 && new_col_diag1 < 8) ;
        Boolean valid_move_diag1 = (row_bound_diag1 && col_bound_diag1);
        
        // move diagonal one space if possible
        if (valid_move_diag1){
            String new_col_str_diag1 = cols_list.get(new_col_diag1);
            String new_row_str_diag1 = Board.rows_trans.get(new_row_diag1);
            String new_loc_str_diag1 = new_col_str_diag1 + new_row_str_diag1;
            Boolean valid_pos = b.getPiece(new_loc_str_diag1) != null;
            if (valid_pos && b.getPiece(new_loc_str_diag1).color() != this.color()){
                validMoves.add(new_loc_str_diag1);
            }
        }

        // check if the forward diagonal positions are occupied by a piece of opposite colo
        int new_col_diag2 = col - forward;
        int new_row_diag2 = row + forward;
    
        Boolean row_bound_diag2 = (new_row_diag2 >= 0 && new_row_diag2 < 8);
        Boolean col_bound_diag2 = (new_col_diag2 >= 0 && new_col_diag2 < 8) ;
        Boolean valid_move_diag2 = (row_bound_diag2 && col_bound_diag2);
        
        // move diagonal one space if possible
        if (valid_move_diag2){
            String new_col_str_diag2 = cols_list.get(new_col_diag2);
            String new_row_str_diag2 = Board.rows_trans.get(new_row_diag2);
            String new_loc_str_diag2 = new_col_str_diag2 + new_row_str_diag2;
            Boolean valid_pos = b.getPiece(new_loc_str_diag2) != null;
            if (valid_pos && b.getPiece(new_loc_str_diag2).color() != this.color()){
                validMoves.add(new_loc_str_diag2);
            }
        }
        return validMoves;
    }

}