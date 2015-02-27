import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JUnitTest {

	private static TextBuddy mTextBuddy = null;
	
	public static void main(String[] args){
		//mTextBuddy = new TextBuddy();
	}
	
	
	@Test
	public void test1_AllCommand(){
		testCommand("Add command", TextBuddy.COMMAND_TYPE.ADD, "add 1234");
		testCommand("Delete command", TextBuddy.COMMAND_TYPE.DELETE, "delete 2");
		testCommand("Clear command", TextBuddy.COMMAND_TYPE.CLEAR, "clear");
		testCommand("Search command", TextBuddy.COMMAND_TYPE.SEARCH, "search 123");
		testCommand("Sort command", TextBuddy.COMMAND_TYPE.SORT, "sort");
	}
	
	private static void testCommand(String description, TextBuddy.COMMAND_TYPE expectedCmdType, String command){
		assertEquals(description, expectedCmdType, TextBuddy.determineCommandType(TextBuddy.dissectCommand(command)[0]));
	}
	
	
	@Test
	public void test2_FileExist(){
		assertEquals("File exists", true, TextBuddy.isFileExist("test.txt"));
	}
	
	@Test
	public void test3_FileNotExist(){
		assertEquals("File not exists", false, TextBuddy.isFileExist("abc.txt"));
	}
	
	@Test
	public void test4_CreateFile(){
		TextBuddy.createFile("123.txt");
		assertEquals("File exists", true, TextBuddy.isFileExist("123.txt"));
	}
	
	@Test
	public void test5_ReadFile(){
		TextBuddy.readFile("test.txt", true, true);
		assertEquals("Read content from file", "123\n456\n789", TextBuddy.testGetDataFromArray());
	}
	
	@Test
	public void test6_SortArray(){
		TextBuddy.readFile("test_2.txt", true, true);
		TextBuddy.sortDataArray(); //sort the array
		assertEquals("Sort Array", "a\nb\nc\nd\ne\nf", TextBuddy.testGetDataFromArray());
	}
	
	@Test
	public void test7_DeleteOneLine(){
		TextBuddy.readFile("test_2.txt", true, true);
		TextBuddy.removeOneLine("test_2.txt", 2);
		TextBuddy.readFile("test_2.txt", true, false);
		assertEquals("Delete one line", "a\nb\nf\nc\ne", TextBuddy.testGetDataFromArray());
	}
	
	
	
	

}
