package model;

import java.util.List;

import org.w3c.dom.DocumentType;

public class Model {
	
	String name;
	List<Entity> entities;
	
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
	
	

}
