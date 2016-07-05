package triangles.test;

import triangles.TriangleCounting;
import triangles.TriangleCounting.DistinctVertexSets;

import java.io.File;
import java.net.URISyntaxException;

import graph.EnronReader;
import graph.Graph;
import graph.Vertex;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TriangleTest {
	private static final double DELTA = 0.0001d;
	
	Graph g;
	TriangleCounting t;
	
	@Before
	public void init() {
		try {
			this.g = EnronReader.constructGraph(new File(
					ClassLoader.getSystemResource("triangleGraph1").toURI()));
			this.t = new TriangleCounting();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@Test
	public void testHHfinding() {
		DistinctVertexSets sets = this.t.findHeavyHitters(g);
		Assert.assertTrue(sets.nonHeavyHitters.size() == 4);
		Assert.assertTrue(sets.heavyHitters.size() == 2);
		Assert.assertTrue(sets.heavyHitters.contains(new Vertex(3)));
		Assert.assertTrue(sets.heavyHitters.contains(new Vertex(6)));
		Assert.assertTrue(sets.nonHeavyHitters.contains(new Vertex(1)));
		Assert.assertTrue(sets.nonHeavyHitters.contains(new Vertex(2)));
		Assert.assertTrue(sets.nonHeavyHitters.contains(new Vertex(4)));
		Assert.assertTrue(sets.nonHeavyHitters.contains(new Vertex(5)));
	}
	
	@Test
	public void testHHTriangleCounting() {
		DistinctVertexSets sets = this.t.findHeavyHitters(g);
		int tris = this.t.countHeavyHitterTriangles(sets.heavyHitters);
		Assert.assertEquals(0, tris);
		Assert.assertEquals(0, this.g.vertices.get(3).triangles);
		Assert.assertEquals(0, this.g.vertices.get(6).triangles);
	}
	
	@Test
	public void testNonHHTriangleCounting() {
		DistinctVertexSets sets = this.t.findHeavyHitters(g);
		int tris = this.t.countNonHHTriangles(sets.nonHeavyHitters, this.g);
		Assert.assertEquals(5, tris);
		Assert.assertEquals(2, this.g.vertices.get(1).triangles);
		Assert.assertEquals(1, this.g.vertices.get(2).triangles);
		Assert.assertEquals(5, this.g.vertices.get(3).triangles);
		Assert.assertEquals(2, this.g.vertices.get(4).triangles);
		Assert.assertEquals(2, this.g.vertices.get(5).triangles);
		Assert.assertEquals(3, this.g.vertices.get(6).triangles);
	}
	
	@Test
	public void testCoefficient() {
		int tris = this.t.countTriangles(this.g);
		Assert.assertEquals(5, tris);
		Assert.assertEquals(2.0/3.0, this.g.vertices.get(1).getClusterCoefficient(), DELTA);
		Assert.assertEquals(1.0/1.0, this.g.vertices.get(2).getClusterCoefficient(), DELTA);
		Assert.assertEquals(5.0/10.0, this.g.vertices.get(3).getClusterCoefficient(), DELTA);
		Assert.assertEquals(2.0/3.0, this.g.vertices.get(4).getClusterCoefficient(), DELTA);
		Assert.assertEquals(2.0/3.0, this.g.vertices.get(5).getClusterCoefficient(), DELTA);
		Assert.assertEquals(3.0/6.0, this.g.vertices.get(6).getClusterCoefficient(), DELTA);
	}
}
