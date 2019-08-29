package ru.baddragon.weather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    public static String getWeather(String city, WeatherStruc weatherStruc) {

        String result = "";
        String strUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city +
                "&units=metric&appid=02ccabbe2f2bf1362b1719d55c83df60";
        try {
            URL url = new URL(strUrl);

            Scanner in = new Scanner((InputStream) url.getContent());

            while (in.hasNext()) {
                result += in.nextLine();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject object = new JSONObject(result);
        weatherStruc.setName(object.getString("name"));

        JSONObject subobject = object.getJSONObject("main");
        weatherStruc.setTemp(subobject.getDouble("temp"));
        weatherStruc.setHumidity(subobject.getDouble("humidity"));

        //subobject = object.getJSONObject("weather");
        //Ниже на случай если несколько объектов
        JSONArray array = object.getJSONArray("weather");
        for (int i = 0; i < array.length(); i++) {
            subobject = array.getJSONObject(i);
            weatherStruc.setIcon(subobject.getString("icon"));
            weatherStruc.setMain(subobject.getString("main"));
        }

        return weatherStruc.normalize();
    }

}
