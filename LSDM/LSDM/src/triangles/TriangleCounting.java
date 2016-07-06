package triangles;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class TriangleCounting {
	public static class DistinctVertexSets
	{
		public final Set<Vertex> nonHeavyHitters;
		public final Set<Vertex> heavyHitters;
		public DistinctVertexSets(Set<Vertex> nonHeavyHitters,
				Set<Vertex> heavyHitters) {
			this.nonHeavyHitters = nonHeavyHitters;
			this.heavyHitters = heavyHitters;
		}
	}
	
	public DistinctVertexSets findHeavyHitters(Graph g)
	{
		Set<Vertex> heavyHitters = new HashSet<Vertex>();
		Set<Vertex> nonHeavyHitters = new HashSet<Vertex>();
		double m = 0;
		for (Entry<Integer, Vertex> vEntry : g.vertices.entrySet())
		{
			m += vEntry.getValue().out.size();
		}
		double sqM = Math.sqrt(m / 2.0d);
		
		for (Entry<Integer, Vertex> vEntry : g.vertices.entrySet())
		{
			Vertex v = vEntry.getValue();
			if (v.out.size() > sqM)
			{
				heavyHitters.add(v);
			} else {
				nonHeavyHitters.add(v);
			}
		}
			
		return new DistinctVertexSets(nonHeavyHitters,
				heavyHitters);
	}
	
	public int countHeavyHitterTriangles(Set<Vertex> heavyHitters)
	{
		int ret = 0;
		
		for (Vertex v1 : heavyHitters)
		{
			for (Vertex v2 : heavyHitters)
			{	
				for (Vertex v3 : heavyHitters)
				{
					if (!(v1.id < v2.id) || !(v1.id < v3.id) || !(v2.id < v3.id))
					{
						continue;
					} else if(v1.out.containsKey(v2.id) &&
							v2.out.containsKey(v3.id) &&
							v3.out.containsKey(v1.id)) {// triangle found
						ret++;
						v1.triangles++;
						v2.triangles++;
						v3.triangles++;
					}
				}
			}
		}
		
		return ret;
	}
	
	public int countNonHHTriangles(Set<Vertex> nonHeavyHitters, Graph g)
	{
		int ret = 0;

		for (Vertex v1 : nonHeavyHitters)
		{
			for (Entry<Integer, Edge> v2Entry : v1.out.entrySet())
			{
				for (Entry<Integer, Edge> v3Entry : v1.out.entrySet())
				{
					Vertex v3 = v3Entry.getValue().to;
					Vertex v2 = v2Entry.getValue().to;

					if (!precedes(v1, v2) || !precedes(v1, v3) || !precedes(v2, v3))
					{
						continue;
					} else if(v2.out.containsKey(v3.id)) {// triangle found
						ret++;
						v1.triangles++;
						v2.triangles++;
						v3.triangles++;
					}
				}
			}
		}
		
		return ret;
	}
	
	public boolean precedes(Vertex v1, Vertex v2)
	{
		return (v1.out.size() < v2.out.size()) ||
				(v1.out.size() == v2.out.size() &&
				v1.id < v2.id);
	}
	
	public int countTriangles(Graph g)
	{
		DistinctVertexSets sets = findHeavyHitters(g);
		
		int ret = countHeavyHitterTriangles(sets.heavyHitters);
		ret += countNonHHTriangles(sets.nonHeavyHitters, g);
		
		return ret;
	}
}
