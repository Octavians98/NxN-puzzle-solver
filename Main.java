import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class Main {
	enum Move{up,down,left,right};
	public static ArrayList<Tile> tiles = new ArrayList<Tile>();
	public static ArrayList<Tile> test = new ArrayList<Tile>();
	

	
	public static Board board, goal;
	public static Tile agent;
	

	public static void main(String[] args) throws FileNotFoundException{
		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
		System.setOut(out);
		tiles.add(new Tile(3, 0, 1));
		tiles.add(new Tile(3, 1, 2));
		tiles.add(new Tile(3, 2, 3));
		
		
		
		
		test.add(new Tile(1, 1, 1));
		test.add(new Tile(2, 1, 2));
		test.add(new Tile(3, 1, 3));
		
		
		agent = new Tile(3,3);
		goal = new Board(4, test, agent);
		board = new Board(4, tiles, agent);
		long startTime = System.currentTimeMillis();
		Solver solver =  new Solver(board, goal);
		solver.DFS();
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Running time:" + totalTime);
		
		//board.makeMove(Board.Move.left);
		//board.print();
		
		
		
	}
}
