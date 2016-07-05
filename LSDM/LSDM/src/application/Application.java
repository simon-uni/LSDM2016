package application;

import indexing.Index;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import clustering.Cluster;
import triangles.TriangleCounting;
import graph.EnronReader;
import graph.Graph;
import graph.Vertex;

public class Application {

	public static void main(String[] args) {
		if (args.length != 2)
		{
			System.err.println("Please submit 2 arguments:");
			System.err.println("1. The path to the file that contains the graph");
			System.err.println("2. The project: \"indexing\" for project 5 or "
					+ "\"clustering\" for project 6");
			System.err.println("Exiting.");
			System.exit(1);
		}
		
		try {
			File f = null;
			if (args[0].equals("nf"))
			{
				f = new File(ClassLoader.getSystemResource("clusterGraph2").toURI());		
			} else {
				f = new File(args[0]);
			}
			Graph g = EnronReader.constructGraph(f);
			
			if (args[1].equals("indexing"))
			{// Shortest Path Indexing
				Index index = new Index();
				index.index(g);
				System.out.println("Indexing done.");
				System.out.println("Type in two nodes to learn about their distance, e.g. \"5 8\"");
				//  open up standard input
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				while (true)
				{
					
				    String consoleInput = null;
				    try {
				    	consoleInput = br.readLine();
				    } catch (IOException ioe) {
				    	System.out.println("An IO error occurred!");
				    	System.exit(1);
				    }
				    
				    String[] vertices = consoleInput.split("\\s+");
				    if (vertices.length == 2)
				    {
				    	int from = Integer.parseInt(vertices[0]);
				    	int to = Integer.parseInt(vertices[1]);
				    	Vertex v1 = g.vertices.get(from);
				    	Vertex v2 = g.vertices.get(to);
				    	int dist = index.shortestPath(v1, v2).distance;
				    	System.out.println("The distance is " + dist + " hops.");
				    	continue;
				    }
				    
				    System.err.println("Something went wrong.");
				}
			} else if (args[1].equals("clustering")) {// Clustering
				Cluster c = new Cluster();
				System.out.println("Type in the modularity value Q.");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				double Q = Double.parseDouble(br.readLine());
				c.cluster(Q, g);
				System.out.println("Clustering done.");
				System.out.println("Type in two nodes to learn if they belong to the same cluster, e.g. \"5 8\"");
				while (true)
				{
					
					String consoleInput = null;
				    try {
				    	consoleInput = br.readLine();
				    } catch (IOException ioe) {
				    	System.out.println("An IO error occurred!");
				    	System.exit(1);
				    }
				    
				    String[] vertices = consoleInput.split("\\s+");
				    if (vertices.length == 2)
				    {
				    	int from = Integer.parseInt(vertices[0]);
				    	int to = Integer.parseInt(vertices[1]);
				    	Vertex v1 = g.vertices.get(from);
				    	Vertex v2 = g.vertices.get(to);
				    	boolean res = c.sameCluster(v1, v2);
				    	if (res)
				    	{
				    		System.out.println("Vertices belong to same cluster.");
				    	} else {
				    		System.out.println("Vertices do not belong to same cluster.");
				    	}
				    	continue;
				    }
				    
				    System.err.println("Something went wrong.");
				}
			} else if (args[1].equals("counting")) {// Triangle Counting
				TriangleCounting counter = new TriangleCounting();
				int tris = counter.countTriangles(g);
				System.out.println("Counting triangles done.");
				System.out.println("Found " + tris + " triangles.");
				System.out.println("Type in a node to learn about its clustering coefficient, e.g. \"5\"");
				//  open up standard input
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				while (true)
				{
					
				    String consoleInput = null;
				    try {
				    	consoleInput = br.readLine();
				    } catch (IOException ioe) {
				    	System.out.println("An IO error occurred!");
				    	System.exit(1);
				    }
				    
				    int vId = Integer.parseInt(consoleInput);
				    int vTris = g.vertices.get(vId).triangles;
				    double cc = g.vertices.get(vId).getClusterCoefficient();
				    System.out.println(vTris + " triangles and cluster coefficient " + cc);
				}
			}
		} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} catch (NumberFormatException e) {
			System.err.println("The vertex numbers you provided could not be parsed.");
			System.err.println("Exiting.");
		} catch (NullPointerException e) {
			System.err.println("The vertex number is not in this graph.");
			System.err.println("Exiting.");
		} catch (IOException e) {
			System.err.println("IO Exception.");
			System.err.println("Exiting.");
		}
	}

}
