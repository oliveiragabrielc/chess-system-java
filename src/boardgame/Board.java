package boardgame;

public class Board {
    private Integer rows;
    private Integer columns;
    private Piece[][] pieces;

    public Board(Integer rows, Integer columns) {
        pieces = new Piece[rows][columns];
        this.rows = rows;
        this.columns = columns;
    }
    @Override
    public String toString(){
        return "Rows: " + rows + " Columns: " + columns;
    }
}
