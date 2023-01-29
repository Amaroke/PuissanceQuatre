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

    public void jouerCoup(int colonne) {
        EnumJeton etatCase = getJoueur() == EnumJoueur.HUMAIN ? EnumJeton.ROUGE : EnumJeton.JAUNE;
        plateau.insererJeton(etatCase, colonne);
        setJoueur(getJoueur() == EnumJoueur.HUMAIN ? EnumJoueur.IA : EnumJoueur.HUMAIN);
    }

    public List<Integer> getCoupsPossibles() {
        List<Integer> coupsPossibles = new ArrayList<>();
        for (int i = 0; i < plateau.getCOLUMNS(); i++) {
            if (plateau.colonneNonPleine(i)) {
                coupsPossibles.add(i);
            }
        }
        return coupsPossibles;
    }
}
