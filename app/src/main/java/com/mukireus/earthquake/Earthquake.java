package com.mukireus.earthquake;

public class Earthquake {

  private String place;
  private double magnitude;
  private long time;
  private double lat;
  private double lon;

  public Earthquake(String place, double magnitude, long time, double lat, double lon) {
    this.place = place;
    this.magnitude = magnitude;
    this.time = time;
    this.lat = lat;
    this.lon = lon;
  }

  public Earthquake() {

  }

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  public double getMagnitude() {
    return magnitude;
  }

  public void setMagnitude(double magnitude) {
    this.magnitude = magnitude;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLon() {
    return lon;
  }

  public void setLon(double lon) {
    this.lon = lon;
  }
}
