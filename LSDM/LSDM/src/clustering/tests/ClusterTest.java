package clustering.tests;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map.Entry;

import graph.Edge;
import graph.EnronReader;
import graph.Graph;
import graph.Vertex;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import clustering.Cluster;

public class ClusterTest {
	private static final double Q = 0.7d;
	private static final double DELTA = 0.0001d;
	Cluster c;
	Graph g;
	
	@Before
	public void init()
	{
		try {
			this.g = EnronReader.constructGraph(new File(
					ClassLoader.getSystemResource("clusterGraph1").toURI()));
			this.c = new Cluster();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@Test
	public void testEdgeWeights() {
		this.c.weightEdges(1, this.g);
		
		// according to lecture example
		for (Entry<Integer, Vertex> vEntry : this.g.vertices.entrySet())
		{
			Vertex v = vEntry.getValue();
			switch(v.id)
			{
				case 1:
					Assert.assertEquals(1, v.inflow);
					Assert.assertEquals(2d, v.out.get(2).weight, DELTA);
					Assert.assertEquals(2d, v.out.get(3).weight, DELTA);
					Assert.assertEquals(4d, v.out.get(4).weight, DELTA);
					Assert.assertEquals(2d, v.out.get(5).weight, DELTA);
					break;
				case 2:
					Assert.assertEquals(1, v.inflow);
					Assert.assertEquals(0d, v.out.get(1).weight, DELTA);
					Assert.assertEquals(2d, v.in.get(1).weight, DELTA);
					Assert.assertEquals(0d, v.out.get(3).weight, DELTA);
					Assert.assertEquals(1d, v.out.get(6).weight, DELTA);
					break;
				case 3:
					Assert.assertEquals(1, v.inflow);
					Assert.assertEquals(0d, v.out.get(1).weight, DELTA);
					Assert.assertEquals(0d, v.out.get(2).weight, DELTA);
					Assert.assertEquals(1d, v.out.get(6).weight, DELTA);
					break;
				case 4:
					Assert.assertEquals(1, v.inflow);
					Assert.assertEquals(0d, v.out.get(1).weight, DELTA);
					Assert.assertEquals(2d, v.out.get(7).weight, DELTA);
					Assert.assertEquals(1d, v.out.get(8).weight, DELTA);
					break;
				case 5:
					Assert.assertEquals(1, v.inflow);
					Assert.assertEquals(0d, v.out.get(1).weight, DELTA);
					Assert.assertEquals(1d, v.out.get(8).weight, DELTA);
					break;
				case 6:
					Assert.assertEquals(2, v.inflow);
					Assert.assertEquals(0d, v.out.get(2).weight, DELTA);
					Assert.assertEquals(0d, v.out.get(3).weight, DELTA);
					Assert.assertEquals(1d, v.out.get(9).weight, DELTA);
					break;
				case 7:
					Assert.assertEquals(1, v.inflow);
					Assert.assertEquals(0d, v.out.get(4).weight, DELTA);
					Assert.assertEquals(0.5d, v.out.get(9).weight, DELTA);
					Assert.assertEquals(0.5d, v.out.get(10).weight, DELTA);
					break;
				case 8:
					Assert.assertEquals(2, v.inflow);
					Assert.assertEquals(0d, v.out.get(4).weight, DELTA);
					Assert.assertEquals(0d, v.out.get(5).weight, DELTA);
					Assert.assertEquals(1d, v.out.get(10).weight, DELTA);
					break;
				case 9:
					Assert.assertEquals(3, v.inflow);
					Assert.assertEquals(0d, v.out.get(6).weight, DELTA);
					Assert.assertEquals(0d, v.out.get(7).weight, DELTA);
					Assert.assertEquals(0.5d, v.out.get(11).weight, DELTA);
					break;
				case 10:
					Assert.assertEquals(3, v.inflow);
					Assert.assertEquals(0d, v.out.get(7).weight, DELTA);
					Assert.assertEquals(0d, v.out.get(8).weight, DELTA);
					Assert.assertEquals(0.5d, v.out.get(11).weight, DELTA);
					break;
				case 11:
					Assert.assertEquals(6, v.inflow);
					Assert.assertEquals(0d, v.out.get(9).weight, DELTA);
					Assert.assertEquals(0d, v.out.get(10).weight, DELTA);
					break;
				default:
					Assert.assertTrue(false);
					break;
			}
		}
	}

	@Test
	public void testModularity()
	{
		double mod = this.c.modularityOf(this.g);
		
		Assert.assertTrue(mod <= 1);
		Assert.assertTrue(mod >= -1);
		
		Assert.assertEquals(0.0d, mod, DELTA);
	}
	
	@Test
	public void testUpdateWeights()
	{
		Graph copy = this.g.deepCopy();
		copy.vertices.get(1).out.get(2).weight = 2.0;
		copy.vertices.get(1).out.get(4).weight = 3.0;
		copy.vertices.get(9).out.get(11).weight = 4.0;
		copy.vertices.get(10).out.get(11).weight = 5.0;
		copy.vertices.get(2).out.get(3).weight = 6.0;
		copy.vertices.get(8).in.get(4).weight = 7.0;
		copy.vertices.get(8).in.get(5).weight = 8.0;
		copy.vertices.get(7).in.get(4).weight = 9.0;
		
		for (Entry<Integer, Vertex> vEntry : this.g.vertices.entrySet())
		{
			for (Entry<Integer, Edge> eEntry :
				vEntry.getValue().out.entrySet())
			{
				Assert.assertEquals(0.0d, eEntry.getValue().weight, DELTA);
			}
			for (Entry<Integer, Edge> eEntry :
				vEntry.getValue().in.entrySet())
			{
				Assert.assertEquals(0.0d, eEntry.getValue().weight, DELTA);
			}
		}
		
		this.c.updateWeights(this.g, copy);
		
		Assert.assertEquals(2.0d,
				g.vertices.get(1).out.get(2).weight, DELTA);
		Assert.assertEquals(3.0d,
				g.vertices.get(1).out.get(4).weight, DELTA);
		Assert.assertEquals(4.0d,
				g.vertices.get(9).out.get(11).weight, DELTA);
		Assert.assertEquals(5.0d,
				g.vertices.get(10).out.get(11).weight, DELTA);
		Assert.assertEquals(6.0d,
				g.vertices.get(2).out.get(3).weight, DELTA);
		Assert.assertEquals(7.0d,
				g.vertices.get(8).in.get(4).weight, DELTA);
		Assert.assertEquals(8.0d,
				g.vertices.get(8).in.get(5).weight, DELTA);
		Assert.assertEquals(9.0d,
				g.vertices.get(7).in.get(4).weight, DELTA);
		// test the other way round
		Assert.assertEquals(2.0d,
				g.vertices.get(2).in.get(1).weight, DELTA);
		Assert.assertEquals(3.0d,
				g.vertices.get(4).in.get(1).weight, DELTA);
		Assert.assertEquals(4.0d,
				g.vertices.get(11).in.get(9).weight, DELTA);
		Assert.assertEquals(5.0d,
				g.vertices.get(11).in.get(10).weight, DELTA);
		Assert.assertEquals(6.0d,
				g.vertices.get(3).in.get(2).weight, DELTA);
		Assert.assertEquals(7.0d,
				g.vertices.get(4).out.get(8).weight, DELTA);
		Assert.assertEquals(8.0d,
				g.vertices.get(5).out.get(8).weight, DELTA);
		Assert.assertEquals(9.0d,
				g.vertices.get(4).out.get(7).weight, DELTA);
	}
	
	@Test
	public void testRemoveEdges()
	{
		this.g.vertices.get(1).out.get(2).weight = 2.0;
		this.g.vertices.get(1).out.get(4).weight = 3.0;
		this.g.vertices.get(9).out.get(11).weight = 4.0;
		this.g.vertices.get(10).out.get(11).weight = 4.0;
		this.c.deleteEdges(g);
		Assert.assertTrue(g.vertices.get(11).in.isEmpty());
		Assert.assertNull(g.vertices.get(9).out.get(11));
		Assert.assertNull(g.vertices.get(10).out.get(11));
		Assert.assertNotNull(g.vertices.get(1).out.get(4));
	}
	
	@Test
	public void testClusterGraph2()
	{
		try {
			Graph g2 = EnronReader.constructGraph(new File(
					ClassLoader.getSystemResource("clusterGraph2").toURI()));
			this.c.cluster(Q, g2);
			Assert.assertEquals(0.75d, this.c.modularityOf(g2), DELTA);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		//this.c.cluster(0.7d, this.g);
	}
}
