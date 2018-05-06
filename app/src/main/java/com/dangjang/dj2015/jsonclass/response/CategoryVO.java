package com.dangjang.dj2015.jsonclass.response;

import java.util.List;

public class CategoryVO {
	private int index;
	private String name;
	private List<CategoryVO> subList;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<CategoryVO> getSubList() {
		return subList;
	}
	public void setSubList(List<CategoryVO> subList) {
		this.subList = subList;
	}
}
