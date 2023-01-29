package puissancequatre;

import ui.PartieUI;

public class Partie {

    private final Plateau plateau;
    private EnumJoueur joueurActuel;
    private final MCTS ia;
    EnumPartie etatPartie;
    int[] dernierCoupJoue = new int[2];
    final PartieUI partieUI;


    public Partie(Plateau plateau, MCTS ia, PartieUI partieUI) {
        this.plateau = plateau;
        this.joueurActuel = (int) (Math.random() * 2) == 0 ? EnumJoueur.IA : EnumJoueur.HUMAIN;
        this.ia = ia;
        this.etatPartie = EnumPartie.EN_COURS;
        this.partieUI = partieUI;
    }

    public void setEtatPartie() {
        // Initialisation des directions pour vérifier les alignements possibles
        int[][] dirs = {{-1, -1}, {-1, 1}, {0, -1}, {-1, 0}};
        // Définition de la couleur du jeton en fonction du tour de jeu et du numéro du joueur
        EnumJeton color = (joueurActuel == EnumJoueur.HUMAIN ? EnumJeton.ROUGE : EnumJeton.JAUNE);
        // Initialisation de la variable pour stocker la longueur maximale d'alignement
        int max = 0;
        // Variables pour stocker les positions de la case en cours de vérification
        int x;
        int y;
        // Variable pour stocker la longueur d'alignement dans une direction donnée
        int somme;

        // Boucle pour vérifier chaque direction
        for (int[] dir : dirs) {
            // Réinitialisation de la variable pour stocker la longueur d'alignement
            somme = -1;
            // Position de la case en cours de vérification
            x = dernierCoupJoue[0];
            y = dernierCoupJoue[1];
            // Boucle pour vérifier l'alignement dans une direction donnée (vers l'avant)
            while (x >= 0 && x < plateau.getCOLUMNS() && y >= 0 && y < plateau.getLINES() && plateau.getCase(x, y) == color) {
                x += dir[0];
                y += dir[1];
                somme++;
            }
            // Position de la case en cours de vérification
            x = dernierCoupJoue[0];
            y = dernierCoupJoue[1];
            // Boucle pour vérifier l'alignement dans une direction donnée (vers l'arrière)
            while (x >= 0 && x < plateau.getCOLUMNS() && y >= 0 && y < plateau.getLINES() && plateau.getCase(x, y) == color) {
                x -= dir[0];
                y -= dir[1];
                somme++;
            }
            // Vérification si la longueur d'alignement dans cette direction est plus grande que la précédente
            if (somme > max) max = somme;
        }

        // Si un joueur a gagné
        if (max >= 4) {
            etatPartie = color == EnumJeton.ROUGE ? EnumPartie.VICTOIRE_ROUGE : EnumPartie.VICTOIRE_JAUNE;
        }
        // Si on est dans un cas de match nul
        else if (plateau.isPlateauPlein()) {
            etatPartie = EnumPartie.MATCH_NUL;
        } else {
            etatPartie = EnumPartie.EN_COURS;
        }
    }

    public void changerJoueur() {
        joueurActuel = joueurActuel == EnumJoueur.HUMAIN ? EnumJoueur.IA : EnumJoueur.HUMAIN;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public EnumJoueur getJoueurActuel() {
        return joueurActuel;
    }

    public MCTS getIa() {
        return ia;
    }

    public EnumPartie getEtatPartie() {
        return etatPartie;
    }

    public void setDernierCoupJoue(int[] dernierCoupJoue) {
        this.dernierCoupJoue = dernierCoupJoue;
    }
}
