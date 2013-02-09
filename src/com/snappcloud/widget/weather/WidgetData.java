package com.snappcloud.widget.weather;

public class WidgetData 
{
	
	private String currentConditionsImgUrl;
	
	private String prediction;
	
	private String temperature;
	
	private String lowTemp;
	
	private String highTemp;
	
	private String location;

	public String getCurrentConditionsImgUrl() {
		return currentConditionsImgUrl;
	}

	public void setCurrentConditionsImgUrl(String currentConditionsImgUrl) {
		this.currentConditionsImgUrl = currentConditionsImgUrl;
	}

	public String getPrediction() {
		return prediction;
	}

	public void setPrediction(String prediction) {
		this.prediction = prediction;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getLowTemp() {
		return lowTemp;
	}

	public void setLowTemp(String lowTemp) {
		this.lowTemp = lowTemp;
	}

	public String getHighTemp() {
		return highTemp;
	}

	public void setHighTemp(String highTemp) {
		this.highTemp = highTemp;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
