package model;

import java.util.ArrayList;
import java.util.List;

import visitor.IVisitable;
import visitor.IVisitor;


public class Model implements IVisitable {
	
	String name;
	List<Entity> entities;
	
    public Model() {
    	this.entities = new ArrayList<>();
    }
	
	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}

	public String getName() {
		return this.name;
	}

	public int getEntitiesSize() {
		return this.entities.size();
	}

	public Entity getEntity(int i) {
		return this.entities.get(i);
	}

	public void setName(String name) {
		this.name = name;
	}
	public void accept(IVisitor visitor) {
		  visitor.visitModel(this);
	}

}
