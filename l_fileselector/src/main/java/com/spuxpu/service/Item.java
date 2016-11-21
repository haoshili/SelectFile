package com.spuxpu.service;

public class Item implements Comparable<Item> {

	/**
	 * 该文件的名称
	 */
	private String name;

	/**
	 * 文件大小byte 
	 */
	private Long data;

	/**
	 * 该文件的最后修改时间
	 */
	private Long date;

	/**
	 * 该文件的路径
	 */
	private String path;

	/**
	 * 显示的图片类型
	 */
	private Integer image;

	public Item(String n, long d, long dt, String p, Integer img) {
		name = n;
		data = d;
		date = dt;
		path = p;
		image = img;

	}

	public String getName() {
		return name;
	}

	public long getData() {
		return data;
	}

	public long getDate() {
		return date;
	}

	public String getPath() {
		return path;
	}

	public Integer getImage() {
		return image;
	}

	public int compareTo(Item o) {
		if (this.name != null)
			return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
		else
			throw new IllegalArgumentException();
	}
}
