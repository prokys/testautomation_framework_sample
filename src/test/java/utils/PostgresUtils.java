package utils;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class PostgresUtils {


    private static Connection conn = null;
    private static Statement stmt = null;

    public static void connectToDb(){

        String connectionUri = "jdbc:postgresql://localhost:5431/petclinic?user=postgres&password=petclinic";

        try {
            conn = DriverManager.getConnection(connectionUri);

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public static void disconnectFromDb() {

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }
    }

    public static ArrayList<HashMap<String, String>>  executeQuery(String query) {
        boolean executed;
        ResultSet rs = null;
        ArrayList<HashMap<String, String>> results = new ArrayList<>();


        try {
            stmt = conn.createStatement();
            executed = stmt.execute(query);

            if (executed) {
                rs = stmt.getResultSet();
            }

            assert rs != null;
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (rs.next()) {
                HashMap<String, String> rowRecord = new HashMap<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnName = rsmd.getColumnName(i);
                    String columnValue = rs.getString(i);

                    // add key value pair to row record data
                    rowRecord.put(columnName, columnValue);
                }

                // add row record data to the array
                results.add(rowRecord);
            }
        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally {
            // release resources
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    // handle any errors
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    // handle any errors
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
                stmt = null;
            }
        }

        return results;
    }

    public static int executeStatement(String query) {
        int affectedRows = 0;

        try {
            stmt = conn.createStatement();
            affectedRows = stmt.executeUpdate(query);

        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally {
            // release resources
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    // handle any errors
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
                stmt = null;
            }
        }
        // return number of affected rows
        return affectedRows;
    }
}
