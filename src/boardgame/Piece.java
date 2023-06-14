package boardgame;

public class Piece {
	
	protected Position position;
	private Board board;
	
	
	public Piece(Board board) {
		this.board = board;
	}

	
	//somente classes d ntro do mesmo pacotes e sub-classes terá acesso
	protected Board getBoard() {
		return board;
	}


	
	

}
