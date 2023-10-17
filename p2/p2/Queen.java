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

    // will consist of code from rook and bishop
    public List<String> moves(Board b, String loc) {
        List<String> validMoves = new ArrayList<String>();
        List<String> cols_list = Arrays.asList(Board.cols);

        // should be in range a-h and 1-8
        String loc_col = loc.substring(0,1);
        String loc_row = loc.substring(1, 2);
        
        // translate loc into the array coordinates (0-7)
        int col = cols_list.indexOf(loc_col);
        int row = Board.rows_trans.indexOf(loc_row);

        // iterate over columns - split between forward and backward as we cannot hop over pieces
        for (int i = 1; i < 8; i++){
            int new_col = col - i;
            int new_row = row;

            Boolean col_bound = (new_col >= 0 && new_col < 8) ;
            Boolean center = (new_col == col && new_row == row);
            Boolean valid_move = (col_bound && !center);
            
            if (valid_move){
                String new_col_str = cols_list.get(new_col);
                String new_row_str = Board.rows_trans.get(new_row);
                String new_loc_str = new_col_str + new_row_str;
                Boolean blocked = b.getPiece(new_loc_str) != null;

                // we break the loop because we should not continue in this direction if a piece is in the way
                if (blocked && b.getPiece(new_loc_str).color() != this.color()){
                    validMoves.add(new_loc_str);
                    break;
                }
                else if(!blocked){
                    validMoves.add(new_loc_str);
                }
                else{
                    break;
                }
            }
        }

        // iterate over columns - split between forward and backward as we cannot hop over pieces
        for (int i = 1; i < 8; i++){
            int new_col = col + i;
            int new_row = row;

            Boolean col_bound = (new_col >= 0 && new_col < 8) ;
            Boolean center = (new_col == col && new_row == row);
            Boolean valid_move = (col_bound && !center);
            
            if (valid_move){
                String new_col_str = cols_list.get(new_col);
                String new_row_str = Board.rows_trans.get(new_row);
                String new_loc_str = new_col_str + new_row_str;
                Boolean blocked = b.getPiece(new_loc_str) != null;

                // we break the loop because we should not continue in this direction if a piece is in the way
                if (blocked && b.getPiece(new_loc_str).color() != this.color()){
                    validMoves.add(new_loc_str);
                    break;
                }
                else if(!blocked){
                    validMoves.add(new_loc_str);
                }
                else{
                    break;
                }
            }
        }

        // iterate over rows
        for (int i = 1; i < 8; i++){
            int new_row = row - i;
            int new_col = col;

            Boolean row_bound = (new_row >= 0 && new_row < 8) ;
            Boolean center = (new_col == col && new_row == row);
            Boolean valid_move = (row_bound && !center);
            
            if (valid_move){
                String new_col_str = cols_list.get(new_col);
                String new_row_str = Board.rows_trans.get(new_row);
                String new_loc_str = new_col_str + new_row_str;
                Boolean blocked = b.getPiece(new_loc_str) != null;

                // we break the loop because we should not continue in this direction if a piece is in the way
                if (blocked && b.getPiece(new_loc_str).color() != this.color()){
                    validMoves.add(new_loc_str);
                    break;
                }
                else if(!blocked){
                    validMoves.add(new_loc_str);
                }
                else{
                    break;
                }
            }       
        }

        for (int i = 1; i < 8; i++){
            int new_row = row + i;
            int new_col = col;

            Boolean row_bound = (new_row >= 0 && new_row < 8) ;
            Boolean center = (new_col == col && new_row == row);
            Boolean valid_move = (row_bound && !center);
            
            if (valid_move){
                String new_col_str = cols_list.get(new_col);
                String new_row_str = Board.rows_trans.get(new_row);
                String new_loc_str = new_col_str + new_row_str;
                Boolean blocked = b.getPiece(new_loc_str) != null;

                // we break the loop because we should not continue in this direction if a piece is in the way
                if (blocked && b.getPiece(new_loc_str).color() != this.color()){
                    validMoves.add(new_loc_str);
                    break;
                }
                else if(!blocked){
                    validMoves.add(new_loc_str);
                }
                else{
                    break;
                }
            }       
        }

        // NW diagonal
        for (int i = 1; i < 8; i++){
            int new_col = col - i;
            int new_row = row + i;

            Boolean col_bound = (new_col >= 0 && new_col < 8);
            Boolean row_bound = (new_row >= 0 && new_row < 8);
            Boolean center = (new_col == col && new_row == row);
            Boolean valid_move = (col_bound && row_bound && !center);

            if (valid_move){
                String new_col_str = cols_list.get(new_col);
                String new_row_str = Board.rows_trans.get(new_row);
                String new_loc_str = new_col_str + new_row_str;
                Boolean blocked = b.getPiece(new_loc_str) != null;

                // we break the loop because we should not continue in this direction if a piece is in the way
                if (blocked && b.getPiece(new_loc_str).color() != this.color()){
                    validMoves.add(new_loc_str);
                    break;
                }
                else if(!blocked){
                    validMoves.add(new_loc_str);
                }
                else{
                    break;
                }
            }
        }

        // NE diagonal
        for (int i = 1; i < 8; i++){
            int new_col = col + i;
            int new_row = row + i;

            Boolean col_bound = (new_col >= 0 && new_col < 8);
            Boolean row_bound = (new_row >= 0 && new_row < 8);
            Boolean center = (new_col == col && new_row == row);
            Boolean valid_move = (col_bound && row_bound && !center);

            if (valid_move){
                String new_col_str = cols_list.get(new_col);
                String new_row_str = Board.rows_trans.get(new_row);
                String new_loc_str = new_col_str + new_row_str;
                Boolean blocked = b.getPiece(new_loc_str) != null;

                // we break the loop because we should not continue in this direction if a piece is in the way
                if (blocked && b.getPiece(new_loc_str).color() != this.color()){
                    validMoves.add(new_loc_str);
                    break;
                }
                else if(!blocked){
                    validMoves.add(new_loc_str);
                }
                else{
                    break;
                }
            }
        }

        // SW diagonal
        for (int i = 1; i < 8; i++){
            int new_col = col - i;
            int new_row = row - i;

            Boolean col_bound = (new_col >= 0 && new_col < 8);
            Boolean row_bound = (new_row >= 0 && new_row < 8);
            Boolean center = (new_col == col && new_row == row);
            Boolean valid_move = (col_bound && row_bound && !center);

            if (valid_move){
                String new_col_str = cols_list.get(new_col);
                String new_row_str = Board.rows_trans.get(new_row);
                String new_loc_str = new_col_str + new_row_str;
                Boolean blocked = b.getPiece(new_loc_str) != null;

                // we break the loop because we should not continue in this direction if a piece is in the way
                if (blocked && b.getPiece(new_loc_str).color() != this.color()){
                    validMoves.add(new_loc_str);
                    break;
                }
                else if(!blocked){
                    validMoves.add(new_loc_str);
                }
                else{
                    break;
                }
            }
        }

        // SE diagonal
        for (int i = 1; i < 8; i++){
            int new_col = col + i;
            int new_row = row - i;

            Boolean col_bound = (new_col >= 0 && new_col < 8);
            Boolean row_bound = (new_row >= 0 && new_row < 8);
            Boolean center = (new_col == col && new_row == row);
            Boolean valid_move = (col_bound && row_bound && !center);

            if (valid_move){
                String new_col_str = cols_list.get(new_col);
                String new_row_str = Board.rows_trans.get(new_row);
                String new_loc_str = new_col_str + new_row_str;
                Boolean blocked = b.getPiece(new_loc_str) != null;

                // we break the loop because we should not continue in this direction if a piece is in the way
                if (blocked && b.getPiece(new_loc_str).color() != this.color()){
                    validMoves.add(new_loc_str);
                    break;
                }
                else if(!blocked){
                    validMoves.add(new_loc_str);
                }
                else{
                    break;
                }
            }
        }

        return validMoves;
    }

}