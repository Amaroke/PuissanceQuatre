# Exécution du programme

### Prérequis

Pour exécuter le programme, il faut avoir installé java.
De plus utilisant une interface graphique, il faut posséder la bibliothèque javaFX.

* Installation de Java
Ubuntu : `sudo apt install default-jre`
Windows : https://www.java.com/fr/download/manual.jsp

* Installation de JavaFX
https://gluonhq.com/products/javafx/8

### Exécution

>  java -jar --module-path "CHEMIN VERS LE DOSSIER LIB DE L'INSTALLATION DE JAVAFX" --add-modules javafx.controls ./PuissanceQuatre.jar

Pour faciliter la tâche de l'exécution, nous avons prévu un dossier contenant les composants de javaFX à la racine du projet, avec le .jar, pour l'exécuter la commande est donc la suivante sous Ubuntu :
> java -jar --module-path "./openjfx-19.0.2.1_linux-x64_bin-sdk/javafx-sdk-19.0.2.1/lib" --add-modules javafx.controls ./PuissanceQuatre.jar

# Structure du programme

Les classes EnumJeton, EnumJoueur et EnumPartie sont trois énumérations utilisées dans le code pour simplifier sa compréhension.
Nous ne détaillerons pas leur fonctionnement.<br>
La classe Partie et la classe Plateau, sont respectivement responsable de la gestion d'une partie et du plateau, et représente donc la partie modèle de notre programme.<br>
Là où PartieUI joue le rôle de vue et gère l'interface associée.<br>
Comme pour les énumérations le code présent dans ces classes est assez basique et commenté, les noms de variables et des fonctions sont également très explicites, il n'a pas besoin d'être détaillé ici.<br>
Etat, Nœud et MCTS, sont l'implémentation de l'algorithme de l'IA selon la méthode MCTS.
Nous allons rapidement décrire leur fonctionnement ici, premièrement les classes Nœuds et Etat correspondent globalement à une traduction du fichier jeu.c

```java
public double calculerValeurB() {
    if (joueur == EnumJoueur.IA) {
        return -((double) total / nbSimulations) + Math.sqrt(2) * Math.sqrt(Math.log(noeudPere.getNbSimulations()) / nbSimulations);
    } else {
        return ((double) total / nbSimulations) + Math.sqrt(2) * Math.sqrt(Math.log(noeudPere.getNbSimulations()) / nbSimulations);

    }
}

public Noeud getValeurMaxFils() {
    Noeud valeurMaxFils = noeudsFils.get(0);
    double valeurMax = valeurMaxFils.getTotal();
    for (Noeud fils : noeudsFils) {
        double total = fils.getTotal();
        if (total > valeurMax) {
            valeurMax = total;
            valeurMaxFils = fils;
        }
    }
    return valeurMaxFils;
}

public Noeud getValeurRobusteFils() {
    Noeud valeurRobusteFils = noeudsFils.get(0);
    double nbSimulationsMax = valeurRobusteFils.getNbSimulations();
    for (Noeud fils : noeudsFils) {
        double nbSimulations = fils.getNbSimulations();
        if (nbSimulations > nbSimulationsMax) {
            nbSimulationsMax = nbSimulations;
            valeurRobusteFils = fils;
        }
    }
    return valeurRobusteFils;
}
```

Ces trois fonctions de la classe Nœud, correspondent au calcul de la B-valeur, et des critères max et robuste, comme définit dans le cours.

Pour la classe MCTS il s'agit d'une implémentation de l'algorithme vu en cours :
```java
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
```



# Réponses aux questions

## Question 1

Il est possible d'activer/désactiver cet affichage simplement en appuyant sur la touche A.

## Question 2

Au-delà de 200ms donnée à l'IA, il est très difficile pour nous de gagner la partie. Cependant, il est important de noter que cela dépend grandement du matériel exécutant l'algorithme, car le nombre de simulations effectuées en dépend grandement. De plus nous ne sommes pas des experts à ce jeu. C'est d'ailleurs sur ce temps que le programme actuel est réglé, il n'est donc pas impossible de gagner par chance, ou si on est bon au jeu, et c'est davantage valable si la machine utilisée possède une relativement faible puissance de calcul.

## Question 3

Cela force l'IA à joueur directement le coup qui lui permet de gagner lorsque cela est possible et permet donc de raccourcir les parties. Mais cela réduit également le nombre de simulations, car des calculs supplémentaires sont nécessaires.

## Question 4

Non concerné, projet réalisé en Java.

## Question 5

Ils donnent effectivement lieu à des coups différents, le critère "robuste", semble davantage s'assurer de ne pas perdre, faisant parfois durer les parties plus longtemps. Mais les deux approches restent très efficaces même avec peu de temps de calcul alloué, même s'il parait tout de même que le critère max soit très légèrement plus performant, mais comme indiqué dans une réponse précédente, nous ne sommes pas des experts au jeu, il nous est donc difficile de juger les deux comportements, d'autant plus qu'il reste une part d'aléatoire.

## Question 6

Facteur de branchement au puissance 4 dans le "pire" des cas : 7.<br>
Profondeur maximale d'une branche au puissance 4 : 6*7 = 42, estimation de la moyenne : 42/2 = 21.<br>

Estimation de la complexité en temps : O(b^n) = 7^21 = 5.5854586e+17, ce qui est, même avec plusieurs milliards d'opération par seconde, plus grand que l'âge de l'univers.
Ce n'est pas envisageable sans un élagage alpha-beta par exemple.<br>

# Démonstration

Nous avons fait une petite demonstration vidéo, accessible facilement sur YouTube :
<a href="https://youtu.be/iLPcJ0DouQE">Lien vers la vidéo</a>