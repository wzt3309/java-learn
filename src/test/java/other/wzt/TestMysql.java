package other.wzt;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Random;

public class TestMysql extends TestCase{

	public void testInsert(){
		Connection conn=null;	
		StringBuffer sql=null;	
		String sql_prefix="INSERT INTO mytable VALUES";
		String url="jdbc:mysql://localhost:3306/test?"
					+"user=root&password=root&useUnicode=true&characterEncoding=UTF8";
		int MAX=1000;
		int index=100;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Sussessful Load JDBC-Driver");

			conn=DriverManager.getConnection(url);
			Statement stmt=conn.createStatement();

			for(;index<MAX;index++){
				sql=new StringBuffer();
				sql.append(sql_prefix);

				int id=index;
				int category_id=generator(MAX);
				int user_id=generator(MAX);
				int adddate=generator(MAX);

				sql.append("("+id+","+category_id+","+user_id+","+adddate+")");
				System.out.print("SQL["+index+"]:"+sql);
				if(stmt.executeUpdate(sql.toString())<0){
					System.out.println("--------fail");
				}else{
					System.out.println("--------success");
				}

			}

		}catch(SQLException e){
			System.out.println("--------fail");
			e.printStackTrace();

		}catch(Exception e){
			System.out.println("--------fail");
			e.printStackTrace();
		}
	}
	public void Update(){
		Connection conn=null;	
		StringBuffer sql=null;	
		String sql_prefix="UPDATE mytable SET ";
		String url="jdbc:mysql://localhost:3306/test?"
					+"user=root&password=root&useUnicode=true&characterEncoding=UTF8";
		int MAX=100;
		int index=2;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Sussessful Load JDBC-Driver");

			conn=DriverManager.getConnection(url);
			Statement stmt=conn.createStatement();

			for(;index<MAX;index++){
				sql=new StringBuffer();
				sql.append(sql_prefix);

				int id=index;
				int category_id=generator(MAX);
				int user_id=index*2;
				int adddate=generator(MAX);

				sql.append("category_id="+category_id+",user_id="+user_id+",adddate="+adddate);
				System.out.print("SQL["+index+"]:"+sql);
				if(stmt.executeUpdate(sql.toString())<0){
					System.out.println("--------fail");
				}else{
					System.out.println("--------success");
				}

			}

		}catch(SQLException e){
			System.out.println("--------fail");
			e.printStackTrace();

		}catch(Exception e){
			System.out.println("--------fail");
			e.printStackTrace();
		}
	}
	public void Generator(){
		for(int i=0;i<10;i++)
			System.out.println(generator(100));
	}
	public int generator(int range){
		Random rand=new Random();
		return rand.nextInt(range);
	}
}