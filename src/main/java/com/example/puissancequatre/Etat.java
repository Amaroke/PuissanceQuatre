package com.example.puissancequatre;

import java.util.ArrayList;
import java.util.List;

public class Etat {

    private Joueur joueur;
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

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public void jouerCoup(int colonne) {
        EtatCase etatCase = getJoueur() == Joueur.Humain ? EtatCase.Rouge : EtatCase.Jaune;
        plateau.insererJeton(etatCase, colonne);
        setJoueur(getJoueur() == Joueur.Humain ? Joueur.IA : Joueur.Humain);
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
