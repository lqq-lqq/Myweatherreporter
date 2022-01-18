package com.example.weather.pojo;

import java.io.Serializable;
import java.util.List;

public class Weather implements Serializable {
    /**
     * reason : 查询成功!
     * result : {"city":"重庆","realtime":{"temperature":"17","humidity":"55","info":"霾","wid":"53","direct":"东风","power":"1级","aqi":"119"},"future":[{"date":"2021-12-05","temperature":"11/17℃","weather":"晴","wid":{"day":"00","night":"00"},"direct":"东北风"},{"date":"2021-12-06","temperature":"10/18℃","weather":"多云转阴","wid":{"day":"01","night":"02"},"direct":"西北风"},{"date":"2021-12-07","temperature":"11/14℃","weather":"阴","wid":{"day":"02","night":"02"},"direct":"东风"},{"date":"2021-12-08","temperature":"7/12℃","weather":"阴","wid":{"day":"02","night":"02"},"direct":"南风"},{"date":"2021-12-09","temperature":"8/15℃","weather":"阴","wid":{"day":"02","night":"02"},"direct":"北风转西北风"}]}
     * error_code : 0
     */
    private String reason;
    private ResultBean result; //重点
    private int error_code;
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public ResultBean getResult() {
        return result;
    }
    public void setResult(ResultBean result) {
        this.result = result;
    }
    public int getError_code() {
        return error_code;
    }
    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
    public static class ResultBean implements Serializable {
        /**
         * city : 重庆
         * realtime : {"temperature":"17","humidity":"55","info":"霾","wid":"53","direct":"东风","power":"1级","aqi":"119"}
         * future : [{"date":"2021-12-05","temperature":"11/17℃","weather":"晴","wid":{"day":"00","night":"00"},"direct":"东北风"},
         * {"date":"2021-12-06","temperature":"10/18℃","weather":"多云转阴","wid":{"day":"01","night":"02"},"direct":"西北风"},
         * {"date":"2021-12-07","temperature":"11/14℃","weather":"阴","wid":{"day":"02","night":"02"},"direct":"东风"},
         * {"date":"2021-12-08","temperature":"7/12℃","weather":"阴","wid":{"day":"02","night":"02"},"direct":"南风"},
         * {"date":"2021-12-09","temperature":"8/15℃","weather":"阴","wid":{"day":"02","night":"02"},"direct":"北风转西北风"}]
         */

        private String city;
        private RealtimeBean realtime;
        private List<FutureBean> future;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public RealtimeBean getRealtime() {
            return realtime;
        }

        public void setRealtime(RealtimeBean realtime) {
            this.realtime = realtime;
        }

        public List<FutureBean> getFuture() {
            return future;
        }

        public void setFuture(List<FutureBean> future) {
            this.future = future;
        }

        //今天的天气
        public static class RealtimeBean implements Serializable {
            /**
             * temperature : 17
             * humidity : 55
             * info : 霾
             * wid : 53
             * direct : 东风
             * power : 1级
             * aqi : 119
             */

            private String temperature;
            private String humidity;
            private String info;
            private String wid;
            private String direct;
            private String power;
            private String aqi;

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getHumidity() {
                return humidity;
            }

            public void setHumidity(String humidity) {
                this.humidity = humidity;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public String getWid() {
                return wid;
            }

            public void setWid(String wid) {
                this.wid = wid;
            }

            public String getDirect() {
                return direct;
            }

            public void setDirect(String direct) {
                this.direct = direct;
            }

            public String getPower() {
                return power;
            }

            public void setPower(String power) {
                this.power = power;
            }

            public String getAqi() {
                return aqi;
            }

            public void setAqi(String aqi) {
                this.aqi = aqi;
            }
            public String getString(){
                String str="温度:"+temperature+" 湿度:"+humidity+" 天气:"+info+" 风力:"+wid+" 风向:"+direct+" 风力:"+power+" aqi:"+aqi;
                return str;
            }
        }

        public static class FutureBean implements Serializable {
            /**
             * date : 2021-12-05
             * temperature : 11/17℃
             * weather : 晴
             * wid : {"day":"00","night":"00"}
             * direct : 东北风
             */

            private String date;
            private String temperature;
            private String weather;
            private WidBean wid;
            private String direct;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public WidBean getWid() {
                return wid;
            }

            public void setWid(WidBean wid) {
                this.wid = wid;
            }

            public String getDirect() {
                return direct;
            }

            public void setDirect(String direct) {
                this.direct = direct;
            }
            public String getString(){
                String str="";
                str="温度:"+temperature+" 天气:"+weather+" 风力:"+wid.getDay()+"(白天);"+wid.getNight()+"(晚上)"+" 风向:"+direct;
                return str;
            }

            public static class WidBean implements Serializable {
                /**
                 * day : 00
                 * night : 00
                 */

                private String day;
                private String night;

                public String getDay() {
                    return day;
                }

                public void setDay(String day) {
                    this.day = day;
                }

                public String getNight() {
                    return night;
                }

                public void setNight(String night) {
                    this.night = night;
                }
            }
        }
    }
}
