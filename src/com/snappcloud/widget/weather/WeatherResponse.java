package com.snappcloud.widget.weather;

import java.util.Date;

public class WeatherResponse
{
	private String location;
	private String temperature;
	private String currentConditionsImgUrl;
	private String currentConditions;
	private String highTemp;
	private String lowTemp;
	private String prediction;
	
	public String getCurrentConditionsImgUrl() {
		return currentConditionsImgUrl;
	}
	public void setCurrentConditionsImgUrl(String currentConditionsImgUrl) {
		this.currentConditionsImgUrl = currentConditionsImgUrl;
	}
	public String getCurrentConditions() {
		return currentConditions;
	}
	public void setCurrentConditions(String currentConditions) {
		this.currentConditions = currentConditions;
	}
	public String getHighTemp() {
		return highTemp;
	}
	public void setHighTemp(String highTemp) {
		this.highTemp = highTemp;
	}
	public String getLowTemp() {
		return lowTemp;
	}
	public void setLowTemp(String lowTemp) {
		this.lowTemp = lowTemp;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public void setPrediction(String prediction) {
		this.prediction = prediction;
	}
	public String getPrediction() {
		return prediction;
	}
	
}