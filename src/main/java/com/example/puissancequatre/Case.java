package com.example.puissancequatre;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Case extends ImageView {

    private Jeton statut;

    public Case() {
        this.statut = Jeton.Aucun;
    }

    public void set(Jeton j) {
        Image image = (j == Jeton.Rouge ? new Image("Rouge.png") : new Image("Jaune.png"));
        this.setImage(image);
        this.statut = j;
    }


    public Jeton getStatut() {
        return statut;
    }

}
