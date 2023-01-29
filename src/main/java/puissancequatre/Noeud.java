package puissancequatre;

import java.util.ArrayList;

public class Noeud {
    private Noeud noeudPere;
    private ArrayList<Noeud> noeudsFils;
    private int nbSimulations;
    private Etat etat;
    private int coup;
    private int total = 0;
    private EnumJoueur joueur;

    //TODO Adapter à l'IA et commenter

    public Noeud(Noeud noeudPere, int coup) {
        nbSimulations = 0;
        if (noeudPere != null && coup != -1) {
            etat = new Etat(noeudPere.getEtat());
            this.coup = coup;
            joueur = noeudPere.joueur == EnumJoueur.HUMAIN ? EnumJoueur.IA : EnumJoueur.HUMAIN;
            etat.jouerCoup(coup);
        } else {
            this.coup = -1;
            this.etat = null;
        }
        this.noeudPere = noeudPere;
        noeudsFils = new ArrayList<>();
    }

    public ArrayList<Noeud> getNoeudsFils() {
        return noeudsFils;
    }

    public Noeud getMeilleursFils() {
        Noeud noeudsFils = this.noeudsFils.get(0);
        double valeurBMax = Double.NEGATIVE_INFINITY;
        for (Noeud fils : this.noeudsFils) {
            if (fils.getNbSimulations() == 0) {
                return fils;
            }
            if (fils.calculerValeurB() > valeurBMax) {
                valeurBMax = fils.calculerValeurB();
                noeudsFils = fils;
            }
        }
        return noeudsFils;
    }

    public void setNoeudsFils(ArrayList<Noeud> noeudsFils) {
        this.noeudsFils = noeudsFils;
    }

    public int getNbSimulations() {
        return nbSimulations;
    }

    public void setNbSimulations(int nbSimulations) {
        this.nbSimulations = nbSimulations;
    }

    public Noeud getNoeudPere() {
        return noeudPere;
    }

    public void setNoeudPere(Noeud noeudPere) {
        this.noeudPere = noeudPere;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public int getCoup() {
        return coup;
    }

    public void setCoup(int coup) {
        this.coup = coup;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public EnumJoueur getJoueur() {
        return joueur;
    }

    public void setJoueur(EnumJoueur joueur) {
        this.joueur = joueur;
    }

    public double calculerValeurB() {
        return ((double)total / nbSimulations) + Math.sqrt(2) * Math.sqrt(Math.log(noeudPere.getNbSimulations()) / nbSimulations);
    }

    public Noeud getValeurMaxFils() {
        Noeud valeurMaxFils = noeudsFils.get(0);
        double valeurMax = valeurMaxFils.getTotal();
        for (Noeud fils : noeudsFils) {
            if (fils.getTotal() > valeurMax) {
                valeurMax = fils.getTotal();
                valeurMaxFils = fils;
            }
        }
        return valeurMaxFils;
    }

    public Noeud getFilsValeurRobuste() {
        Noeud valeurRobusteFils = noeudsFils.get(0);
        double valeurMax = valeurRobusteFils.getNbSimulations();
        for (Noeud fils : noeudsFils) {
            if (fils.getNbSimulations() > valeurMax) {
                valeurMax = fils.getNbSimulations();
                valeurRobusteFils = fils;
            }
        }
        return valeurRobusteFils;
    }

    public Etat getEtat() {
        return etat;
    }
}
