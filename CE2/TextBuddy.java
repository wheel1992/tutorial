import java.util.*;
import java.io.*;

public class TextBuddy {
	
	/*
	 * Assume the text file used is always mytextfile.txt
	 * and the text file is always locate at the same directory as this program
	 * 
	 * When user enters the file name, program will automatically create a file on same directory
	 * Each user operation will save the content in the file
	 * 
	 * */
	private static final String MESSAGE_INVALID_COMMAND = "invalid command format.";
	private static final String MESSAGE_EMPTY_FILE_NAME = "Please key in a file name.";
			
	private static Scanner sc = null;		
	/*
	 * Store all data inside arraylist
	 * */
	private static ArrayList<String> mDataArray = null;
			
			
	public enum COMMAND_TYPE{
		ADD, DISPLAY, SEARCH, DELETE, CLEAR, EXIT, INVALID, SORT;
	}
	
	
	public static void main(String[] args){
		
		if(args.length < 1){
			printMessage(MESSAGE_EMPTY_FILE_NAME);
			exitSystem();
		}//end if 

		run(args);
		

		
	}//end main

	private static void run(String[] args){
		String fileName = "";
		
		//Get file name from argument index 0
		fileName = args[0];
		//Create the file with given name
		loadFile(fileName);
		initDataArray();
		//createFile(fileName);
		
		sc = new Scanner(System.in);

		
		
		while(sc.hasNext()){
			try{
				String str = sc.nextLine();
				/*
				 * Assume the command is always valid
				 * first word in every sentence is the command
				 * command such as:
				 * add [value]-> add [value]
				 * display -> print out all lines 
				 * delete [value]-> delete the [value] line, [value] is integer 
				 * clear -> remove all content 
				 * exit -> exit program
				 * 
				 * If user enter a invalid command, program will prompt an alert
				 * */
				String firstCommand = str.split(" ")[0];
				int firstCommandLength = firstCommand.length();
				
				COMMAND_TYPE mCommandType = determineCommandType(firstCommand);
				
				switch(mCommandType){
				
					case ADD: //e.g. add abc
						String newLine = str.substring(firstCommandLength + 1);
						writeFile(fileName, newLine);
						break;
						
					case DISPLAY:
						readFile(fileName, true, true); //replace the old arraylist as well
						break;
						
					case SEARCH:
						String keyword = str.substring(firstCommandLength + 1);
						searchKeyword(keyword); //search using keyword
						break;
					
					case SORT:
						sortDataArray(); //sort the data array
						overrideDataArrayToFile(fileName); //override file using data array
						break;
					
					case DELETE: //e.g. delete 1
						int lineIndexToRemove = Integer.parseInt(str.substring(firstCommandLength + 1));
						removeOneLine(fileName, lineIndexToRemove);
						break;
						
					case CLEAR:
						clearAll(fileName);
						break;
						
					case EXIT:
						sc.close();
						exitSystem(); //stop program 
						break;
						
					case INVALID:
						printMessage(MESSAGE_INVALID_COMMAND);
						break;
						
				}//end switch
			
			}catch(Exception e){
				printMessage(MESSAGE_INVALID_COMMAND);
			}
		
		}//end while
		
		
		
	}
	

	private static void initDataArray(){
		mDataArray = new ArrayList<String>();
	}
	
	private static void addDataToArray(String data){
		if(mDataArray != null){
			mDataArray.add(data);
		}
	}
	
	//Print message
	private static void printMessage(String msg){
		System.out.println(msg);
	}//end printMessage
	
	//Exit system
	private static void exitSystem(){
		System.exit(0);
	}//end exitSystem
	
	
	private static COMMAND_TYPE determineCommandType(String cmd){
		if(cmd == null){
			throw new Error("Command cannot be null.");
		}else{
			if (cmd.equalsIgnoreCase("add")) {
				return COMMAND_TYPE.ADD;
				
			} else if (cmd.equalsIgnoreCase("display")) {
				return COMMAND_TYPE.DISPLAY;
			
			} else if (cmd.equalsIgnoreCase("search")) {
				return COMMAND_TYPE.SEARCH;
			
			} else if (cmd.equalsIgnoreCase("sort")) {
				return COMMAND_TYPE.SORT;
				
			} else if (cmd.equalsIgnoreCase("delete")) {
				return COMMAND_TYPE.DELETE;
				
			} else if (cmd.equalsIgnoreCase("clear")) {
				return COMMAND_TYPE.CLEAR;
				
			} else if (cmd.equalsIgnoreCase("exit")) {
			 	return COMMAND_TYPE.EXIT;
			 	
			} else {
				return COMMAND_TYPE.INVALID;
				
			}//end if 
		}//end if 
	}//end determineCommandType

	
	private static void loadFile(String fileName){
		if(isFileExist(fileName)){
			readFile(fileName, true, false);
		}else{
			createFile(fileName);
		}
		
	}
	

