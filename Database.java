import java.sql.ResultSet;
import java.sql.*;
public class Database {

    public String url;
    public Database(String url){
        this.url=url;
    }

    public void executeSQL(String sql){
        try(Connection conn = DriverManager.getConnection(url);Statement stmt= conn.createStatement()){
            stmt.execute(sql);

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public void createNewDatabase(){
    try(Connection conn = DriverManager.getConnection(url)){
        if (conn != null){

        }
    }
    catch(SQLException e){
        System.out.println(e.getMessage());
    }
    }



    public ResultSet getResultSet(String sql){
        try( Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
        return rs;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }





}
