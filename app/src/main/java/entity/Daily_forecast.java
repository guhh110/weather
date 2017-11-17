/**
  * Copyright 2017 bejson.com 
  */
package entity;
import java.util.Date;

/**
 * Auto-generated: 2017-11-09 15:41:2
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Daily_forecast {

    private String date;
    private String pop;
    private String hum;
    private String uv;
    private String vis;
    private Astro astro;
    private String pres;
    private String pcpn;
    private Tmp tmp;
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

    public void setUv(String uv) {
         this.uv = uv;
     }
     public String getUv() {
         return uv;
     }

    public void setVis(String vis) {
         this.vis = vis;
     }
     public String getVis() {
         return vis;
     }

    public void setAstro(Astro astro) {
         this.astro = astro;
     }
     public Astro getAstro() {
         return astro;
     }

    public void setPres(String pres) {
         this.pres = pres;
     }
     public String getPres() {
         return pres;
     }

    public void setPcpn(String pcpn) {
         this.pcpn = pcpn;
     }
     public String getPcpn() {
         return pcpn;
     }

    public void setTmp(Tmp tmp) {
         this.tmp = tmp;
     }
     public Tmp getTmp() {
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