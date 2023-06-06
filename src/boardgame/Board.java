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
    public Integer getRows() {
        return rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    public Piece piece(int row, int column){
        return pieces[row][column];
    }
    public Piece piece(Position position){
        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position){
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    @Override
    public String toString(){
        return "Rows: " + rows + " Columns: " + columns;
    }
}
