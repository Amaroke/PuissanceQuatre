package ui;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import puissancequatre.*;

public class PartieUI extends Application {

    private Partie partie;
    private ImageView[][] casesUI;
    private static int previousSelection;
    Label gameState;
    Label victoryState;

    @Override
    public void start(Stage primaryStage) {

        // On crée la partie.
        partie = new Partie(new Plateau(), new MCTS(), this);
        int nbColonnes = partie.getPlateau().getCOLUMNS();
        int nbLignes = partie.getPlateau().getLINES();

        // On crée la fenêtre de base.
        Group root = new Group();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        // On la fait en fonction de la taille de l'écran.
        Scene scene = new Scene(root, screenBounds.getWidth() * 0.9, screenBounds.getHeight() * 0.9);

        // On crée la grille bleue du jeu.
        int width = (int) (screenBounds.getWidth() / (nbColonnes + 2) * nbColonnes);
        int height = (int) (screenBounds.getHeight() / (nbLignes + 2) * nbLignes);
        int borderX = (int) (screenBounds.getWidth() - width) / 4;
        // On utilise borderX pour centre la grille au milieu de la fenêtre.
        Rectangle r = new Rectangle(borderX, 0, width, height);
        r.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.BLUE), new Stop(0.5, Color.rgb(0, 0, 100)), new Stop(1, Color.BLUE)));
        root.getChildren().add(r);

        // On crée les différentes cases de la grille.
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                Circle c = new Circle(150 * j, 150 * i, 45);
                c.setFill(Color.WHITE);
                c.radiusProperty().bind(r.heightProperty().divide(12).subtract(5));
                c.centerXProperty().bind(r.widthProperty().divide(nbColonnes).multiply(j + 0.5).add(borderX));
                c.centerYProperty().bind(r.heightProperty().divide(nbLignes).multiply(i + 0.5));
                root.getChildren().add(c);
            }
        }

        // On crée les images Cases.
        casesUI = new ImageView[nbColonnes][nbLignes];
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                casesUI[j][i] = new ImageView();
                casesUI[j][i].layoutXProperty().bind(r.widthProperty().divide(nbColonnes).multiply(j).add(borderX * 1.37));
                casesUI[j][i].layoutYProperty().bind(r.heightProperty().divide(nbLignes).multiply(i));
                casesUI[j][i].fitHeightProperty().bind(r.heightProperty().divide(nbLignes));
                casesUI[j][i].fitWidthProperty().bind(r.widthProperty().divide(nbColonnes));
                casesUI[j][i].setPreserveRatio(true);
                root.getChildren().add(casesUI[j][i]);
            }
        }

        // Création de l'affichage du tour.
        gameState = new Label("C'est au tour " + (partie.getJoueurActuel() == EnumJoueur.HUMAIN ? "du joueur (rouge)." : "de l'IA (jaune)."));
        gameState.setTextFill(Color.BLACK);
        gameState.setFont(Font.font("Arial", scene.getHeight() / 20));
        gameState.setLayoutX(borderX);
        gameState.layoutYProperty().bind(r.heightProperty());

        // Création de l'affichage de la victoire.
        victoryState = new Label("");
        victoryState.setFont(Font.font("Arial", scene.getHeight() / 20));
        victoryState.setLayoutX(borderX);
        victoryState.layoutYProperty().bind(r.heightProperty());
        victoryState.setVisible(false);

        root.getChildren().addAll(gameState, victoryState);

        // Création des cadres de positionnement.
        Rectangle[] rects = new Rectangle[nbColonnes];
        for (int i = 0; i < nbColonnes; i++) {
            rects[i] = new Rectangle(0, 0, 10, 10);
            rects[i].layoutXProperty().bind(r.widthProperty().divide(nbColonnes).multiply(i).add(borderX));
            rects[i].heightProperty().bind(r.heightProperty());
            rects[i].widthProperty().bind(r.widthProperty().divide(nbColonnes));
            rects[i].setFill(Color.TRANSPARENT);
            rects[i].setStroke(Color.BLACK);
            rects[i].setStrokeType(StrokeType.INSIDE);
            rects[i].setStrokeWidth(4);
            rects[i].setVisible(false);
            root.getChildren().addAll(rects[i]);
        }

        Rectangle r2 = new Rectangle(0, 0, 10, 10);
        r2.heightProperty().bind(r.heightProperty());
        r2.widthProperty().bind(r.widthProperty());
        r2.setFill(Color.TRANSPARENT);
        root.getChildren().addAll(r2);

        previousSelection = -1;
        r2.setOnMouseMoved(e -> {
            int val = (int) ((e.getX() - borderX) / (r.getWidth() / nbColonnes));
            if (val != previousSelection) {
                rects[val].setVisible(true);
                if (previousSelection > -1)
                    rects[previousSelection].setVisible(false);
            }
            previousSelection = val;
        });

        // Le joueur joue.
        r2.setOnMouseClicked(e -> {
            int colonne = (int) ((e.getX() - borderX) / (r.getWidth() / nbColonnes));
            // Gestion du placement du jeton.
            if (partie.getPlateau().colonneNonPleine(colonne) && partie.getEtatPartie() == EnumPartie.EN_COURS) {
                int ligne = partie.getPlateau().insererJeton(EnumJeton.ROUGE, colonne);
                int[] dernierCoup = {colonne, ligne};
                this.partie.setDernierCoupJoue(dernierCoup);
                // Changement de joueur, rafraichissement de l'UI.
                partie.changerJoueur();
                partie.setEtatPartie();
                this.rafraichirUI();
                this.faireJouerIA();
            }
        });

        // On paramètre la fenêtre.
        primaryStage.setTitle("IA - Puissance 4");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        this.faireJouerIA();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void faireJouerIA() {
        //TODO mettre un temps de latence pendant que l'IA joue (géré par MCTS ?)
        if(partie.getJoueurActuel() == EnumJoueur.IA && partie.getEtatPartie() == EnumPartie.EN_COURS) {
            int colonne = partie.getIa().jouer(new Etat(partie.getPlateau()));
            int ligne = partie.getPlateau().insererJeton(EnumJeton.ROUGE, colonne);
            int[] dernierCoup = {colonne, ligne};
            this.partie.setDernierCoupJoue(dernierCoup);
            System.out.println(this.partie.getEtatPartie());
            partie.setEtatPartie();
            partie.changerJoueur();
            this.rafraichirUI();
        }
    }

    public void rafraichirUI() {
        // Affichage de l'état de la partie.
        gameState.setText("C'est au tour " + (partie.getJoueurActuel() == EnumJoueur.HUMAIN ? "du joueur (rouge)." : "de l'IA (jaune)."));
        // Affichage des jetons.
        for (int i = 0; i < partie.getPlateau().getLINES(); i++) {
            for (int j = 0; j < partie.getPlateau().getCOLUMNS(); j++) {
                EnumJeton emplacement = partie.getPlateau().getCase(j, i);
                casesUI[j][i].setImage(emplacement == EnumJeton.ROUGE ? new Image("Rouge.png") : emplacement == EnumJeton.JAUNE ? new Image("Jaune.png") : null);
            }
        }
        // Affichage de l'état de fin de partie.
        if(partie.getEtatPartie() != EnumPartie.EN_COURS) {
            gameState.setVisible(false);
            if(partie.getEtatPartie() == EnumPartie.VICTOIRE_JAUNE) {
                victoryState.setText("Victoire de l'IA (jaune) !");
            } else if(partie.getEtatPartie() == EnumPartie.VICTOIRE_ROUGE) {
                victoryState.setText("Victoire du joueur (rouge) !");
            } else if(partie.getEtatPartie() == EnumPartie.MATCH_NUL) {
                victoryState.setText("Match nul !");
            }
            victoryState.setVisible(true);
        }

        //TODO DESACTIVER INTERACTION JOUEUR SI TOUR DE LIA
    }
}
