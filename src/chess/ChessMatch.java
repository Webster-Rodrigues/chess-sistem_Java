package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Rook;

public class ChessMatch {
	
	private int turn;
	private Board board;
	private Color currentPlayer;
	private boolean check;
	private boolean checkMate;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	
	
	public ChessMatch() {
		board = new Board (8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
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
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;

		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else {
			nextTurn();
		}
		
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
			throw new ChessException("A peça escolhida não pode se mover para a posição alvo");
		}
	}
	
	private Piece makeMove(Position position, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(position);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return capturedPiece;
	}
	
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);//tira a peça que foi movida para o destino
		p.decreaseMoveCount();
		board.placePiece(p, source); // devolve essa peça para a posição de origem
		
		//caso tenha sido capturada, além de remover da lista de peças capturadas, essa peça é adicionada novamente no tabuleiro
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	}
	
	private Color opponent (Color color) {
		return (color == Color.WHITE)? Color.BLACK : Color.WHITE;
	}
	
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece) p;
			}
		}
		throw new IllegalStateException("Não existe o rei na cor " + color + " no tabuleiro");
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition(); //pegar a posição do rei
		List<Piece> opponentPieces = piecesOnTheBoard.stream()
				.filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());// lista de peças oponentes ao rei
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		//pra cada peça dessa lista, testa se existe algum movimento possível de levar o rei 
		return false;
	}
	

	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();//matriz com posições possíveis da peça p
			for (int i=0; i<board.getRows(); i++) {
				for (int j=0; j<board.getColumns(); j++) {
					if (mat[i][j]) {
						
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						
						boolean testCheck = testCheck(color);//depois testo se ainda está em check, se ainda estiver então retorna verdadeiro
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	
	
	private void initialSetup() {
        placeNewPiece('A', 1, new Rook(board, Color.WHITE));
        placeNewPiece('B', 1, new Knight(board, Color.WHITE));
        placeNewPiece('C', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('E', 1, new King(board, Color.WHITE));
        placeNewPiece('F', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('G', 1, new Knight(board, Color.WHITE));
        placeNewPiece('H', 1, new Rook(board, Color.WHITE));
        placeNewPiece('A', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('B', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('C', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('D', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('E', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('F', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('G', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('H', 2, new Pawn(board, Color.WHITE));

        placeNewPiece('A', 8, new Rook(board, Color.BLACK));
        placeNewPiece('B', 8, new Knight(board, Color.BLACK));
        placeNewPiece('C', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('E', 8, new King(board, Color.BLACK));
        placeNewPiece('F', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('G', 8, new Knight(board, Color.BLACK));
        placeNewPiece('H', 8, new Rook(board, Color.BLACK));
        placeNewPiece('A', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('B', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('C', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('D', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('E', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('F', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('G', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('H', 7, new Pawn(board, Color.BLACK));
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE)? Color.BLACK : Color.WHITE;
	}


}
