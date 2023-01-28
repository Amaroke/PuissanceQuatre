package old;

import com.example.puissancequatre.EtatCase;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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


public class Partie extends Application {

    private static int previousSelection;
    private static int gameTurn = 1;
    private Case[][] boxes;
    private final int playerNum = (int) (Math.random() * 2);
    private final int ALIGNMENT = 4;
    private final int COLUMNS = 7;
    private final int LINES = 6;


    public void start(Stage primaryStage) {

        // On crée la fenêtre de base.
        Group root = new Group();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        // On la fait en fonction de la taille de l'écran.
        Scene scene = new Scene(root, screenBounds.getWidth() * 0.9, screenBounds.getHeight() * 0.9);

        // On crée la grille bleue du jeu.
        int width = (int) (screenBounds.getWidth() / (COLUMNS + 2) * COLUMNS);
        int height = (int) (screenBounds.getHeight() / (LINES + 2) * LINES);
        int borderX = (int) (screenBounds.getWidth() - width) / 4;
        // On utilise borderX pour centre la grille au milieu de la fenêtre.
        Rectangle r = new Rectangle(borderX, 0, width, height);
        r.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.BLUE), new Stop(0.5, Color.rgb(0, 0, 100)), new Stop(1, Color.BLUE)));
        root.getChildren().add(r);

        // On crée les différentes cases de la grille.
        for (int i = 0; i < LINES; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                Circle c = new Circle(150 * j, 150 * i, 45);
                c.setFill(Color.WHITE);
                c.radiusProperty().bind(r.heightProperty().divide(12).subtract(5));
                c.centerXProperty().bind(r.widthProperty().divide(COLUMNS).multiply(j + 0.5).add(borderX));
                c.centerYProperty().bind(r.heightProperty().divide(LINES).multiply(i + 0.5));
                root.getChildren().add(c);
            }
        }

        // On crée les objets Cases.
        boxes = new Case[COLUMNS][LINES];
        for (int i = 0; i < LINES; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                boxes[j][i] = new Case();
                boxes[j][i].layoutXProperty().bind(r.widthProperty().divide(COLUMNS).multiply(j).add(borderX*1.37));
                boxes[j][i].layoutYProperty().bind(r.heightProperty().divide(LINES).multiply(i));
                boxes[j][i].fitHeightProperty().bind(r.heightProperty().divide(LINES));
                boxes[j][i].fitWidthProperty().bind(r.widthProperty().divide(COLUMNS));
                boxes[j][i].setPreserveRatio(true);
                root.getChildren().add(boxes[j][i]);
            }
        }

        // Création de l'affichage du tour.
        Label gameState = new Label("Tour " + gameTurn + ", c'est au tour " + (gameTurn % 2 == playerNum ? "du joueur (rouge)." : "de l'IA (jaune)."));
        gameState.setTextFill(Color.BLACK);
        gameState.setFont(Font.font("Arial", scene.getHeight() / 20));
        gameState.setLayoutX(borderX);
        gameState.layoutYProperty().bind(r.heightProperty());

        // Création de l'affichage de la victoire.
        Label victoryState = new Label("");
        victoryState.setFont(Font.font("Arial", scene.getHeight() / 20));
        victoryState.setLayoutX(borderX);
        victoryState.layoutYProperty().bind(r.heightProperty());
        victoryState.setVisible(false);

        root.getChildren().addAll(gameState, victoryState);

        // Création des cadres de positionnement.
        Rectangle[] rects = new Rectangle[COLUMNS];
        for (int i = 0; i < COLUMNS; i++) {
            rects[i] = new Rectangle(0, 0, 10, 10);
            rects[i].layoutXProperty().bind(r.widthProperty().divide(COLUMNS).multiply(i).add(borderX));
            rects[i].heightProperty().bind(r.heightProperty());
            rects[i].widthProperty().bind(r.widthProperty().divide(COLUMNS));
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

        // Affichage du cadre de positionnement.
        previousSelection = -1;
        r2.setOnMouseMoved(e -> {
            int val = (int) ((e.getX() - borderX) / (r.getWidth() / COLUMNS));
            if (val != previousSelection) {
                rects[val].setVisible(true);
                if (previousSelection > -1)
                    rects[previousSelection].setVisible(false);
            }
            previousSelection = val;
        });

        r2.setOnMouseClicked(e -> {
            int colonne = (int) ((e.getX() - borderX) / (r.getWidth() / COLUMNS));
            // Gestion du placement du jeton.
            if (boxes[colonne][0].getStatut() == EtatCase.Aucun && !victoryState.isVisible()) {
                int rang = LINES - 1;
                while (boxes[colonne][rang].getStatut() != EtatCase.Aucun) {
                    rang--;
                }
                boxes[colonne][rang].set(gameTurn % 2 == playerNum ? EtatCase.Rouge : EtatCase.Jaune);


                int[][] dirs = {{-1, -1}, {-1, 1}, {0, -1}, {-1, 0}};
                EtatCase color = (gameTurn % 2 == playerNum ? EtatCase.Rouge : EtatCase.Jaune);
                int max = 0;
                int x;
                int y;
                int somme;

                for (int[] dir : dirs) {
                    somme = -1;
                    x = colonne;
                    y = rang;
                    while (x >= 0 && x < COLUMNS && y >= 0 && y < LINES && boxes[x][y].getStatut() == color) {
                        x += dir[0];
                        y += dir[1];
                        somme++;
                    }
                    x = colonne;
                    y = rang;
                    while (x >= 0 && x < COLUMNS && y >= 0 && y < LINES && boxes[x][y].getStatut() == color) {
                        x -= dir[0];
                        y -= dir[1];
                        somme++;
                    }
                    if (somme > max) max = somme;
                }

                // Affichage en cas de victoire.
                if (max >= ALIGNMENT) {
                    gameState.setVisible(false);
                    victoryState.setVisible(true);
                    victoryState.setTextFill(color == EtatCase.Rouge ? Color.RED : Color.YELLOW);
                    victoryState.setText("Victoire " + (color == EtatCase.Rouge ? "du joueur (rouge) !" : "de l'IA (jaune)."));
                    gameTurn--;
                }

                // On change de tour.
                gameTurn++;

                // Affichage en cas de match nul.
                if (gameTurn > COLUMNS * LINES && max < ALIGNMENT) {
                    gameState.setVisible(false);
                    victoryState.setVisible(true);
                    victoryState.setText("Match nul !");
                    gameTurn--;
                }

                // Affichage de l'état de la partie.
                gameState.setText("Tour " + gameTurn + ", c'est au tour " + (gameTurn % 2 == playerNum ? "du joueur (rouge)." : "de l'IA (jaune)."));
            }
        });

        primaryStage.setTitle("IA - Puissance 4");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}