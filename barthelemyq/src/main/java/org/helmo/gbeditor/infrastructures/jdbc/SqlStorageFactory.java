package org.helmo.gbeditor.infrastructures.jdbc;


import org.helmo.gbeditor.repositories.DataInterface;
import org.helmo.gbeditor.repositories.StorageFactoryInterface;
import org.helmo.gbeditor.repositories.exceptions.ConnectionFailedException;
import org.helmo.gbeditor.repositories.exceptions.JdbcDriverNotFoundException;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe fabricante de repository bd sql
 */
public class SqlStorageFactory implements StorageFactoryInterface {
    private String db = "";
    private String username = "";
    private String password = "";

    private IdKeeper idKeeper;

    /**
     * Constructeur de la classe fabricante de repository bd sql
     * @param driverName (String) Driver
     * @param db (String) chaîne de connexion
     * @param username (String) nom d'utilisateur
     * @param pass (String) mot de passe
     */
    public SqlStorageFactory(String driverName, String db, String username, String pass) {
        try {
            Class.forName(driverName);
            this.db = db;
            this.username = username;
            this.password = pass;

            idKeeper = new IdKeeper();
        } catch (ClassNotFoundException e) {
            throw new JdbcDriverNotFoundException(driverName);
        }
    }

    /**
     * Récupère une nouvelle connexion et renvoie une interface de la nouvelle instance de SqlStorage
     * @return (DataInterface) SqlStorage
     */
    @Override
    public DataInterface newStorageSession() {
        try {
            return new SqlStorage(DriverManager.getConnection(db, username, password), idKeeper);
        } catch (SQLException e) {
            throw new ConnectionFailedException("GameBookEditor : Impossible d'accéder à la base de donnée : " + e.getMessage(), e);
        }
    }
}
