package visitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import model.Attribute;
import model.Collection;
import model.Entity;
import model.Model;

import utils.Utils;

public class Visitor implements IVisitor
{
	protected Model model;  
	protected StringBuilder javaSource, importsBuilder;
	protected String generatedPath;
	protected ArrayList<String> imports;

	public Visitor()
	{
	   this.javaSource = new StringBuilder();
	   this.importsBuilder = new StringBuilder();
	   this.imports = new ArrayList<>();
	}
	

	@Override
	public void visitModel(Model model) {
		// Generate package 
		this.generatedPath = "src/generatedVisitor/" + model.getName() + "/";
		File generatedPackagePath = new File(this.generatedPath);
		if (!generatedPackagePath.exists()){
			generatedPackagePath.mkdirs();
		} else {
			generatedPackagePath.delete();
			generatedPackagePath.mkdirs();
		}
		this.model = model;
		// Generate all entities
		for (int i=0; i<model.getEntitiesSize(); i++){
			model.getEntity(i).accept(this);
		}		
	}

	@Override
	public void visitEntity(Entity entity) {
		// Create the source code package line
		this.importsBuilder.append("package generatedVisitor." + this.model.getName() + ";\r\n\r\n");
		String filePath = this.generatedPath + entity.getName() + ".java";
		// Create the java file 
		File file = new File(filePath);
		if(file.exists()) file.delete();
		try {
			if(file.createNewFile()) {
				// Generate first class line 
				this.javaSource.append("public class " 
						+ entity.getName() 
						+ " {\r\n\r\n");
				// Attributs 
				for (int i=0; i<entity.getAttributeSize(); i++) {
					entity.getAttribute(i).accept(this);
					if (i == entity.getAttributeSize()-1 && entity.getAttributeSize() == 0) {
						this.javaSource.append("\r\n");
					}
				}
				// Collections
				for (int i=0; i<entity.getCollectionSize(); i++) {
					entity.getCollection(i).accept(this);
					if (i == entity.getCollectionSize()-1) {
						this.javaSource.append("\r\n");
					}
				}
				// Generate constructor
				this.javaSource.append("\tpublic "
						+ entity.getName()
						+ "() {}\r\n");
				// Generate Getters and Setters
				generateGettersSetters(entity);
				// Generate Collection methods 
				generateCollectionMethods(entity);
				this.javaSource.append("}");
				// Add the imports
				for(int i=0; i<this.imports.size(); i++) {
					if(i == this.imports.size()-1) {
						this.importsBuilder.append("import " + this.imports.get(i) + ";\r\n\r\n");
					} else {
						this.importsBuilder.append("import " + this.imports.get(i) + ";\r\n");
					}
				}
				this.javaSource.insert(0, this.importsBuilder);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// Write class file
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			fos.write(this.javaSource.toString().getBytes());
			fos.close();
			cleanAll();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void cleanAll() {
		this.javaSource.delete(0, this.javaSource.length());
		this.importsBuilder.delete(0, this.importsBuilder.length());
		this.imports.clear();
	}


	@Override
	public void visitAttribute(Attribute attribute) {
		this.javaSource.append( "\t" 
				+ attribute.getType() 
				+ " " 
				+ attribute.getName() 
				+";\r\n");		
	}
	
	/*
	 * Generate Setter and Getters in source file
	 */
	private void generateGettersSetters(Entity entity) {
		for (int i=0; i<entity.getAttributeSize(); i++) {
			this.javaSource.append( "\r\n\tpublic "
					+ entity.getAttribute(i).getType()
					+ " get"
					+ Utils.capitalize(entity.getAttribute(i).getName()) 
					+ "() {\r\n\t\treturn this."
					+ entity.getAttribute(i).getName()
					+ ";\r\n\t}\r\n\r\n\tpublic void set"
					+ Utils.capitalize(entity.getAttribute(i).getName()) 
					+ "("
					+ entity.getAttribute(i).getType()
					+ " "
					+ entity.getAttribute(i).getName()
					+ ") {\r\n\t\tthis."
					+ entity.getAttribute(i).getName()
					+ " = "
					+ entity.getAttribute(i).getName()
					+ ";\r\n\t}\r\n");		
		}
	}
	
	/*
	 * Generate Collections methods in source file
	 */
	private void generateCollectionMethods(Entity entity) {
		for (int i=0; i<entity.getCollectionSize(); i++) {
			this.javaSource.append( "\r\n\tpublic "
					+ entity.getCollection(i).getTypeCollection()
					+ "<"
					+ Utils.capitalize(entity.getCollection(i).getType()) 
					+ "> get" 
					+ Utils.capitalize(entity.getCollection(i).getName())
					+ "() {\r\n\t\treturn this."
					+ entity.getCollection(i).getName()
					+ ";\r\n\t}\r\n\r\n\tpublic void add"
					+ Utils.capitalize(entity.getCollection(i).getName()) 
					+ "("
					+ entity.getCollection(i).getType()
					+ " "
					+ entity.getCollection(i).getName()
					+ ") {\r\n\t\tthis."
					+ entity.getCollection(i).getName()
					+ ".add("
					+ entity.getCollection(i).getName()
					+ ");\r\n\t}\r\n");		
		}
	}


	@Override
	public void visitCollection(Collection collection) {
			this.javaSource.append( "\t"
					+ collection.getTypeCollection()
					+ "<" 
					+ collection.getType() 
					+ "> " 
					+ collection.getName() 
					+";\r\n");	
			this.imports.add("java.util." + collection.getTypeCollection());
	}
}