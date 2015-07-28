/*
 * MyConnection: class that helps to create connection with database
 * Autor: Eduardo Pinhata, Ricardo Nantes Liang,
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnection {
    /* Singleton */

    private static MyConnection singleton = null;

    public static MyConnection getInstance(String ip, String port, String user, String password) {
        try {
            /* Lazy Initialization */
            if (singleton == null || singleton.sqlConnection.isClosed()) {
                singleton = new MyConnection(ip, port, user, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return singleton;
    }
    public Connection sqlConnection;


    //Constructor
    private MyConnection() {
        try {
            //Class.forName("org.sqlite.JDBC");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String textoConexao = "jdbc:mysql://localhost:8889/VIET_TIEN_SIMPLE?user=root&password=root";
            //String textoConexao = "jdbc:mysql://172.17.44.187/bolao_copa?user=root&password=root";
            sqlConnection = DriverManager.getConnection(textoConexao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MyConnection(String ip, String port, String user, String password) {
        try {
            //Class.forName("org.sqlite.JDBC");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String textoConexao = "jdbc:mysql://" + ip + ":" +
		    port + "/F2B_LOGS_DB?user="+ user+ "&password=" + password;
            //String textoConexao = "jdbc:mysql://172.17.44.187/bolao_copa?user=root&password=root";
            sqlConnection = DriverManager.getConnection(textoConexao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
