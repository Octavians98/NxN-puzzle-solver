import java.util.ArrayList;
import java.util.Arrays;

public class Board {

	private int n;
	private int[][] matrix;
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	private Tile agent;

	public Board(int n, int[][] matrix, ArrayList<Tile> tiles, Tile agent) {
		this.n = n;
		this.matrix = matrix;
		this.tiles = tiles;
		this.agent = agent;
		update();
	}

	public Board(int n, ArrayList<Tile> tiles, Tile agent) {
		this.n = n;
		matrix = new int[n][n];
		this.tiles = tiles;
		this.agent = agent;
		update();
	}

	private void update() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				matrix[i][j] = 0;
			}
		}
		for (Tile t : tiles) {
			matrix[t.getX()][t.getY()] = t.getC();
		}
		matrix[agent.getX()][agent.getY()] = -1;
	}

	enum Move {
		up, down, left, right
	}

	public void move(Move m) throws Exception{
		int x = agent.getX();
		int y = agent.getY();

		switch (m) {
		case up:
			x--;
			makeMove(x, y);

			break;

		case down:
			x++;
			makeMove(x, y);

			break;

		case left:
			y--;
			makeMove(x, y);

			break;
		case right:
			y++;
			makeMove(x, y);

			break;
		default:
			break;
		}
	}

	private void makeMove(int a, int b) throws Exception{
		if (a >= 0 && a < n && b >= 0 && b < n) {
			if (matrix[a][b] == 0) {
				matrix[agent.getX()][agent.getY()] = 0;
				matrix[a][b] = -1;

				agent.setX(a);
				agent.setY(b);
			} else {
				int c = matrix[a][b];
				matrix[agent.getX()][agent.getY()] = c;
				matrix[a][b] = -1;

				for (Tile t : tiles) {
					if (t.getX() == a && t.getY() == b) {
						t.setX(agent.getX());
						t.setY(agent.getY());
						break;
					}
				}

				agent.setX(a);
				agent.setY(b);
			}
		} else {
			throw new Exception("Invalid move");
		}
	}

	public Board makeCopy(){
		ArrayList<Tile> tilesCopy = new ArrayList<Tile>();
		for (Tile t: tiles){
			tilesCopy.add(t.copy());
		}

		int[][] matrixCopy = new int[this.n][this.n];
		for (int i=0; i<n; i++){
			for (int j=0; j<n; j++){
				matrixCopy[i][j] = this.matrix[i][j];
			}
		}

		Board copy = new Board (this.n, matrixCopy , tilesCopy, agent.copy());
		return copy;
	}

	public Boolean isGoal(Board b){
		//This way I`m making sure that the agent is not considered
		int max =0;
		for (int i=0; i<n; i++){
			for (int j=0; j<n; j++){
				if(max<this.matrix[i][j]) {
					max =  this.matrix[i][j];
				}
			}}
		for (int i=0; i<n; i++){
			for (int j=0; j<n; j++){
				for(int k=1;k<=max;k++) {
					if(b.matrix[i][j]==k && this.matrix[i][j]!=k) {
						return false;
					}
					}
				}

		}

		return true;
	}



	public void printBoard(){
		for (int i=0; i<n; i++){
			for (int j=0; j<n; j++){
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public int getHam(Board b) {
		int ham = 0;
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				if(this.matrix[i][j]>0) {
					if(this.matrix[i][j] != b.matrix[i][j]) {
						ham++;
					}
				}
			}
		}
		return ham;
	}

	public int getMan(Board b) {
		int man = 0;
		for(Tile t :this.tiles) {
			for(Tile y:b.tiles) {
				if(t.getC() == y.getC()) {
					man = man + Math.abs(t.getX() - y.getX()) + Math.abs(t.getY()-y.getY());
				}
			}
		}
		return man;
	}
	public boolean getEqual(Board b) {
		if(Arrays.deepEquals(this.matrix, b.matrix)) {
			return true;
		}
		return false;
	}
}