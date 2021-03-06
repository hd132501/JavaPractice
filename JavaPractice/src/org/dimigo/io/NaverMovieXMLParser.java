package org.dimigo.io;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class NaverMovieXMLParser {

	public static List<Movie> parse(String xmlString) {
		List<Movie> movieList = null;
		
		try {
			if(xmlString == null || xmlString.length() == 0)
				throw new Exception("No input data");
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new ByteArrayInputStream(xmlString.getBytes()));
			NodeList list = document.getElementsByTagName("item");
			
			if(list.getLength() == 0) {
				throw new Exception("No item data");
			}
			int cnt = list.getLength();
			movieList = new ArrayList<Movie>();
			Movie movie;
			NodeList tagList;
			
			for(int i=0; i<cnt; i++) {
				movie = new Movie();
				tagList = list.item(i).getChildNodes();
				String title = tagList.item(0).getTextContent();
				movie.setTitle(title.replaceAll("\\<.*?>",""));
				movie.setLink(tagList.item(1).getTextContent());
				movie.setImage(tagList.item(2).getTextContent());
				movie.setSubtitle(tagList.item(3).getTextContent());
				movie.setPubDate(tagList.item(4).getTextContent());
				movie.setDirector(tagList.item(5).getTextContent());
				String actors = tagList.item(6).getTextContent();
				
				String[] actorArr = actors.split("\\|");
				List<String> actorList = new ArrayList<String>(actorArr.length);
				
				for(int j=0; j<actorArr.length; j++) {
					actorList.add(actorArr[j]);
				}	
				movie.setActors(actorList);
				movie.setUserRating(tagList.item(7).getTextContent());
				
				movieList.add(movie);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return movieList;
	}
}