	private static boolean isFileExist(String fileName){
		File mFile = new File(fileName);
		return mFile.exists();
	}
	
	
	//Create the new file with given name
	private static void createFile(String filename){
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(filename);
			
		} catch (FileNotFoundException ex) {
			printMessage(ex.getMessage());
			
		}finally{
			pw.close();
			printMessage("Welcome to TextBuddy. "+ filename +" is ready for use");
		}//end try
		
	}//end createFile
	
	
	
	//Read input file
	private static void readFile(String filename, boolean isRefreshDataArray, boolean toListAllValueFromFile){
		String line = null;
		int lineIndex = 0;
		
		File file = new File(filename);
		if(file.length() == 0){
			printMessage(filename +" is empty");
			return;
			
		}else{
			try{
				
				if(isRefreshDataArray){
					initDataArray();
				}
				
				FileReader fileReader = new FileReader(filename);

				//Always wrap FileReader in BufferedReader.
				BufferedReader bufferedReader = new BufferedReader(fileReader);
		
				//Read line by line
				while((line = bufferedReader.readLine()) != null) {
					
					addDataToArray(line);
					
					if (toListAllValueFromFile){
						printMessage(++lineIndex +". "+ line);	
					}
					
				}//end while
				
				//Always close files
				//Close buffer reader after reading
	            bufferedReader.close(); 
	            fileReader.close();
	            
			}catch (FileNotFoundException ex){
				printMessage(ex.getMessage());
				
			}catch(IOException ex) {
				printMessage(ex.getMessage());
			}//end try
			
		}//end if 	
		
	
	}//end readFile
	
	//Write file with the new line
	private static void writeFile(String filename, String newline){
		 try {
			 FileWriter fileWriter = new FileWriter(filename, true);
			
			 //Always wrap FileWriter in BufferedWriter
			 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			 //write() does not automatically append a newline character
			 bufferedWriter.write(newline);
			 bufferedWriter.newLine();
			 
			 //add new line to arraylist
			 addDataToArray(newline);
			 
			 //Always close files
			 bufferedWriter.close();
			 fileWriter.close();
			 
		 }catch(IOException ex) {
			 printMessage(ex.getMessage());
		
		 }finally{
			 printMessage("added to "+ filename + ": \""+ newline + "\"");	
		 }//end try
	
	}//end writeFile
		
	//Remove all content in the file
	private static void clearAll(String filename){
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(filename);
			
		} catch (FileNotFoundException ex) {
			printMessage(ex.getMessage());
			
		}finally{
			pw.close();
			printMessage("all content deleted from "+ filename);
		}
		
	}
	
	
	/*
	 * Remove a line from the text file
	 * */
	private static void removeOneLine(String filename, int lineIndexToRemove){
		String line = "";
		String lineToRemove = null;
		int currentIndex = 0;
		
		try{
			//target is the file that is reading now
			File targetFile = new File(filename);
			FileReader fr = new FileReader(targetFile);
			BufferedReader targetBuffer = new BufferedReader(fr);

			//Open a temporary file
			File tempFile = new File("temp.txt");
			PrintWriter printTemp = new PrintWriter(tempFile);
			
			
			while((line = targetBuffer.readLine()) != null){
				++currentIndex;
				
				if(currentIndex != lineIndexToRemove){
					printTemp.println(line);
					printTemp.flush();
				}else{
					lineToRemove = line;
				}//end if 
				
			}//end while
			
			printTemp.close();
			fr.close();
			targetBuffer.close();

			
			//delete the target file
			targetFile.delete();

			//rename temp file to target file name
			tempFile.renameTo(targetFile);
			
			
		} catch (Exception ex) {
			printMessage(ex.getMessage());
			
		}finally{
			if(lineToRemove != "")
				printMessage("deleted from " + filename + ": \"" + lineToRemove + "\"");
			else
				printMessage("Invalid line index to remove.");
		}//end try
	}
	
	
	/*
	 * Using data from arraylist
	 * replace the content in the file
	 * */
	private static void overrideDataArrayToFile(String filename){
		 try {
			 FileWriter fileWriter = new FileWriter(filename, false);
			
			 //Always wrap FileWriter in BufferedWriter
			 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			 
			 for(String item : mDataArray){
				 bufferedWriter.write(item);
				 bufferedWriter.newLine();
			 }

			 //Always close files
			 bufferedWriter.close();
			 fileWriter.close();
			 
		 }catch(IOException ex) {
			 printMessage(ex.getMessage());
		
		 }finally{
			 printMessage("Has sorted successfully.");	
		 }//end try
	}
	
	
	private static void sortDataArray(){
		Collections.sort(mDataArray);
		for(String item: mDataArray){
			printMessage(item);
		}
	}
	
	
	private static void searchKeyword(String key){
		String result = resultSearchArray(key);
		
		if (result == ""){
			printMessage("No result found.");
		}else{
			printMessage(result);
		}
		
	}
	
	private static String resultSearchArray(String str){
		for(String item: mDataArray){
			if (item.toLowerCase().contains(str)){
				return item;
			}//end if
		}//end for
		
		return "";
	}
	
	
	
	
}//end class
