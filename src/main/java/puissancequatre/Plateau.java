package puissancequatre;

public class Plateau {
    private EnumJeton[][] plateau;

    public Plateau() {
        initPlateau();
    }

    public Plateau(Plateau plateau1) {
        plateau = new EnumJeton[7][6];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                plateau[i][j] = plateau1.getPlateau()[i][j];
            }
        }
    }

    public void initPlateau() {
        plateau = new EnumJeton[7][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                plateau[j][i] = EnumJeton.AUCUN;
            }
        }
    }

    public EnumJeton[][] getPlateau() {
        return plateau.clone();
    }

    public EnumJeton getCase(int x, int y) {
        return plateau[x][y];
    }

    public boolean colonneNonPleine(int colonne) {
        return plateau[colonne][0] == EnumJeton.AUCUN;
    }

    public void insererJeton(EnumJeton jeton, int colonne) {
        int ligne = 6 - 1;
        for (int i = 0; i < 6 - 1; i++) {
            if (plateau[colonne][i + 1] != EnumJeton.AUCUN) {
                ligne = i;
                break;
            }
        }
        plateau[colonne][ligne] = jeton;
    }

    public EnumPartie getEtatPlateau() {
        // Tableau pour stocker le nombre de cases libres dans chaque colonne
        int[] colonnes = new int[7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                // On incrémente le compteur de colonne si la case est vide
                if (plateau[i][j] == EnumJeton.AUCUN) {
                    colonnes[i]++;
                } else {
                    // Vérifier s'il y a une victoire horizontale à gauche
                    if (j <= 2) {
                        if (plateau[i][j] == plateau[i][j + 1] && plateau[i][j + 1] == plateau[i][j + 2] && plateau[i][j + 2] == plateau[i][j + 3]) {
                            return plateau[i][j] == EnumJeton.JAUNE ? EnumPartie.VICTOIRE_JAUNE : EnumPartie.VICTOIRE_ROUGE;
                        }
                    }
                    // Vérifier s'il y a une victoire verticale
                    if (i <= 3) {
                        if (plateau[i][j] == plateau[i + 1][j] && plateau[i + 1][j] == plateau[i + 2][j] && plateau[i + 2][j] == plateau[i + 3][j]) {
                            return plateau[i][j] == EnumJeton.JAUNE ? EnumPartie.VICTOIRE_JAUNE : EnumPartie.VICTOIRE_ROUGE;
                        }
                    }
                    // Vérifier s'il y a une victoire en diagonale vers la gauche
                    if (i <= 3 && j <= 2) {
                        if (plateau[i][j] == plateau[i + 1][j + 1] && plateau[i + 1][j + 1] == plateau[i + 2][j + 2] && plateau[i + 2][j + 2] == plateau[i + 3][j + 3]) {
                            return plateau[i][j] == EnumJeton.JAUNE ? EnumPartie.VICTOIRE_JAUNE : EnumPartie.VICTOIRE_ROUGE;
                        }
                    }
                    // Vérifier s'il y a une victoire en diagonale vers la droite
                    if (i <= 3 && j >= 3) {
                        if (plateau[i][j] == plateau[i + 1][j - 1] && plateau[i + 1][j - 1] == plateau[i + 2][j - 2] && plateau[i + 2][j - 2] == plateau[i + 3][j - 3]) {
                            return plateau[i][j] == EnumJeton.JAUNE ? EnumPartie.VICTOIRE_JAUNE : EnumPartie.VICTOIRE_ROUGE;
                        }
                    }
                }
            }
        }
        // Si une colonne n'est pas vide, alors la partie est en cours
        for (int i = 0; i < 7; i++) {
            if (colonnes[i] != 0) {
                return EnumPartie.EN_COURS;
            }
        }
        return EnumPartie.MATCH_NUL;
    }


}
