import java.util.Scanner;

import com.source.TwitterMessenger;

import java.io.*;
import java.util.regex.*;

public class Kalah {
	public static boolean player = true;
	public static int[] board = new int[] {3,3,3,3,3,3,0,3,3,3,3,3,3,0};
	//public static int[] board = new int[] {0,4,0,0,4,4,5,0,4,1,0,4,4,6};
	public static String boardasstring="3 3 3 3 3 3 0 3 3 3 3 3 3 0 ";
	public static int turn = 0;
	public static String prevMove = "3 3 3 3 3 3 0 3 3 3 3 3 3 0 ";
	//Kevin's - moves made in the game in format ( L1 3 3 3 3 3 3 2 2 2 2 2 2) where 3 = P1 and 2 = P2 and L1 = move made
	public static String[] moves = new String[40]; 
	public static String move;
	public static void main(String[] args) throws IOException{
		boolean game=true;
		boolean playerplz;
		Scanner reader = new Scanner(System.in);

		//String move;
		int ind = 0;
		System.out.println("Ez algo ez lyfe");
		//Kevin's - temporary initialize move array for the each game
		for(int i = 0; i<40; i++){
			moves[i] = null;
		}
		while (game){
			Printboard();

			if (player) System.out.println("Player L's Move");
			else System.out.println("Player R's Move");
			playerplz=player;
			System.out.println("Previous Memory Move: " + MemoryRecal());
			System.out.println("PREDICTION: "+HeuristicMove());
			player=playerplz;
			move = reader.next();
			System.out.println(move.substring(0,1));
			System.out.println(Integer.parseInt(move.substring(1,2)));
			if ((move.substring(0,1).equals("L")||move.substring(0,1).equals("R"))&&((Integer.parseInt(move.substring(1,2))>0)&&(Integer.parseInt(move.substring(1,2))<7))){
				if (move.substring(0,1).equals("L")){
					ind = Integer.parseInt(move.substring(1))-1;
					if (board[ind]>0 && player){
						Move(ind,move);
						moves[turn] = move + " "+ prevMove;
						prevMove = boardasstring;
						System.out.println("TURN:"+moves[turn]);

						game = Gamegoing();
					}
					else System.out.println("Move invalid.");

				}
				else if (move.substring(0,1).equals("R")){      		
					ind = Integer.parseInt(move.substring(1))+6;
					if (board[ind]>0 && !player){
						Move(ind, move);
						moves[turn] = move + " "+ prevMove;
						prevMove = boardasstring;
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
			learn("L");
		}
		else if (board[6]<board[13]){
			System.out.println("Player R wins");
			learn("R");
		}
		else{
			System.out.println("Tie game");
			learn("T");
		}
		reader.close();
	}
	static String Move (int arnum,String mov){
		int pointer = arnum;
		boardasstring = "";
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
				boardasstring = boardasstring+board[x]+" ";
			}
			return (boardstring);
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
			boardasstring = boardasstring+board[x]+" ";
		}
		player = !player;

		return boardstring;}
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
		public static void learn(String winner) throws IOException{		
			String everything = "";
			String state = "";
			Pattern patternLog = null;
			Matcher matcherLog = null;
			//read log file
			File file = new File("log.txt");
			try {
				FileReader fileReader = new FileReader(file);
				BufferedReader br = new BufferedReader(fileReader);
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();
				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}
				everything = sb.toString();
				br.close();
			} finally {
				System.out.println("found!");
				for(int i = 0; i < 40 ; i++){
					//go through all moves made
					state = moves[i];
					if(state == null){
						break;
					}
					String [] temp = state.split(" ");
					state = temp[0]+ " "+ temp[1]+ " "+ temp[2]+ " "+ temp[3]+ " "+ temp[4]+ " "+ temp[5]+ " "+ temp[6]+ " "+ temp[8]+ " "+ temp[9]+ " "+ temp[10]+ " "+ temp[11]+ " "+ temp[12]+ " "+ temp[13]+ "\n";
					String match = temp[1] + " " + temp[2]+ " "+ temp[3]+ " "+ temp[4]+ " "+ temp[5]+ " "+ temp[6]+ " "+ temp[8]+ " "+ temp[9]+ " "+ temp[10]+ " "+ temp[11]+ " "+ temp[12]+ " "+ temp[13];
					//System.out.println(temp);
					System.out.println(match);
					patternLog = Pattern.compile(match);
					matcherLog = patternLog.matcher(everything);
					if(!matcherLog.find()){
						//append to file
						System.out.println("Appending!");
						System.out.println(state.substring(0,1));
						if(winner.equals("L") && state.substring(0,1).equals("L")){
							FileWriter fileWritter = new FileWriter(file.getName(),true);
							BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
							bufferWritter.write(state);
							bufferWritter.close();
						}
						if(winner.equals("R") && state.substring(0,1).equals("R")){
							FileWriter fileWritter = new FileWriter(file.getName(),true);
							BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
							bufferWritter.write(state);
							bufferWritter.close();
						}
						if(winner.equals("T")){
							FileWriter fileWritter = new FileWriter(file.getName(),true);
							BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
							bufferWritter.write(state);
							bufferWritter.close();
						}
					}
				}
				System.out.println("Done!");
			}
		}
	// boolean to create a value if the recall is successful. If not, then use heuristic
	// takes input of the current state of board as string 044433333333
		static public String MemoryRecal() {

			//accessing past experience log file
			try {
				File file = new File("log.txt");
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String line;
				
				String [] temp = boardasstring.split(" ");
				
				String match = temp[0] + " " + temp[1]+ " "+ temp[2]+ " "+ temp[3]+ " "+ temp[4]+ " "+ temp[5]+ " "+ temp[7]+ " "+ temp[8]+ " "+ temp[9]+ " "+ temp[10]+ " "+ temp[11]+ " "+ temp[12];

				//taking in line by line, comparing previous board states to find one that matches current
				while ((line = bufferedReader.readLine()) != null) {
					
					if (line.length()!= 0){
						String prevMove = line.substring(0,2);
						String prevBoard = line.substring(3);
						prevBoard = prevBoard.replaceAll("\n","");
		
						//if a match is found, set the move to the previous move
						if (match.equals(prevBoard)) {
							move =  prevMove;
							fileReader.close();
							bufferedReader.close();
							return prevMove;
						}
					}
				}
				// no match is found, return false
				fileReader.close();
				bufferedReader.close();
				return "No previous scenario";
			}
			//IO error catching
			catch (IOException e) {
				e.printStackTrace();
			}
			return "Could not read file";
		}
	static public String HeuristicMove(){
		String fboard; 
		int[] copyboard = new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int[] futureboard = new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int moveprio = 6;
		String moveoption="";
		if (player){
			for (int x=0;x<6;x++){
				//check if the move will give a free turn
				if (board[x]!=0){
					if (x+board[x]==6){
						copyboard = boardreplace(board);
						fboard = Move(x,"L"+(x+1));
						if (!player) player=!player;
						board = boardreplace(copyboard);
						for (int z=0;z<futureboard.length;z++){
							futureboard[z] = Integer.parseInt(fboard.substring(z,z+1));
						}
						for (int y=0;y<6;y++){                                                       
							if ((y+futureboard[y]<6) && (futureboard[y+futureboard[y]]==0) && (futureboard[(y+futureboard[y])+((6-(y+futureboard[y]))*2)]>0)) return "L"+(x+1);
							//check if the free move will be advantegeous
							else if (y+futureboard[y]==0 && moveprio > 1) {
								moveoption= "L"+(x+1);
								moveprio=1;
							}
							else if (futureboard[y+7]==0){
								for (int z=y+7;z>6;z--){
									if (z + futureboard[z]!=y+7 && moveprio > 4){
										moveoption = "L"+(x+1);
										moveprio=4;
									}
								}
							}
							else if (moveprio > 3){
								moveoption = "L"+(x+1);
								moveprio = 3;
							}
						}
					}


					else if ((x+board[x]<6) && (board[x+board[x]]==0) && (board[(x+board[x])+((6-(x+board[x]))*2)]>0)&&(moveprio > 2)){
						moveoption = "L"+(x+1);
						moveprio=2;
					}
					else{
						copyboard = boardreplace(board);
						fboard = Move(x,"L"+(x+1));
						if (!player) player=!player;
						board = boardreplace(copyboard);
						for (int z=0;z<futureboard.length;z++){
							futureboard[z] = Integer.parseInt(fboard.substring(z,z+1));
						}
						for (int w=7;w<13;w++){
							if (futureboard[w]==0){
								for (int z=w;z>6;z--){
									if (z + futureboard[z]!=w && moveprio > 4){
										moveoption = "L"+(x+1);
										moveprio=4;
									}
								}
							}
						}
					}

					//check for move that will steal points
					//checks for simple free turn
					//check for defensive move
				}
			}
			if (moveoption == ""){
				for (int x=5;x>=0;x--){
					if (board[x]!=0) moveoption = "L"+(x+1);
				}
			}
		}
		if (!player){
			for (int x=12;x>6;x--){
				copyboard = boardreplace(board);
				fboard = Move(x,"R"+(x+1));
				if (player) player=!player;
				board = boardreplace(copyboard);
				//check if the move will give a free turn
				if (board[x]!=0){
					if (x+board[x]<=13){	
						if (x+board[x]==13){

							for (int z=0;z<futureboard.length;z++){
								futureboard[z] = Integer.parseInt(fboard.substring(z,z+1));
							}
							for (int y=12;y>6;y--){
								if(futureboard[y]+y<13){                                                
									if ((y+futureboard[y]>6) && (futureboard[y+futureboard[y]]==0) && (futureboard[(y+futureboard[y])-(((y+futureboard[y])-6)*2)]>0)) return "R"+(x-6);
									//check if the free move will be advantegeous
									else if (y+futureboard[y]==0 && moveprio > 1) {
										moveoption= "R"+(x-6);
										moveprio=1;
									}
								}
								else if (futureboard[y-7]==0){
									for (int z=0;z<6;z++){
										if (z + futureboard[z]!=y-7 && moveprio > 4){
											moveoption = "R"+(x-6);
											moveprio=5;
										}
									}
								}
								else if (moveprio > 3){
									moveoption = "R"+(x-6);
									moveprio = 3;
								}
							}

						}
						else if (((x+board[x]>6) && (board[x+board[x]]==0) && (board[(x+board[x])-(((x+board[x])-6)*2)]>0)) && (moveprio>2)){
							moveoption = "R"+(x-6);
							moveprio=2;
						}
						else{
							copyboard = boardreplace(board);
							fboard = Move(x,"L"+(x+1));
							if (!player) player=!player;
							board = boardreplace(copyboard);
							for (int z=0;z<futureboard.length;z++){
								futureboard[z] = Integer.parseInt(fboard.substring(z,z+1));
							}
							for (int w=0;w<6;w++){
								if (futureboard[w]==0){
									for (int z=w;z<6;z++){
										if (z + futureboard[z]!=w && moveprio > 4){
											moveoption = "R"+(x-6);
											moveprio=4;
										}
									}
								}
							}
						}



						//check for move that will steal points
						//checks for simple free turn
						//check for defensive move
					}
				}
			}
			if (moveoption == ""){
				for (int x=12;x>6;x--){
					if (board[x]!=0) moveoption = "R"+(x-6);
				}
			}
		}
		return moveoption;
	}
	static public int[] boardreplace(int[] boardr){
		int[] boardreplaced = new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		for (int x = 0;x<14;x++){
			boardreplaced[x]=boardr[x];
		}
		return boardreplaced;
	}
}