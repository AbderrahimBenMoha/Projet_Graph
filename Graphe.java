import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Graphe {
    private Map<Integer, String> sommets;
    private List<Arete> aretes;
    private Map<Integer, Integer> degres;

    public Graphe() {
        sommets = new HashMap<>();
        aretes = new ArrayList<>();
        degres = new HashMap<>();
    }
    
    // Méthode pour obtenir les sommets
    public Map<Integer, String> getSommets() {
        return sommets;
    }
    
    // Méthode pour obtenir les arêtes
    public List<Arete> getAretes() {
        return aretes;
    }
    
    // Méthode pour obtenir les degrés
    public Map<Integer, Integer> getDegres() {
        return degres;
    }

    // Ajouter un sommet
    public void ajouterSommet(int id, String nom) {
        sommets.put(id, nom);
        degres.put(id, 0); // Initialisation du degré à 0
    }

    // Ajouter une arête
    public void ajouterArete(int sommetDepart, int sommetArrivee, int poids) {
        aretes.add(new Arete(sommetDepart, sommetArrivee, poids));
        degres.put(sommetDepart, degres.get(sommetDepart) + 1);
        degres.put(sommetArrivee, degres.get(sommetArrivee) + 1);
    }

    // Affichage du graphe
    public void afficherGraphe() {
        System.out.println("Sommets :");
        for (Map.Entry<Integer, String> sommet : sommets.entrySet()) {
            System.out.println("ID: " + sommet.getKey() + ", Nom: " + sommet.getValue());
        }

        System.out.println("\nArêtes :");
        for (Arete arete : aretes) {
            String nomDepart = sommets.get(arete.sommetDepart);
            String nomArrivee = sommets.get(arete.sommetArrivee);
            System.out.println("De " + nomDepart + " à " + nomArrivee + " avec un poids de " + arete.poids);
        }

        System.out.println("\nDegrés des sommets :");
        for (Map.Entry<Integer, Integer> entry : degres.entrySet()) {
            System.out.println("Sommet " + sommets.get(entry.getKey()) + " a un degré de " + entry.getValue());
        }
    }

    // Algorithme PlusCourtCheminNbArcs (parcours en largeur)
    public Map<Integer, Integer> plusCourtCheminNbArcs(int s) {
        Map<Integer, Integer> longueur = new HashMap<>();
        Map<Integer, Boolean> atteint = new HashMap<>();
        Queue<Integer> F = new LinkedList<>();

        for (Integer v : sommets.keySet()) {
            atteint.put(v, false);
            longueur.put(v, Integer.MAX_VALUE);
        }

        atteint.put(s, true);
        longueur.put(s, 0);
        F.add(s);

        while (!F.isEmpty()) {
            int v = F.poll();
            for (Arete arete : aretes) {
                if (arete.sommetDepart == v) {
                    int y = arete.sommetArrivee;
                    if (!atteint.get(y)) {
                        atteint.put(y, true);
                        longueur.put(y, longueur.get(v) + 1); // Mise à jour de la distance
                        F.add(y);
                    }
                }
            }
        }
        return longueur;
    }

    // Algorithme de Dijkstra (pour les graphes pondérés)
    public Map<Integer, Integer> dijkstra(int s) {
        Map<Integer, Integer> distance = new HashMap<>();
        Map<Integer, Boolean> atteint = new HashMap<>();
        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(distance::get));

        for (Integer v : sommets.keySet()) {
            distance.put(v, Integer.MAX_VALUE);
            atteint.put(v, false);
        }

        distance.put(s, 0);
        queue.add(s);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            atteint.put(v, true);

            for (Arete arete : aretes) {
                if (arete.sommetDepart == v && !atteint.get(arete.sommetArrivee)) {
                    int nouvelleDistance = distance.get(v) + arete.poids;
                    if (nouvelleDistance < distance.get(arete.sommetArrivee)) {
                        distance.put(arete.sommetArrivee, nouvelleDistance);
                        queue.add(arete.sommetArrivee);
                    }
                }
            }
        }

        return distance;
    }

    // Fonction d'importation du graphe depuis un fichier .gra
    public void importerGraphe(String fichier) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fichier));
        String ligne;
        boolean verticesSection = false;
        boolean edgesSection = false;
        
        while ((ligne = br.readLine()) != null) {
            ligne = ligne.trim();
            if (ligne.startsWith("VERTICES")) {
                verticesSection = true;
                edgesSection = false;
                continue;
            }
            if (ligne.startsWith("EDGES")) {
                edgesSection = true;
                verticesSection = false;
                continue;
            }

            if (verticesSection) {
                if (!ligne.startsWith("VERTICES") && !ligne.isEmpty()) {
                    String[] parts = ligne.split(" ");
                    if (parts.length >= 2) {
                        int id = Integer.parseInt(parts[0]);
                        String nom = parts[1];
                        ajouterSommet(id, nom);
                    }
                }
            }

            if (edgesSection) {
                if (!ligne.startsWith("EDGES") && !ligne.isEmpty()) {
                    String[] parts = ligne.split(" ");
                    if (parts.length >= 3) {
                        int sommetDepart = Integer.parseInt(parts[0]);
                        int sommetArrivee = Integer.parseInt(parts[1]);
                        int poids = Integer.parseInt(parts[2]);
                        ajouterArete(sommetDepart, sommetArrivee, poids);
                    }
                }
            }
        }
        br.close();
    }
    
    // Méthode pour vérifier si deux mots diffèrent d'exactement une lettre
    private boolean differeDUneLettre(String mot1, String mot2) {
        if (mot1.length() != mot2.length()) {
            return false;
        }
        
        int differences = 0;
        for (int i = 0; i < mot1.length(); i++) {
            if (mot1.charAt(i) != mot2.charAt(i)) {
                differences++;
                if (differences > 1) {
                    return false;
                }
            }
        }
        return differences == 1;
    }
    
    // Méthode pour construire un graphe de mots à partir d'un fichier
    public void construireGrapheMots(String cheminFichier) throws IOException {
        // Vider le graphe actuel
        sommets.clear();
        aretes.clear();
        degres.clear();
        
        // Lire tous les mots du fichier
        List<String> mots = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(cheminFichier));
        String ligne;
        
        while ((ligne = br.readLine()) != null) {
            ligne = ligne.trim().toUpperCase(); // Normaliser en majuscules
            if (!ligne.isEmpty()) {
                mots.add(ligne);
            }
        }
        br.close();
        
        // Créer les sommets (un pour chaque mot)
        for (int i = 0; i < mots.size(); i++) {
            ajouterSommet(i, mots.get(i));
        }
        
        // Créer les arêtes entre mots qui diffèrent d'exactement une lettre
        for (int i = 0; i < mots.size(); i++) {
            for (int j = i + 1; j < mots.size(); j++) {
                if (differeDUneLettre(mots.get(i), mots.get(j))) {
                    // Poids de 1 pour toutes les arêtes (non pondéré)
                    ajouterArete(i, j, 1);
                    // Comme le graphe est non orienté, ajouter l'arête dans les deux sens
                    ajouterArete(j, i, 1);
                }
            }
        }
    }
      // Méthode pour construire tous les graphes de mots et analyser leurs propriétés
    public static void construireTousLesGraphesMots() {
        System.out.println("=== ANALYSE COMPLÈTE DES GRAPHES DE MOTS ===\n");
        
        for (int longueur = 4; longueur <= 18; longueur++) {
            String nomFichier = String.format("Mots/mots%02dlettres.txt", longueur);
            System.out.println("=== Graphe des mots de " + longueur + " lettres ===");
            
            try {
                Graphe graphe = new Graphe();
                graphe.construireGrapheMots(nomFichier);
                
                // Question 2: Nombre de sommets et d'arêtes
                int nbSommets = graphe.sommets.size();
                int nbAretes = graphe.aretes.size() / 2; // Diviser par 2 car non orienté
                  // Question 3: Nombre de composantes connexes
                int nbComposantes = graphe.nombreComposantesConnexes();
                List<List<Integer>> composantes = graphe.getComposantesConnexes();
                  // Question 4: Nombre de mots sans voisin
                int nbMotsSansVoisin = graphe.nombreMotsSansVoisin();
                
                // Question 5: Nombre de composantes composées uniquement de deux mots
                int nbComposantesDeuxMots = graphe.nombreComposantesDeuxMots();
                
                System.out.println("  - Nombre de sommets: " + nbSommets);
                System.out.println("  - Nombre d'arêtes: " + nbAretes);
                System.out.println("  - Nombre de composantes connexes: " + nbComposantes);
                System.out.println("  - Nombre de mots sans voisin: " + nbMotsSansVoisin);
                System.out.println("  - Nombre de composantes de deux mots: " + nbComposantesDeuxMots);
                System.out.println("  - Fichier: " + nomFichier);
                System.out.println();
                
            } catch (IOException e) {
                System.err.println("Erreur lors de la lecture du fichier " + nomFichier + ": " + e.getMessage());
            }
        }
    }
    
    // Méthode pour effectuer un parcours en profondeur (DFS) à partir d'un sommet
    private void dfs(int sommet, Map<Integer, Boolean> visite) {
        visite.put(sommet, true);
        
        // Parcourir tous les voisins du sommet
        for (Arete arete : aretes) {
            if (arete.sommetDepart == sommet) {
                int voisin = arete.sommetArrivee;
                if (!visite.get(voisin)) {
                    dfs(voisin, visite);
                }
            }
        }
    }
    
    // Méthode pour déterminer le nombre de composantes connexes (Question 3)
    public int nombreComposantesConnexes() {
        Map<Integer, Boolean> visite = new HashMap<>();
        
        // Initialiser tous les sommets comme non visités
        for (Integer sommet : sommets.keySet()) {
            visite.put(sommet, false);
        }
        
        int nbComposantes = 0;
        
        // Pour chaque sommet non visité, lancer un DFS et incrémenter le compteur
        for (Integer sommet : sommets.keySet()) {
            if (!visite.get(sommet)) {
                dfs(sommet, visite);
                nbComposantes++;
            }
        }
        
        return nbComposantes;
    }
    
    // Méthode pour obtenir les composantes connexes avec leurs tailles
    public List<List<Integer>> getComposantesConnexes() {
        Map<Integer, Boolean> visite = new HashMap<>();
        List<List<Integer>> composantes = new ArrayList<>();
        
        // Initialiser tous les sommets comme non visités
        for (Integer sommet : sommets.keySet()) {
            visite.put(sommet, false);
        }
        
        // Pour chaque sommet non visité, explorer sa composante
        for (Integer sommet : sommets.keySet()) {
            if (!visite.get(sommet)) {
                List<Integer> composante = new ArrayList<>();
                dfsCollecte(sommet, visite, composante);
                composantes.add(composante);
            }
        }
        
        return composantes;
    }
    
    // DFS qui collecte les sommets de la composante
    private void dfsCollecte(int sommet, Map<Integer, Boolean> visite, List<Integer> composante) {
        visite.put(sommet, true);
        composante.add(sommet);
        
        // Parcourir tous les voisins du sommet
        for (Arete arete : aretes) {
            if (arete.sommetDepart == sommet) {
                int voisin = arete.sommetArrivee;
                if (!visite.get(voisin)) {
                    dfsCollecte(voisin, visite, composante);
                }
            }
        }
    }
    
    // Méthode pour déterminer le nombre de mots sans voisin (Question 4)
    public int nombreMotsSansVoisin() {
        int nbMotsSansVoisin = 0;
        
        // Parcourir tous les sommets et compter ceux avec degré 0
        for (Map.Entry<Integer, Integer> entry : degres.entrySet()) {
            if (entry.getValue() == 0) {
                nbMotsSansVoisin++;
            }
        }
        
        return nbMotsSansVoisin;
    }
    
    // Méthode pour obtenir la liste des mots sans voisin
    public List<String> getMotsSansVoisin() {
        List<String> motsSansVoisin = new ArrayList<>();
        
        // Parcourir tous les sommets et collecter ceux avec degré 0
        for (Map.Entry<Integer, Integer> entry : degres.entrySet()) {
            if (entry.getValue() == 0) {
                int idSommet = entry.getKey();
                String mot = sommets.get(idSommet);
                motsSansVoisin.add(mot);
            }
        }
        
        return motsSansVoisin;
    }
    
    // Méthode pour déterminer le nombre de composantes composées uniquement de deux mots (Question 5)
    public int nombreComposantesDeuxMots() {
        List<List<Integer>> composantes = getComposantesConnexes();
        int nbComposantesDeuxMots = 0;
        
        // Compter les composantes de taille exactement 2
        for (List<Integer> composante : composantes) {
            if (composante.size() == 2) {
                nbComposantesDeuxMots++;
            }
        }
        
        return nbComposantesDeuxMots;
    }
    
    // Méthode pour obtenir les composantes de deux mots avec leurs mots
    public List<List<String>> getComposantesDeuxMots() {
        List<List<Integer>> composantes = getComposantesConnexes();
        List<List<String>> composantesDeuxMots = new ArrayList<>();
        
        // Collecter les composantes de taille exactement 2
        for (List<Integer> composante : composantes) {
            if (composante.size() == 2) {
                List<String> paireMots = new ArrayList<>();
                for (Integer idSommet : composante) {
                    paireMots.add(sommets.get(idSommet));
                }
                composantesDeuxMots.add(paireMots);
            }
        }
        
        return composantesDeuxMots;
    }
}