package fr.hydrogen.bot.other;

public class IsRightType {

    public static boolean isInt(String chaine) {
        boolean valeur = true;
        char[] tab = chaine.toCharArray();

        for (char carac : tab) {
            if (!Character.isDigit(carac) && valeur) {
                valeur = false;
            }
        }
        return valeur;
    }
}
