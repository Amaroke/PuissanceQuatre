package com.example.puissancequatre;

public class Plateau {

    private final int ALIGNMENT = 4;
    private final int COLUMNS = 7;
    private final int LINES = 6;
    private EtatCase[][] plateau;

    public Plateau() {
        initPlateau();
    }

    public Plateau(Plateau plateau1){
        plateau = new EtatCase[COLUMNS][LINES];
        for(int i = 0 ; i < COLUMNS ; i++) {
            for(int j = 0 ; j < LINES ; j++) {
                plateau[i][j] = plateau1.getPlateau()[i][j];
            }
        }
    }

    public void initPlateau(){
        plateau = new EtatCase[COLUMNS][LINES];
        for (int i = 0; i < LINES; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                plateau[j][i] = EtatCase.Aucun;
            }
        }
    }

    public EtatCase[][] getPlateau() {
        EtatCase[][] res = new EtatCase[COLUMNS][LINES];
        for(int i = 0 ; i < LINES ; i++) {
            for(int j = 0 ; j < COLUMNS ; j++) {
                res[i][j] = plateau[i][j];
            }
        }
        return res;
    }

    public boolean insererJeton(EtatCase jeton, int col) {
        if(col < 0 || plateau[col][0] != EtatCase.Aucun) {
            return false;
        }

        int ligne = LINES - 1;
        for(int j = 0 ; j < LINES - 1 ; j++) {
            if(plateau[col][j + 1] != EtatCase.Aucun) {
                ligne = j;
                j = LINES;
            }
        }
        plateau[col][ligne] = jeton;
        return true;
    }



}
