package boardgame;

public abstract class Piece {
    protected Position position;
    private Board board;

    public Piece(Board board) {
        this.board = board;
        this.position = null;
    }

    public Position getPosition() {
        return position;
    }

    protected Board getBoard() {
        return board;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString(){
        return " Hi, i'm here: " + position;
    }
}
