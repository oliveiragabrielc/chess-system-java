package application;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public class Program {

    public static void main(String[] args){

        Position position = new Position(1,1);
        System.out.println(position);
        Board board =  new Board(8,8);

       Piece piece = new Piece(board,position);
        System.out.println(piece);


    }
}
