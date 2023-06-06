package boardgame;

public class Piece {
    protected Position position;
    private Board board;

    public Piece(Board board, Position position) {
        this.position = position;
        this.board = board;
    }

    @Override
    public String toString(){
        return "Board: " + board + " Position: " + position;
    }
}
