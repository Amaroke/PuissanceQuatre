package puissancequatre;

import java.util.ArrayList;

public class Noeud {
    private final Noeud noeudPere;
    private final ArrayList<Noeud> noeudsFils;
    private int nbSimulations;
    private Etat etat;
    private final int coup;
    private int total = 0;
    private EnumJoueur joueur;

    public Noeud(Noeud noeudPere, int coup) {
        nbSimulations = 0;
        if (noeudPere != null && coup != -1) {
            etat = new Etat(noeudPere.getEtat());
            this.coup = coup;
            joueur = noeudPere.joueur == EnumJoueur.HUMAIN ? EnumJoueur.IA : EnumJoueur.HUMAIN;
            etat.jouerColonne(coup);
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

    public void ajouterFils(Noeud fils) {
        noeudsFils.add(fils);
    }

    public Noeud getMeilleursFils() {
        double valeurBMax = Double.NEGATIVE_INFINITY;
        Noeud noeudsFils = null;
        for (Noeud fils : this.noeudsFils) {
            if (fils.getNbSimulations() == 0) {
                return fils;
            }
            double valeurB = fils.calculerValeurB();
            if (valeurB > valeurBMax) {
                valeurBMax = valeurB;
                noeudsFils = fils;
            }
        }
        return noeudsFils;
    }


    public int getNbSimulations() {
        return nbSimulations;
    }

    public Noeud getNoeudPere() {
        return noeudPere;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public int getCoup() {
        return coup;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setJoueur(EnumJoueur joueur) {
        this.joueur = joueur;
    }

    public double calculerValeurB() {
        if (joueur == EnumJoueur.IA) {
            return -((double) total / nbSimulations) + Math.sqrt(2) * Math.sqrt(Math.log(noeudPere.getNbSimulations()) / nbSimulations);
        } else {
            return ((double) total / nbSimulations) + Math.sqrt(2) * Math.sqrt(Math.log(noeudPere.getNbSimulations()) / nbSimulations);

        }
    }

    public Noeud getValeurMaxFils() {
        Noeud valeurMaxFils = noeudsFils.get(0);
        double valeurMax = valeurMaxFils.getTotal();
        for (Noeud fils : noeudsFils) {
            double total = fils.getTotal();
            if (total > valeurMax) {
                valeurMax = total;
                valeurMaxFils = fils;
            }
        }
        return valeurMaxFils;
    }

    public Noeud getValeurRobusteFils() {
        Noeud valeurRobusteFils = noeudsFils.get(0);
        double nbSimulationsMax = valeurRobusteFils.getNbSimulations();
        for (Noeud fils : noeudsFils) {
            double nbSimulations = fils.getNbSimulations();
            if (nbSimulations > nbSimulationsMax) {
                nbSimulationsMax = nbSimulations;
                valeurRobusteFils = fils;
            }
        }
        return valeurRobusteFils;
    }

    public Etat getEtat() {
        return etat;
    }

    public void nbSimulationPlusPlus() {
        nbSimulations++;
    }
}
