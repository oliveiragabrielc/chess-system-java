package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

    public Pawn(Board board, Color color){
        super(board,color);
    }
    @Override
    public String toString(){
        return "P";
    }
    @Override
    public boolean[][] possibleMoves(){
        boolean[][] mat = new boolean[position.getRow()][position.getRow()];
        Position p = new Position(0,0);

        p.setPosition(position.getRow() -1 ,position.getColumn() - 2 );
        if(getBoard().positionExists(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        p.setPosition(position.getRow() -2 ,position.getColumn() -1 );
        if(getBoard().positionExists(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        p.setPosition(position.getRow() -2 ,position.getColumn() +1 );
        if(getBoard().positionExists(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        p.setPosition(position.getRow() -1 ,position.getColumn() +2 );
        if(getBoard().positionExists(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        p.setPosition(position.getRow() +1 ,position.getColumn() +2 );
        if(getBoard().positionExists(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        p.setPosition(position.getRow() +2 ,position.getColumn() +1 );
        if(getBoard().positionExists(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        p.setPosition(position.getRow() -1 ,position.getColumn() -1 );
        if(getBoard().positionExists(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        p.setPosition(position.getRow() +1 ,position.getColumn() -2 );
        if(getBoard().positionExists(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }
        return mat;

    }
}
