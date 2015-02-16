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

	public static void main(String[] args){
		
		Scanner sc = null;
		String fileName = "";
		
		if(args.length < 1){
			printMessage("Please key in a file name");
			exitSystem();
		}//end if 
		
		//Get file name from argument index 0
		fileName = args[0];
		//Create the file with given name
		createFile(fileName);
		
		sc = new Scanner(System.in);

		while(sc.hasNext()){
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
			
			switch(firstCommand){
			
				case "add": //e.g. add abc
					String newLine = str.substring(firstCommandLength + 1);
					writeFile(fileName, newLine);
					break;
					
				case "display":
					readFile(fileName);
					break;
				
				case "delete": //e.g. delete 1
					int lineIndexToRemove = Integer.parseInt(str.substring(firstCommandLength + 1));
					removeOneLine(fileName, lineIndexToRemove);
					break;
					
				case "clear":
					clearAll(fileName);
					break;
					
				case "exit":
					sc.close();
					exitSystem(); //stop program 
					break;
			
				default: //Wrong command
					printMessage("invalid command");
					break;
			
			}//end switch
			
		}//end while
		
	}//end main


	//Print message
	private static void printMessage(String msg){
		System.out.println(msg);
	}//end printMessage
	
	//Exit system
	private static void exitSystem(){
		System.exit(0);
	}//end exitSystem
	
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
	private static void readFile(String filename){
		String line = null;
		int lineIndex = 0;
		
		File file = new File(filename);
		if(file.length() == 0){
			printMessage(filename +" is empty");
			return;
			
		}else{
			try{
				FileReader fileReader = new FileReader(filename);

				//Always wrap FileReader in BufferedReader.
				BufferedReader bufferedReader = new BufferedReader(fileReader);
		
				//Read line by line
				while((line = bufferedReader.readLine()) != null) {
					printMessage(++lineIndex +". "+ line);
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
	private static void removeOneLine3(String filename, int lineIndexToRemove){
		String line = "";
		String lineToRemove = null;
		ArrayList<String> valueArray = new ArrayList<String>();
		
		//read the contents inside the file
		File file = new File(filename);
		if(file.length() == 0){
			printMessage(filename +" is empty");
			return;
			
		}else{
			try{
				FileReader fileReader = new FileReader(filename);

				//Always wrap FileReader in BufferedReader.
				BufferedReader bufferedReader = new BufferedReader(fileReader);
		
				//Read line by line
				while((line = bufferedReader.readLine()) != null) {
					valueArray.add(line);
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
		
		//remove the line from arraylist 
		lineToRemove = valueArray.get(lineIndexToRemove - 1);
		valueArray.remove(lineIndexToRemove - 1);
		
		try {
			 FileWriter fileWriter = new FileWriter(filename, false);
			
			 //Always wrap FileWriter in BufferedWriter
			 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			 
			 for(int i=0; i<valueArray.size(); i++){
				 bufferedWriter.write(valueArray.get(i));
				 bufferedWriter.newLine();
			 }//end for
			 
			 //Always close files
			 bufferedWriter.close();
			 fileWriter.close();
			 
		 }catch(IOException ex) {
			 printMessage(ex.getMessage());
		
		 }finally{
			 printMessage("deleted from " + filename + ": \"" + lineToRemove + "\"");
		 }//end try		
		
	}//end removeOneLine
	*/
	
}//end class
