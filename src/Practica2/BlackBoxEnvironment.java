package Practica2;

import java.util.ArrayList;
//import java.util.HashMap;
import java.util.Random;

public class BlackBoxEnvironment {
	
	// problem data
	private int dimension;
	private int[][] maze = null;
	private int initialCarRow=0;
	private int initialCarColumn=0;
	
	// rewards
	private double finalReward = 0;
	private double localReward = 0;
	
	// transition probabilities
	private double probCorrectMove = 0;
	private double probWrongMove = 0;
	
	// possile actions
	private String[] actions = new String[] {"UP","DOWN","RIGHT","LEFT"};
	
	
	/*
	 * constructor
	 */
	
	public BlackBoxEnvironment(int n, int seed, double correctProb){
		this.maze = this.getProblemInstance(n, 1, seed);
		this.dimension = n;
		
		// obtainint the initial position of the car
		initialCarRow = 0;
		for (int c=0; c<dimension; c++){
			if (this.maze[0][c] == 1){ // the car
				initialCarColumn = c;
				c = dimension; //exiting of for loop
			}
		}
		
		// setting transitions probabilities and rewards
		
		probWrongMove = (1.0 - correctProb)/3.0;
		probCorrectMove = 1.0 - 3.0*probWrongMove;
		
		localReward = -1.0;
		finalReward = (double) (2*dimension);
		
	}
	
	
	// access methods
	
	public int getInitialCarColumn(){
		return this.initialCarColumn;
	}
	
	public String[] getActions(){
		return this.actions;
	}
	
	/**
	 * check if a given position (cell) corresponds to a final state or not 
	 */
	
	public boolean isGoal(int row, int column){
		if (row == (dimension-1)) return true;
		else return false;
	}	
	
	/**
	 * getReward for a given position (cell)
	 */
	
	public double getReward(int row, int column){
		if (row == (dimension-1)) return finalReward;
		else return localReward;
	}	
	
	
	/*
	 * method to initialise the environment
	 */
	
	private int [][] getProblemInstance(int n, int nCars, int seed){
		int[][] maze = new int[n][n];
		Random gen = new Random(seed);

		// number of walls
		int nWalls = (int) (n*(n-2) * 0.2); 
		
		// placing walls
		for(int i=0; i<nWalls; i++)
			maze[ gen.nextInt(n-2) + 1 ][ gen.nextInt(n)] = -1;
		
		// placing cars, labelled as 1, 2, ..., nCars
		if (nCars > n){
			System.out.println("\n** Error **, number of cars must be <= dimension of maze!!\n");
			System.exit(0);
		}
		ArrayList<Integer> list = new ArrayList<Integer>(n);
		for(int i=0;i<n;i++) 
			list.add(i);
		
		int pos;
		for(int c=0;c<nCars;c++){
			pos = list.remove( gen.nextInt(list.size()));
			//System.out.println(pos);
			maze[0][pos] = c+1;
		}
		
		return maze;
	}
	
	
	/**
	 * Get next position by action.
	 * Returns the obtained cells when DETERMINISTICALLY applying each action over the current one
	 */
	private int[][] getNextPositionsByDeterministicAction(int row, int column){
		int next[][] = new int[this.actions.length][2]; // next row and column by action
		
		for(int a=0;a<this.actions.length; a++){
			
			next[a][0] = row;
			next[a][1] = column;
			
			// accion UP
			if (this.actions[a].equals("UP") && (row > 0) 
						&& (this.maze[row-1][column]>=0)){
				next[a][0] = row-1;
			}
			
			// accion DOWN		
			if (this.actions[a].equals("DOWN") && (row < (this.dimension-1)) 
						&& (this.maze[row+1][column]>=0)){
				next[a][0] = row+1;
			}
			
			// accion RIGHT			
			if (this.actions[a].equals("RIGHT") && (column < (this.dimension-1)) 
					&& (this.maze[row][column+1]>=0)){
				next[a][1] = column+1;
			}
		
			// accion LEFT			
			if (this.actions[a].equals("LEFT") && (column > 0) 
					&& (this.maze[row][column-1]>=0)){
				next[a][1] = column-1;
			}
		
		}
		
		
		return next;
	}
	
	/**
	 * get the index in the vector of actions for the passed (string) action
	 */
	
	private int getActionIndex(String action){
		for(int i=0; i<this.actions.length; i++)
			if (this.actions[i].equals(action)) return i;
			
		return -1; //error: not possible action
	}
	
	
	/**
	 * Apply action. 
	 * 
	 * Receives:
	 * 	   The current cell (row,column)
	 * 	   The action to be applied (UP,DOWN,RIGHT,LEFT) 
	 * 	   An initialised random number generator.
	 * 
	 * Returns an ArrayList with three objects:
	 *     index=0: row of the obtained cell (int)
	 *     index=1: column of the obtained cell (int)
	 *     index=2: obtained reward (double)
	 */
	
	public ArrayList applyAction(int row, int column, String action, Random gen){
		ArrayList outcome = new ArrayList(3); // newX, newY, reward
		
		int[][] next = getNextPositionsByDeterministicAction(row,column);
		int actionIndex = this.getActionIndex(action);
		
		double[] accProbs = new double[this.actions.length];
		double acc = 0.0;
		for(int i=0;i<this.actions.length;i++){
			if (i==actionIndex) acc+= this.probCorrectMove;
			else acc+= this.probWrongMove;
			
			accProbs[i] = acc;
		}
		accProbs[this.actions.length-1] = 1.0; // to avoid round errors
		
		double r = gen.nextDouble();
		int move = -1;
		for(int i=0; i<this.actions.length;i++){
			if (r <= accProbs[i]){
				move = i;
				break;
			}			
		}
		
		outcome.add(next[move][0]);
		outcome.add(next[move][1]);
		if (this.isGoal(next[move][0],next[move][1])) outcome.add(this.finalReward);
		else outcome.add(this.localReward);
		
		return outcome;
	}
	

	/*
	 * Print the input maze.
	 */
	
	public void printMaze( ){
			
		int n=this.maze[0].length;
				
		// upper row
		System.out.println();
		for(int i=0; i<n;i++) 
			System.out.print("--");
		System.out.println("-");
		
		// maze content (by row)
		for(int j=0; j<n;j++){
			System.out.print("|");
			for(int i=0; i<n; i++)
				if (this.maze[j][i] == -1) System.out.print("x|");
				else if (this.maze[j][i] == 1) System.out.print("c|");
				else System.out.print(" |");
			System.out.println("");
		}
		// lower row
		for(int i=0; i<n;i++) 
			System.out.print("--");
		System.out.println("-");
	
	}
		
	
} // end of class



