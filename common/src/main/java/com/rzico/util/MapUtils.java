/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package com.rzico.util;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Utils - Map
 *
 * @author rsico Team
 * @version 3.0
 */
public final class MapUtils {

    /** 地球半径 */
    private static final double EARTH_RADIUS = 6371000;
    /** 范围距离 */
    private double distance;
    /** 左上角 */
    private double left_lat = 0;
    /** 右上角 */
    private double left_lng = 0;
    /** 左下角 */
    private double right_lat = 0;
    /** 右下角 */
    private double right_lng = 0;

    /**
     * 不可实例化
     */
    public MapUtils(double distance) {
        this.distance = distance;
    }

    public void rectangle4Point(double lat, double lng) {

        double dlng = 2 * Math.asin(Math.sin(distance/(2*EARTH_RADIUS))/Math.cos(Math.toRadians(lat)));
        dlng = Math.toDegrees(dlng);

        double dlat = distance / EARTH_RADIUS;
        dlat = Math.toDegrees(dlat); // # 弧度转换成角度  
        left_lat = lat - dlat;
        left_lng = lng - dlng;

        right_lat = lat + dlat;
        right_lng = lng + dlng;

    }

    public static double hav(double theta) {
        double s = Math.sin(theta / 2);
        return s * s;
    }

    /**
     * 经纬度计算两点距离
     *
     * double lat1, double lat2, double lon1,    double lon2
     *            对象
     * @return double公里数
     */
    public static double getDistatce(double lat0, double lat1, double lng0,double lng1) {
        lat0 = Math.toRadians(lat0);
        lat1 = Math.toRadians(lat1);
        lng0 = Math.toRadians(lng0);
        lng1 = Math.toRadians(lng1);

        double dlng = Math.abs(lng0 - lng1);
        double dlat = Math.abs(lat0 - lat1);
        double h = hav(dlat) + Math.cos(lat0) * Math.cos(lat1) * hav(dlng);
        double distance = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(h));

        return distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public static double getEarthRadius() {
        return EARTH_RADIUS;
    }

    public double getLeft_lat() {
        return left_lat;
    }

    public void setLeft_lat(double left_lat) {
        this.left_lat = left_lat;
    }

    public double getLeft_lng() {
        return left_lng;
    }

    public void setLeft_lng(double left_lng) {
        this.left_lng = left_lng;
    }

    public double getRight_lat() {
        return right_lat;
    }

    public void setRight_lat(double right_lat) {
        this.right_lat = right_lat;
    }

    public double getRight_lng() {
        return right_lng;
    }

    public void setRight_lng(double right_lng) {
        this.right_lng = right_lng;
    }

    public static void main(String[] args) {
        double lon1 = 109.0145193757;
        double lat1 = 34.236080797698;
        double lon2 = 108.9644583556;
        double lat2 = 34.286439088548;
        double dist;

        MapUtils mapUtils = new MapUtils(20000);
        mapUtils.rectangle4Point(34.236080797698,109.0145193757);

        System.out.println("r：" );

    }
}