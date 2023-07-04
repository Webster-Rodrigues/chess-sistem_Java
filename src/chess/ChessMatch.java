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
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {
	
	private int turn;
	private Board board;
	private Color currentPlayer;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;
	
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
	
	public ChessPiece getPromoted() {
		return promoted;
	}
	
	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
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
			throw new ChessException("Você não pode se colcoar em check!!");
		}
		
		ChessPiece movedPiece = (ChessPiece) board.piece(target);
		
		//#SpecialMove promotion
		promoted = null;
		
		if(movedPiece instanceof Pawn) {
			if((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7 )) {
				promoted = (ChessPiece)board.piece(target);
				promoted = replacePromotedPiece("Q");
	
			}
		}

		check = (testCheck(opponent(currentPlayer))) ? true : false;

		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else {
			nextTurn();
		}
		//SpecialMove en passant
		if(movedPiece instanceof Pawn && (target.getRow() == source.getRow() -2 || target.getRow() == source.getRow() + 2)) {
			enPassantVulnerable = movedPiece;
		}
		else {
			enPassantVulnerable = null;
		}
		
		return (ChessPiece)capturedPiece;
	}
	
	public ChessPiece  replacePromotedPiece (String type) {
		if (promoted == null) {
			throw new IllegalStateException("Não há peça para ser promovida");
		}
		if(!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
			return promoted;
		}
		
		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);
		
		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;
		
	}
	
	private ChessPiece newPiece(String type, Color color) {
		if(type.equals("B")) return new Bishop(board, color);
		if(type.equals("N")) return new Knight(board, color);
		if(type.equals("Q")) return new Queen(board, color);
		return new Rook(board, color);
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
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		// #specialmove castling kingSide rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}

		// #specialmove castling queenSide rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		
		// #SpecialMove en passant
		
		if(p instanceof Pawn) {
			if(source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawPosition;
				if(p.getColor() == Color.WHITE) {
					pawPosition = new Position(target.getRow() +1, target.getColumn());
				}
				else {
					pawPosition = new Position(target.getRow() -1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
		
		return capturedPiece;
	}
	
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) board.removePiece(target);// tira a peça que foi movida para o destino
		p.decreaseMoveCount();
		board.placePiece(p, source); // devolve essa peça para a posição de origem

		// caso tenha sido capturada, além de remover da lista de peças capturadas, essa
		// peça é adicionada novamente no tabuleiro
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}

		// #specialmove castling kingSide rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}

		// #specialmove castling queenSide rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		// #SpecialMove en passant

		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				Position pawPosition;
				ChessPiece paw = (ChessPiece)board.removePiece(target);
				
				if (p.getColor() == Color.WHITE) {
					pawPosition = new Position(3, target.getColumn());
				}
				else {
					pawPosition = new Position(4, target.getColumn());
				}
				board.placePiece(paw, pawPosition);
			}
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
        placeNewPiece('D', 1, new Queen(board, Color.WHITE));
        placeNewPiece('E', 1, new King(board, Color.WHITE, this));
        placeNewPiece('F', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('G', 1, new Knight(board, Color.WHITE));
        placeNewPiece('H', 1, new Rook(board, Color.WHITE));
        placeNewPiece('A', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('B', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('C', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('D', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('E', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('F', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('G', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('H', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('A', 8, new Rook(board, Color.BLACK));
        placeNewPiece('B', 8, new Knight(board, Color.BLACK));
        placeNewPiece('C', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('D', 8, new Queen(board, Color.BLACK));
        placeNewPiece('E', 8, new King(board, Color.BLACK, this));
        placeNewPiece('F', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('G', 8, new Knight(board, Color.BLACK));
        placeNewPiece('H', 8, new Rook(board, Color.BLACK));
        placeNewPiece('A', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('B', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('C', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('D', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('E', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('F', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('G', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('H', 7, new Pawn(board, Color.BLACK, this));
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE)? Color.BLACK : Color.WHITE;
	}


}
