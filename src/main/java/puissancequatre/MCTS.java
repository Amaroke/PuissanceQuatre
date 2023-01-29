package puissancequatre;

import java.util.List;
import java.util.Random;

public class MCTS {

    //TODO FAIRE LIA

    private Noeud courant;

    public MCTS() {
        //courant = new Noeud(null);
    }

    public int jouer(Etat etat) {
        List<Integer>  coupsDispos = etat.getCoupsPossibles();
        Random rand = new Random();
        int colonne = -1;
        while(!coupsDispos.contains(colonne)) {
            colonne = rand.nextInt(7);
        }
        return colonne;
    }

}
