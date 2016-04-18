package com.kintiger.mall.api.ip.bo;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Internet Protocol(http://ip.taobao.com/).
 * 
 * @author
 * 
 */
public class IP implements Serializable {

	private static final long serialVersionUID = 3146493755385362399L;

	/**
	 * id.
	 */
	private String id;

	/**
	 * 国家.
	 */
	private String country;

	/**
	 * 国家id.
	 */
	@JSONField(name = "country_id")
	private String countryId;

	/**
	 * 省.
	 */
	private String area;

	/**
	 * 省id.
	 */
	@JSONField(name = "area_id")
	private String areaId;

	/**
	 * 市.
	 */
	private String region;

	/**
	 * 市id.
	 */
	@JSONField(name = "region_id")
	private String regionId;

	/**
	 * 地区.
	 */
	private String city;

	/**
	 * 地区id.
	 */
	@JSONField(name = "city_id")
	private String cityId;

	/**
	 * 县.
	 */
	private String county;

	/**
	 * 县id.
	 */
	@JSONField(name = "county_id")
	private String countyId;

	/**
	 * 网络服务供应商.
	 */
	private String isp;

	/**
	 * 网络服务供应商id.
	 */
	@JSONField(name = "isp_id")
	private String ispId;

	/**
	 * ip地址.
	 */
	private String ip;

	/**
	 * 创建人.
	 */
	private String modifyUser;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public String getIspId() {
		return ispId;
	}

	public void setIspId(String ispId) {
		this.ispId = ispId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

}
