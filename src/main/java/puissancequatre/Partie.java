package puissancequatre;

public class Partie {

    private final Plateau plateau;
    private EnumJoueur joueurActuel;
    private final MCTS ia;
    private EnumPartie etatPartie;
    private boolean robusteActive;

    private boolean affichageIA;
    private int nbSimulations;
    private double probaVictoire;

    public Partie(Plateau plateau, MCTS ia) {
        this.plateau = plateau;
        this.joueurActuel = (int) (Math.random() * 2) == 0 ? EnumJoueur.IA : EnumJoueur.HUMAIN;
        this.ia = ia;
        this.etatPartie = EnumPartie.EN_COURS;
        this.robusteActive = false;
        this.affichageIA = false;
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

    public void setEtatPartie(EnumPartie e) {
        this.etatPartie = e;
    }

    public boolean isRobusteActive() {
        return robusteActive;
    }

    public void setRobusteActive() {
        this.robusteActive = !robusteActive;
    }

    public boolean isAffichageIA() {
        return affichageIA;
    }

    public void setAffichageIA() {
        this.affichageIA = !affichageIA;
    }

    public int getNbSimulations() {
        return nbSimulations;
    }

    public void setNbSimulations(int nbSimulations) {
        this.nbSimulations = nbSimulations;
    }

    public double getProbaVictoire() {
        return probaVictoire;
    }

    public void setProbaVictoire(double probaVictoire) {
        this.probaVictoire = probaVictoire;
    }
}
