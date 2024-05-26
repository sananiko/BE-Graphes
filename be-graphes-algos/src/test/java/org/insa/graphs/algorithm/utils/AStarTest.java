package org.insa.graphs.algorithm.utils;
import org.insa.graphs.algorithm.shortestpath.*;

//import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;
import org.junit.Assert;
import org.junit.Test;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Node;


import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Path;
import org.insa.graphs.algorithm.AbstractSolution;
import java.util.ArrayList;
import java.util.List;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.ArcInspector;
import org.junit.Assert.*;

import org.junit.Test;

public class AStarTest {

    public static void Test_Basique() throws IOException{
        String mapName1 = "mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/guadeloupe.mapgr";
        BinaryGraphReader reader1 = new BinaryGraphReader(
            new DataInputStream(new BufferedInputStream(new FileInputStream(mapName1))));
    
        Graph graph1 = reader1.read();
    
        //chemin nul
        Node origin1 = graph1.get(520);
        Node destination1 = graph1.get(520); 
        AStarAlgorithm Astar = new AStarAlgorithm(new ShortestPathData(graph1,origin1,destination1,ArcInspectorFactory.getAllFilters().get(0)));
        ShortestPathSolution solAstar1 = Astar.run();
    
        //chemin inexistant sur la map
        Node origin2 = graph1.get(14282);
        Node destination2= graph1.get(15024); 
        AStarAlgorithm Astar2 = new AStarAlgorithm(new ShortestPathData(graph1,origin2,destination2,ArcInspectorFactory.getAllFilters().get(0)));
        ShortestPathSolution solAstar2 = Astar2.run();
    
       
    
    }
    public void Test_grand_scenario(String mapname,int origine, int destination) throws IOException {
        /*Apres avoir cherché comment nous pouvions verifier sur les grandes cartes nos algorithmes ,nous avions procédé à faire des tests de coherences pour verifier si les chemins les plus rapides sont inferieurs en temps des PCC et supérieurs en distance */
    
        
        double coutAStar_fastestsolution_distance=0;
        double coutAStar_shortestsolution_distance=0;
        double coutAStar_fastestsolution_temps=0;
        double coutAStar_shortestsolution_temps=0;
        
    
        //Mapname1 graph1=> paris
        final GraphReader reader1 = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapname))));
    
        final Graph graph1 = reader1.read();
    
        int nbNodes=graph1.size();
    
            //filtre mode temps => ArcInspectorFactory.getAllFilters().get(2) all roads allowed
            //filtre mode distance => ArcInspectorFactory.getAllFilters().get(0) all roads allowed  
            System.out.println("Commencons les tests");
            //temps=>fastest
            ShortestPathData data = new ShortestPathData(graph1, graph1.get(origine),graph1.get(destination), ArcInspectorFactory.getAllFilters().get(2));
            
            AStarAlgorithm AStar = new AStarAlgorithm(data);
            ShortestPathSolution AStarsolution = AStar.run();
    
            if (AStarsolution.getPath() == null) {
                System.out.println("on n a pas trouvé de solution et pas de cout");
            }
            //comparaison des couts
            else {
    
                    coutAStar_fastestsolution_temps = AStarsolution.getPath().getMinimumTravelTime();
                    coutAStar_fastestsolution_distance = AStarsolution.getPath().getLength();
        }
        //Mode distance=>shortest
    
            data = new ShortestPathData(graph1, graph1.get(origine),graph1.get(destination), ArcInspectorFactory.getAllFilters().get(0));
            
            AStar = new AStarAlgorithm(data);
    
            AStarsolution = AStar.run();
    
            if (AStarsolution.getPath() == null) {
                System.out.println("on n a pas trouvé de solution et pas de cout");
            }
            //comparaison des couts
            else {
    
                    coutAStar_shortestsolution_temps = AStarsolution.getPath().getMinimumTravelTime();
                    coutAStar_shortestsolution_distance = AStarsolution.getPath().getLength();
        }
    
            System.out.println("Cout en temps du chemin le plus fast : " + coutAStar_fastestsolution_temps);
            System.out.println("Cout en distance du chemin le plus fast : " + coutAStar_fastestsolution_distance);
            System.out.println("Cout en temps du chemin le plus short : " + coutAStar_shortestsolution_temps);
            System.out.println("Cout en distance du chemin le plus short  : " + coutAStar_shortestsolution_distance);
            assertTrue(coutAStar_fastestsolution_temps <= coutAStar_shortestsolution_temps);
            assertTrue(coutAStar_fastestsolution_distance >= coutAStar_shortestsolution_distance);
    }
    }

    
    
    

