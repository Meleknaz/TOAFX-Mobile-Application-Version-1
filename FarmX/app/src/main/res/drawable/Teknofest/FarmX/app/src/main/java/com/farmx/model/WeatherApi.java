package com.farmx.model;

import java.util.List;

import retrofit2.Call;

@SuppressWarnings("all")
public class WeatherApi {
    private final Coord coord;

    private final List<Weather> weather;

    private final String base;

    private final Main main;

    private final int visibility;

    private final Wind wind;

    private final Clouds clouds;

    private final int dt;

    private final Sys sys;

    private final int timezone;

    private final int id;

    private final String name;

    private final int cod;

    public WeatherApi(Coord coord, List<Weather> weather, String base, Main main, int visibility,
                      Wind wind, Clouds clouds, int dt, Sys sys, int timezone, int id, String name, int cod) {
        this.coord = coord;
        this.weather = weather;
        this.base = base;
        this.main = main;
        this.visibility = visibility;
        this.wind = wind;
        this.clouds = clouds;
        this.dt = dt;
        this.sys = sys;
        this.timezone = timezone;
        this.id = id;
        this.name = name;
        this.cod = cod;
    }

    public Coord getCoord() {
        return coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public int getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public int getDt() {
        return dt;
    }

    public Sys getSys() {
        return sys;
    }

    public int getTimezone() {
        return timezone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }


    public static class Coord {
        private final double lon;

        private final double lat;

        public Coord(double lon, double lat) {
            this.lon = lon;
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public double getLat() {
            return lat;
        }
    }

    public static class Weather {
        private final int id;

        private final String main;

        private final String description;

        private final String icon;

        public Weather(int id, String main, String description, String icon) {
            this.id = id;
            this.main = main;
            this.description = description;
            this.icon = icon;
        }

        public int getId() {
            return id;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }

    public static class Main {
        private final double temp;

        private final double feelsLike;

        private final double tempMin;

        private final int tempMax;

        private final int pressure;

        private final int humidity;

        public Main(double temp, double feelsLike, double tempMin, int tempMax, int pressure,
                    int humidity) {
            this.temp = temp;
            this.feelsLike = feelsLike;
            this.tempMin = tempMin;
            this.tempMax = tempMax;
            this.pressure = pressure;
            this.humidity = humidity;
        }

        public double getTemp() {
            return temp;
        }

        public double getFeelsLike() {
            return feelsLike;
        }

        public double getTempMin() {
            return tempMin;
        }

        public int getTempMax() {
            return tempMax;
        }

        public int getPressure() {
            return pressure;
        }

        public int getHumidity() {
            return humidity;
        }
    }

    public static class Wind {
        private final double speed;

        private final int deg;

        public Wind(double speed, int deg) {
            this.speed = speed;
            this.deg = deg;
        }

        public double getSpeed() {
            return speed;
        }

        public int getDeg() {
            return deg;
        }
    }

    public static class Clouds {
        private final int all;

        public Clouds(int all) {
            this.all = all;
        }

        public int getAll() {
            return all;
        }
    }

    public static class Sys {
        private final int type;

        private final int id;

        private final String country;

        private final int sunrise;

        private final int sunset;

        public Sys(int type, int id, String country, int sunrise, int sunset) {
            this.type = type;
            this.id = id;
            this.country = country;
            this.sunrise = sunrise;
            this.sunset = sunset;
        }

        public int getType() {
            return type;
        }

        public int getId() {
            return id;
        }

        public String getCountry() {
            return country;
        }

        public int getSunrise() {
            return sunrise;
        }

        public int getSunset() {
            return sunset;
        }
    }
}
