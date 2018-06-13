Small Apps Collection
=======================

Within WorldMapGenerator, add rule of generating a HashMap of <Country,Color>, then you can get a colorful world map.

#### Example:

```java

public static void addToMap() throws Exception {

		List<String> list = Files.readAllLines(Paths.get("world-map-generator/List.file"));

		for (String s : list) {
			String[] arr = s.split("\t");
			int amount = Integer.parseInt(arr[1].replaceAll(",", ""));
			String country = arr[0].trim().replaceAll((char) 160 + "", "");

			country = fixCountry(country);

			if (amount > 50000) {
				map.put(country, "#ff0000");// RED
			} else if (amount > 40000) {
				map.put(country, "#ff8000");// ORANGE
			} else if (amount > 30000) {
				map.put(country, "#ffcc00");// YELLOW
			} else if (amount > 20000) {
				map.put(country, "#33cc33");// GREEN
			} else if (amount > 10000) {
				map.put(country, "#0040ff");// BLUE
			} else if (amount > 5000) {
				map.put(country, "#8000ff");// PURPLE
			} else {
				map.put(country, "#000000");// BLACK
			}
		}
	}

  
 ```

List Of Countries GDP Per Capital

[List.file](world-map-generator/List.file)

Qatar	124,927 
Macau	114,430 
Luxembourg	109,192 
Singapore	90,531 
Brunei	76,743 
Ireland	72,632 
Norway	70,590 
...

![alt text](world-map-generator/WorldMap.svg)

