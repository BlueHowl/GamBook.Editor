package org.helmo.gbeditor.infrastructures.jdbc;


import org.helmo.gbeditor.repositories.exceptions.ConnectionFailedException;
import org.helmo.gbeditor.repositories.exceptions.JdbcDriverNotFoundException;

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

    public SqlStorage newStorageSession() throws ConnectionFailedException{
        try {
            return new SqlStorage(DriverManager.getConnection(db, username, password));
        } catch (SQLException e) {
            throw new ConnectionFailedException("Impossible d'accéder à la base de donnée", e);
        }
    }
}
