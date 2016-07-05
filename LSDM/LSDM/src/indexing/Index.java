package indexing;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import graph.Graph;
import graph.Vertex;

public class Index {
	
	public Index()
	{
	}
	
	public static class DistancePair
	{
		public final Vertex point;
		public final int distance;
		public DistancePair(Vertex point, int distance)
		{
			this.point = point;
			this.distance = distance;
		}
	}
	
	public DistancePair shortestPath(Vertex from, Vertex to)
	{
		int ret = Vertex.noShortestPath;
		
		Set<Vertex> intersectionSet = new HashSet<Vertex>(from.distanceTo.keySet());
		intersectionSet.retainAll(to.distanceTo.keySet());
		Vertex midPoint = null;
		for (Vertex point : intersectionSet)
		{
			int distance = from.distanceTo.get(point) + to.distanceTo.get(point);
			if (ret == Vertex.noShortestPath || ret > distance)
			{
				midPoint = point;
				ret = distance;
			}
		}
		
		return new DistancePair(midPoint, ret);
	}
	
	public void indexFromNode(Graph g, Vertex startNode)
	{
		Set<Vertex> visited = new HashSet<Vertex>();
		Queue<DistancePair> queue = new ArrayDeque<DistancePair>();
		queue.add(new DistancePair(startNode, 0));
		while (!queue.isEmpty())
		{
			DistancePair curDistPair = queue.poll();
			visited.add(curDistPair.point);
			
			DistancePair pair = shortestPath(startNode, curDistPair.point);
			if (pair.distance != Vertex.noShortestPath &&
					pair.distance <= curDistPair.distance + 1)
			{// prune
			} else {
				curDistPair.point.distanceTo.put(startNode, curDistPair.distance);
				
				for (Integer neighbourInt : curDistPair.point.out.keySet())
				{
					Vertex neighbour = g.vertices.get(neighbourInt);
					// dig deeper bfs-style
					if (!queue.contains(neighbour) && !visited.contains(neighbour))
					{
						queue.add(new DistancePair(neighbour, curDistPair.distance + 1));
					}	
				}
			}
		}
	}
	
	public void index(Graph g)
	{
		for (Entry<Integer, Vertex> vEntry : g.vertices.entrySet())
		{
			Vertex v = vEntry.getValue();
			indexFromNode(g, v);
		}
	}
	
	// impossible
	public List<Vertex> findPath(Vertex from, Vertex to)
	{
		List<Vertex> ret = new ArrayList<Vertex>();
		if (shortestPath(from, to).distance == Vertex.noShortestPath)
		{
			return ret;
		} else if (shortestPath(from, to).distance == 0) {
			ret.add(from);
			return ret;
		}
		
		
		
		return ret;
	}
}
