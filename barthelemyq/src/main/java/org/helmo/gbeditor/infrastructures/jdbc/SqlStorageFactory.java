package org.helmo.gbeditor.infrastructures.jdbc;


import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlStorageFactory {
    private String db = "";
    private String username = "";
    private String password = "";

    public SqlStorageFactory(String driverName, String db, String username, String pass) {
        try {
            Class.forName(driverName);
            this.db = db;
            this.username = username;
            this.password = pass;
        } catch (ClassNotFoundException e) {
            throw new JdbcDriverNotFoundException(driverName);
        }
    }

    public SqlStorage newStorageSession() { //TODO pourquoi n'était pas public ??
        try {
            return new SqlStorage(DriverManager.getConnection(db, username, password));
        } catch (SQLException e) {
            throw new ConnectionFailedException("Unable to acces db " + db, e);
        }
    }
}
