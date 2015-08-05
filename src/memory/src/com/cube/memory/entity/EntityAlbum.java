package com.cube.memory.entity;

import java.util.ArrayList;
import java.util.List;

public class EntityAlbum<T> {
	private String name;
	private int total;
	private List<T> items = new ArrayList<T>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<T> getItems() {
		return items;
	}
	public void setItems(List<T> items) {
		this.items = items;
	}
	
	public void addItem(T item){
		this.items.add(item);
	}
}
