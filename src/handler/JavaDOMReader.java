package handler;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.xml.sax.SAXException;

import model.Model;
import model.Entity;
import model.Attribute;

public class JavaDOMReader {
	
	private Model model;
	private Entity entity;
	private Attribute attribute;

	public void read(String xmlFile) throws SAXException, IOException, ParserConfigurationException {
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder;
		 builder = factory.newDocumentBuilder();
		 Document document = builder.parse(new File(xmlFile));
		 document.getDocumentElement().normalize();
		 Element root = document.getDocumentElement();
		 if (root != null) {
			 model = new Model();
			 Element EmodelNode = (Element) document.getElementsByTagName("model").item(0);
			 this.model.setName(EmodelNode.getAttribute("name"));
			 NodeList nListEntity = document.getElementsByTagName("entity");
			 for (int temp = 0; temp < nListEntity.getLength(); temp++)
			 {
			  Node nodeEntity = nListEntity.item(temp);
			  if (nodeEntity.getNodeType() == Node.ELEMENT_NODE)
			  {
				 Element eElementEntity = (Element) nodeEntity;
				 this.entity = new Entity(eElementEntity.getAttribute("name"));
				 NodeList nListAttr = document.getElementsByTagName("attribute");
				 for (int temp2 = 0; temp2 < nListAttr.getLength(); temp2++) {
					  Node nodeAttr = nListAttr.item(temp2);
					  if (nodeAttr.getNodeType() == Node.ELEMENT_NODE)
					  {
							 Element eElementAttr = (Element) nodeAttr;
							 this.attribute =  new Attribute(eElementAttr.getAttribute("name"), eElementAttr.getAttribute("type"));
					         this.entity.addAttribute(this.attribute);
					  }				    
				 }
		         this.model.addEntity(this.entity);
			  }
			}
		 }
	} 
	
	public void writeXML(String xmlOutputFilename, Model model) {
        try {
        	 
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
 
            // root element
            Element root = document.createElement("model");
            document.appendChild(root);
 
            // set an attribute to root element
            Attr attrName = document.createAttribute("name");
            attrName.setValue(model.getName());
            root.setAttributeNode(attrName);
 
            for (int i=0; i<model.getEntitiesSize(); i++) {
                // contact element
                Element entity = document.createElement("entity");
                root.appendChild(entity);
                // set attributes to contact element
                Attr attrNameEntity = document.createAttribute("name");
                attrNameEntity.setValue(model.getEntity(i).getName());
                entity.setAttributeNode(attrNameEntity);
            
                for (int j=0; j<model.getEntity(i).getAttributeSize(); j++) {
                	 Element attr = document.createElement("attribute");
                     entity.appendChild(attr);
                     // set attributes to autre element
                     Attr nameAttribute = document.createAttribute("name");
                     nameAttribute.setValue(model.getEntity(i).getAttribute(j).getName());
                     attr.setAttributeNode(nameAttribute);
                     
                     Attr typeAttribute = document.createAttribute("type");
                     typeAttribute.setValue(model.getEntity(i).getAttribute(j).getType());
                     attr.setAttributeNode(typeAttribute);
                }
               
            }
           
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMImplementation domImpl = document.getImplementation();
            DocumentType doctype = domImpl.createDocumentType("doctype",
            	    "model",
            	    "model.dtd");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());

            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlOutputFilename));
 
            transformer.transform(domSource, streamResult);
  
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
	}
	
	public Model getModel(){
	      return model;
	}
		
}
