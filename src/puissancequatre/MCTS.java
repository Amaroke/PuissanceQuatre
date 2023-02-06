package puissancequatre;

import java.util.List;
import java.util.Random;

public class MCTS {

    private Noeud noeudCourant;
    private Partie partie;

    public void setPartie(Partie partie) {
        this.partie = partie;
    }

    public int jouer(Etat etat) {
        noeudCourant = new Noeud(null, -1);
        noeudCourant.setEtat(etat);
        noeudCourant.setJoueur(etat.getJoueur());
        List<Integer> coupsPossibles;
        int meilleurCoup;
        long debut = System.currentTimeMillis();
        long fin = System.currentTimeMillis();
        Random random = new Random();
        while ((fin - debut) < 200) {
            if (noeudCourant.getNoeudsFils().size() == 0) {
                if (noeudCourant.getNbSimulations() != 0) {
                    coupsPossibles = etat.getCoupsPossibles();
                    int i = 0;
                    while (i < coupsPossibles.size() && coupsPossibles.get(i) != -1) {
                        noeudCourant.ajouterFils(new Noeud(noeudCourant, coupsPossibles.get(i)));
                        i++;
                    }
                    noeudCourant = noeudCourant.getNoeudsFils().get(random.nextInt(noeudCourant.getNoeudsFils().size()));
                }
                int valeur = lancerUneSimulation();
                while (noeudCourant.getNoeudPere() != null) {
                    noeudCourant.nbSimulationPlusPlus();
                    noeudCourant.setTotal(noeudCourant.getTotal() + valeur);
                    noeudCourant = noeudCourant.getNoeudPere();
                }
                noeudCourant.nbSimulationPlusPlus();
                noeudCourant.setTotal(noeudCourant.getTotal() + valeur);
            } else {
                noeudCourant = noeudCourant.getMeilleursFils();
            }
            fin = System.currentTimeMillis();
        }

        while (noeudCourant.getNoeudPere() != null) {
            noeudCourant = noeudCourant.getNoeudPere();
        }

        if (noeudCourant.getNoeudsFils().size() != 0) {
            if (partie.isRobusteActive()) {
                meilleurCoup = noeudCourant.getValeurRobusteFils().getCoup();
            } else {
                meilleurCoup = noeudCourant.getValeurMaxFils().getCoup();
            }
        } else {
            meilleurCoup = noeudCourant.getCoup();
        }
        partie.setNbSimulations(noeudCourant.getNbSimulations());
        partie.setProbaVictoire(noeudCourant.getTotal() / (float) noeudCourant.getNbSimulations() * 100.0);
        return meilleurCoup;
    }

    public int lancerUneSimulation() {
        Plateau plateau = new Plateau(partie.getPlateau());
        List<Integer> coupsPossibles;
        Random random = new Random();
        Etat etat = new Etat(noeudCourant.getEtat());
        EnumPartie etatPartie;
        while (true) {
            etatPartie = plateau.getEtatPlateau();
            if (etatPartie == EnumPartie.VICTOIRE_JAUNE) {
                return 1;
            }
            if (etatPartie == EnumPartie.VICTOIRE_ROUGE || etatPartie == EnumPartie.MATCH_NUL) {
                return 0;
            }
            int colonne;
            coupsPossibles = etat.getCoupsPossibles();
            int coupVictoire = -1;
            for (int coup : coupsPossibles) {
                Etat etatCopie = new Etat(etat);
                etatCopie.jouerColonne(coup);
                Plateau plateauCopie = etatCopie.getPlateau();
                if (plateauCopie.getEtatPlateau() == EnumPartie.VICTOIRE_JAUNE) {
                    coupVictoire = coup;
                }
            }
            if (coupsPossibles.size() != 0) {
                colonne = coupsPossibles.get(random.nextInt(coupsPossibles.size()));
            } else {
                return 0;
            }
            if (coupVictoire != -1) {
                etat.jouerColonne(coupVictoire);
            } else {
                etat.jouerColonne(colonne);
            }
            plateau = etat.getPlateau();
        }
    }

}
