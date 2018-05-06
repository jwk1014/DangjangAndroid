package com.dangjang.dj2015.jsonclass.response;

public class MartVO {
	private int index;
	private String name;
	private String admin_name;
	private String phone;
	private int address_index;
	private String address_front;
	private String address_detail;
	private int delivery_free_cost;
	private int delivery_cost;
	private double lat;
	private double lng;
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
	public String getAdmin_name() {
		return admin_name;
	}
	public void setAdmin_name(String admin_name) {
		this.admin_name = admin_name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getAddress_index() {
		return address_index;
	}
	public void setAddress_index(int address_index) {
		this.address_index = address_index;
	}
	public String getAddress_detail() {
		return address_detail;
	}
	public void setAddress_detail(String address_detail) {
		this.address_detail = address_detail;
	}
	public int getDelivery_free_cost() {
		return delivery_free_cost;
	}
	public void setDelivery_free_cost(int delivery_free_cost) {
		this.delivery_free_cost = delivery_free_cost;
	}
	public int getDelivery_cost() {
		return delivery_cost;
	}
	public void setDelivery_cost(int delivery_cost) {
		this.delivery_cost = delivery_cost;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getAddress_front() {
		return address_front;
	}
	public void setAddress_front(String address_front) {
		this.address_front = address_front;
	}
}
