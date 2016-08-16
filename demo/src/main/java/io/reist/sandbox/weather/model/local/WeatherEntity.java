package io.reist.sandbox.weather.model.local;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 25.07.16.
 *
 * @author Timofey Plotnikov <timofey.plot@gmail.com>
 */

public class WeatherEntity {

    @SerializedName("location")
    private LocationData location;

    @SerializedName("current")
    private WeatherData weatherData;

    public void setLocation(LocationData location) {
        this.location = location;
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    public String getAddress() {
        return String.format("%s, %s, %s", location.country, location.region, location.name);
    }

    public double getTemperature() {
        return weatherData.tempC;
    }

    public double getPressure() {
        return weatherData.pressure;
    }

    public class LocationData {
        @SerializedName("name")
        private String name;

        @SerializedName("region")
        private String region;

        @SerializedName("country")
        private String country;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    public class WeatherData {
        @SerializedName("temp_c")
        private double tempC;

        @SerializedName("pressure_mb")
        private double pressure;

        public double getTempC() {
            return tempC;
        }

        public void setTempC(double tempC) {
            this.tempC = tempC;
        }

        public double getPressure() {
            return pressure;
        }

        public void setPressure(double pressure) {
            this.pressure = pressure;
        }
    }
}
