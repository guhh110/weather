/**
  * Copyright 2017 bejson.com 
  */
package entity;

/**
 * Auto-generated: 2017-11-09 15:41:2
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Hourly_forecast {

    private String date;
    private String pop;
    private String hum;
    private String pres;
    private String tmp;
    private Cond cond;
    private Wind wind;
    public void setDate(String date) {
         this.date = date;
     }
     public String getDate() {
         return date;
     }

    public void setPop(String pop) {
         this.pop = pop;
     }
     public String getPop() {
         return pop;
     }

    public void setHum(String hum) {
         this.hum = hum;
     }
     public String getHum() {
         return hum;
     }

    public void setPres(String pres) {
         this.pres = pres;
     }
     public String getPres() {
         return pres;
     }

    public void setTmp(String tmp) {
         this.tmp = tmp;
     }
     public String getTmp() {
         return tmp;
     }

    public void setCond(Cond cond) {
         this.cond = cond;
     }
     public Cond getCond() {
         return cond;
     }

    public void setWind(Wind wind) {
         this.wind = wind;
     }
     public Wind getWind() {
         return wind;
     }

}