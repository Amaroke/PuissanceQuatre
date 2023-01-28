package com.example.puissancequatre;

import java.util.ArrayList;
import java.util.List;

public class Etat {

    private Joueur joueur;
    private Plateau plateau;

    public Etat(Plateau plateau){
        this.plateau = plateau;
    }

    public Etat(Etat etat){
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

    public void jouerCoup(int columns){
        EtatCase c = getJoueur() == Joueur.Humain ? EtatCase.Rouge : EtatCase.Jaune;

    }

}
