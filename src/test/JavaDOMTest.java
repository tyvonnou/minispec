package test;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import handler.JavaDOMReader;
import model.Model;

class JavaDOMTest {
	
	/*
	 * Find the differences between two string
	 */
	public static List<String> findNotMatching(String sourceStr, String anotherStr){
	    StringTokenizer at = new StringTokenizer(sourceStr, " ");
	    StringTokenizer bt = null;
	    int i = 0, token_count = 0;
	    String token = null;
	    boolean flag = false;
	    List<String> missingWords = new ArrayList<String>();
	    while (at.hasMoreTokens()) {
	        token = at.nextToken();
	        bt = new StringTokenizer(anotherStr, " ");
	        token_count = bt.countTokens();
	        while (i < token_count) {
	            String s = bt.nextToken();
	            if (token.equals(s)) {
	                flag = true;
	                break;
	            } else {
	                flag = false;
	            }
	            i++;
	        }
	        i = 0;
	        if (flag == false)
	            missingWords.add(token);
	    }
	    return missingWords;
	}

	/*
	 * Transform XML file to objects and export the objects in XML file
	 */
	@Test
	void testXMLtoXML() throws SAXException, IOException, ParserConfigurationException {
		// READ
		JavaDOMReader dom = new JavaDOMReader();
		dom.read("dtd/Satellite.xml");
		
		Model model = dom.getModel();
        assertTrue(model.getName().equals("projet"));
        assertTrue(model.getEntity(0).getName().equals("Satellite"));
        assertTrue(model.getEntity(0).getAttribute(0).getName().equals("name"));
        assertTrue(model.getEntity(0).getAttribute(0).getType().equals("String"));
        assertTrue(model.getEntity(0).getAttribute(1).getName().equals("id"));
        assertTrue(model.getEntity(0).getAttribute(1).getType().equals("Integer"));

        // WRITE
        String filename = "dtd/OUTPUTDOMTEST.xml";
        dom.writeXML(filename, model);
        dom.writeJAVA(model);
    	try (FileInputStream fis = new FileInputStream(filename)) {
			byte[] buf = new byte[10240];
			int size = fis.read(buf);
			String test = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n"
					+ "<!DOCTYPE model PUBLIC \"model\" \"model.dtd\">\r\n"
					+ "<model name=\"projet\">\r\n"
					+ "    <entity name=\"Satellite\">\r\n"
					+ "        <attribute name=\"name\" type=\"String\"/>\r\n"
					+ "        <attribute name=\"id\" type=\"Integer\"/>\r\n"
					+ "    </entity>\r\n"
					+ "</model>\r\n";
			String contents = new String(buf,0,size);
			// If you have some trouble with the test,
			// you can compare the test string and the output string thanks to the findNotMatching function
			//List<String> diff = findNotMatching(contents, test);
			//System.out.println(diff.get(0));
			assertTrue(contents.equals(test));
		} catch (FileNotFoundException e) {
			fail("");
		} catch (IOException e) {
			fail("");
		}
	}
	
