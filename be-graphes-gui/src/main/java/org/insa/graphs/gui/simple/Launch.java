package org.insa.graphs.gui.simple;




import java.sql.Date;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;




public class Launch {
    //les verifications des tests automatiques later 

    /**
     * Create a new Drawing inside a JFrame an return it.
     * 
     * @return The created drawing.
     * 
     * @throws Exception if something wrong happens when creating the graph.
     */
    //comparer les performances de Djikstra et Astar en temps et en distance 
    private static long dureeAStar=0;
    private static String pathtomaps="mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps";
    private static long dureeDjikstra=0;
    private static int nb_sommets_visites_djikstra=0;
    private static int nb_sommets_visites_astar=0;

    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    public static int test_temps(Graph graph,String algorithme,int origine,int destination){

        int resultat_test_temps=0;
        //test distance
        long temps_deb;
        long temps_fin;

        Path pathAlgo = null;

            temps_deb = System.currentTimeMillis();
            pathAlgo = TestPath(graph,  origine, destination, algorithme);
            temps_fin = System.currentTimeMillis();
            dureeAStar += (temps_fin-temps_deb);

        Path res_BellmanFord = TestPath(graph,  origine, destination, "BellmanFord");

        if (comparer_temps(pathAlgo, res_BellmanFord) == 1) {
            resultat_test_temps++;
        }

        return resultat_test_temps;
    }

    public static int test_distance(Graph graph,String algorithme,int origine,int destination){

        int resultat_test_distance=0;
        //test distance
        long temps_deb=0;
        long temps_fin=0;

        Path pathAlgo = null;

        if (algorithme.equals("Astar")){
            //Astar
            temps_deb = System.currentTimeMillis();
            pathAlgo = TestPath(graph,  origine, destination,  "Astar");
            temps_fin = System.currentTimeMillis();
            dureeAStar += (temps_fin-temps_deb);
        } else {
            //Djikstra
            temps_deb = System.currentTimeMillis();
            pathAlgo = TestPath(graph,  origine, destination, "Djikstra");
            temps_fin = System.currentTimeMillis();
            dureeDjikstra += (temps_fin-temps_deb);
        }
        //if (pathAlgo==null) return 0;

        Path res_BellmanFord = TestPath(graph,  origine, destination, "BellmanFord");

        if (comparer_distance(pathAlgo, res_BellmanFord) == 1) {
            resultat_test_distance++;
        }

        return resultat_test_distance;
    }

