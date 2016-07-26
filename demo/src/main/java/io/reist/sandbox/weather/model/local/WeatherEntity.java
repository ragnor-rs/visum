package io.reist.sandbox.weather.model.local;

/**
 * Created on 25.07.16.
 *
 * @author Timofey Plotnikov <timofey.plot@gmail.com>
 */

public class WeatherEntity {

    private LocationData location;
    private WeatherData current;

    private String address;
    private double temperature;
    private double pressure;

    public LocationData getLocation() {
        return location;
    }

    public void setLocation(LocationData location) {
        this.location = location;
        this.address = String.format("%s, %s, %s", location.country, location.region, location.name);
    }

    public WeatherData getCurrent() {
        return current;
    }

    public void setCurrent(WeatherData current) {
        this.current = current;
        this.temperature = current.temp_c;
        this.pressure = current.pressure_mb;
    }

    public String getAddress() {
        return String.format("%s, %s, %s", location.country, location.region, location.name);
    }

    public double getTemperature() {
        return current.temp_c;
    }

    public double getPressure() {
        return current.pressure_mb;
    }

    private class LocationData {
        private String name;
        private String region;
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

    private class WeatherData {
        private double temp_c;
        private double pressure_mb;

        public double getTemp_c() {
            return temp_c;
        }

        public void setTemp_c(double temp_c) {
            this.temp_c = temp_c;
        }

        public double getPressure_mb() {
            return pressure_mb;
        }

        public void setPressure_mb(double pressure_mb) {
            this.pressure_mb = pressure_mb;
        }
    }
}
