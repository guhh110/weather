/**
  * Copyright 2017 bejson.com 
  */
package entity;
import java.util.List;

/**
 * Auto-generated: 2017-11-08 0:2:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class HeWeather5 {

    private Now now;
    private Suggestion suggestion;
    private Aqi aqi;
    private Basic basic;
    private List<Daily_forecast> daily_forecast;
    private List<Hourly_forecast> hourly_forecast;
    private String status;
    public void setNow(Now now) {
         this.now = now;
     }
     public Now getNow() {
         return now;
     }

    public void setSuggestion(Suggestion suggestion) {
         this.suggestion = suggestion;
     }
     public Suggestion getSuggestion() {
         return suggestion;
     }

    public void setAqi(Aqi aqi) {
         this.aqi = aqi;
     }
     public Aqi getAqi() {
         return aqi;
     }

    public void setBasic(Basic basic) {
         this.basic = basic;
     }
     public Basic getBasic() {
         return basic;
     }

    public void setDaily_forecast(List<Daily_forecast> daily_forecast) {
         this.daily_forecast = daily_forecast;
     }
     public List<Daily_forecast> getDaily_forecast() {
         return daily_forecast;
     }

    public void setHourly_forecast(List<Hourly_forecast> hourly_forecast) {
         this.hourly_forecast = hourly_forecast;
     }
     public List<Hourly_forecast> getHourly_forecast() {
         return hourly_forecast;
     }

    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

}