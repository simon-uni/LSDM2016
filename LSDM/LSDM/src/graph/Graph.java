package graph;

import java.util.HashMap;
import java.util.Map.Entry;

public class Graph {
	public HashMap<Integer, Vertex> vertices;
	
	public Graph()
	{
		this.vertices = new HashMap<Integer, Vertex>();
	}
	
	public Graph(HashMap<Integer, Vertex> vertices)
	{
		this.vertices = vertices;
	}
	
	public void addVertex(int id)
	{
		if (!this.vertices.containsKey(id))
		{
			this.vertices.put(id, new Vertex(id));
		}
	}
	
	public void addEdge(int a, int b)
	{
		Vertex aV = null, bV = null;
		if (this.vertices.containsKey(a))
		{
			aV = this.vertices.get(a);
		} else {
			aV = new Vertex(a);
			this.vertices.put(a, aV);
		}
		if (this.vertices.containsKey(b))
		{
			bV = this.vertices.get(b);
		} else {
			bV = new Vertex(b);
			this.vertices.put(b, bV);
		}
		
		Edge e = aV.out.get(bV.id); 
		if (e == null)
		{
			e = bV.in.get(aV.id);
			if (e == null)
			{
				e = new Edge(aV, bV);
				aV.out.put(bV.id, e);
				bV.in.put(aV.id, e);
			} else {
				aV.out.put(bV.id, e);
			}
		} else {
			bV.in.put(aV.id, e);
		}
	}
	
	public Graph deepCopy()
	{
		Graph g = new Graph();
		for (Entry<Integer, Vertex> v : this.vertices.entrySet())
		{
			Vertex newV = new Vertex(v.getValue().id);
			g.vertices.put(newV.id, newV);
			for (Entry<Integer, Edge> e :
				v.getValue().out.entrySet()) {
				if (g.vertices.containsKey(e.getValue().to.id))
				{
					Vertex to = g.vertices.get(e.getValue().to.id);
					g.addEdge(newV.id, to.id);
				}
			}
			for (Entry<Integer, Edge> e :
				v.getValue().in.entrySet()) {
				if (g.vertices.containsKey(e.getValue().from.id))
				{
					Vertex from = g.vertices.get(e.getValue().from.id);
					g.addEdge(from.id, newV.id);
				}
			}
		}
		return g;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Entry<Integer, Vertex> v : this.vertices.entrySet())
		{
			sb.append(v.getValue() + System.lineSeparator());
		}
		return sb.toString();
	}
}
