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
    private Noeud papas = null;


    public Noeud(Noeud noeudParent) {
        this.papas = noeudParent;
        fils = new ArrayList<>();
        //clement colne ajoute un signe mais je comprends pas pq
    }

}
