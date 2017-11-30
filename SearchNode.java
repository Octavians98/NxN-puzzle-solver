public class SearchNode {
	private Board current;
	private SearchNode parent;
	private double cost;
	private double hCost;
	
	
	public SearchNode (Board b){
		current = b;
		parent = null;
		cost = 0;
		hCost = 0;
	
	}
	
	public SearchNode (SearchNode prev, Board b, double c, double h){
		parent = prev;
		current = b; 
		cost = c;


	}
	
	public Board getCurrent(){
		return current;
	}
	
	public SearchNode getParent(){
		return parent;
	}
	
	public double getCost(){
		return cost;
	}
	
	public double getHCost(){
		return hCost;
	}

}