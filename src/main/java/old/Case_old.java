package old;

import com.example.puissancequatre.EtatCase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Case_old extends ImageView {

    private EtatCase statut;

    public Case() {
        this.statut = EtatCase.Aucun;
    }

    public void set(EtatCase j) {
        Image image = (j == EtatCase.Rouge ? new Image("Rouge.png") : new Image("Jaune.png"));
        this.setImage(image);
        this.statut = j;
    }


    public EtatCase getStatut() {
        return statut;
    }

}
