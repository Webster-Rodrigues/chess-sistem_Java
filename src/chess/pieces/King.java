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

	@Override
	public boolean[][] possibleMoves() {
		boolean [] [] matriz = new boolean [getBoard().getRows()] [ getBoard().getColumns()];
		
		Position pos = new Position(0, 0);
		
		//above
		pos.setValues(position.getRow() - 1, position.getColum());
		if(getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColum()] = true;
		}
		
		//below
		pos.setValues(position.getRow() + 1, position.getColum());
		if(getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColum()] = true;
		}
		
		//left
		pos.setValues(position.getRow(), position.getColum() - 1);
		if(getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColum()] = true;
		}
		
		//right
		pos.setValues(position.getRow(), position.getColum() + 1);
		if(getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColum()] = true;
		}
		
		//nw
		pos.setValues(position.getRow() - 1, position.getColum() - 1);
		if(getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColum()] = true;
		}
		
		//ne
		pos.setValues(position.getRow() - 1, position.getColum() + 1);
		if(getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColum()] = true;
		}
		
		//sw
		pos.setValues(position.getRow() + 1, position.getColum() - 1);
		if(getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColum()] = true;
		}
		
		//se
		pos.setValues(position.getRow() + 1, position.getColum() + 1);
		if(getBoard().positionExists(pos) && canMove(pos)) {
			matriz[pos.getRow()][pos.getColum()] = true;
		}
		return matriz;
	}
	
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p == null || p.getColor() != getColor();
	}

}
