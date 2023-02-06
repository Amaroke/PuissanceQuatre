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
                    int k = 0;
                    while (k < coupsPossibles.size() && coupsPossibles.get(k) != -1) {
                        noeudCourant.ajouterFils(new Noeud(noeudCourant, coupsPossibles.get(k)));
                        k++;
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
            if(partie.isRobusteActive()) {
                meilleurCoup = noeudCourant.getValeurRobusteFils().getCoup();
            } else {
                meilleurCoup = noeudCourant.getValeurMaxFils().getCoup();
            }
        } else {
            meilleurCoup = noeudCourant.getCoup();
        }
        System.out.println("Nombre de simulations totales : " + noeudCourant.getNbSimulations());
        System.out.println("Nombre de parties gagnantes simulées : " + noeudCourant.getTotal());
        return meilleurCoup;
    }

    public int lancerUneSimulation() {
        Plateau p = new Plateau(partie.getPlateau());
        List<Integer> nbPossibilites;
        Random rand = new Random();
        Etat e = new Etat(noeudCourant.getEtat());
        EnumPartie etatPartie;
        while (true) {
            etatPartie = p.getEtatPlateau();
            if (etatPartie == EnumPartie.VICTOIRE_JAUNE) {
                return 1;
            }
            if (etatPartie == EnumPartie.VICTOIRE_ROUGE || etatPartie == EnumPartie.MATCH_NUL) {
                return 0;
            }
            int colonne;
            nbPossibilites = e.getCoupsPossibles();
            if (nbPossibilites.size() != 0) {
                 colonne = nbPossibilites.get(rand.nextInt(nbPossibilites.size()));
            } else {
                return 0;
            }
            e.jouerColonne(colonne);
            p = e.getPlateau();
        }
    }

}
