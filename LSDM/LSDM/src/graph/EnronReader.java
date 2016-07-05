package graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnronReader {
	private static final String commentChar = "#";
	private static final String pattern = "(\\d+)(\\s+)(\\d+)";
	
	public static Graph constructGraph(File f)
	{
		Graph g = new Graph();
		
		try (BufferedReader br = new BufferedReader(
				new FileReader(f))) {
		    String line;
		    Pattern p = Pattern.compile(pattern);
		    while ((line = br.readLine()) != null) {
		       if (line.startsWith(commentChar))
		       {
		    	   continue;
		       } else {
		    	   
		    	   Matcher m = p.matcher(line);
		    	   if (m.find()) {
		    		   int from = (Integer.valueOf(m.group(1)));
		    		   int to = (Integer.valueOf(m.group(3)));
		    		   
		    		   g.addEdge(from, to);
		    	   }
		    	   
		       }
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return g;
	}
}
