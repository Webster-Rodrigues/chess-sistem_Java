package chess;

import java.util.ArrayList;
import java.util.List;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	
	private int turn;
	private Board board;
	private Color currentPlayer;
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	
	public ChessMatch() {
		board = new Board (8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}
	
	public ChessPiece[][] getPieces(){
		ChessPiece [][] matriz = new ChessPiece[board.getRows()] [board.getColumns()];
		for(int i = 0; i < board.getColumns(); i++) {
			for(int j = 0; j < board.getRows(); j++) {
				matriz [i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		
		return matriz;
	}
	
	
	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean [][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source  = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		nextTurn();
		return (ChessPiece)capturedPiece;
	}
	
	private void validateSourcePosition(Position position){
		if(!board.thereIsAPiece(position)) {
			throw new ChessException("Não existe peça na posição de origem");
		}
		
		if(currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
			throw new ChessException("Selecione apenas suas peças. Você não pode mover as peças do outro jogador!");
		}
		
		if(!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("Não existe movimentos possíveis para a peça selecionada");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}
	
	private Piece makeMove(Position position, Position target) {
		Piece p = board.removePiece(position);
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return capturedPiece;
	}
	
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	private void initialSetup() {
		placeNewPiece('C', 1, new Rook(board, Color.WHITE));
        placeNewPiece('C', 2, new Rook(board, Color.WHITE));
        placeNewPiece('D', 2, new Rook(board, Color.WHITE));
        placeNewPiece('E', 2, new Rook(board, Color.WHITE));
        placeNewPiece('E', 1, new Rook(board, Color.WHITE));
        placeNewPiece('D', 1, new King(board, Color.WHITE));

        placeNewPiece('C', 7, new Rook(board, Color.BLACK));
        placeNewPiece('C', 8, new Rook(board, Color.BLACK));
        placeNewPiece('D', 7, new Rook(board, Color.BLACK));
        placeNewPiece('E', 7, new Rook(board, Color.BLACK));
        placeNewPiece('E', 8, new Rook(board, Color.BLACK));
        placeNewPiece('D', 8, new King(board, Color.BLACK));
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE)? Color.BLACK : Color.WHITE;
	}


}
