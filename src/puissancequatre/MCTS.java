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
        // Initialisation du nœud courant
        noeudCourant = new Noeud(null, -1);
        noeudCourant.setEtat(etat);
        noeudCourant.setJoueur(etat.getJoueur());

        // Liste des coups possibles
        List<Integer> coupsPossibles;
        int meilleurCoup;

        // Timer pour limiter le temps de calcul
        long debut = System.currentTimeMillis();
        long fin = System.currentTimeMillis();

        // Générateur de nombres aléatoires pour la simulation
        Random random = new Random();

        // Boucle de jeu
        while ((fin - debut) < 200) {
            // Si le nœud courant n'a pas de fils
            if (noeudCourant.getNoeudsFils().size() == 0) {
                // Si le nœud courant a déjà été simulé
                if (noeudCourant.getNbSimulations() != 0) {
                    // Récupération des coups possibles
                    coupsPossibles = etat.getCoupsPossibles();
                    int i = 0;
                    // Ajout de tous les fils possibles
                    while (i < coupsPossibles.size() && coupsPossibles.get(i) != -1) {
                        noeudCourant.ajouterFils(new Noeud(noeudCourant, coupsPossibles.get(i)));
                        i++;
                    }
                    // Sélection d'un fils au hasard pour la simulation
                    noeudCourant = noeudCourant.getNoeudsFils().get(random.nextInt(noeudCourant.getNoeudsFils().size()));
                }
                // Simulation d'un coup
                int valeur = lancerUneSimulation();
                // Mise à jour des nœuds pères avec les résultats de la simulation
                while (noeudCourant.getNoeudPere() != null) {
                    noeudCourant.nbSimulationPlusPlus();
                    noeudCourant.setTotal(noeudCourant.getTotal() + valeur);
                    noeudCourant = noeudCourant.getNoeudPere();
                }
                // Mise à jour du nœud racine avec les résultats de la simulation
                noeudCourant.nbSimulationPlusPlus();
                noeudCourant.setTotal(noeudCourant.getTotal() + valeur);
            } else {
                // Sélection du meilleur fils
                noeudCourant = noeudCourant.getMeilleursFils();
            }
            // Mise à jour du timer
            fin = System.currentTimeMillis();
        }

        // Boucle pour remonter à la racine de l'arbre en suivant les nœuds pères
        while (noeudCourant.getNoeudPere() != null) {
            noeudCourant = noeudCourant.getNoeudPere();
        }

        // Si le nœud courant a des fils, on détermine le meilleur coup en fonction de la méthode Robuste ou Max
        if (noeudCourant.getNoeudsFils().size() != 0) {
            if (partie.isRobusteActive()) {
                meilleurCoup = noeudCourant.getValeurRobusteFils().getCoup();
            } else {
                meilleurCoup = noeudCourant.getValeurMaxFils().getCoup();
            }
        } else {
            // Si le nœud courant n'a pas de fils, le meilleur coup est celui du nœud courant
            meilleurCoup = noeudCourant.getCoup();
        }

        // Mise à jour du nombre de simulations et de la probabilité de victoire dans la partie
        partie.setNbSimulations(noeudCourant.getNbSimulations());
        partie.setProbaVictoire(noeudCourant.getTotal() / (float) noeudCourant.getNbSimulations() * 100.0);

        // Retourne le meilleur coup
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
