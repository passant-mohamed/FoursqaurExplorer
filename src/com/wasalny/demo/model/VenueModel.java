package com.wasalny.demo.model;

public class VenueModel {

	public VenueModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public VenueModel(String id, String name, String lat, String lng,
			String img_url, int checkIn_count, int usersCounts, int tipCount) {
		super();
		this.id = id;
		this.name = name;
		this.lat = lat;
		this.lng = lng;
		this.img_url = img_url;
		this.checkIn_count = checkIn_count;
		this.usersCounts = usersCounts;
		this.tipCount = tipCount;
	}
	String id, name,lat,lng,img_url;
	int  checkIn_count,usersCounts,tipCount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public int getCheckIn_count() {
		return checkIn_count;
	}
	public void setCheckIn_count(int checkIn_count) {
		this.checkIn_count = checkIn_count;
	}
	public int getUsersCounts() {
		return usersCounts;
	}
	public void setUsersCounts(int usersCounts) {
		this.usersCounts = usersCounts;
	}
	public int getTipCount() {
		return tipCount;
	}
	public void setTipCount(int tipCount) {
		this.tipCount = tipCount;
	}
}
