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
public class WeatherEntity {

    private String code;
    private boolean charge;
    private String msg;
    private Result result;
    public void setCode(String code) {
         this.code = code;
     }
     public String getCode() {
         return code;
     }

    public void setCharge(boolean charge) {
         this.charge = charge;
     }
     public boolean getCharge() {
         return charge;
     }

    public void setMsg(String msg) {
         this.msg = msg;
     }
     public String getMsg() {
         return msg;
     }

    public void setResult(Result result) {
         this.result = result;
     }
     public Result getResult() {
         return result;
     }

}