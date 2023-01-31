package puissancequatre;

import java.util.List;
import java.util.Random;

public class MCTS {

    //TODO FAIRE LIA

    private Noeud courant;
    private Partie partie;
    private int cpt;

    public void setPartie(Partie partie) {
        this.partie = partie;
    }

    public int jouer(Etat etat) {
        cpt = 0;
        courant = new Noeud(null, -1);
        courant.setEtat(etat);
        courant.setJoueur(etat.getJoueur());
        List<Integer> coups;

        long fin;
        int meilleurCoup;
        long debut = System.currentTimeMillis();
        Random random = new Random();
        do {
            if(courant.getNoeudsFils().size() == 0) {
                if(courant.getNbSimulations() != 0) {
                    coups = etat.getCoupsPossibles();
                    int k = 0;
                    while (k < coups.size() && coups.get(k) != -1) {
                        courant.getNoeudsFils().add(new Noeud(courant, coups.get(k)));
                        k++;
                    }
                    courant = courant.getNoeudsFils().get(random.nextInt(courant.getNoeudsFils().size()));
                }
                int valeur = rollout();
                while(courant.getNoeudPere() != null) {
                    courant.nbSimuPlusPlus();
                    courant.setTotal((int) (courant.getTotal() + valeur));
                    courant = courant.getNoeudPere();
                }
                courant.nbSimuPlusPlus();
                courant.setTotal((int) (courant.getTotal() + valeur));
            }else{
                courant = courant.getMeilleursFils();
            }

            fin = System.currentTimeMillis();

        }while((fin - debut) < 3000);


        while(courant.getNoeudPere() != null) {
            courant = courant.getNoeudPere();
        }

        if (courant.getNoeudsFils().size() != 0) {
            meilleurCoup = courant.getValeurMaxFils().getCoup();
        }else{
            meilleurCoup = courant.getCoup();
        }
        return meilleurCoup;
    }

    public int rollout() {
        List<Integer> nbPossibilites;
        Random rand = new Random();
        int coup, coupVictoire;
        System.out.println(courant.getEtat().toString());
        Etat e = new Etat(courant.getEtat());
        EnumPartie fp;

        while(true) {
            partie.setEtatPartie();
            fp = partie.getEtatPartie();
            System.out.println(fp);
            if (fp == EnumPartie.VICTOIRE_JAUNE) {
                return 1;
            }
            if (fp == EnumPartie.VICTOIRE_ROUGE || fp == EnumPartie.MATCH_NUL) {
                return 0;
            }
            nbPossibilites = e.getCoupsPossibles();
            System.out.println(nbPossibilites.size());
            if(nbPossibilites.size() == 0) {
                return 0;
            }
            coup = nbPossibilites.get(rand.nextInt(nbPossibilites.size()));

            e.jouerCoup(coup);

        }
    }

}
