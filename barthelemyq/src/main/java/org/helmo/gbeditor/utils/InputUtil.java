package org.helmo.gbeditor.utils;

/**
 * Classe utile de vérification des entrées utilisateur
 */
public class InputUtil {

    /**
     * Vérifie si la chaine de caractères est vide ou remplie d'espaces
     * @param string (String) chaine de caractères à vérifier
     * @return (boolean) retourne vrai si la chaine est vide ou remplie d'espaces sinon retourne faux
     */
    public static boolean isEmptyOrBlank(String string) {
        return string.isEmpty() || string.isBlank();
    }

    /**
     * Vérifie si la longueur de la chaine de caractères est bien comprise entre les bornes données
     * @param string (String) chaine de caractères à vérifier
     * @param min (int) longueur minimum (Inclu)
     * @param max (int) longueur maximum (Inclu)
     * @return (boolean) retourne vrai si la longueur de la chaine de caractères est bien comprise entre les bornes données sinon retourne faux
     */
    public static boolean isInBound(String string, int min, int max) {
        return string.length() >= min && string.length() <= max;
    }
}
