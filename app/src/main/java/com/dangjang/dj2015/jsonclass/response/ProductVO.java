package com.dangjang.dj2015.jsonclass.response;

import java.util.Date;

public class ProductVO {
	private int index;
	private String name;
	private int barcode;
	private int category_index;
	private String img_url;
	private String unit;
	private int mart_index;
	private String mart_name;
	private int origin_price;
	private int sale_price;
	private String sale_comment;
	private Date start_date;
	private Date end_date;
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
	public int getBarcode() {
		return barcode;
	}
	public void setBarcode(int barcode) {
		this.barcode = barcode;
	}
	public int getCategory_index() {
		return category_index;
	}
	public void setCategory_index(int category_index) {
		this.category_index = category_index;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getMart_index() {
		return mart_index;
	}
	public void setMart_index(int mart_index) {
		this.mart_index = mart_index;
	}
	public int getOrigin_price() {
		return origin_price;
	}
	public void setOrigin_price(int origin_price) {
		this.origin_price = origin_price;
	}
	public int getSale_price() {
		return sale_price;
	}
	public void setSale_price(int sale_price) {
		this.sale_price = sale_price;
	}
	public String getSale_comment() {
		return sale_comment;
	}
	public void setSale_comment(String sale_comment) {
		this.sale_comment = sale_comment;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public String getMart_name() {
		return mart_name;
	}
	public void setMart_name(String mart_name) {
		this.mart_name = mart_name;
	}
}
