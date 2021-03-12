package model;

import java.util.ArrayList;
import java.util.List;

import visitor.IVisitable;
import visitor.IVisitor;

public class Entity implements IVisitable {
	
	String name ;
	List<Attribute> attributes ;
	List<Collection> collections ;

	public Entity(String name) {
		this.name = name;
		this.attributes = new ArrayList<>();
		this.collections = new ArrayList<>();
	}

	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}
	
	public void addCollection(Collection collection) {
		this.collections.add(collection);
	}

	public String getName() {
		return this.name;
	}

	public int getAttributeSize() {
		return this.attributes.size();
	}
	
	public int getCollectionSize() {
		return this.collections.size();
	}

	public Attribute getAttribute(int index) {
		return this.attributes.get(index);
	}
	
	public Collection getCollection(int index) {
		return this.collections.get(index);
	}
	
	public void accept(IVisitor visitor) {
		visitor.visitEntity(this);
	}
}
