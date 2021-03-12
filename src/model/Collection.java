package model;

import visitor.IVisitable;
import visitor.IVisitor;

public class Collection implements IVisitable {
	
	String name;
	String type;
	String typeCollection;
	
	public Collection(String name, String type, String typeCollection) {
		this.name = name;
		this.type = type;
		this.typeCollection = typeCollection;
	}

	public void accept(IVisitor visitor) {
		  visitor.visitCollection(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeCollection() {
		return typeCollection;
	}

	public void setTypeCollection(String typeCollection) {
		this.typeCollection = typeCollection;
	}
}
