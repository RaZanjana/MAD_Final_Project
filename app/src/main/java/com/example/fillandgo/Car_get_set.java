package com.example.fillandgo;

public class Car_get_set {
    private int id;
    private String carName;

    public int getId() {
        return id;
    }

    public Car_get_set(int id, String carName, String fuelType) {

      setId(id);
      setCarName(carName);
      setFuelType(fuelType);

    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    private String fuelType;

}
