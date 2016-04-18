package com.kintiger.mall.points.dao;

import java.util.List;

import com.kintiger.mall.api.points.bo.Points;

/**
 * 
 * @author xujiakun
 * 
 */
public interface IPointsDao {

	/**
	 * 
	 * @param points
	 * @return
	 */
	int getPointsCount(Points points);

	/**
	 * 
	 * @param points
	 * @return
	 */
	List<Points> getPointsList(Points points);

	/**
	 * 
	 * @param points
	 * @return
	 */
	String createPoints(Points points);

	/**
	 * 
	 * @param points
	 * @return
	 */
	int deletePoints(Points points);

}
