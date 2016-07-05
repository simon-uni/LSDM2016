package clustering;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

public class Cluster {
	
	public Cluster()
	{
	}
	
	public Graph cluster(double Q, Graph g)
	{
		while(Q > this.modularityOf(g))
		{
			for (Entry<Integer, Vertex> vEntry : g.vertices.entrySet())
			{
				Vertex startV = vEntry.getValue();
				Graph copy = g.deepCopy();
				weightEdges(startV.id, copy);
				updateWeights(g, copy);
			}
			deleteEdges(g);
			resetWeights(g);
		}
		
		return g;
	}
	
	public void resetWeights(Graph g)
	{
		for (Entry<Integer, Vertex> vEntry : g.vertices.entrySet())
		{
			Vertex v = vEntry.getValue();
			for (Entry<Integer, Edge> eEntry : v.out.entrySet())
			{
				eEntry.getValue().weight = 0;
			}
		}
	}
	
	public void updateWeights(Graph beUpdated, Graph updater)
	{
		for (Entry<Integer, Vertex> v1Entry : beUpdated.vertices.entrySet())
		{
			Vertex v1 = v1Entry.getValue();
			Vertex v2 = updater.vertices.get(v1.id);
			for (Entry<Integer, Edge> e1Entry : v1.out.entrySet())
			{
				Edge e1 = e1Entry.getValue();
				Edge e2 = v2.out.get(e1.to.id);
				e1.weight += e2.weight;
			}
		}
	}
	
	public void deleteEdges(Graph g)
	{
		double max = -1;
		Set<Edge> vList = new HashSet<Edge>();
		for (Entry<Integer, Vertex> vEntry : g.vertices.entrySet())
		{
			Vertex v = vEntry.getValue();
			for (Entry<Integer, Edge> eEntry : v.out.entrySet())
			{
				Edge e = eEntry.getValue();
				if (e.weight > max)
				{
					vList.clear();
					vList.add(e);
					max = e.weight;
				} else if (e.weight == max) {
					vList.add(e);
				}
			}
		}
		
		// remove edges from in and out lists
		for (Edge e : vList)
		{
			g.vertices.get(e.from.id).out.remove(e.to.id);
			g.vertices.get(e.to.id).in.remove(e.from.id);
		}
	}
	
	public double modularityOf(Graph g)
	{
		double ret = 0;
		
		double m = 0;
		double neg = 0;
		HashSet<Vertex> visited = new HashSet<Vertex>();
		for (Entry<Integer, Vertex> vEntry : g.vertices.entrySet())
		{
			Vertex v = vEntry.getValue();
			m += v.out.size();
			if (!visited.contains(v))
			{
				// start a new bfs
				double aij = 0;
				HashSet<Vertex> curCommunity = new HashSet<Vertex>();
				Queue<Vertex> queue = new ArrayDeque<Vertex>();
				queue.add(v);
				while (!queue.isEmpty())
				{
					Vertex curV = queue.poll();
					visited.add(curV);
					curCommunity.add(curV);
					for (Entry<Integer, Edge> edge :
						curV.out.entrySet())
					{
						aij++;
						
						Vertex neighbour = g.vertices.get(
								edge.getValue().to.id);
						if (!curCommunity.contains(neighbour)
							&& !queue.contains(neighbour))
						{
							queue.add(neighbour);
						}
					}
				}
				
				// be aware of expected number of edges
				for (Vertex v1 : curCommunity)
				{
					for (Vertex v2 : curCommunity)
					{
						neg += - (v1.out.size() * v2.out.size());
					}
				}
				
				ret += aij;
			}
		}
		
		ret += (neg / (m));
		ret /= m;
		
		return ret;
	}
	
	public boolean sameCluster(Vertex from, Vertex to)
	{
		boolean ret = false;
		
		Queue<Vertex> queue = new ArrayDeque<Vertex>();
		Set<Vertex> visited = new HashSet<Vertex>();
		queue.add(from);
		while (!queue.isEmpty())
		{
			Vertex curVertex = queue.remove();
			visited.add(curVertex);
			for (Entry<Integer, Edge> outEdgeEntry : curVertex.out.entrySet())
			{
				Edge outEdge = outEdgeEntry.getValue();
				//dig deeper in the graph
				if (!visited.contains(outEdge.to) &&
						!queue.contains(outEdge.to))
				{
					if (outEdge.to.equals(to))
					{
						return true;
					}
					queue.add(outEdge.to);
				}
			}
		}
		if (visited.contains(to)) {
			ret = true;
		}
		
		return ret;
	}
	
	public Graph weightEdges(int startNodeId, Graph copy)
	{
		Vertex startNode = copy.vertices.get(startNodeId);
		if (startNode == null)
			throw new RuntimeException("Embarrassing: This should not have happened!");
		
		Queue<Vertex> queue = new ArrayDeque<Vertex>();
		Set<Vertex> visited = new HashSet<Vertex>();
		Map<Integer, Set<Vertex>> levels = new HashMap<Integer, Set<Vertex>>();
		queue.add(startNode);
		startNode.distance = 0;
		startNode.inflow = 1;
		
		while (!queue.isEmpty())
		{
			Vertex curVertex = queue.remove();
			visited.add(curVertex);
			if (levels.containsKey(curVertex.distance))
			{
				levels.get(curVertex.distance).add(curVertex);
			} else {
				Set<Vertex> vSet = new HashSet<Vertex>();
				vSet.add(curVertex);
				levels.put(curVertex.distance, vSet);
			}
			for (Entry<Integer, Edge> outEdgeEntry : curVertex.out.entrySet())
			{
				Edge outEdge = outEdgeEntry.getValue();
				//dig deeper in the graph
				if (!visited.contains(outEdge.to) &&
						!queue.contains(outEdge.to))
				{
					queue.add(outEdge.to);
				}
				//create or update distance and inflow labels
				if (outEdge.to.distance == Vertex.noDistance)
				{
					outEdge.to.distance = curVertex.distance + 1;
					outEdge.to.inflow = curVertex.inflow;
				} else if (outEdge.to.distance == curVertex.distance + 1) {
					outEdge.to.inflow += curVertex.inflow;
				}
			}
		}
		
		//work your way downwards starting from the leaves
		List<Integer> sortedKeys = new ArrayList<Integer>(levels.keySet());
		Collections.sort(sortedKeys);
		Collections.reverse(sortedKeys);
		for (int key : sortedKeys)
		{
			for (Vertex v : levels.get(key))
			{
				double vertexWeight = 1;
				for(Entry<Integer, Edge> e : v.out.entrySet())
				{
					if (e.getValue().to.distance == v.distance + 1)
					{
						vertexWeight += e.getValue().weight;
					}
				}
				for(Entry<Integer, Edge> e : v.in.entrySet())
				{
					if (e.getValue().from.distance == v.distance - 1)
					{
						e.getValue().weight = (vertexWeight / v.inflow) *
								e.getValue().from.inflow;
					}
				}
			}
		}
		
		return copy;
	}
}
