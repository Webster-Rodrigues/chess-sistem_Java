package boardgame;

public abstract class Piece {
	
	protected Position position;
	private Board board;
	
	
	public Piece(Board board) {
		this.board = board;
	}

	
	//somente classes d ntro do mesmo pacotes e sub-classes terá acesso
	protected Board getBoard() {
		return board;
	}
	
	//método que é uma matriz
	public abstract boolean[][] possibleMoves();
	
	
	//Método concreto utilizando o método abstrato = HookMethod (Gancho c/ a subclasse)
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];
	}

	public boolean isThereAnyPossibleMove() {
		boolean[][] matriz = possibleMoves();
		for (int i=0; i<matriz.length; i++) {
			for (int j=0; j<matriz.length; j++) {
				if (matriz[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
	

}
