package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Knight extends ChessPiece {

	public Knight(Board board, Color color) {
		super(board, color);
	}

	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p == null || p.getColor() != getColor();
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] matriz = new boolean[getBoard().getRows()][getBoard().getColumns()];

		Position pos = new Position(0, 0);


		pos.setValues(position.getRow() - 1, position.getColumn() - 2);
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}

		pos.setValues(position.getRow() -2, position.getColumn() - 1);
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}


		pos.setValues(position.getRow() - 2, position.getColumn() + 1);
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}


		pos.setValues(position.getRow() - 1, position.getColumn() + 2);
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}


		pos.setValues(position.getRow() + 1, position.getColumn() + 2);
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}


		pos.setValues(position.getRow() + 2, position.getColumn() + 1);
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}

		pos.setValues(position.getRow() + 2, position.getColumn() - 1);
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}


		pos.setValues(position.getRow() + 1, position.getColumn() -2);
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}
		return matriz;
	}
	
	@Override
	public String toString() {
		return "N";
	}

}
