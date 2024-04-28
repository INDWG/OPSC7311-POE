package com.example.proactive_opsc7311_poe.models;

import com.google.type.DateTime;

public class Exercise {

    private String name;
    private String description;
    private String image;
    private DateTime date;
    private DateTime startTime;
    private DateTime endTime;
    private String category;
    private int min;
    private int max;
    private int loggedTime;
    private boolean goalsMet;

    public Exercise(String name, String description, String image, DateTime date, DateTime startTime, DateTime endTime, String category, int min, int max) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.category = category;
        this.min = min;
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getLoggedTime() {
        return loggedTime;
    }

    public void setLoggedTime(int loggedTime) {
        this.loggedTime = loggedTime;
    }

    public boolean isGoalsMet() {
        return goalsMet;
    }

    public void setGoalsMet(boolean goalsMet) {
        this.goalsMet = goalsMet;
    }
}
