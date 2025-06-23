import java.util.Map;
import java.util.List;
import java.io.IOException;

public class Lancer {
    public static void main(String[] args) {
        // QUESTIONS 1, 2 et 3: Construction et analyse des graphes de mots  
        Graphe.construireTousLesGraphesMots();
        
        // Test détaillé avec un petit exemple pour vérifier
        System.out.println("=== ANALYSE DÉTAILLÉE - Mots de 4 lettres ===");
        try {
            Graphe grapheTest = new Graphe();
            grapheTest.construireGrapheMots("Mots/mots04lettres.txt");
            
            // Informations générales
            System.out.println("Nombre total de mots: " + grapheTest.getSommets().size());
            System.out.println("Nombre total d'arêtes: " + (grapheTest.getAretes().size() / 2));
              // Question 3: Composantes connexes
            int nbComposantes = grapheTest.nombreComposantesConnexes();
            List<List<Integer>> composantes = grapheTest.getComposantesConnexes();
              // Question 4: Mots sans voisin
            int nbMotsSansVoisin = grapheTest.nombreMotsSansVoisin();
            List<String> motsSansVoisin = grapheTest.getMotsSansVoisin();
            
            // Question 5: Composantes de deux mots
            int nbComposantesDeuxMots = grapheTest.nombreComposantesDeuxMots();
            List<List<String>> composantesDeuxMots = grapheTest.getComposantesDeuxMots();
            
            System.out.println("Nombre de composantes connexes: " + nbComposantes);
            System.out.println("Nombre de mots sans voisin: " + nbMotsSansVoisin);
            System.out.println("Nombre de composantes de deux mots: " + nbComposantesDeuxMots);
            
            // Afficher les détails des composantes
            composantes.sort((a, b) -> Integer.compare(b.size(), a.size())); // Trier par taille décroissante
            System.out.println("\nDétails des composantes connexes:");
            for (int i = 0; i < Math.min(5, composantes.size()); i++) {
                List<Integer> comp = composantes.get(i);
                System.out.print("  Composante " + (i + 1) + " (" + comp.size() + " mots): ");
                
                // Afficher quelques mots de cette composante
                for (int j = 0; j < Math.min(5, comp.size()); j++) {
                    System.out.print(grapheTest.getSommets().get(comp.get(j)));
                    if (j < Math.min(4, comp.size() - 1)) System.out.print(", ");
                }
                if (comp.size() > 5) System.out.print("...");
                System.out.println();
            }
              if (composantes.size() > 5) {
                System.out.println("  ... et " + (composantes.size() - 5) + " autres composantes");
            }
            
            // Afficher quelques mots sans voisin
            if (nbMotsSansVoisin > 0) {
                System.out.println("\nQuelques mots sans voisin:");
                for (int i = 0; i < Math.min(10, motsSansVoisin.size()); i++) {
                    System.out.print("  " + motsSansVoisin.get(i));
                    if (i < Math.min(9, motsSansVoisin.size() - 1)) System.out.print(", ");
                }
                if (motsSansVoisin.size() > 10) {
                    System.out.print("...");
                }
                System.out.println();            } else {
                System.out.println("\nAucun mot sans voisin dans ce graphe.");
            }
            
            // Afficher quelques composantes de deux mots
            if (nbComposantesDeuxMots > 0) {
                System.out.println("\nQuelques composantes de deux mots:");
                for (int i = 0; i < Math.min(10, composantesDeuxMots.size()); i++) {
                    List<String> paire = composantesDeuxMots.get(i);
                    System.out.println("  " + paire.get(0) + " - " + paire.get(1));
                }
                if (composantesDeuxMots.size() > 10) {
                    System.out.println("  ... et " + (composantesDeuxMots.size() - 10) + " autres paires");
                }
            } else {
                System.out.println("\nAucune composante de deux mots dans ce graphe.");
            }
            
        } catch (IOException e) {
            System.err.println("Erreur lors du test: " + e.getMessage());
        }
    }
}