# Exécution du programme

Pour lancer l'application depuis la racine :
> java -jar PuissanceQuatre.jar

# Structure du programme

Les classes EnumJeton, EnumJoueur et EnumPartie sont trois énumérations utilisées dans le code pour simplifier sa compréhension.
Etat, Nœud et MCTS, sont l'implémentation de l'algorithme de l'IA selon la méthode MCTS.
La classe Partie et la classe Plateau, sont respectivement responsable de la gestion d'une partie et du plateau, et représente donc la partie modèle de notre programme.
Là où PartieUI joue le rôle de vue et gère l'interface associée.

# Réponses aux questions

## Question 1

Il est possible d'activer/désactiver cet affichage simplement en appuyant sur la touche A.

## Question 2

Au-delà de 200ms donné à l'IA, il est très difficile de gagner la partie. Cependant, il est important de noter que cela dépend grandement du matériel exécutant l'algorithme, car le nombre de simulations effectuées en dépend grandement. De plus nous ne sommes pas des experts à ce jeu.

## Question 3

Cela force l'IA à joueur directement le coup qui lui permet de gagner lorsque cela est possible et permet donc de raccourcir les parties. Mais cela réduit également le nombre de simulations, car des calculs supplémentaires sont nécessaires.

## Question 4

Non concerné, projet réalisé en Java.

## Question 5

Ils donnent effectivement lieu à des coups différents, le critère "robuste", semble davantage s'assurer de ne pas perdre. Mais les deux approches restent très efficaces même avec peu de temps de calcul alloué.

## Question 6

Facteur de branchement au puissance 4 dans le "pire" des cas : 7.<br>
Profondeur maximale d'une branche au puissance 4 : 6*7 = 42, estimation de la moyenne : 42/2 = 21.<br>

Estimation de la complexité en temps : O(b^n) = 7^21 = 5.5854586e+17, ce qui est, même avec plusieurs milliards d'opération par seconde, plus grand que la taille de l'univers.
Ce n'est pas envisageable sans élagage alpha-beta par exemple.<br>

# Démonstration

<a href="https://youtu.be/iLPcJ0DouQE">Lien vers la vidéo</a>