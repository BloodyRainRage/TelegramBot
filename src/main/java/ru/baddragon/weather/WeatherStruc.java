package ru.baddragon.weather;

public class WeatherStruc {

    private String name;
    private Double temp;
    private Double humidity;
    private String icon;
    private String main;
    private String result = "";
    public String getName() {
        return name;
    }

    //Returns all values as a string
    public String normalize(){
        return result = "City: " + this.name + "\n" +
                "Temperature: " + this.temp +" C°" + "\n" +
                "Humidity: " + this.humidity + "%" + "\n" +
                "Main: " + this.main;
    }


    public void setName(String name) {
        this.name = name;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }



}
