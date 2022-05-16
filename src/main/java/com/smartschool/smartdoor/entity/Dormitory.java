package com.smartschool.smartdoor.entity;

public class Dormitory {
    private String id;
    private String building_area;
    private String building_id;
    private String floor_id;
    private String equipment_id;
    private String name_of_resident;
    private String bunk_num;
    private String vacant_bed_num;
    private String occupied_bed_num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuilding_area() {
        return building_area;
    }

    public void setBuilding_area(String building_area) {
        this.building_area = building_area;
    }

    public String getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(String building_id) {
        this.building_id = building_id;
    }

    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
    }

    public String getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(String equipment_id) {
        this.equipment_id = equipment_id;
    }

    public String getName_of_resident() {
        return name_of_resident;
    }

    public void setName_of_resident(String name_of_resident) {
        this.name_of_resident = name_of_resident;
    }

    public String getBunk_num() {
        return bunk_num;
    }

    public void setBunk_num(String bunk_num) {
        this.bunk_num = bunk_num;
    }

    public String getVacant_bed_num() {
        return vacant_bed_num;
    }

    public void setVacant_bed_num(String vacant_bed_num) {
        this.vacant_bed_num = vacant_bed_num;
    }

    public String getOccupied_bed_num() {
        return occupied_bed_num;
    }

    public void setOccupied_bed_num(String occupied_bed_num) {
        this.occupied_bed_num = occupied_bed_num;
    }

    public Dormitory(String id, String building_area, String building_id, String floor_id, String equipment_id, String name_of_resident, String bunk_num, String vacant_bed_num, String occupied_bed_num) {
        this.id = id;
        this.building_area = building_area;
        this.building_id = building_id;
        this.floor_id = floor_id;
        this.equipment_id = equipment_id;
        this.name_of_resident = name_of_resident;
        this.bunk_num = bunk_num;
        this.vacant_bed_num = vacant_bed_num;
        this.occupied_bed_num = occupied_bed_num;
    }

    @Override
    public String toString() {
        return "Dormitory{" +
                "id='" + id + '\'' +
                ", building_area='" + building_area + '\'' +
                ", building_id='" + building_id + '\'' +
                ", floor_id='" + floor_id + '\'' +
                ", equipment_id='" + equipment_id + '\'' +
                ", name_of_resident='" + name_of_resident + '\'' +
                ", bunk_num='" + bunk_num + '\'' +
                ", vacant_bed_num='" + vacant_bed_num + '\'' +
                ", occupied_bed_num='" + occupied_bed_num + '\'' +
                '}';
    }
}
