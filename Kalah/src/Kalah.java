import java.util.Scanner;

import com.source.TwitterMessenger;
import java.io.*;
import java.util.regex.*;

public class Kalah {
	public static boolean player = true;
	//public static int[] board = new int[] {3,3,3,3,3,3,0,3,3,3,3,3,3,0};
	public static int[] board = new int[] {0,3,0,0,0,1,0,0,0,0,0,0,1,0};
	public static int turn = 0;
	//Kevin's - moves made in the game in format ( L1 3 3 3 3 3 3 2 2 2 2 2 2) where 3 = P1 and 2 = P2 and L1 = move made
	public static String[] moves = new String[40]; 
	//Kevin's - increment move counter when a move is made to add more moves
	public static int moveCount = 0;
	//Raab's - no idea LOL
	public static String move;
	public static void main(String[] args){
		boolean game=true;

		String[] moves = new String[40];
		Scanner reader = new Scanner(System.in);
		
		String move;
		int ind = 0;
        System.out.println("Ez algo ez lyfe");
        //Kevin's - temporary initialize move array for the each game
        for(int i = 0; i<40; i++){
        	moves[i] = null;
        }
        moveCount = 0;
        
        
        while (game){
        	Printboard();
        	if (player) System.out.println("Player L's Move");
        	else System.out.println("Player R's Move");
        	move = reader.next();
        	System.out.println(move.substring(0,1));
        	System.out.println(Integer.parseInt(move.substring(1,2)));
        	if ((move.substring(0,1).equals("L")||move.substring(0,1).equals("R"))&&((Integer.parseInt(move.substring(1,2))>0)&&(Integer.parseInt(move.substring(1,2))<7))){
        		if (move.substring(0,1).equals("L")){
        			ind = Integer.parseInt(move.substring(1))-1;
        			if (board[ind]>0 && player){
        				moves[turn] = Move(ind,move);
        				System.out.println("TURN:"+moves[turn]);
        				game = Gamegoing();
        			}
        			else System.out.println("Move invalid.");

        		}
        		else if (move.substring(0,1).equals("R")){      		
        			ind = Integer.parseInt(move.substring(1))+6;
        			if (board[ind]>0 && !player){
        				moves[turn] = Move(ind,move);
        				System.out.println("TURN:"+moves[turn]);
        				game = Gamegoing();
        			}
        			else System.out.println("Move invalid.");
        		}
        	}
        	else System.out.println("Move invalid.");
        	
    		turn++;    
    }
    Printboard();
    if (board[6]>board[13]){
    	System.out.println("Player L wins");
    }
    else if (board[6]<board[13]){
    	System.out.println("Player R wins");
    }
    else{
    	System.out.println("Tie game");
    }
	}
	static String Move (int arnum,String mov){
		int pointer = arnum;
		while (board[arnum]>0){
			pointer++;
			if (player == true && pointer == 13) pointer++;
			if (player == false && pointer == 6) pointer++;
			if (pointer == 14) pointer = 0;

			board[pointer]++;
			board[arnum]--;
			
		}
		String boardstring="";

		if (pointer == 6 || pointer == 13){	
			for (int x=0;x<14;x++){
				boardstring=boardstring+board[x];
			}
			return (mov+" "+boardstring);
		}
		else if (board[pointer]==1){
			if (pointer>6 && !player && board[pointer-((pointer-6)*2)]>0){
				board[13]+=board[pointer]+board[pointer-((pointer-6)*2)];
				board[pointer]=0;
				board[pointer-((pointer-6)*2)]=0;

			}
			else if(pointer<6 && player && board[pointer+((6-pointer)*2)]>0){
				board[6]+=board[pointer]+board[pointer+((6-pointer)*2)];
				board[pointer]=0;
				board[pointer+((6-pointer)*2)]=0;
			}
		}
		for (int x=0;x<14;x++){
			boardstring=boardstring+board[x];
		}
		player = !player;

		return mov+" "+boardstring;}
	static boolean Gamegoing(){
		boolean end=true;
		for (int x=0;x<6;x++){
			if (board[x]>0){ end=false;
		}
		}
		if (end==true) {
			for (int x=12;x>6;x--){
				board[13]+=board[x];
				board[x]=0;
			}
		
			return false;
	}
		end = true;
		for (int x=12;x>6;x--){
			if (board[x]>0) end=false;
		}
		if (end==true) {
			for (int x=0;x<6;x++){
				board[6]+=board[x];
				board[x]=0;
			}
			return false;
		}
		return true;
	}
	static void Printboard(){
		for (int x=13;x>6;x--){
        	System.out.print(board[x]);	
    	}
    	System.out.println(" ");
    	System.out.print(" ");
    	for (int x=0;x<7;x++){
    		System.out.print(board[x]);
    	}
    	System.out.println("");
}
	//Appends any moves not already in the file into the files
		public void learn() throws IOException{		
			String everything = "";
		    String state = "";
		    Pattern patternLog = null;
		    Matcher matcherLog = null;
			//read log file
			BufferedReader br = new BufferedReader(new FileReader("log.txt"));
		    try {
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();

		        while (line != null) {
		            sb.append(line);
		            sb.append(System.lineSeparator());
		            line = br.readLine();
		        }
		        everything = sb.toString();
		    } finally {
		        br.close();
		        System.out.println("found!");
		    }
		    for(int i = 0; i < 40 ; i++){
		    	//go through all moves made
		    	state = moves[i];
		    	if(state == null){
		    		break;
		    	}
		    	patternLog = Pattern.compile("\\w*\\s" + state);
			    matcherLog = patternLog.matcher(everything);
		    	if(matcherLog.matches() == false){
			    	//append to file
		    		System.out.println("Appending!");
			    }
		    }
		}
		public boolean MemoryRecal(String find) {
			try {
				File file = new File("log.txt");
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String line;
				while ((line = bufferedReader.readLine()) != null) {

					String[] parts = line.split("-");
					String board = parts[1];
					String prevMove = parts[0];

					if (board == find) {
						move = 	prevMove;
						fileReader.close();
						bufferedReader.close();
						return true;
					}
				}
				fileReader.close();
				bufferedReader.close();
				return false;
			}catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		/*public String HeuristicMove(){
			if (player){
				for (int x=0;x<6;x++){
					//check if the move will give a free turn
					if (x+board[x]==6){
						if ((x+board[x]<6) && (board[x+board[x]]==0) && (board[x+((6-x)*2)]>0)){
							return "L"+x;
						}
						else if (x+board[x])
						//check if the free move will be advantegeous
					}
				}
				//check for move that will steal points
				//checks for simple free turn
				//check for defensive move
			}
			return fmove;
		}
		*/
}