import java.util.Scanner;
import java.io.*;
import java.util.Arrays;

public class Grading {
private int status = 0; // value represents the stauts after i/o or button clicked.
// initially, it represents the stuat when login window is activiated only.
private Faculty facultyRole;
private Student studentRole;
private Course current;
private final static String [] item = {"H: homework", "P: project", "Q: quiz", "T: test", "F: final"};
private String itemSelected;
private String [] values={};

private void display(int index){
         double avg = 0;
         
         for(int i = 0; i<item.length; i++){
            int j = index/((int)(Math.pow(10,i)));
            int r = j%10;
            if (r!=0) {
               String [] ret = Trans.read("User", "name", "where id = '"+studentRole.toString()+"'");
               String tmp = ret [0];
               String sname = tmp.substring(0, tmp.indexOf("\n"));
               System.out.println("("+sname+")"+current);
               ret = Trans.read("Course_for_Grade", "score", "where did = '"+studentRole.toString()+"' and "+
                     "name = '"+current.get_name() +"' and "+   
                     "year = '"+current.get_year() +"' and "+   
                     "section = '"+current.get_section() +"' and "+
                     "category like '"+item[i].charAt(0)+"%'");
               System.out.println(item[i]+":\n"+ret[0]);
            }     
         }
}

public Grading(String fac, String stu)
{
   if(!Trans.exist("User")){
      Trans.create("User", "name varchar(20), id varchar(9), passwd varchar(9)"); 
      setting("User", fac);
      setting("User", stu);
   }
}

private void setting(String table, String filename)
{
		try {
      	File file= new File(filename);
      	Scanner inputFile=new Scanner(file);
         String line;
         int n =0;
         int mod = 3;
	
	      while(inputFile.hasNext())
           {
	      	line = inputFile.nextLine();
            n++;	
		      }
         if(n%mod!=0) {
   			System.out.print("Initialization of "+filename+" is not evenly completed,\n");
            System.out.println("please contact your administrator");
            System.exit(0);
         }
         
	      inputFile=new Scanner(file);
         String [] result = new String [mod];
	      while(inputFile.hasNext())
           {
            for(int i = 0; i<mod; i++)
		         result [i] = inputFile.nextLine();
            Trans.write(table, result);
   		   }
   	   inputFile.close();   
			}
		catch (FileNotFoundException e)
		{
			System.out.print("Initialization of "+filename+" is wrong,\n");
         System.out.println("please contact your administrator");
         System.exit(0);
		}		
}

public void event(){
   Scanner kb = new Scanner(System.in);
   int index=0; // for student record display
   int k =-1;  // k=0, 10^0 for 1  
   
   String c_name;
   int c_year;
   int c_section; // for course information

   String tmp="";

   String ans="";
   String did="";
   
   switch(status){
   case 0:
      // login
      System.out.println("Input your user id and passwd please.");
      System.out.print("id (t_xxx for faculty and d_xxx for student):");
      String id = kb.nextLine();
      System.out.print("passwd (pxxx):");
      String pw = kb.nextLine();
      if(id.charAt(0)=='d' && Trans.found("User", "id", "where id='"+id+"' and passwd='"+pw+"'")) {
         status = 1;
         studentRole = new Student(id);
      }
      else if (id.charAt(0)=='t' && Trans.found("User", "id", "where id='"+id+"' and passwd='"+pw+"'")){
         status = 11; 
         facultyRole = new Faculty(id);
      }
      else 
         status = status;
      // this is a simple implementation of abstracction, otherwise,
      // check dxxx in student table and txxx in faculty table.
      break;
   case 1:
      // after student login, need to provide course information. <step 1 of UC1>
      System.out.println("Input your course information please.");
      System.out.print("Course name (e.g., CSC321):");
      c_name = kb.nextLine();
      System.out.print("Course year (e.g., 2014):");
      c_year = kb.nextInt();
      System.out.print("Section number (e.g., 5):");
      c_section = kb.nextInt();
      if(Trans.exist("Course_for_Grade")){
         if(Trans.found("Course_for_Grade", "*", 
            "where Course_for_Grade.name='"+c_name+"' and "+ 
                  " Course_for_Grade.year="+c_year+" and "+
                  " Course_for_Grade.section="+c_section+" and "+
                  " Course_for_Grade.did='"+(studentRole.toString())+"'")){
             status = 2;
             current = new Course(c_name, c_year, c_section); 
             }
          // can be better when "else status = 100" is used here
      }
      // Same as the above comment. As the result, the input will be repeated.
      // It is decided in the UC description. 
      // if the user feels confused here, a confirmation page with more detailed 
      // informatin will be more helpful.
      break;
   case 2:
      // <Step 1 of UC 1.1>
      System.out.println("Would you like to see all grades?");
      System.out.println("Input your choice: Y/N");
      do{
      ans = kb.nextLine();
      }while(ans.length()<1);
      if(ans.toUpperCase().charAt(0)=='Y') 
         status = 3;
      else 
         status = 4;
      break;      
   case 3:
      // <Step 2 of UC 1.1, i.e., UC 1.1.1>
      index = 11111;
      display(index);
      status = 100; // another course? y->1 otherwise  exit
      break;
   case 4:
      // <Step 3 of UC 1.1, i.e., UC 1.1.2>
      ans = "Y";
      index = 0;
      k=-1;
      
      do{
         // available vs. choosen
         if(0<=k && k<item.length) {
               if((int)(index/Math.pow(10,k))%10==0)index+=(int)Math.pow(10,k);
                  else index-=(int)Math.pow(10,k); 
            } 
         System.out.print("display option:");
         for(int i = 0; i<item.length; i++){
            int j = index/((int)(Math.pow(10,i)));
            int r = j%10;
            if (r==0) System.out.print("("+i+")"+item[i]+",");
            }          
         System.out.print("\ndisplay selected:");
         for(int i = 0; i<item.length; i++){
            int j = index/((int)(Math.pow(10,i)));
            int r = j%10;
            if (r!=0) System.out.print("("+i+")"+item[i]+",");
         }
                 
         System.out.print("\nInput your choice 0-"+(item.length-1)+
               " to add/cancel; other number will finalize your selection:"); 
         k = kb.nextInt();
      }while (0<=k && k<item.length);
      System.out.println(index);   
      display(index);
      status = 100; // another course? y->1 otherwise  exit 
      break;
   case 11:
      // teacher <Step 1 of UC 2>
      // check if it is the first 
      // create the course and student tables
      System.out.println("Input your course to initiate/update.");
      System.out.print("Course name (e.g., CSC321):");
      c_name = kb.nextLine();
      System.out.print("Course year (e.g., 2014):");
      c_year = kb.nextInt();
      System.out.print("Section number (e.g., 5):");
      c_section = kb.nextInt();
      if(Trans.exist("Course_for_Grade" )&&
         Trans.found("Course_for_Grade", "*", 
            "where name='"+c_name+"' and "+ 
                  " year="+c_year+" and "+
                  " section="+c_section+" and "+
                  " tid='"+(facultyRole.toString())+"'")){
             current = new Course(c_name, c_year, c_section); 
             status = 13; // update score
      }
      else{
         System.out.println("We cannot find your record.");
         System.out.println("Would you like to create the whole class? Y/N");
         do{
            ans = kb.nextLine();
         }while(ans.length()<1);
         if(ans.toUpperCase().charAt(0)=='Y') {
            status = 12;
            current = new Course(c_name, c_year, c_section);
            }
         else 
            status = 200;
      }
      break;
   case 12: 
      // <Step 2 of UC2>: current and facultyRole
      if(!Trans.exist("Course_for_Grade"))
         Trans.create("Course_for_Grade", 
            "name varchar(20), year int, section int, tid varchar(9), did varchar(9), category varchar(9), score int"); 
      values = new String [7];
      // name of course
      // year
      // section
      // tid as facultyRole.toString()
      // did from name
      // final given latter
      values[3] = facultyRole.toString();
      values[0] = current.get_name();
      values[1] = current.get_year()+"";
      values[2] = current.get_section()+"";   

      do{
         int a;
         System.out.println("Please key in your student names:");
         tmp = kb.nextLine();
         String [] ret = Trans.read("User", "id", "where name = '"+tmp+"'");
         // id + password will be the key to obtain the access 
         // the instructor never gets both and wins a chance here. 
         tmp = ret[0];
         a = tmp.indexOf("\n");
         int n = 0;
         while (a>0){
            tmp = tmp.substring(a+1);
            n++;
            a = tmp.indexOf("\n");
         }
         tmp = ret[0];
         if(n>1) {
            System.out.println("There are multiple records.");
            System.out.println("Please choose your target student: 0-"+(n-1));
            System.out.println(ret[0]);
            do{
               a = kb.nextInt();
            }while(a<0||a>=n);
            for(int i = 0; i<a; i++)
               tmp = tmp.substring(tmp.indexOf("\n")+1);
            if(tmp.indexOf("\n")>=0)
               did = tmp.substring(0, tmp.indexOf("\n"));
            else
               did = tmp;
         }      
         else {
            if(tmp.indexOf("\n")>=0)
               did = tmp.substring(0, tmp.indexOf("\n"));
            else
               did = tmp;
         } 
         
         if (tmp.length()>4 && ((tmp.substring(0,5)).toUpperCase()).equals("EMPTY")){
               System.out.println("Record is not found");
            }
         else {
               // values, did
               values[4] = did;
               values[5] = "final";
               values[6] = "0";
               Trans.write("Course_for_Grade", values); 
               // insertion of Course_for_Grade
               // and confirm the relation between facultyRole and did in current.
            
         } 
         System.out.println("Do you want to input another student?");
         do{
            ans = kb.nextLine();
         }while(ans.length()<1);
      }
      while(ans.toUpperCase().charAt(0)=='Y');        
      status = 200;
      break;
   case 13: 
      // current and facultyRole
      // update, step 1 of UC3, reusing step 1 of UC2; 
      // i.e., this status 13 comes out from status 11
      System.out.println("You have the access to change the scores for any student");  
      for(int i = 0; i< item.length; i++){
         System.out.println(i+":"+item[i]);
      }
      status = 14;
      break;
   case 14: 
      // update, step 2 of UC3. 
      System.out.println("Please determine the category. For instance, input Q1 as quiz 1.");
      do{
         itemSelected = kb.nextLine();
      }while(itemSelected.length()<1||
         (itemSelected.toUpperCase().charAt(0)!='Q'&&
          itemSelected.toUpperCase().charAt(0)!='T'&&
          itemSelected.toUpperCase().charAt(0)!='F'&&
          itemSelected.toUpperCase().charAt(0)!='H'&&
          itemSelected.toUpperCase().charAt(0)!='P'));  
      // facultyRole.toString, current    
      status = 15;
      break;
   case 15:
      // UC 3 step 3
      // course, facultyRole, itemSelected -> student and score.
      System.out.println("Select the name of each student to register the score.");
      String [] ret = Trans.read("Course_for_Grade, User", "did, User.name", 
         "where id = did and "+
               "tid = '"+facultyRole.toString()+"' and "+
               "course_for_grade.name = '"+current.get_name()+"' and "+
               "year ="+current.get_year()+" and "+
               "section ="+current.get_section()+" group by did, User.name");
      did = ret[0];  
      String name = ret[1];        
      System.out.println(name);
      System.out.println(did);

      // facultyRole.toString, current, itemSelected(category)
      // muliple student records      
      k=0;  //reuse k
      tmp=did;
      while(tmp.indexOf("\n")>=0){
            k++;
            tmp=tmp.substring(tmp.indexOf("\n")+1);
      }       
      System.out.print("Please select the student from 0 to "+(k-1)+":");
      int j = kb.nextInt(); 
      if(j>=0&&j<k){
            System.out.print("\nPlease input the score:");
            int score = kb.nextInt();
            for(int i = 0; i<j; i++){
               did = did.substring(did.indexOf("\n")+1);
            }
            did = did.substring(0, did.indexOf("\n"));
      System.out.println("Student:"+did+"("+score+") on "+itemSelected);
            values = new String [7];
            values[0] = current.get_name();
            values[1] = current.get_year()+"";
            values[2] = current.get_section()+"";   
            values[3] = facultyRole.toString();
            values[4] = did;
            values[5] = itemSelected;
            values[6] = score+"";
            if(!Trans.found("Course_for_Grade", "*", 
            "where name='"+values[0]+"' and "+ 
                  " year="+values[1]+" and "+
                  " section="+values[2]+" and "+
                  " tid='"+values[3]+"' and "+
                  " did='"+values[4]+"' and "+
                  " category='"+values[5]+"'"))
               Trans.write("Course_for_Grade",  
                  values); // add new grade
            else
               System.out.println("Record exists!");
               // need update, but not implemented here.
      } 
      System.out.print("Do you like to input another student's record? (Y/N):");
         do{
            ans = kb.nextLine();
         }while(ans.length()<1);
      if(ans.toUpperCase().charAt(0)!='Y')
            status = 200;             
      //otherwise, repeated.      
      break;
   case 100:// student
      System.out.println("Would you like to check another course?");
      System.out.println("Input your choice: Y/N");
      do{
      ans = kb.nextLine();
      }while(ans.length()<1);
      if(ans.toUpperCase().charAt(0)=='Y') 
         status = 1;
      else 
         status = 101;
      break;      
   case 101:
      System.out.println("Would you like to terminate the syste?");
      System.out.println("Otherwise, you will go back to login status.");
      System.out.println("Input your choice: Y (stop) or N (relogin):");
      do{
      ans = kb.nextLine();
      }while(ans.length()<1);
      if(ans.toUpperCase().charAt(0)=='Y') 
         status = 500;
      else{ 
         status = 0;
         studentRole = null;
         facultyRole = null;
         current = null;
      }      
      break;         
   case 200://faculty another course? y->11 otherwise exit
      System.out.println("Would you like to check another course?");
      System.out.println("Input your choice: Y/N");
      do{
         ans = kb.nextLine();
      }while(ans.length()<1);
      if(ans.toUpperCase().charAt(0)=='Y') 
         status = 11;
      else 
         status = 101;
      break;      
   case 500:
      System.out.println("System successfully ends!");
      System.exit(0);
      break;
   default:
      System.out.println("Out of control");
      status = 0;
      break;
   }
}

public String toString()
{
String ret="";
ret+= "Student:"+studentRole+"\t";
ret+= "Faculty:"+facultyRole+"\t";
ret+= "Course:"+current+"\t";
ret+= "Status:"+status;
return ret;
}

public static void main(String [] args)
{
   Grading a=new Grading("admin_fac", "admin_stu");
   while(true){
      a.event();
      System.out.println(a);
   }
}
}