    //fonction comparant les deux chemins en temps
    public static int comparer_temps(Path chemin1, Path chemin2){
        if (chemin1 == null){
            if (chemin2 == null){
                return 1;
            } else {
                return 0;
            }
        } else {
            if (chemin2 == null){
                return 0;
            }
        }
            if (Double.compare(chemin1.getMinimumTravelTime(), chemin2.getMinimumTravelTime()) == 0) {
                return 1;
            } else {
                return 0;
            }
        }
    //fonction comparant les deux chemins en distance
    public static int comparer_distance(Path chemin1, Path chemin2){
        if (chemin1 == null){
            if (chemin2 == null){
                return 1;
            } else {
                return 0;
            }
            } else {
                if (chemin2 == null){
                    return 0;
                }
            }
            if (Float.compare(chemin1.getLength(), chemin2.getLength()) == 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
    

    //la fonction qui cree les chemins pour comparer_temps et distance

    public static  Path TestPath(Graph graph,int idOrigine,int idDestination,String algorithme){

        Node origine = graph.get(idOrigine);
        Node destination = graph.get(idDestination);
        ShortestPathSolution res_algo;
        ShortestPathData data= new ShortestPathData(graph,origine,destination,ArcInspectorFactory.getAllFilters().get(0));
        Path chemin = null;



        if (algorithme.equals("Astar")){ 
                AStarAlgorithm AstarAlgo = new AStarAlgorithm(data);
                res_algo=  AstarAlgo.doRun();
                chemin = res_algo.getPath();
                nb_sommets_visites_astar+=res_algo.getNb_sommets_visites();
                
        }
        else if (algorithme.equals("Djikstra")){
                DijkstraAlgorithm DjikstraAlgo = new DijkstraAlgorithm(data);
                res_algo=  DjikstraAlgo.doRun();
                chemin = res_algo.getPath();    
                nb_sommets_visites_djikstra+=res_algo.getNb_sommets_visites();
                    
        }
        else if (algorithme.equals("BellmanFord")){  
                BellmanFordAlgorithm BellmanFord = new BellmanFordAlgorithm(data);
                res_algo=  BellmanFord.doRun();
                chemin = res_algo.getPath();
          
        }
        else{
            System.out.println("algorithme non valide");
            return chemin;
         
        }  

        //verification que le coût du chemin calcule par Dijkstra est bien le même que celui calcule par la classe Path
       
        
        //test de validite du chemin trouve      
        /*if (chemin==null || chemin.isValid()==false){
            System.out.println("Chemin non valide");
            return null;
        }*/
        return chemin;

    }
    public static double test_longue_distance(Graph graph,String algorithme,int origine,int destination){

        //test distance

        Path pathAlgo = null;
    

        pathAlgo = TestPath(graph,  origine, destination,  algorithme);
        if (pathAlgo==null) return 0.0;
        double distance_vol_oiseau = graph.getNodes().get(origine).getPoint().distanceTo(graph.getNodes().get(destination).getPoint());

        return Math.abs(pathAlgo.getLength() - distance_vol_oiseau); 
    }

    public static void batterie_test_distance() throws Exception{
        Double erreurMoyenneAstar;
        Double erreurMoyenneDijkstra;
        Double quantiteErrueurAstar =0.0;
        Double quantiteErreurDijkstra=0.0;
        Double distanceMax=0.0;
        Double distanceMin=Double.MAX_VALUE;
        Double distance_vol_oiseau=0.0;
        Double distanceTotale=0.0;
        Double DistanceMoyenne=0.0;
        int origine;
        int destination;
        
        int nb_cartes=2;
        int nb_test=10;
        
        System.out.println("Tests sur de longs trajet, on ne peut pas se servir de Bellman ford comme reference");
        final String[] mapNames = {Launch.pathtomaps+"/bretagne.mapgr", Launch.pathtomaps+"/california.mapgr"};

        for (int i =0;i<nb_cartes;i++){
            final GraphReader reader = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(mapNames[i]))));

            final Graph graph = reader.read();
            System.out.println("tests sur la carte : " + mapNames[i]);
            quantiteErreurDijkstra=0.0;
            quantiteErrueurAstar=0.0;
            distanceTotale=0.0;
            DistanceMoyenne=0.0;
            distanceMax=0.0;
            distanceMin=Double.MAX_VALUE;

            for (int j = 0; j < nb_test; j++) {
                origine= (int)Math.floor(Math.random()*graph.size());
                destination = (int) Math.floor(Math.random()*graph.size());

                quantiteErreurDijkstra+=test_longue_distance(graph, "Djikstra", origine, destination);
                quantiteErrueurAstar+=test_longue_distance(graph, "Astar", origine, destination);
                
                distance_vol_oiseau = graph.getNodes().get(origine).getPoint().distanceTo(graph.getNodes().get(destination).getPoint());
                distanceMax=Double.max(distanceMax, distance_vol_oiseau);
                distanceMin=Double.min(distanceMin, distance_vol_oiseau);
                distanceTotale+=distance_vol_oiseau;

            }
            erreurMoyenneAstar=(quantiteErrueurAstar)/(nb_test);
            erreurMoyenneDijkstra=(quantiteErreurDijkstra)/(nb_test);
            DistanceMoyenne= (distanceTotale)/(nb_test);
            System.out.println("l'ecart moyen entre dijkstra et la distance a vol d'oiseau est : " + Math.floor(erreurMoyenneDijkstra) + " metres");
            System.out.println("l'ecart moyen entre Astar et la distance a vol d'oiseau est : " + Math.floor(erreurMoyenneAstar) + " metres");
            System.out.println("Distances parcourues : max : " +Math.floor( distanceMax )+ "  min : " + Math.floor(distanceMin) + " moyenne : " + Math.floor(DistanceMoyenne));
            if (erreurMoyenneAstar.compareTo(erreurMoyenneDijkstra)==1){
                System.out.printf("Astar et djikstra trouvent les même chemins");
            }
        }

    }





    public static void main(String[] args) throws Exception {

//Test de prise de points randoms avec un nb de test =200 avec des cartes routieres et non routieres

        int resultatDjikstra_correct_en_temps=0;

        int resultatAStar_correct_en_temps=0;

        int resultatDjikstra_correct_en_distance=0;

        int resultatAStar_correct_en_distance=0;

        int resultatDjikstra_correct_en_temps_nul=0;

        int resultatAStar_correct_en_temps_nul=0;

        int resultatDjikstra_correct_en_distance_nul=0;

        int resultatAStar_correct_en_distance_nul=0;

        int origine,destination;
        
        // Visit these directory to see the list of available files on Commetud.
        
        //test avec les cartes routieres=>insa et non routieres =>carre et cartes non connexes =>guadeloupe
        final String[] mapNames = {pathtomaps +"/insa.mapgr", pathtomaps +"/insa.mapgr"};
        int nb_test=200;
        int nb_cartes = 2;

        for (int j =0; j<nb_cartes; j++){

        
        // Create a graph reader.
        final GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapNames[j]))));

        // TODO: Read the graph.
        final Graph graph = reader.read();


        System.out.println("commencons les tests avec la carte "+mapNames[j]);
        ///la boucle qui va renvoyer le nombre de resultats bons
        for (int i = 0; i < nb_test; i++){
            //avoir un nombre aleatoire entre 0 et la taille du graphe 
            origine= (int)Math.floor(Math.random()*graph.size());
            destination = (int) Math.floor(Math.random()*graph.size());

            resultatDjikstra_correct_en_temps=resultatDjikstra_correct_en_temps+test_temps(graph,"Djikstra",origine,destination);
            resultatAStar_correct_en_temps=resultatAStar_correct_en_temps+test_temps(graph,"Astar",origine,destination);
        
            resultatDjikstra_correct_en_distance=resultatDjikstra_correct_en_distance+test_distance(graph,"Djikstra",origine,destination);
            resultatAStar_correct_en_distance=resultatAStar_correct_en_distance+test_distance(graph,"Astar",origine,destination);
    }
    
        }

    System.out.println("le nombre de test en total c'est "+nb_test*nb_cartes);

    //veracite Djikstra en temps et distance
    int pourcentage_veracite_Djikstra_en_temps=(resultatDjikstra_correct_en_temps*100/(nb_test*nb_cartes));
    int pourcentage_veracite_Djikstra_en_distance=(resultatDjikstra_correct_en_distance*100/(nb_test*nb_cartes));
    System.out.println("le nombre de  resultats corrects de djikstra en temps est de "+resultatDjikstra_correct_en_temps);
    System.out.println("le nombre de  resultats corrects de djikstra en distance est de "+resultatDjikstra_correct_en_distance);
    System.out.println("le pourcentage de veracite de Djikstra en temps est de  "+pourcentage_veracite_Djikstra_en_temps);
    System.out.println("le pourcentage de veracite de Djikstra en distance est de  "+pourcentage_veracite_Djikstra_en_distance);

    //veracite AStar en temps et distance
    int pourcentage_veracite_AStar_en_temps=(resultatAStar_correct_en_temps*100/(nb_test*nb_cartes));
    int pourcentage_veracite_AStar_en_distance=(resultatAStar_correct_en_distance*100/(nb_test*nb_cartes));
    System.out.println("le nombre de  resultats corrects de AStar en temps est de "+resultatAStar_correct_en_temps);
    System.out.println("le nombre de  resultats corrects de AStar en distance est de "+resultatAStar_correct_en_distance);
    System.out.println("le pourcentage de veracite de AStar en temps est de  "+pourcentage_veracite_AStar_en_temps);
    System.out.println("le pourcentage de veracite de AStar en distance est de  "+pourcentage_veracite_AStar_en_distance);

    //veracite total Djikstra et AStar
    int nb_total_Djikstra=resultatDjikstra_correct_en_temps+resultatDjikstra_correct_en_distance;
    int nb_total_AStar=resultatAStar_correct_en_temps+resultatAStar_correct_en_distance;

    int pourcentage_veracite_Djikstra=(nb_total_Djikstra*100/(2*(nb_test*nb_cartes)));
    int pourcentage_veracite_Astar=(nb_total_AStar*100/(2*(nb_test*nb_cartes)));
    System.out.println("le pourcentage de veracite de djikstra en temps est distance "+pourcentage_veracite_Djikstra);
    System.out.println("le pourcentage de veracite de AStar en temps et distance est de  "+pourcentage_veracite_Astar);

    //comparaison des performances de AStar et Djikstra en duree d'execution
    System.out.println("la duree de AStar "+dureeAStar);
    System.out.println("la duree de Djikstra "+dureeDjikstra);
    System.out.println("le pourcentage de difference de duree entre AStar et Djikstra est de "+(dureeAStar-dureeDjikstra)*100/dureeDjikstra+"%");
    System.out.println("le pourcentage de difference de duree entre Djikstra et AStar est de "+(dureeDjikstra-dureeAStar)*100/dureeAStar+"%");
 
   //Test des chemin nuls et voir le temps d'execution des deux algos

    dureeAStar=0;
    dureeDjikstra=0;
    System.out.println("comparaison de performance sur un trajet de distance 0");

    for (int j =0; j<nb_cartes; j++){

        
        // Create a graph reader.
        final GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapNames[j]))));

        // TODO: Read the graph.
        final Graph graph = reader.read();
        ///Test de validite des algorithmes Djikstra et AStar
