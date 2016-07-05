package graph;

import java.util.HashMap;
import java.util.Map.Entry;

public class Vertex {
	public final static int noShortestPath = -1;
	public final static int noDistance = -1;
	public final static int noInFlow = -1;
	
	public int id;
	public HashMap<Integer, Edge> out;
	public HashMap<Integer, Edge> in;
	public int distance;
	public int inflow;
	public HashMap<Vertex, Integer> distanceTo;
	public int triangles;
	
	public Vertex(int id)
	{
		this.id = id;
		this.out = new HashMap<Integer, Edge>();
		this.in = new HashMap<Integer, Edge>();
		this.distance = noDistance;
		this.inflow = noInFlow;
		this.distanceTo = new HashMap<Vertex, Integer>();
		this.triangles = 0;
	}
	
	public double getClusterCoefficient()
	{
		if (this.out.size() <= 1)
		{
			return Integer.MIN_VALUE;
		}
		return (this.triangles * 2.0) / (this.out.size() * (this.out.size() - 1));
	}
	
	public void addOut(Edge toEdge)
	{
		if (!this.out.containsKey(toEdge.to.id))
		{
			this.out.put(toEdge.to.id, toEdge);
		}
	}
	
	public void addIn(Edge fromEdge)
	{
		if (!this.in.containsKey(fromEdge.from.id))
		{
			this.in.put(fromEdge.from.id, fromEdge);
		}
	}
	
	@Override
	public int hashCode() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Vertex))
		{
			return false;
		} else if (((Vertex) obj).id == this.id) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.id + System.lineSeparator() + "\t");
		for (Entry<Integer, Edge> e : this.out.entrySet())
		{
			sb.append(e.getValue().to.id + 
				" (" + e.getValue().weight + ") ");
		}
		return sb.toString();
	}
}
