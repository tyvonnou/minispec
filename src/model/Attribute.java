package model;

import visitor.IVisitable;
import visitor.IVisitor;

public class Attribute implements IVisitable {
	
	String name;
	String type;
	
	public Attribute(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	 public void accept(IVisitor visitor) {
		  visitor.visitAttribute(this);
	 }
}
