import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class Solver {

	public Board board, goal;
	
	public Solver (Board board, Board goal){
		this.board = board;
		this.goal = goal;
	}
	
	public ArrayList<Board> genSuccessors(Board board) {
		ArrayList<Board> states = new ArrayList<Board>();

		Board b1 = board.makeCopy();
		Board b2 = board.makeCopy();
		Board b3 = board.makeCopy();
		Board b4 = board.makeCopy();
		
		try {
			b1.move(Board.Move.up);
			states.add(b1);
		} catch (Exception e) {
		}
		try {
			b2.move(Board.Move.down);
			states.add(b2);
		} catch (Exception e) {
		}
		try {
			b3.move(Board.Move.left);
			states.add(b3);
		} catch (Exception e) {
		}
		try {
			b4.move(Board.Move.right);
			states.add(b4);
		} catch (Exception e) {
		}
		return states;
	}

	public void BFS() {
		System.out.println("BFS:");
		SearchNode root = new SearchNode(board);

		Queue<SearchNode> queue = new LinkedList<SearchNode>();
		
		queue.add(root);
	
		searchBFS(queue);
		System.out.println("Number of extended nodes:" + queue.size());
	
	}
	

	private boolean checkRepeats(SearchNode n){
		boolean legal = false;
		SearchNode checkNode = n;
		while (n.getParent() != null && !legal){
			if (n.getParent().getCurrent().equals(checkNode.getCurrent())){
				legal = true;
			}
			n = n.getParent();
		}
		return legal;
	}
	
	private void searchBFS (Queue<SearchNode> q){
		int count = 1;
		int max = 0;
		ArrayList<Board> visited = new ArrayList<Board>();
		
		while (!q.isEmpty()){
			
			SearchNode tempNode = q.poll();
			visited.add(tempNode.getCurrent());

			if (!tempNode.getCurrent().isGoal(goal)){
				
				ArrayList<Board> tempSuccessors = this.genSuccessors(tempNode.getCurrent());
				
				for(Board b: visited) {
					for(Board c: tempSuccessors) {
						if(b.getEqual(c)) {
						tempSuccessors.remove(c);
						break;
						}
					}
				}
				
				for (int i=0; i<tempSuccessors.size(); i++){
					SearchNode newNode = new SearchNode(tempNode, tempSuccessors.get(i), tempNode.getCost() + 1, 0);
				
					if (!checkRepeats(newNode)){
						q.add(newNode);
					}
				}
				count++;
			} else {
				Stack<SearchNode> path = new Stack<SearchNode>();
				
				do {
					path.push(tempNode);
					tempNode = tempNode.getParent();
				} while (tempNode.getParent() != null);
				 path.push(tempNode);
				
				int loopSize = path.size();
				for (int i=0; i<loopSize; i++){
					tempNode = path.pop();
					tempNode.getCurrent().printBoard();
				}
				System.out.println("Cost:" + tempNode.getCost());
				if(path.size() > max) max =  path.size();
				break;
			}
		} //System.out.println("The number of expended nodes:" +max);
	}
	
	public void DFS() {
		System.out.println("DFS:");
		SearchNode root = new SearchNode(board);

		Stack<SearchNode> stack = new Stack<SearchNode>();
		
		stack.add(root);
	
		searchDFS(stack);
		System.out.println("Number of expended nodes:" + stack.size());
		
		
	}
	


	public void searchDFS(Stack<SearchNode> s) {
		int count = 1;
		int max = 0;
		while(!s.isEmpty()) {
			SearchNode tempNode =s.pop();
			
			if (!tempNode.getCurrent().isGoal(goal)){
				
				ArrayList<Board> tempSuccessors = this.genSuccessors(tempNode.getCurrent());
				Collections.shuffle(tempSuccessors);
				for(int i=0; i< tempSuccessors.size(); i++) {
					
					SearchNode newNode = new SearchNode(tempNode, tempSuccessors.get(i), tempNode.getCost()+1,0);
					
					if(!checkRepeats(newNode)) {
						s.add(newNode);
					}
				}
				count++;
				} else {
					Stack<SearchNode> path =  new Stack<SearchNode>();
					
					do {
						path.push(tempNode);
						tempNode = tempNode.getParent();
					}while(tempNode.getParent() != null);
					path.push(tempNode);
					
					int loopSize = path.size();
					for(int j = 0; j<loopSize; j++) {
						tempNode = path.pop();
						tempNode.getCurrent().printBoard();
					}
					System.out.println("Cost:" + tempNode.getCost());
					if(path.size() > max) max = path.size();
					//System.out.println(path.size());
					break;
				}
				
			} System.out.println("The number of expended nodes:" +max);
		}
	
	public void printSol(SearchNode sol) {
		Stack<SearchNode> nodes = new Stack<SearchNode>();
		
		try {
			do {
				nodes.push(sol);
				sol = sol.getParent();
			}while(sol.getParent() != null);
			nodes.push(sol);
		} catch (Exception e) {
			System.out.println("The current state is the goal state");
		}
		
		int s = nodes.size();
		for(int i=0; i < s; i++) {
			sol = nodes.pop();
			sol.getCurrent().printBoard();
		}
		System.out.println("Cost="+sol.getCost());
			
		
	}
	
	public void AHam() {
		System.out.println("A* Hamming:");
		SearchNode root = new SearchNode(board);
		Comparator<SearchNode> comparator = new Comparator<SearchNode>() {
			@Override
			public int compare(SearchNode a, SearchNode b) {
				return a.getCurrent().getHam(goal)-b.getCurrent().getHam(goal);
			}
		};
		PriorityQueue<SearchNode> pq = new PriorityQueue<SearchNode>(comparator);
		pq.add(root);
		searchAStar(pq);
	
	}
	public void AMan() {
		System.out.println("A* Manhattan");
		SearchNode root = new SearchNode(board);
		Comparator<SearchNode> comparator = new Comparator<SearchNode>() {
			@Override
			public int compare(SearchNode a, SearchNode b) {
				return a.getCurrent().getMan(goal)-b.getCurrent().getMan(goal);
			}
		};
		PriorityQueue<SearchNode> pq = new PriorityQueue<SearchNode>(comparator);
		pq.add(root);
		searchAStar(pq);
	
		
	}
	
	private void searchAStar(PriorityQueue<SearchNode> pq) {
		int max = 0;
		
		ArrayList<Board> visited = new ArrayList<Board>();
		
		while(!pq.isEmpty()) {
			SearchNode tempNode = pq.poll();
			visited.add(tempNode.getCurrent());
			
			if(!tempNode.getCurrent().isGoal(goal)) {
				ArrayList<Board> tempSuccessors = this.genSuccessors(tempNode.getCurrent());
				
				for(Board b : visited) {
					for(Board c : tempSuccessors) {
						if (b.getEqual(c)) {
							tempSuccessors.remove(c);
							break;
						}
					}
				}
				
				for(Board b : tempSuccessors) {
					SearchNode newNode = new SearchNode(tempNode, b, tempNode.getCost()+1,0);
					pq.add(newNode);
				}
				
			} else {
				printSol(tempNode);
				break;
		 }
		
	  }if(visited.size()>max) max = visited.size();
	  System.out.println("The number of expended nodes:" +max);
	}
	
	public void IDFS() {
		System.out.println("IDFS:");
		SearchNode root = new SearchNode(board);
		Stack<SearchNode> stack = new Stack<SearchNode>();
		
		stack.add(root);
		
		int depth = 10;
		while(!searchIDFS(stack, depth)) {
			stack.add(root);
			depth++;
			
			
		}
		System.out.println("The number of extended nodes:" + stack.size());
		}
	
	
	
	public boolean searchIDFS(Stack<SearchNode> stack, int depth) {
		ArrayList<Board> visited = new ArrayList<Board>();
		while(!stack.isEmpty()) {
			int max = -1;
			SearchNode tempNode = stack.pop();
			visited.add(tempNode.getCurrent());
			if(tempNode.getCost() <= depth ) {
				if(!tempNode.getCurrent().isGoal(goal))
				{
					ArrayList<Board> tempSuccessors = this.genSuccessors(tempNode.getCurrent());
					Collections.shuffle(tempSuccessors);
					
					for(Board a : visited) {
						for(Board c : tempSuccessors) {
							
							if(a.getEqual(c)) {
								tempSuccessors.remove(c);
								break;
							}
							
						}
					}
					
					
					
					
					for(Board b : tempSuccessors) {
						
					SearchNode newNode = new SearchNode(tempNode,b,tempNode.getCost()+1,0);
					if(!checkRepeats(newNode)) {
						stack.add(newNode);
					}
					}
				} 
				else {
					printSol(tempNode);
					return true;
				}
			}
		}
		
		return false;
	}
	
	
}
