# JavaMySQL

These provided files branch was the base of what we needed to make a drop in addon to use their methods.
The provided TestMysql.java confirms the JDBC connection with the MySQL server on the device where the program is being run.

The goal was to make a Walkthrough.java file which used the Trans.java class to implement MySQL functions.
It needed to be able to create a new table, insert a new row, and select then display some records.

Trans.java needed to be edited to have the correct information for your own MySQL server.
Importantly, while you were allowed to edit Trans.java, the code reviewer would be using the base Trans.java.

This means you need to create a highly portable Walkthrough.java class.
If the reviewer had a different MySQL url in Trans.java than you it needed to work with your Walkthrough.java
