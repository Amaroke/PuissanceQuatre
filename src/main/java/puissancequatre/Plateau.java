package puissancequatre;

public class Plateau {
    private final int COLUMNS = 7;
    private final int LINES = 6;
    private EnumJeton[][] plateau;

    public Plateau() {
        initPlateau();
    }

    public Plateau(Plateau plateau1) {
        plateau = new EnumJeton[COLUMNS][LINES];
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < LINES; j++) {
                plateau[i][j] = plateau1.getPlateau()[i][j];
            }
        }
    }

    public void initPlateau() {
        plateau = new EnumJeton[COLUMNS][LINES];
        for (int i = 0; i < LINES; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                plateau[j][i] = EnumJeton.AUCUN;
            }
        }
    }

    public EnumJeton[][] getPlateau() {
        EnumJeton[][] res = new EnumJeton[COLUMNS][LINES];
        for (int i = 0; i < LINES; i++) {
            System.arraycopy(plateau[i], 0, res[i], 0, COLUMNS);
        }
        return res;
    }

    public EnumJeton getCase(int x, int y) {
        return plateau[x][y];
    }

    public boolean colonneNonPleine(int colonne) {
        return plateau[colonne][0] == EnumJeton.AUCUN;
    }

    public int insererJeton(EnumJeton jeton, int colonne) {
        int ligne = LINES - 1;
        for (int i = 0; i < LINES - 1; i++) {
            if (plateau[colonne][i + 1] != EnumJeton.AUCUN) {
                ligne = i;
                break;
            }
        }
        plateau[colonne][ligne] = jeton;
        return ligne;
    }

    public int getCOLUMNS() {
        return COLUMNS;
    }

    public int getLINES() {
        return LINES;
    }

    public boolean isPlateauPlein() {
        for (EnumJeton[] enumJetons : plateau) {
            if (enumJetons[0] == EnumJeton.AUCUN) {
                return false;
            }
        }
        return true;
    }


}
