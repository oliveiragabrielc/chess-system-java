package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {
    private Board board;
    private int turn = 1;
    private Color currentPlayer = Color.WHITE;
    private boolean check;
    private List<ChessPiece> piecesOnTheBoard = new ArrayList<>();
    private List<ChessPiece> capturedPieces = new ArrayList<>();
    private boolean checkMate;


    public ChessMatch(){
        board = new Board(8,8);
        initialSetup();
    }

    public int getTurn(){
        return turn;
    }
    public Color getCurrentPlayer(){
        return currentPlayer;
    }

    public boolean getCheck(){
        return check;
    }

    public boolean getCheckMate(){
        return checkMate;
    }
    public ChessPiece[][] getPieces(){
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i< board.getRows();i++){
            for (int j =0; j < board.getColumns(); j++){
                mat[i][j] = (ChessPiece) board.piece(i,j);

            }
        }
        return mat;
    }
    private void nextTurn(){
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }
    private void placeNewPiece(char column, int row, ChessPiece piece){
        board.placePiece(piece,new ChessPosition(column,row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private Color opponent(Color color){
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }
    private ChessPiece king(Color color){
        List<ChessPiece> list = piecesOnTheBoard.stream().filter(x -> x.getColor() == color).collect(Collectors.toList());
        for(ChessPiece p : list){
            if( p instanceof King){
                return p;
            }
        }
        throw new IllegalStateException("There is no " + color + "King on the board");
    }
    private boolean testCheck(Color color){
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<ChessPiece> opponentPieces = piecesOnTheBoard.stream().filter(x -> x.getColor() == opponent(color)).collect(Collectors.toList());
        for(ChessPiece p : opponentPieces){
            boolean[][] mat = p.possibleMoves();
            if( mat[kingPosition.getRow()][kingPosition.getColumn()]){
                return true;
            }
        }
        return false;
    }
    private boolean testCheckMate(Color color){
        if(!testCheck(color)){
            return false;
        }
        List<ChessPiece> list = piecesOnTheBoard.stream().filter(x-> x.getColor() == color).collect(Collectors.toList());
        for (ChessPiece p : list){
            boolean[][] mat = p.possibleMoves();
            for(int i=0; i< board.getRows(); i++){
                for(int j=0; j<board.getColumns(); j++){
                    if(mat[i][j]){
                        Position source = p.getChessPosition().toPosition();
                        Position target = new Position(i,j);
                        Piece capturedPiece = makeMove(source,target);
                        boolean testCheck = testCheck(color);
                        undoMove(source,target,capturedPiece);
                        if(!testCheck){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    public boolean[][] possibleMoves(ChessPosition sourcePosition){
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }
    public ChessPiece performeChessMove(ChessPosition sourcePosition, ChessPosition targetPosition){
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source,target);
        Piece capturedPiece = makeMove(source, target);
        if(testCheck(currentPlayer)){
            undoMove(source,target,(ChessPiece) capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }
        check = testCheck(opponent(currentPlayer)) ? true : false;

        if(testCheckMate(opponent(currentPlayer))){
            checkMate = true;
        }
        nextTurn();
        return (ChessPiece) capturedPiece;
    }
    private Piece makeMove(Position source, Position target){
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p,target);
        if(capturedPiece != null){
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add((ChessPiece) capturedPiece);
        }

        // #specialmove castling kinside rook
        if(p instanceof King && target.getColumn() == source.getColumn() + 2){
            Position sourceT = new Position(source.getRow(),source.getColumn() +3);
            Position targetT = new Position(source.getRow(),source.getColumn() +1);
            ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
            board.placePiece(rook,targetT);
            rook.increaseMoveCount();

        }
        // #specialmove castling queenside rook
        if(p instanceof King && target.getColumn() == source.getColumn() - 2){
            Position sourceT = new Position(source.getRow(),source.getColumn() -4);
            Position targetT = new Position(source.getRow(),source.getColumn() -1);
            ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
            board.placePiece(rook,targetT);
            rook.increaseMoveCount();

        }
        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece){
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);
        if(capturedPiece != null){
            board.placePiece(capturedPiece,target);
            capturedPieces.remove((ChessPiece) capturedPiece);
            piecesOnTheBoard.add((ChessPiece) capturedPiece);
        }
        if(p instanceof King && target.getColumn() == source.getColumn() + 2){
            Position sourceT = new Position(source.getRow(),source.getColumn() +3);
            Position targetT = new Position(source.getRow(),source.getColumn() +1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetT);
            board.placePiece(rook,sourceT);
            rook.decreaseMoveCount();

        }
        // #specialmove castling queenside rook
        if(p instanceof King && target.getColumn() == source.getColumn() - 2){
            Position sourceT = new Position(source.getRow(),source.getColumn() -4);
            Position targetT = new Position(source.getRow(),source.getColumn() -1);
            ChessPiece rook = (ChessPiece)board.removePiece(targetT);
            board.placePiece(rook,sourceT);
            rook.decreaseMoveCount();

        }
    }
    private void validateSourcePosition(Position source){
        if(!board.thereIsAPiece(source)){
            throw new ChessException("There is no piece on source position");
        }
        if(currentPlayer != ((ChessPiece) board.piece(source)).getColor()){
            throw new ChessException("The chosen piece is not yours");
        }
        if(!board.piece(source).isThereAnyPossibleMove()){
            throw new ChessException("There is no possible moves for the chosen piece.");
        }
    }
    private  void validateTargetPosition(Position source, Position target){
        if(!board.piece(source).possibleMove(target)){
            throw new ChessException("The chosen piece can't move to target position.");
        }
    }
    private void initialSetup(){
        placeNewPiece('a',1, new Rook(board,Color.WHITE));
        placeNewPiece('b',1, new Knight(board,Color.WHITE));
        placeNewPiece('c',1, new Bishop(board, Color.WHITE));
        placeNewPiece('d',1, new Quenn(board,Color.WHITE));
        placeNewPiece('e',1, new King(board,Color.WHITE,this));
        placeNewPiece('f',1, new Bishop(board, Color.WHITE));
        placeNewPiece('g',1, new Knight(board,Color.WHITE));
        placeNewPiece('h',1, new Rook(board,Color.WHITE));
        placeNewPiece('a',2, new Pawn(board,Color.WHITE));
        placeNewPiece('b',2, new Pawn(board,Color.WHITE));
        placeNewPiece('c',2, new Pawn(board,Color.WHITE));
        placeNewPiece('d',2, new Pawn(board,Color.WHITE));
        placeNewPiece('e',2, new Pawn(board,Color.WHITE));
        placeNewPiece('f',2, new Pawn(board,Color.WHITE));
        placeNewPiece('g',2, new Pawn(board,Color.WHITE));
        placeNewPiece('h',2, new Pawn(board,Color.WHITE));


        placeNewPiece('a',8, new Rook(board,Color.BLACK));
        placeNewPiece('b',8, new Knight(board,Color.BLACK));
        placeNewPiece('c',8, new Bishop(board, Color.BLACK));
        placeNewPiece('d',8, new Quenn(board,Color.BLACK));
        placeNewPiece('e',8, new King(board,Color.BLACK,this));
        placeNewPiece('f',8, new Bishop(board, Color.BLACK));
        placeNewPiece('g',8, new Knight(board,Color.BLACK));
        placeNewPiece('h',8, new Rook(board,Color.BLACK));
        placeNewPiece('a',7, new Pawn(board,Color.BLACK));
        placeNewPiece('b',7, new Pawn(board,Color.BLACK));
        placeNewPiece('c',7, new Pawn(board,Color.BLACK));
        placeNewPiece('d',7, new Pawn(board,Color.BLACK));
        placeNewPiece('e',7, new Pawn(board,Color.BLACK));
        placeNewPiece('f',7, new Pawn(board,Color.BLACK));
        placeNewPiece('g',7, new Pawn(board,Color.BLACK));
        placeNewPiece('h',7, new Pawn(board,Color.BLACK));



    }
}