//Decommentaire pour test des chemin nuls => tous les chemins ont bien un cout de 0

        System.out.println("commencons les tests avec la carte avec des points identiques"+mapNames[j]);
        ///la boucle qui va renvoyer le nombre de resultats bons
        for (int i = 0; i < nb_test; i++){
            //avoir un nombre aleatoire entre 0 et la taille du graphe 
            origine= i;
            destination = i;

            resultatDjikstra_correct_en_temps_nul=resultatDjikstra_correct_en_temps_nul+test_temps(graph,"Djikstra",origine,destination);
            resultatAStar_correct_en_temps_nul=resultatAStar_correct_en_temps_nul+test_temps(graph,"Astar",origine,destination);
        
            resultatDjikstra_correct_en_distance_nul=resultatDjikstra_correct_en_distance_nul+test_distance(graph,"Djikstra",origine,destination);
            resultatAStar_correct_en_distance_nul=resultatAStar_correct_en_distance_nul+test_distance(graph,"Astar",origine,destination);
    }
    
        }
        //comparaison des performances de AStar et Djikstra en duree d'execution
    System.out.println("la duree de AStar "+dureeAStar);
    System.out.println("la duree de Djikstra "+dureeDjikstra);
    System.out.println("le pourcentage de difference de duree entre AStar et Djikstra est de "+(dureeAStar-dureeDjikstra)*100/dureeDjikstra+"%");
    System.out.println("le pourcentage de difference de duree entre Djikstra et AStar est de "+(dureeDjikstra-dureeAStar)*100/dureeAStar+"%");

    batterie_test_distance();

    System.out.println("nombre de sommets visités par Djikstra sur tous les tests: " + nb_sommets_visites_djikstra);
    System.out.println("nombre de sommets visités par Astar sur tous les tests: " + nb_sommets_visites_astar);
    System.out.println("le pourcentage de sommets visites par djikstra en plus de Astar est "+(nb_sommets_visites_djikstra-nb_sommets_visites_astar)*100/nb_sommets_visites_astar+"%");
}

}


