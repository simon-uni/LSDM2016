package graph;

public class Edge {
	public Vertex from;
	public Vertex to;
	public double weight;
	
	public Edge(Vertex from, Vertex to)
	{
		this.from = from;
		this.to = to;
		this.weight = 0.0d;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Edge))
		{
			return false;
		} else if (((Edge) obj).from.id == this.from.id &&
				((Edge) obj).to.id == this.to.id) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString()
	{
		return (from.id + "->" + to.id + "(" + weight + ")");
	}
}
