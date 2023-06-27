package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece{

	public King(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "K";
	}

	
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p == null || p.getColor() != getColor();
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] matriz = new boolean[getBoard().getRows()][getBoard().getColumns()];

		Position pos = new Position(0, 0);

		// above
		pos.setValues(position.getRow() - 1, position.getColumn());
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}

		// below
		pos.setValues(position.getRow() + 1, position.getColumn());
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}

		// left
		pos.setValues(position.getRow(), position.getColumn() - 1);
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}

		// right
		pos.setValues(position.getRow(), position.getColumn() + 1);
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}

		// nw
		pos.setValues(position.getRow() - 1, position.getColumn() - 1);
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}

		// ne
		pos.setValues(position.getRow() - 1, position.getColumn() + 1);
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}

		// sw
		pos.setValues(position.getRow() + 1, position.getColumn() - 1);
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}

		// se
		pos.setValues(position.getRow() + 1, position.getColumn() + 1);
		if (getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColumn()] = true;
		}
		return matriz;
	}

}
