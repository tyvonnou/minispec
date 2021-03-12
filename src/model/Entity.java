package model;

import java.util.ArrayList;
import java.util.List;

import visitor.IVisitable;
import visitor.IVisitor;

public class Entity implements IVisitable {
	
	String name ;
	List<Attribute> attributes ;
	
	public Entity(String name) {
		this.name = name;
		this.attributes = new ArrayList<>();
	}

	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}

	public String getName() {
		return this.name;
	}

	public int getAttributeSize() {
		return this.attributes.size();
	}

	public Attribute getAttribute(int index) {
		return this.attributes.get(index);
	}
	
	public void accept(IVisitor visitor) {
		visitor.visitEntity(this);
	}
}
