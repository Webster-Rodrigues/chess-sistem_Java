package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	
	private Board board;
	
	public ChessMatch() {
		board = new Board (8, 8);
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
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source  = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		Piece capturedPiece = makeMove(source, target);
		return (ChessPiece)capturedPiece;
	}
	
	private void validateSourcePosition(Position position){
		if(!board.thereIsAPiece(position)) {
			throw new ChessException("Não existe peça na posição de origem");
		}
	}
	
	private Piece makeMove(Position position, Position target) {
		Piece p = board.removePiece(position);
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		return capturedPiece;
	}
	
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
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


}
