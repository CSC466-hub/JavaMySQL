/**
 * To design and implement a java file to operate SQL database.
 * CSC 321 Data Base Management Systems
 * Final Project
 * @author John Lysle
 * @version 2020/12/05
 */
import java.sql.*;
import java.util.Scanner;

public class Walkthrough {

//String Array for displaying
private static String [] values={};
// Main Logic of Program   
   public static void main(String[] args) {
   char choice;
   Scanner input = new Scanner(System.in);
   while(true){
   choice = menu();
   if (choice == 'Q'){
   break;
   }
   switch(choice)
      {
       case 'C': createTable();
                System.out.println("\nPress the enter key to continue");
                input.nextLine();
       break;
       
       case 'I': createEntry();
                System.out.println("\nPress the enter key to continue");
                input.nextLine();
       break;
       
       case 'S': selectRecords();
                System.out.println("\nPress the enter key to continue");
                input.nextLine();
       break;
       }
   }

}

   /**
    * Simple menu
    * @return a char to the main logic for use in a case there
    */ 
   static char menu()
   {
      Scanner menuInput = new Scanner(System.in);
      System.out.println("\nSelect Activity below:\n");
      System.out.println("\t[C]reate new Table\n");
      System.out.println("\t[I]nsert new row\n");
      System.out.println("\t[S]elect some record\n");
      System.out.println("\t[Q]uit\n");
      System.out.println("Enter the letter in the \"[ ]\": ");
      char choice = menuInput.next().charAt(0);
      choice = Character.toUpperCase(choice);
      return choice;
    }

   /**
    * Method for creating table.
    */ 
static void createTable(){

   Scanner tScanner = new Scanner(System.in);
   while(true){
   System.out.println("\nThis option will create a new table.");
   System.out.print("Enter desired table name:");
   String tName = tScanner.nextLine();
   System.out.println("\nThis option will take in the table attributes.");
   System.out.println("WARNING: Invalid atributes will result in exception.");
   System.out.print("Enter desired table attributes:");
   //"name varchar(20), id varchar(9), passwd varchar(9)"
   String tAtt = tScanner.nextLine();
   
   
   if(!Trans.exist(tName)){
      Trans.create(tName, tAtt);
      System.out.println("Would you like to create aother table? Enter N to exit.");
   }else{
   System.out.println("Error: Table with this name already exists.");
   System.out.println("Would you like to try again? Enter N to exit.");
   }
   
   char enter = tScanner.next().charAt(0);
   enter = Character.toUpperCase(enter);
   tScanner.nextLine();
   if(enter=='N'){
   break;
   }
   }
}

   /**
    * Method for inserting entry(Row)
    */ 
static void createEntry(){

   Scanner eScanner = new Scanner(System.in);
   while(true){
   System.out.println("\nThis option will create a new entry.");
   System.out.print("Enter name of table to add entry:");
   String eName = eScanner.nextLine();
   
   int counter = 0;
   System.out.print("\nEnter number of attributes:");
   while(counter<1){
           while (!eScanner.hasNextInt()) 
           {        
           eScanner.next();
           System.out.print("Please enter an integer: ");
         }  
       counter = eScanner.nextInt();
       if(counter<1)
       System.out.print("Please enter an integer greater than zero: ");
    }
   values = new String [counter];
   eScanner.nextLine();
   
   for(int i = 0; i<counter;i++){

   System.out.println("\nTaking in table attributes.");
   System.out.println("WARNING: Invalid atributes will result in exception.");
   System.out.printf("Enter attribute[%d]:",i+1);

   values[i]= eScanner.nextLine();
   }
   
   if(Trans.exist(eName)){
      Trans.write(eName, values);
      System.out.println("Would you like to create another entry? Enter N to exit.");
   }else{
   System.out.println("Error: Table does not exist.");
   System.out.println("Would you like to try again? Enter N to exit.");
   }
   
   char enter = eScanner.next().charAt(0);
   enter = Character.toUpperCase(enter);
   eScanner.nextLine();
   if(enter=='N'){
   break;
   }
   
   }
}

// e.g., read("abc", "qty", "order by qty") for "select qty from abc order by qty;"
//found(String table, String column, String condition)
   /**
    * Method for searching within a column
    */ 
static void selectRecords(){
   Scanner srScanner = new Scanner(System.in);
   while(true){
   System.out.println("\nThis option will search a column.");
   System.out.print("Enter name of table to search:");
   String srName = srScanner.nextLine();
   
   System.out.print("\nEnter name of column to search:");
   String srColumn = srScanner.nextLine();
   
   System.out.print("\nEnter your condition (i.e. where value = x:");
   String srCond = srScanner.nextLine();
   
   String [] ret = Trans.read(srName, srColumn, srCond);
   
   
   if(Trans.exist(srName)){
      System.out.println("\nResults of search:");
      System.out.println(ret[0]);
      System.out.println("Would you like to try another query? Enter N to exit.");
   }else{
   System.out.println("Error: Table does not exist.");
   System.out.println("Would you like to try again? Enter N to exit.");
   }
   char enter = srScanner.next().charAt(0);
   enter = Character.toUpperCase(enter);
   srScanner.nextLine();
   if(enter=='N'){
   break;
   }
   
   }

}

}