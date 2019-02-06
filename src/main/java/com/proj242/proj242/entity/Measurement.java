package com.proj242.proj242.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "measurement_id")
    private Long id;

    @Column(name = "measurement_description")
    private String description;

    @Column(name = "measurement_area")
    private int area;

    @Column(name = "measurement_perimeter")
    private int perimeter;

    @Column(name = "measurement_diameter")
    private int diameter;

    @Column(name = "measurement_asymmetry")
    private float asymmetry;

    @Column(name = "bounds")
    private float bounds;

    @Column(name = "measurement_local_date_time")
    private LocalDateTime localDateTime;

    @Column(name = "measurement_string_data")
    private String stringData;

    @Column(name = "measurement_original_image")
    private String originalImage;

    @Column(name = "measurement_proccessing_image")
    private String proccessingImage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Measurement() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(int perimeter) {
        this.perimeter = perimeter;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    public float getAsymmetry() {
        return asymmetry;
    }

    public void setAsymmetry(float asymmetry) {
        this.asymmetry = asymmetry;
    }

    public float getBounds() {
        return bounds;
    }

    public void setBounds(float bounds) {
        this.bounds = bounds;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getStringData() {
        return stringData;
    }

    public void setStringData(String stringData) {
        this.stringData = stringData;
    }

    public String getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(String originalImage) {
        this.originalImage = originalImage;
    }

    public String getProccessingImage() {
        return proccessingImage;
    }

    public void setProccessingImage(String proccessingImage) {
        this.proccessingImage = proccessingImage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
