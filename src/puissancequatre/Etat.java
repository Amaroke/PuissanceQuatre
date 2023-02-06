package puissancequatre;

import java.util.ArrayList;
import java.util.List;

public class Etat {

    private EnumJoueur joueur;
    private final Plateau plateau;

    public Etat(Plateau plateau) {
        this.plateau = plateau;
    }

    public Etat(Etat etat) {
        plateau = new Plateau(etat.getPlateau());
        joueur = etat.getJoueur();
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public EnumJoueur getJoueur() {
        return joueur;
    }

    public void setJoueur(EnumJoueur joueur) {
        this.joueur = joueur;
    }

    public void jouerColonne(int colonne) {
        // On regarde qui joue
        EnumJeton etatCase = getJoueur() == EnumJoueur.HUMAIN ? EnumJeton.ROUGE : EnumJeton.JAUNE;
        plateau.insererJeton(etatCase, colonne);
        // On change de joueur
        setJoueur(getJoueur() == EnumJoueur.HUMAIN ? EnumJoueur.IA : EnumJoueur.HUMAIN);
    }

    public List<Integer> getCoupsPossibles() {
        List<Integer> coupsPossibles = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (plateau.colonneNonPleine(i)) {
                coupsPossibles.add(i);
            }
        }
        return coupsPossibles;
    }
}
