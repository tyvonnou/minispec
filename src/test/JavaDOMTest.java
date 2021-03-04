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

	@Test
	void testXMLtoXML() throws SAXException, IOException, ParserConfigurationException {
		// READ
		JavaDOMReader dom = new JavaDOMReader();
		dom.read("dtd/Satellite.xml");
		
		Model model = dom.getModel();
        assertTrue(model.getName().equals("Projet"));
        assertTrue(model.getEntity(0).getName().equals("Satellite"));
        assertTrue(model.getEntity(0).getAttribute(0).getName().equals("name"));
        assertTrue(model.getEntity(0).getAttribute(0).getType().equals("String"));
        assertTrue(model.getEntity(0).getAttribute(1).getName().equals("id"));
        assertTrue(model.getEntity(0).getAttribute(1).getType().equals("Integer"));

        // WRITE
        String filename = "dtd/OUTPUTDOMTEST.xml";
        dom.writeXML(filename, model);
        
    	try (FileInputStream fis = new FileInputStream(filename)) {
			byte[] buf = new byte[10240];
			int size = fis.read(buf);
			String test = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n"
					+ "<!DOCTYPE model PUBLIC \"model\" \"model.dtd\">\r\n"
					+ "<model name=\"Projet\">\r\n"
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
	
	

}
