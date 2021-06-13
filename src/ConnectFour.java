import java.util.Random;

public class ConnectFour {
	
	
	private String finalInsult;
	
	private String[] insult= {
			"I saw 14,000,605 futures. You lose in all of them.", "That's checkmate in 5 ... Whoops wrong game ",
			"At least try to connect 3 of them ", "I get headache trying to think down to your level","Looks like you evolved from a monkey","Not Bad for a Human",
			"That's your move? I haven't considered that, or did i?","Are you bluffing me?"
	};
			
	//0-field empty
	//1-filed blue
	//-1-field red
	private int[][] board = new int[7][6];
	private int winner =0;
	//red player plays first
	int turn = -1;
				
	//returns true if game is finished
	//winner of game is stored @winner 
	
	public boolean isOver() {
		 return isOver(this.board,this.turn);
	}
	
	//Method for debugging	
	public void print2D(int mat[][])   {
        // Loop through all rows
        for (int i = 0; i < mat.length; i++) {
            // Loop through all elements of current row
            for (int j = 0; j < mat[i].length; j++) {
            	System.out.print(mat[i][j] + " ");
            }
          System.out.println();     
        }
        
    }
	
	
		
	private boolean isOver(int board[][],int turn) {				
		for(int i=0;i<board.length;i++) {
			for(int j=0;j<board[i].length;j++) {
				if (board[i][j]==0)
						continue;
				//Check rows 
				if(i<4) {
					if(board[i][j]==board[i+1][j] && board[i+1][j]==board[i+2][j] && board[i+2][j]==board[i+3][j]) {
						winner=board[i][j];
						return true;	
					}
				}
				//Check columns
				if(j<3) {
					if(board[i][j]==board[i][j+1] && board[i][j+1]==board[i][j+2] && board[i][j+2]==board[i][j+3]) {
						winner=board[i][j];
						return true;
					}
				}
				// Check diangoal (left down to right up)
				if(i<4 && j<3) {
					if(board[i][j]==board[i+1][j+1] && board[i+1][j+1]==board[i+2][j+2] && board[i+2][j+2]==board[i+3][j+3]) {
						winner=board[i][j];
						return true;
					}
				}
				// Check diangoal (left up to right down)
				if(i>2 && j<3) {
					if(board[i][j]==board[i-1][j+1] && board[i-1][j+1]==board[i-2][j+2] && board[i-2][j+2]==board[i-3][j+3]) {
						winner=board[i][j];
						return true;
					}
				}
			}
		}
		//Check if it is draw 
		for(int i=0;i<7;i++) {
			if(board[i][5]==0) {
				break;
			}
			if(i==6) {
				return true;
			}			
		}
	
		return false;
	}
	
	public int monteCarlo() {
		int[] monteCarlo= new int[7];   //Store values of each move    

		for(int i=0;i<7;i++) {			//Check what moves are playable than call minMax of every move with small depth (4)
			if(board[i][5]!=0) {
				if (turn==-1)
					monteCarlo[i]=1000000;
				else
					monteCarlo[i]=-1000000;
			}
			else {
				int t1=turn;;
				int[][] b = new int[7][6];
				for(int k=0;k<b.length;k++){
				    for(int j=0;j<b[k].length;j++){
				        b[k][j]=board[k][j];
				    }
				}
				play(i,b,turn);
				monteCarlo[i]=-minMax(7,b,turn*-1);
				turn=t1;
			}
		}				
		//Simulate 70k games
		for(int j=0;j<10000;j++) {
			for(int i=0;i<7;i++) {
				if(monteCarlo[i]>=10000 || monteCarlo[i]<=-10000)
					continue;
				monteCarlo[i]+=monteCarlo(i);
			}			
		}	
		int largest = monteCarlo[0], indexMax = 0;     // Find best move 
		int smallest =monteCarlo[0], indexMin = 0;
		for (int i = 1; i < monteCarlo.length; i++) {
			  if (monteCarlo[i] > largest ) {
			      largest = monteCarlo[i];
			      indexMax = i;
			   }
			  if(monteCarlo[i]<smallest) {
				  smallest=monteCarlo[i];
				  indexMin=i;
			  }
		}		
				
		Random r = new Random();
		if(turn==-1) {
			if(smallest<0) {
				finalInsult=insult[r.nextInt(4)+4];
			}
			else {
				finalInsult=insult[r.nextInt(4)];	
			}
			return indexMin;			
		}
		else 
			if(largest>0) {
				finalInsult=insult[r.nextInt(4)];			
			}
			else {
				finalInsult=insult[r.nextInt(4)+4];	
			}
			return indexMax;		
	}
	
	private int monteCarlo(int row) {
		int w1=winner;
		int t = turn;
		int[][] b = new int[7][6];
		for(int i=0;i<b.length;i++){
		    for(int j=0;j<b[i].length;j++){
		        b[i][j]=board[i][j];
		    }
		}		
		play(row);
		Random r = new Random();
		while(!isOver()) {			
			play(r.nextInt(7));
		}
		
		for(int i=0;i<b.length;i++){
		    for(int j=0;j<b[i].length;j++){
		        board[i][j]=b[i][j];
		    }
		}
		
		turn=t;
		int w2= winner;
		winner=w1;				
		return w2;
	}

	private int minMax(int depth,int[][] board,int turn) {	
		if(depth==0 || isOver(board,turn)){							
			int w1=winner;	
			winner=0;
			return w1*-10000;			
		}		
		if(turn==-1) {			
			int bestVal=-100000;
			for (int i=0;i<7;i++) {
				if(board[i][5]!=0) {
					return 0;
				}
				int undo=play(i,board,turn);
				bestVal=Math.max(bestVal,minMax(depth-1,board,turn*-1));
				board[i][undo] =0;
				
			}
			return bestVal;						
		}
		else {
			int bestVal=100000;
			for (int i=0;i<7;i++) {
				if(board[i][5]!=0) {
					return 0;
				}				
				int undo=play(i,board,turn);						
				bestVal=Math.min(bestVal,minMax(depth-1,board,turn*-1));
				board[i][undo] =0;				
			}
			return bestVal;					
		}
	}
	
	public int play(int row) {
		turn=turn*-1;
		return play(row,this.board,this.turn*-1);
	}
	
	private int play(int row,int board[][],int turn) {
		if(row<0 || row>6)  {
			throw new IllegalArgumentException();
		}
		if(board[row][5]==-1 || board[row][5]==1)
			return -1;
		for(int j=0;j<6;j++) {
			if(board[row][j]==0) {
				board[row][j]=turn;
				return j;
			}
		}
		return -1;
	}	
	public ConnectFour() {
		for(int i=0;i<board.length;i++) {
			for(int j=0;j<board[i].length;j++) {
				board[i][j]=0;
			}
		}
	}
	public int[][] getBoard() {
		return board;
	}
	public int getWinner() {
		return winner;
	}
	public void setBoard(int[][] board) {
		this.board = board;
	}
	
	public int getTurn() {
		return turn;
	}
	public String getFinalInsult() {
		return finalInsult;
	}
}
