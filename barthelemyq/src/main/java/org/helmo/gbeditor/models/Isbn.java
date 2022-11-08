package org.helmo.gbeditor.models;

import org.helmo.gbeditor.customexceptions.IsbnNotValidException;

/**
 * Classe Isbn
 * Crée et stocke des numéros isbn
 */
public class Isbn {

    private final String isbnNumber;

    /**
     * Constructeur de la classe isbn sur base d'un numéro existant
     * @param isbnNumber (String) numéro isbn à stocker
     * @throws IsbnNotValidException si le numéro isbn est invalide
     */
    public Isbn(String isbnNumber) throws IsbnNotValidException {
        if(isbnNumber.matches("^\\d-\\d{6}-\\d{2}-(\\d|x|0)$")) {
            String isbnStart = isbnNumber.substring(0, isbnNumber.length()-1);
            String isbnVerif = isbnNumber.substring(isbnNumber.length()-2);

            if(getIsbnVerification(isbnStart).equals(isbnVerif)) {
                this.isbnNumber = isbnNumber;
            } else {
                throw new IsbnNotValidException("Le code isbn est invalide, le code de vérification ne correspond pas");
            }
        } else {
            throw new IsbnNotValidException("Le code isbn est invalide, veuillez respecter la syntaxe");
        }
    }

    /**
     * Récupère le numéro isbn préalablement généré
     * @return (String) numéro ISBN
     */
    public String getIsbn() { return isbnNumber; }


    /**
     * Génère le numéro ISBN, composé du code de langue 2, des 6 chiffres de l'id auteur (matricule), du numéro unique du livre et du code de vérification
     * @return (String) nouveau numéro ISBN
     */
    public static String generateIsbn(String id, int bookId) {
        String temp = "2-" + id + String.format("-%02d", bookId);
        return temp + getIsbnVerification(temp);
    }

    /**
     * Retourne le résultat du calcul de l'isbn (9 premiers chiffres de l’ISBN chacuns multiplié par un poids allant de 10 à 2)
     * @param isbnStart, premiere partie du numéro isbn (9 premiers chiffres)
     * @return (int) résultat du calcul de vérification
     */
    public static String getIsbnVerification(String isbnStart) {
        int sum = 0;
        int weight = 10;

        char[] sequence = isbnStart.replace("-", "").toCharArray();

        for (char c : sequence) {
            sum += Character.getNumericValue(c) * weight--;
        }

        int code = 11 - sum % 11;

        return (code >= 11 ? "-0" : (code >= 10 ? "-x" : String.format("-%d", code)));
    }

}
