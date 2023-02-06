package puissancequatre;

public class Partie {

    private final Plateau plateau;
    private EnumJoueur joueurActuel;
    private final MCTS ia;
    private EnumPartie etatPartie;
    private boolean robusteActive;


    public Partie(Plateau plateau, MCTS ia) {
        this.plateau = plateau;
        this.joueurActuel = (int) (Math.random() * 2) == 0 ? EnumJoueur.IA : EnumJoueur.HUMAIN;
        this.ia = ia;
        this.etatPartie = EnumPartie.EN_COURS;
        this.robusteActive = false;
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
}