	/*
	 * Transform XML file to object and export objects in java sources files
	 */
	@Test
	void testXMLtoJava() throws SAXException, IOException, ParserConfigurationException {
		// READ
		JavaDOMReader dom = new JavaDOMReader();
		dom.read("dtd/Satellite.xml");
		
		Model model = dom.getModel();
        assertTrue(model.getName().equals("projet"));
        assertTrue(model.getEntity(0).getName().equals("Satellite"));
        assertTrue(model.getEntity(0).getAttribute(0).getName().equals("name"));
        assertTrue(model.getEntity(0).getAttribute(0).getType().equals("String"));
        assertTrue(model.getEntity(0).getAttribute(1).getName().equals("id"));
        assertTrue(model.getEntity(0).getAttribute(1).getType().equals("Integer"));

        // WRITE
        String filename = "src/generated/projet/Satellite.java";
        dom.writeJAVA(model);
    	try (FileInputStream fis = new FileInputStream(filename)) {
			byte[] buf = new byte[10240];
			int size = fis.read(buf);
			String test = "package generated.projet;\r\n\r\n"
					+ "public class Satellite {\r\n\r\n"
					// Attributes
					+ "\tString name;\r\n"
					+ "\tInteger id;\r\n\r\n"
					// Constructor
					+ "\tpublic Satellite() {}\r\n\r\n"
					// Getters and Setters
					+ "\tpublic String getName() {\r\n\t\treturn this.name;\r\n\t}\r\n\r\n"
					+ "\tpublic void setName(String name) {\r\n\t\tthis.name = name;\r\n\t}\r\n\r\n"
					+ "\tpublic Integer getId() {\r\n\t\treturn this.id;\r\n\t}\r\n\r\n"
					+ "\tpublic void setId(Integer id) {\r\n\t\tthis.id = id;\r\n\t}\r\n"
					+ "}";
			String contents = new String(buf,0,size);
//			If you have some trouble with the test,
//			you can compare the test string and the output string thanks to the findNotMatching function
//			List<String> diff = findNotMatching(contents, test);
//			System.out.println(diff.get(0));
			assertTrue(contents.equals(test));
		} catch (FileNotFoundException e) {
			fail("");
		} catch (IOException e) {
			fail("");
		}
	}
	
	/*
	 * Transform XML file to object and export objects in java sources files using visitor model
	 */
	@Test
	void testXMLtoJavaVisitorSatelliteFile() throws SAXException, IOException, ParserConfigurationException {
		// READ
		JavaDOMReader dom = new JavaDOMReader();
		dom.read("dtd/SatelliteVisitor.xml");
		
		Model model = dom.getModel();
        assertTrue(model.getName().equals("projet"));
        assertTrue(model.getEntity(0).getName().equals("Satellite"));
        assertTrue(model.getEntity(0).getAttribute(0).getName().equals("name"));
        assertTrue(model.getEntity(0).getAttribute(0).getType().equals("String"));
        assertTrue(model.getEntity(0).getAttribute(1).getName().equals("id"));
        assertTrue(model.getEntity(0).getAttribute(1).getType().equals("Integer"));

        // WRITE
        String filename = "src/generatedVisitor/projet/Satellite.java";
        dom.writeVisitorJAVA(model);
    	try (FileInputStream fis = new FileInputStream(filename)) {
			byte[] buf = new byte[10240];
			int size = fis.read(buf);
			String test = "package generatedVisitor.projet;\r\n"
					+ "\r\n"
					+ "import java.util.Set;\r\n"
					+ "import java.util.List;\r\n"
					+ "\r\n"
					+ "public class Satellite {\r\n"
					+ "\r\n"
					+ "	String name;\r\n"
					+ "	Integer id;\r\n"
					+ "	Set<PanneauSolaire> PanneauxSolaires;\r\n"
					+ "	List<Flotte> Balises;\r\n"
					+ "\r\n"
					+ "	public Satellite() {}\r\n"
					+ "\r\n"
					+ "	public String getName() {\r\n"
					+ "		return this.name;\r\n"
					+ "	}\r\n"
					+ "\r\n"
					+ "	public void setName(String name) {\r\n"
					+ "		this.name = name;\r\n"
					+ "	}\r\n"
					+ "\r\n"
					+ "	public Integer getId() {\r\n"
					+ "		return this.id;\r\n"
					+ "	}\r\n"
					+ "\r\n"
					+ "	public void setId(Integer id) {\r\n"
					+ "		this.id = id;\r\n"
					+ "	}\r\n"
					+ "\r\n"
					+ "	public Set<PanneauSolaire> getPanneauxSolaires() {\r\n"
					+ "		return this.PanneauxSolaires;\r\n"
					+ "	}\r\n"
					+ "\r\n"
					+ "	public void addPanneauxSolaires(PanneauSolaire PanneauxSolaires) {\r\n"
					+ "		this.PanneauxSolaires.add(PanneauxSolaires);\r\n"
					+ "	}\r\n"
					+ "\r\n"
					+ "	public List<Flotte> getBalises() {\r\n"
					+ "		return this.Balises;\r\n"
					+ "	}\r\n"
					+ "\r\n"
					+ "	public void addBalises(Flotte Balises) {\r\n"
					+ "		this.Balises.add(Balises);\r\n"
					+ "	}\r\n"
					+ "}";
			String contents = new String(buf,0,size);
//			If you have some trouble with the test,
//			you can compare the test string and the output string thanks to the findNotMatching function
//			List<String> diff = findNotMatching(contents, test);
//			System.out.println(diff.get(0));
			assertTrue(contents.equals(test));
		} catch (FileNotFoundException e) {
			fail("");
		} catch (IOException e) {
			fail("");
		}
	}
	
