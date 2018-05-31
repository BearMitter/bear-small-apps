package site.notfound;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WorldMapGenerator {
	
	public static void  addToMap() throws Exception {

		List<String> list = Files.readAllLines(Paths.get("world-map-generator/List.file"));

		for (String s : list) {
			String[] arr = s.split("\t");
			int gdp = Integer.parseInt(arr[1].replaceAll(",", ""));
			String country = arr[0].trim();

			if (country.equals("USA") || country.contains("United States"))
				country = "United States of America";

			else if (country.equals("UK"))
				country = "United Kingdom";

			else if (country.contains("Czech"))
				country = "Czechia";

			else if (country.contains("Congo") && !country.contains("Dem"))
				country = "Republic of the Congo";

			else if (country.contains("Congo") && country.contains("Dem"))
				country = "Democratic Republic of the Congo";

			if (gdp > 50000) {
				map.put(country, "#ff0000");// RED
			} else if (gdp > 40000) {
				map.put(country, "#ff8000");// ORANGE
			} else if (gdp > 30000) {
				map.put(country, "#ffcc00");// YELLOW
			} else if (gdp > 20000) {
				map.put(country, "#33cc33");// GREEN
			} else if (gdp > 10000) {
				map.put(country, "#0040ff");// BLUE
			} else if (gdp > 5000) {
				map.put(country, "#8000ff");// PURPLE
			} else {
				map.put(country, "#000000");// BLACK
			}
		}
	}

	static HashMap<String, String> map = new HashMap<>();
	static HashSet<String> usedSet = new HashSet<>();
	
	public static void main(String argv[]) throws Exception {

		String style = "fill:color;fill-rule:evenodd;stroke:#ffffff;stroke-width:0.30000001";
		
		addToMap();

		File fXmlFile = new File("world-map-generator/BlankMap-World-Sovereign_Nations.svg");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("g");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			Element eElement = (Element) nNode;

			NodeList titleList = eElement.getElementsByTagName("title");

			if (titleList.item(0) != null) {

				String color = "#e0e0e0";
				String s = titleList.item(0).getTextContent().trim();
				if (map.containsKey(s)) {
					color = map.get(s);
					usedSet.add(s);
				}

				eElement.setAttribute("style", style.replace("color", color));

				NodeList pathList = eElement.getElementsByTagName("path");
				for (int i = 0; i < pathList.getLength(); i++) {

					Element subElement = (Element) pathList.item(i);

					if (subElement != null && subElement.hasAttribute("style")) {
						subElement.setAttribute("style", style.replace("color", color));

					}

				}

				NodeList gList = eElement.getElementsByTagName("g");
				
				for (int i = 0; i < gList.getLength(); i++) {

					Element subElement = (Element) gList.item(i);

					if (subElement != null && subElement.hasAttribute("style")) {
						subElement.setAttribute("style", style.replace("color", color));
					}
				}

				NodeList circleList = eElement.getElementsByTagName("circle");

				for (int i = 0; i < circleList.getLength(); i++) {

					Element subElement = (Element) circleList.item(i);

					if (subElement != null && subElement.hasAttribute("style")) {
						subElement.setAttribute("style", style.replace("color", color));
					}
				}

			}
		}

		NodeList pList = doc.getElementsByTagName("path");

		for (int temp = 0; temp < pList.getLength(); temp++) {

			Node nNode = pList.item(temp);

			Element eElement = (Element) nNode;

			NodeList titleList = eElement.getElementsByTagName("title");

			if (titleList.item(0) != null) {

				String color = "#e0e0e0";
				String s = titleList.item(0).getTextContent().trim();
				if (map.containsKey(s)) {
					color = map.get(s);
					usedSet.add(s);
				}

				eElement.setAttribute("style", style.replace("color", color));

			}
		}

		Set<String> set = map.keySet();
		set.removeAll(usedSet);

		System.out.println("Unfound Countries:");
		for (String s : set) {
			System.out.println(s);
		}

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("world-map-generator/WorldMap.svg"));
		transformer.transform(source, result);

	}

}
