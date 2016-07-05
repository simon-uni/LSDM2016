package indexing.tests;

import indexing.Index;

import java.io.File;
import java.net.URISyntaxException;

import graph.EnronReader;
import graph.Graph;
import graph.Vertex;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndexTest {
	Graph g;
	Index i;
	
	@Before
	public void init() {
		try {
			this.g = EnronReader.constructGraph(new File(
					ClassLoader.getSystemResource("labelGraph1").toURI()));
			this.i = new Index();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@Test
	public void testShortestPath() {
		Vertex v1 = g.vertices.get(1);
		Vertex v2 = g.vertices.get(2);
		g.vertices.get(1).distanceTo.put(v1, 0);
		g.vertices.get(2).distanceTo.put(v1, 4);
		
		Assert.assertEquals(4, i.shortestPath(v1, v2).distance);
		Assert.assertEquals(4, i.shortestPath(v2, v1).distance);
		
		// with a new point in between, shortest path is 2
		Vertex v3 = g.vertices.get(3);
		g.vertices.get(1).distanceTo.put(v3, 1);
		g.vertices.get(2).distanceTo.put(v3, 1);
		Assert.assertEquals(2, i.shortestPath(v1, v2).distance);
		Assert.assertEquals(2, i.shortestPath(v2, v1).distance);
	}

	@Test
	public void testIndexFromFirstVertex() {
		Vertex v1 = g.vertices.get(1);
		Vertex v2 = g.vertices.get(2);
		Vertex v3 = g.vertices.get(3);
		Vertex v4 = g.vertices.get(4);
		Vertex v5 = g.vertices.get(5);
		Vertex v6 = g.vertices.get(6);
		Vertex v7 = g.vertices.get(7);
		Vertex v8 = g.vertices.get(8);
		Vertex v9 = g.vertices.get(9);
		Vertex v10 = g.vertices.get(10);
		Vertex v11 = g.vertices.get(11);
		Vertex v12 = g.vertices.get(12);
		this.i.indexFromNode(g, v1);
		
		Assert.assertEquals(0, i.shortestPath(v1, v1).distance);
		Assert.assertEquals(2, i.shortestPath(v1, v2).distance);
		Assert.assertEquals(2, i.shortestPath(v1, v3).distance);
		Assert.assertEquals(1, i.shortestPath(v1, v4).distance);
		Assert.assertEquals(2, i.shortestPath(v1, v5).distance);
		Assert.assertEquals(1, i.shortestPath(v1, v6).distance);
		Assert.assertEquals(1, i.shortestPath(v1, v7).distance);
		Assert.assertEquals(1, i.shortestPath(v1, v8).distance);
		Assert.assertEquals(1, i.shortestPath(v1, v9).distance);
		Assert.assertEquals(1, i.shortestPath(v1, v10).distance);
		Assert.assertEquals(2, i.shortestPath(v1, v11).distance);
		Assert.assertEquals(1, i.shortestPath(v1, v12).distance);
	}
	
	@Test
	public void testIndex() {
		Vertex v1 = g.vertices.get(1);
		Vertex v2 = g.vertices.get(2);
		Vertex v3 = g.vertices.get(3);
		Vertex v4 = g.vertices.get(4);
		Vertex v5 = g.vertices.get(5);
		Vertex v6 = g.vertices.get(6);
		Vertex v7 = g.vertices.get(7);
		Vertex v8 = g.vertices.get(8);
		Vertex v9 = g.vertices.get(9);
		Vertex v10 = g.vertices.get(10);
		Vertex v11 = g.vertices.get(11);
		Vertex v12 = g.vertices.get(12);
		this.i.index(g);
		Assert.assertEquals(0, i.shortestPath(v1, v1).distance);
		Assert.assertEquals(0, i.shortestPath(v2, v2).distance);
		Assert.assertEquals(2, i.shortestPath(v1, v2).distance);
		Assert.assertEquals(1, i.shortestPath(v4, v5).distance);
		Assert.assertEquals(2, i.shortestPath(v12, v11).distance);
		Assert.assertEquals(2, i.shortestPath(v10, v9).distance);
		Assert.assertEquals(2, i.shortestPath(v8, v7).distance);
		Assert.assertEquals(3, i.shortestPath(v3, v6).distance);
		Assert.assertEquals(4, i.shortestPath(v3, v5).distance);
		Assert.assertEquals(0, i.shortestPath(v11, v11).distance);
	}
}