	/*
	 * Transform XML file to object and export objects in java sources files using visitor model
	 */
	@Test
	void testXMLtoJavaVisitorFlotteFile() throws SAXException, IOException, ParserConfigurationException {
		// READ
		JavaDOMReader dom = new JavaDOMReader();
		dom.read("dtd/SatelliteVisitor.xml");
		
		Model model = dom.getModel();
        assertTrue(model.getName().equals("projet"));
        assertTrue(model.getEntity(0).getName().equals("Satellite"));
        assertTrue(model.getEntity(0).getAttribute(0).getName().equals("name"));
        assertTrue(model.getEntity(0).getAttribute(0).getType().equals("String"));
        assertTrue(model.getEntity(0).getAttribute(1).getName().equals("id"));
        assertTrue(model.getEntity(0).getAttribute(1).getType().equals("Integer"));

        // WRITE
        String filename = "src/generatedVisitor/projet/Flotte.java";
        dom.writeVisitorJAVA(model);
    	try (FileInputStream fis = new FileInputStream(filename)) {
			byte[] buf = new byte[10240];
			int size = fis.read(buf);
			String test = "package generatedVisitor.projet;\r\n"
					+ "\r\n"
					+ "public class Flotte {\r\n"
					+ "\r\n"
					+ "	public Flotte() {}\r\n"
					+ "}";
			String contents = new String(buf,0,size);
//			If you have some trouble with the test,
//			you can compare the test string and the output string thanks to the findNotMatching function
//			List<String> diff = findNotMatching(contents, test);
//			System.out.println(diff.get(0));
			assertTrue(contents.equals(test));
		} catch (FileNotFoundException e) {
			fail("");
		} catch (IOException e) {
			fail("");
		}
	}
	
	/*
	 * Transform XML file to object and export objects in java sources files using visitor model
	 */
	@Test
	void testXMLtoJavaVisitorPanneauSolaireFile() throws SAXException, IOException, ParserConfigurationException {
		// READ
		JavaDOMReader dom = new JavaDOMReader();
		dom.read("dtd/SatelliteVisitor.xml");
		
		Model model = dom.getModel();
        assertTrue(model.getName().equals("projet"));
        assertTrue(model.getEntity(0).getName().equals("Satellite"));
        assertTrue(model.getEntity(0).getAttribute(0).getName().equals("name"));
        assertTrue(model.getEntity(0).getAttribute(0).getType().equals("String"));
        assertTrue(model.getEntity(0).getAttribute(1).getName().equals("id"));
        assertTrue(model.getEntity(0).getAttribute(1).getType().equals("Integer"));

        // WRITE
        String filename = "src/generatedVisitor/projet/PanneauSolaire.java";
        dom.writeVisitorJAVA(model);
    	try (FileInputStream fis = new FileInputStream(filename)) {
			byte[] buf = new byte[10240];
			int size = fis.read(buf);
			String test = "package generatedVisitor.projet;\r\n"
					+ "\r\n"
					+ "public class PanneauSolaire {\r\n"
					+ "\r\n"
					+ "	public PanneauSolaire() {}\r\n"
					+ "}";
			String contents = new String(buf,0,size);
//			If you have some trouble with the test,
//			you can compare the test string and the output string thanks to the findNotMatching function
//			List<String> diff = findNotMatching(contents, test);
//			System.out.println(diff.get(0));
			assertTrue(contents.equals(test));
		} catch (FileNotFoundException e) {
			fail("");
		} catch (IOException e) {
			fail("");
		}
	}
}
