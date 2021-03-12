package visitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import model.Attribute;
import model.Entity;
import model.Model;

import utils.Utils;

public class Visitor implements IVisitor
{
	protected Model model;  
	protected StringBuilder javaSource;
	protected String generatedPath;

	public Visitor()
	{
	   this.javaSource = new StringBuilder();
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
		this.javaSource.append("package generatedVisitor." + this.model.getName() + ";\r\n\r\n");
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
				
				for (int i=0; i<entity.getAttributeSize(); i++) {
					entity.getAttribute(i).accept(this);
					if (i == entity.getAttributeSize()-1) {
						this.javaSource.append("\r\n");
					}
				}
				// Generate constructor
				this.javaSource.append("\tpublic "
						+ entity.getName()
						+ "() {}\r\n");
				// Generate Getters and Setters
				generateGettersSetters(entity);
				this.javaSource.append("}");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Write class file
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			fos.write(this.javaSource.toString().getBytes());
			fos.close();
			this.javaSource.delete(0, this.javaSource.length());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}