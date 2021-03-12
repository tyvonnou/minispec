package handler;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileOutputStream;
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
import visitor.IVisitor;
import visitor.Visitor;
import model.Entity;
import model.Attribute;

public class JavaDOMReader {
	
	private Model model;
	private Entity entity;
	private Attribute attribute;
	
	/*
	 * Add a capital letter at the beginning of the string.
	 */
	public static String capitalize(String str) {
	    if(str == null || str.isEmpty()) {
	        return str;
	    }
	    return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/*
	 * Xml file to object 
	 */
	public void read(String xmlFile) throws SAXException, IOException, ParserConfigurationException {
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder;
		 builder = factory.newDocumentBuilder();
		 Document document = builder.parse(new File(xmlFile));
		 document.getDocumentElement().normalize();
		 Element root = document.getDocumentElement();
		 if (root != null) {
			 // Init model
			 model = new Model();
			 // Get the model element
			 Element EmodelNode = (Element) document.getElementsByTagName("model").item(0);
			 this.model.setName(EmodelNode.getAttribute("name"));
			 // Get the list of entities 
			 NodeList nListEntity = document.getElementsByTagName("entity");
			 // For each entity
			 for (int temp = 0; temp < nListEntity.getLength(); temp++)
			 {
			  Node nodeEntity = nListEntity.item(temp);
			  if (nodeEntity.getNodeType() == Node.ELEMENT_NODE)
			  {
				 // Get the entity
				 Element eElementEntity = (Element) nodeEntity;
				 this.entity = new Entity(eElementEntity.getAttribute("name"));
				 // Get the list of attributes 
				 NodeList nListAttr = eElementEntity.getElementsByTagName("attribute");
				 // For each attribute
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
	
	/*
	 * Transform a model object into an XML file.
	 */
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

	/*
	 * Generate model object into java source files.
	 */
	public void writeJAVA(Model model) {
		String generatedPath = "src/generated/" + model.getName() + "/";
		try {
			// Create the pakage
			File generatedPackagePath = new File(generatedPath);
			if (!generatedPackagePath.exists()){
				generatedPackagePath.mkdirs();
			} else {
				generatedPackagePath.delete();
				generatedPackagePath.mkdirs();
			}
			// Foreach entities
			for(int i=0; i<model.getEntitiesSize(); i++) {
				String filePath = generatedPath += model.getEntity(i).getName() + ".java";
				// Create the java file 
				File file = new File(filePath);
				if(file.exists()) file.delete();
				if(file.createNewFile()) {
					// Create the file content
					// Adding title
					StringBuilder fileContent = new StringBuilder("package generated."
												+ model.getName()
												+";\r\n\r\npublic class " 
												+ model.getEntity(i).getName() 
												+ " {\r\n\r\n");
					// Adding attributes
					for(int j=0; j<model.getEntity(i).getAttributeSize(); j++) {
						fileContent.append( "\t" 
									+ model.getEntity(i).getAttribute(j).getType() 
									+ " " 
									+ model.getEntity(i).getAttribute(j).getName() 
									+";\r\n");
					}
					// Adding constructor
					fileContent.append("\r\n\tpublic "
								+ model.getEntity(i).getName()
								+ "() {}\r\n");
					// Adding setters and getters 
					for(int j=0; j<model.getEntity(i).getAttributeSize(); j++) {
						fileContent.append( "\r\n\tpublic "
								+ model.getEntity(i).getAttribute(j).getType()
								+ " get"
								+ capitalize(model.getEntity(i).getAttribute(j).getName()) 
								+ "() {\r\n\t\treturn this."
								+ model.getEntity(i).getAttribute(j).getName()
								+ ";\r\n\t}\r\n\r\n\tpublic void set"
								+ capitalize(model.getEntity(i).getAttribute(j).getName()) 
								+ "("
								+ model.getEntity(i).getAttribute(j).getType()
								+ " "
								+ model.getEntity(i).getAttribute(j).getName()
								+ ") {\r\n\t\tthis."
								+ model.getEntity(i).getAttribute(j).getName()
								+ " = "
								+ model.getEntity(i).getAttribute(j).getName()
								+ ";\r\n\t}\r\n");			
					}
					// Write the class file 
					fileContent.append("}");
					FileOutputStream fos = null;
					fos = new FileOutputStream(filePath);
					fos.write(fileContent.toString().getBytes());
					fos.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Generate model object into java source files using visitor.
	 */
	public void writeVisitorJAVA(Model model) {
	    IVisitor visitor = new Visitor();
	    model.accept(visitor);
	}
	
	/*
	 * Return the model
	 */
	public Model getModel(){
	      return model;
	}
		
}