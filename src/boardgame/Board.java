package boardgame;

public class Board {
    private Integer rows;
    private Integer columns;
    private Piece[][] pieces;


    public Board(Integer rows, Integer columns) {
        if(rows < 1 ||columns < 1){
            throw new BoardException("Error creating board: there must be at least 1 row and 1 column");
        }
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
        if(positionExists(new Position(row, column))){
            throw new BoardException("Position not on the board");
        }
        return pieces[row][column];
    }
    public Piece piece(Position position){
        if(positionExists(position)){
            throw new BoardException("Position not on the board");
        }
        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position){
        if(thereIsAPiece(position)){
            throw new BoardException("There is already a piece on position " + position);
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }
    public Piece removePiece(Position position){
        if (positionExists(position)) {
            throw new BoardException("Position not on the board");
        }
        if (piece(position) == null){
            return null;
        }
        Piece aux = piece(position);
        aux.position = null;
        pieces[position.getRow()][position.getColumn()] = null;
        return aux;
    }
    public boolean positionExists(Position position){
        return position.getRow() < 0 || position.getColumn() < 0
                || position.getRow() >= this.rows || position.getColumn() >= this.columns;
    }
    public boolean thereIsAPiece(Position position){
        if(positionExists(position)){
            throw new BoardException("Position not on the board");
        }
       return piece(position) != null;
    }

    @Override
    public String toString(){
        return "Rows: " + rows + " Columns: " + columns;
    }
}
