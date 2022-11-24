package org.helmo.gbeditor.repositories;

/**
 * Interface de la classe frabricante de SqlStorage
 */
public interface StorageFactoryInterface {

    /**
     * Crée une nouvelle connexion et renvoi une instance de SqlStorage via l'interface
     * @return (DataInterface) SqlStorage
     */
    DataInterface newStorageSession();
}
