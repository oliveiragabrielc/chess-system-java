package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Rook;

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
        placeNewPiece('h',7, new Rook(board,Color.WHITE));
        placeNewPiece('d',1, new Rook(board,Color.WHITE));
        placeNewPiece('e',1, new King(board,Color.WHITE));

        placeNewPiece('b',8, new Rook(board,Color.BLACK));
        placeNewPiece('a',8, new King(board,Color.BLACK));

    }
}
