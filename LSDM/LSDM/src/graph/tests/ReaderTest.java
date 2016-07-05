package graph.tests;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map.Entry;

import graph.Edge;
import graph.EnronReader;
import graph.Graph;
import graph.Vertex;

import org.junit.Assert;
import org.junit.Test;

public class ReaderTest {

	@Test
	public void testClusterGraph() {
		File f;
		try {
			f = new File(ClassLoader.getSystemResource("clusterGraph").toURI());
			Graph g = EnronReader.constructGraph(f);
			// vertex list size
			Assert.assertEquals(11, g.vertices.size());
			// inSize == outSize
			for (int i = 1; i <= g.vertices.size(); i++)
			{
				Assert.assertEquals(g.vertices.get(i).in.size(), g.vertices.get(i).out.size());
			}
			// from is correct and
			// there is a matchingIn edge for an out edge
			for (int i = 1; i <= g.vertices.size(); i++)
			{
				for (Entry<Integer, Edge> e :
					g.vertices.get(i).out.entrySet())
				{
					Assert.assertEquals("Not for vertex " + i, i, e.getValue().from.id);
					boolean matchingIn = false;
					for (Entry<Integer, Edge> e2 :
						g.vertices.get(e.getValue().to.id).in.entrySet())
					{
						if (e2.getValue().from.id == i) matchingIn = true;
					}
					Assert.assertTrue(matchingIn);
					
					// in and out are referencing same edge object
					Assert.assertTrue(e.getValue() == 
							g.vertices.get(e.getValue().to.id).
								in.get(e.getValue().from.id));
				}
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDeepCopy() {
		File f;
		try {
			f = new File(ClassLoader.getSystemResource("clusterGraph1").toURI());
			Graph g = EnronReader.constructGraph(f);
			Graph copy = g.deepCopy();
			
			Assert.assertEquals(g.vertices.size(), copy.vertices.size());
			for (Entry<Integer, Vertex> v1Entry : g.vertices.entrySet())
			{
				boolean hasPartner = false;
				Vertex v1 = v1Entry.getValue();
				Assert.assertEquals(v1.id, copy.vertices.get(v1.id).id);
				for (Entry<Integer, Vertex> v2Entry : copy.vertices.entrySet())
				{
					Vertex v2 = v2Entry.getValue();
					Assert.assertTrue(v1 != v2);
					
					// check if all edges exist
					if (v1.id == v2.id)
					{
						hasPartner = true;
						// outgoing edges
						Assert.assertEquals(v1.out.size(), v2.out.size());
						for (Entry<Integer, Edge> e1Entry : v1.out.entrySet())
						{
							Edge e1 = e1Entry.getValue();
							Assert.assertEquals(e1.to.id, v2.out.get(e1.to.id).to.id);
							Assert.assertEquals(e1.from.id, v2.out.get(e1.to.id).from.id);
						}
						
						// incoming edges
						Assert.assertEquals(v1.in.size(), v2.in.size());
						for (Entry<Integer, Edge> e1Entry : v1.in.entrySet())
						{
							Edge e1 = e1Entry.getValue();
							Assert.assertEquals(e1.from.id, v2.in.get(e1.from.id).from.id);
							Assert.assertEquals(e1.to.id, v2.in.get(e1.from.id).to.id);
						}
					}
					
					// check that edge objects are not equal
					for (Entry<Integer, Edge> e1Entry : v1.out.entrySet())
					{
						for (Entry<Integer, Edge> e2Entry : v2.out.entrySet())
						{
							Assert.assertTrue(e1Entry.getValue() != e2Entry.getValue());
						}
						for (Entry<Integer, Edge> e2Entry : v2.in.entrySet())
						{
							Assert.assertTrue(e1Entry.getValue() != e2Entry.getValue());
						}
					}
					for (Entry<Integer, Edge> e1Entry : v1.in.entrySet())
					{
						for (Entry<Integer, Edge> e2Entry : v2.out.entrySet())
						{
							Assert.assertTrue(e1Entry.getValue() != e2Entry.getValue());
						}
						for (Entry<Integer, Edge> e2Entry : v2.in.entrySet())
						{
							Assert.assertTrue(e1Entry.getValue() != e2Entry.getValue());
						}
					}
				}
				Assert.assertTrue(hasPartner);
				
			}
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
