public class Course{
private String name;
private int year;
private int section;
public Course (String n, int y, int s) {
name = n;
year = y;
section = s;
}
public String get_name(){return name;}
public int get_year(){return year;}
public int get_section(){return section;}
public String toString(){
return "<"+name+","+year+","+section+">";
}
}