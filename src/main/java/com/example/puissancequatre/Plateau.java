package com.example.puissancequatre;

public class Plateau {
    private final int COLUMNS = 7;
    private final int LINES = 6;
    private EtatCase[][] plateau;

    public Plateau() {
        initPlateau();
    }

    public Plateau(Plateau plateau1) {
        plateau = new EtatCase[COLUMNS][LINES];
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < LINES; j++) {
                plateau[i][j] = plateau1.getPlateau()[i][j];
            }
        }
    }

    public void initPlateau() {
        plateau = new EtatCase[COLUMNS][LINES];
        for (int i = 0; i < LINES; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                plateau[j][i] = EtatCase.Aucun;
            }
        }
    }

    public EtatCase[][] getPlateau() {
        EtatCase[][] res = new EtatCase[COLUMNS][LINES];
        for (int i = 0; i < LINES; i++) {
            System.arraycopy(plateau[i], 0, res[i], 0, COLUMNS);
        }
        return res;
    }

    public boolean colonneNonPleine(int colonne) {
        return plateau[colonne][0] == EtatCase.Aucun;
    }

    public void insererJeton(EtatCase jeton, int colonne) {
        if (colonne > 0 && colonne < COLUMNS && colonneNonPleine(colonne)) {
            int ligne = LINES - 1;
            for (int i = 0; i < LINES - 1; i++) {
                if (plateau[colonne][i + 1] != EtatCase.Aucun) {
                    ligne = i;
                    i = LINES;
                }
            }
            plateau[colonne][ligne] = jeton;
        }
    }

    public int getCOLUMNS() {
        return COLUMNS;
    }

}
