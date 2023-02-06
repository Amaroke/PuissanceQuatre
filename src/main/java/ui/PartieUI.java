package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
    private Label gameState;
    private Label victoryState;
    private Rectangle rectangle;
    private final Button options = new Button();


    @Override
    public void start(Stage stage) {

        // On crée la partie.
        MCTS mcts = new MCTS();
        partie = new Partie(new Plateau(), mcts);
        mcts.setPartie(partie);
        int nbColonnes = 7;
        int nbLignes = 6;

        // On crée la fenêtre de base.
        Group root = new Group();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        // On la fait en fonction de la taille de l'écran.
        Scene scene = new Scene(root, screenBounds.getWidth() * 0.9, screenBounds.getHeight() * 0.9);

        options.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.R) {
                partie.setRobusteActive();
                rafraichirUI();
            }
            if (event.getCode() == KeyCode.A) {
                System.out.println("La touche A a été appuyée");
            }
            if (event.getCode() == KeyCode.T) {
                System.out.println("La touche T a été appuyée");
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });


        root.getChildren().addAll(options);

        // On crée la grille bleue du jeu.
        int width = (int) (screenBounds.getWidth() / (nbColonnes + 2) * nbColonnes);
        int height = (int) (screenBounds.getHeight() / (nbLignes + 2) * nbLignes);
        int borderX = (int) (screenBounds.getWidth() - width) / 4;
        int borderY = 30;
        // On utilise borderX pour centre la grille au milieu de la fenêtre.
        Rectangle r = new Rectangle(borderX, borderY, width, height);
        r.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.BLUE), new Stop(0.5, Color.rgb(0, 0, 100)), new Stop(1, Color.BLUE)));
        root.getChildren().add(r);

        // On crée les différentes cases de la grille.
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                Circle c = new Circle(150 * j, 150 * i, 45);
                c.setFill(Color.WHITE);
                c.radiusProperty().bind(r.heightProperty().divide(12).subtract(5));
                c.centerXProperty().bind(r.widthProperty().divide(nbColonnes).multiply(j + 0.5).add(borderX));
                c.centerYProperty().bind(r.heightProperty().divide(nbLignes).multiply(i + 0.5).add(borderY));
                root.getChildren().add(c);
            }
        }

        // On crée les images Cases.
        casesUI = new ImageView[nbColonnes][nbLignes];
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                casesUI[j][i] = new ImageView();
                casesUI[j][i].layoutXProperty().bind(r.widthProperty().divide(nbColonnes).multiply(j).add(borderX * 1.37));
                casesUI[j][i].layoutYProperty().bind(r.heightProperty().divide(nbLignes).multiply(i).add(borderY));
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
        gameState.layoutYProperty().bind(r.heightProperty().add(30));
        gameState.setAlignment(Pos.CENTER);
        gameState.setPrefHeight(75);
        gameState.setMinWidth(scene.getWidth() / 2);

        // Création de l'affichage de la victoire.
        victoryState = new Label("");
        victoryState.setFont(Font.font("Arial", scene.getHeight() / 20));
        victoryState.setLayoutX(borderX);
        victoryState.layoutYProperty().bind(r.heightProperty().add(30));
        victoryState.setVisible(false);
        victoryState.setAlignment(Pos.CENTER);
        victoryState.setPrefHeight(75);
        victoryState.setMinWidth(scene.getWidth() / 2);

        root.getChildren().addAll(gameState, victoryState);

        // Création des cadres de positionnement.
        Rectangle[] rectangles = new Rectangle[nbColonnes];
        for (int i = 0; i < nbColonnes; i++) {
            rectangles[i] = new Rectangle(0, borderY, 10, 10);
            rectangles[i].layoutXProperty().bind(r.widthProperty().divide(nbColonnes).multiply(i).add(borderX));
            rectangles[i].heightProperty().bind(r.heightProperty());
            rectangles[i].widthProperty().bind(r.widthProperty().divide(nbColonnes));
            rectangles[i].setFill(Color.TRANSPARENT);
            rectangles[i].setStroke(Color.BLACK);
            rectangles[i].setStrokeType(StrokeType.INSIDE);
            rectangles[i].setStrokeWidth(4);
            rectangles[i].setVisible(false);
            root.getChildren().addAll(rectangles[i]);
        }

        rectangle = new Rectangle(0, 0, 10, 10);
        rectangle.heightProperty().bind(r.heightProperty());
        rectangle.widthProperty().bind(r.widthProperty());
        rectangle.setFill(Color.TRANSPARENT);
        root.getChildren().addAll(rectangle);

        previousSelection = -1;
        rectangle.setOnMouseMoved(e -> {
            int val = (int) ((e.getX() - borderX) / (r.getWidth() / nbColonnes));
            if (val != previousSelection) {
                rectangles[val].setVisible(true);
                if (previousSelection > -1)
                    rectangles[previousSelection].setVisible(false);
            }
            previousSelection = val;
        });

        // Le joueur joue.
        rectangle.setOnMouseClicked(e -> {
            int colonne = (int) ((e.getX() - borderX) / (r.getWidth() / nbColonnes));
            // Gestion du placement du jeton.
            if (partie.getPlateau().colonneNonPleine(colonne) && partie.getEtatPartie() == EnumPartie.EN_COURS) {
                partie.getPlateau().insererJeton(EnumJeton.ROUGE, colonne);
                // Changement de joueur, rafraichissement de l'UI.
                this.partie.setEtatPartie(partie.getPlateau().getEtatPlateau());
                partie.changerJoueur();
                this.rafraichirUI();
                this.faireJouerIA();
            }
        });

        // On paramètre la fenêtre.
        stage.setTitle("IA - Puissance 4");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        this.rafraichirUI();
        this.faireJouerIA();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void faireJouerIA() {
        if (partie.getJoueurActuel() == EnumJoueur.IA && partie.getEtatPartie() == EnumPartie.EN_COURS) {
            new Thread(() -> {
                int colonne = partie.getIa().jouer(new Etat(partie.getPlateau()));
                partie.getPlateau().insererJeton(EnumJeton.JAUNE, colonne);
                this.partie.setEtatPartie(partie.getPlateau().getEtatPlateau());
                partie.changerJoueur();
                Platform.runLater(this::rafraichirUI);
            }).start();
        }
    }

    public void rafraichirUI() {
        // Changer l'affichage des options
        options.setText("Appuyez sur la touche R, pour passer en mode " + (partie.isRobusteActive() ? "MAX" : "ROBUSTE") + ", la touche A pour changer l'affichage, la touche T pour changer le temps de réflexion de l'IA et ECHAP pour QUITTER.");
        // Affichage de l'état de la partie
        gameState.setText("C'est au tour " + (partie.getJoueurActuel() == EnumJoueur.HUMAIN ? "du joueur (rouge)." : "de l'IA (jaune)."));
        // Affichage des jetons
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                EnumJeton emplacement = partie.getPlateau().getCase(j, i);
                casesUI[j][i].setImage(emplacement == EnumJeton.ROUGE ? new Image("Rouge.png") : emplacement == EnumJeton.JAUNE ? new Image("Jaune.png") : null);
            }
        }
        // Affichage de l'état de fin de partie
        if (partie.getEtatPartie() != EnumPartie.EN_COURS) {
            gameState.setVisible(false);
            if (partie.getEtatPartie() == EnumPartie.VICTOIRE_JAUNE) {
                victoryState.setStyle("-fx-text-fill: yellow");
                victoryState.setText("Victoire de l'IA (jaune) !");
            } else if (partie.getEtatPartie() == EnumPartie.VICTOIRE_ROUGE) {
                victoryState.setText("Victoire du joueur (rouge) !");
                victoryState.setStyle("-fx-text-fill: red");
            } else if (partie.getEtatPartie() == EnumPartie.MATCH_NUL) {
                victoryState.setText("Match nul !");
                victoryState.setStyle("-fx-text-fill: blue");
            }
            victoryState.setVisible(true);
        }
        // On désactive le rectangle permettant au joueur de jouer
        rectangle.setDisable(partie.getJoueurActuel() == EnumJoueur.IA);
    }
}
