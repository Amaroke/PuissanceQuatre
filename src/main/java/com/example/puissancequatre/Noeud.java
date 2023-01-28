package com.example.puissancequatre;

import java.util.ArrayList;
import java.util.List;

public class Noeud {

    /* de ce que j'ai compris le noeud doit connaitre:
    - parents
    - fils
    - nb desimulation
    - une constante qui permet de regler un compromis (pas compris)
    */

    private ArrayList<Noeud> fils;
    private int nbSimulations = 0;
    private Noeud papa = null;
    private Etat etat;
    private double c;
    private int coup;
    private int valeurTotale = 0;
    private Joueur joueur;



    public Noeud(Noeud papa, int coup) {
        nbSimulations = 0;
        if(papa != null && coup != -1) {
            etat = new Etat(papa.getEtat());

            this.coup = coup;
            joueur = papa.joueur == Joueur.Humain ? Joueur.IA : Joueur.Humain;
            etat.jouerCoup(coup);
        }else{
            this.coup = -1;
            this.etat = null;
        }
        this.papa = papa;
        fils = new ArrayList<>();
    }

    public ArrayList<Noeud> getFils() {
        return fils;
    }

    public Noeud getBestChild() {
        Noeud res = fils.get(0);
        double highestBVal = Double.NEGATIVE_INFINITY;

        for(Noeud fils : fils) {
            if(fils.getNbSimulations() == 0) {
                return fils;
            }
            if(fils.computeBValeur() > highestBVal) {
                highestBVal = fils.computeBValeur();
                res = fils;
            }
        }
        return res;
    }

    public void setFils(ArrayList<Noeud> fils) {
        this.fils = fils;
    }

    public int getNbSimulations() {
        return nbSimulations;
    }

    public void setNbSimulations(int nbSimulations) {
        this.nbSimulations = nbSimulations;
    }

    public Noeud getPapa() {
        return papa;
    }

    public void setPapa(Noeud papa) {
        this.papa = papa;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public int getCoup() {
        return coup;
    }

    public void setCoup(int coup) {
        this.coup = coup;
    }

    public int getValeurTotale() {
        return valeurTotale;
    }

    public void setValeurTotale(int valeurTotale) {
        this.valeurTotale = valeurTotale;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public double computeBValeur() {
        return (valeurTotale/nbSimulations) + c * Math.sqrt(Math.log(papa.getNbSimulations())/nbSimulations);
    }

    public Noeud getFilsMaxVal() {
        Noeud res = fils.get(0);
        double highestVal = res.getValeurTotale();

        for(Noeud fils : fils) {
            if(fils.getValeurTotale() > highestVal) {
                highestVal = fils.getValeurTotale();
                res = fils;
            }
        }

        return res;
    }

    public Noeud getFilsRobusteVal() {
        Noeud res = fils.get(0);
        double highestVal = res.getNbSimulations();

        for(Noeud fils : fils) {
            if(fils.getNbSimulations() > highestVal) {
                highestVal = fils.getNbSimulations();
                res = fils;
            }
        }

        return res;
    }

    public Etat getEtat() {
        return etat;
    }
}
