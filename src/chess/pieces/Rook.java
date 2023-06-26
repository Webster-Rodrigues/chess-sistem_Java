package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece{

	public Rook(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "R";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean [] [] matriz = new boolean [getBoard().getRows()] [ getBoard().getColumns()];
		
		Position pos = new Position(0, 0);
		
		//above (acima)
		pos.setValues(position.getRow() -1, position.getColum());
		//enquanto a posição "pos" existir e não tiver peça lá marque a posição como verdadeira
		while(getBoard().positionExists(pos) && !getBoard().thereIsAPiece(pos)) {
			matriz[pos.getRow()] [pos.getColum()] = true;
			pos.setRow(pos.getRow() - 1);
			
			if(getBoard().positionExists(pos) && IsThereOpponentPiece(pos)) {
				matriz[pos.getRow()] [pos.getColum()] = true;
			}
		}
		
		// left (esquerda)
		pos.setValues(position.getRow(), position.getColum() - 1);
		while (getBoard().positionExists(pos) && !getBoard().thereIsAPiece(pos)) {
			matriz[pos.getRow()][pos.getColum()] = true;
			pos.setColum(pos.getColum() - 1);

			if (getBoard().positionExists(pos) && IsThereOpponentPiece(pos)) {
				matriz[pos.getRow()][pos.getColum()] = true;
			}
		}
		
		
		// right (direita)
		pos.setValues(position.getRow(), position.getColum() + 1);
		while (getBoard().positionExists(pos) && !getBoard().thereIsAPiece(pos)) {
			matriz[pos.getRow()][pos.getColum()] = true;
			pos.setColum(pos.getColum() + 1);

			if (getBoard().positionExists(pos) && IsThereOpponentPiece(pos)) {
				matriz[pos.getRow()][pos.getColum()] = true;
			}
		}
		
		// below (abaixo)
		pos.setValues(position.getRow() + 1, position.getColum());
		while (getBoard().positionExists(pos) && !getBoard().thereIsAPiece(pos)) {
			matriz[pos.getRow()][pos.getColum()] = true;
			pos.setRow(pos.getRow() + 1);

			if (getBoard().positionExists(pos) && IsThereOpponentPiece(pos)) {
				matriz[pos.getRow()][pos.getColum()] = true;
			}
		}

		return matriz;
	}

}
