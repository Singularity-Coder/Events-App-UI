package com.singularitycoder.tablayout.model;

import java.util.ArrayList;

public class EventFullViewModel {
    private int intEventFullViewGalleryImage;

    private String strEventFullViewEventName;
    private String strEventFullViewEventType;
    private String strEventFullViewEventLocation;
    private String strEventFullViewEventVenue;
    private String strEventFullViewEventDescription;
    private String strEventFullViewEventCategory;
    private String strEventFullViewEventFrequency;
    private String strEventFullViewEventStartDate;
    private String strEventFullViewEventStartTime;
    private String strEventFullViewEventEndDate;
    private String strEventFullViewEventEndTime;

    private ArrayList<String> imageList;

    public EventFullViewModel() {
    }

    public EventFullViewModel(int intEventFullViewGalleryImage) {
        this.intEventFullViewGalleryImage = intEventFullViewGalleryImage;
    }

    public EventFullViewModel(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public EventFullViewModel(int intEventFullViewGalleryImage, ArrayList<String> imageList, String strEventFullViewEventName, String strEventFullViewEventType, String strEventFullViewEventLocation, String strEventFullViewEventVenue, String strEventFullViewEventDescription, String strEventFullViewEventCategory, String strEventFullViewEventFrequency, String strEventFullViewEventStartDate, String strEventFullViewEventStartTime, String strEventFullViewEventEndDate, String strEventFullViewEventEndTime) {
        this.intEventFullViewGalleryImage = intEventFullViewGalleryImage;
        this.strEventFullViewEventName = strEventFullViewEventName;
        this.strEventFullViewEventType = strEventFullViewEventType;
        this.strEventFullViewEventLocation = strEventFullViewEventLocation;
        this.strEventFullViewEventVenue = strEventFullViewEventVenue;
        this.strEventFullViewEventDescription = strEventFullViewEventDescription;
        this.strEventFullViewEventCategory = strEventFullViewEventCategory;
        this.strEventFullViewEventFrequency = strEventFullViewEventFrequency;
        this.strEventFullViewEventStartDate = strEventFullViewEventStartDate;
        this.strEventFullViewEventStartTime = strEventFullViewEventStartTime;
        this.strEventFullViewEventEndDate = strEventFullViewEventEndDate;
        this.strEventFullViewEventEndTime = strEventFullViewEventEndTime;
        this.imageList = imageList;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public String getStrEventFullViewEventName() {
        return strEventFullViewEventName;
    }

    public void setStrEventFullViewEventName(String strEventFullViewEventName) {
        this.strEventFullViewEventName = strEventFullViewEventName;
    }

    public String getStrEventFullViewEventType() {
        return strEventFullViewEventType;
    }

    public void setStrEventFullViewEventType(String strEventFullViewEventType) {
        this.strEventFullViewEventType = strEventFullViewEventType;
    }

    public String getStrEventFullViewEventLocation() {
        return strEventFullViewEventLocation;
    }

    public void setStrEventFullViewEventLocation(String strEventFullViewEventLocation) {
        this.strEventFullViewEventLocation = strEventFullViewEventLocation;
    }

    public String getStrEventFullViewEventVenue() {
        return strEventFullViewEventVenue;
    }

    public void setStrEventFullViewEventVenue(String strEventFullViewEventVenue) {
        this.strEventFullViewEventVenue = strEventFullViewEventVenue;
    }

    public String getStrEventFullViewEventDescription() {
        return strEventFullViewEventDescription;
    }

    public void setStrEventFullViewEventDescription(String strEventFullViewEventDescription) {
        this.strEventFullViewEventDescription = strEventFullViewEventDescription;
    }

    public String getStrEventFullViewEventCategory() {
        return strEventFullViewEventCategory;
    }

    public void setStrEventFullViewEventCategory(String strEventFullViewEventCategory) {
        this.strEventFullViewEventCategory = strEventFullViewEventCategory;
    }

    public String getStrEventFullViewEventFrequency() {
        return strEventFullViewEventFrequency;
    }

    public void setStrEventFullViewEventFrequency(String strEventFullViewEventFrequency) {
        this.strEventFullViewEventFrequency = strEventFullViewEventFrequency;
    }

    public String getStrEventFullViewEventStartDate() {
        return strEventFullViewEventStartDate;
    }

    public void setStrEventFullViewEventStartDate(String strEventFullViewEventStartDate) {
        this.strEventFullViewEventStartDate = strEventFullViewEventStartDate;
    }

    public String getStrEventFullViewEventStartTime() {
        return strEventFullViewEventStartTime;
    }

    public void setStrEventFullViewEventStartTime(String strEventFullViewEventStartTime) {
        this.strEventFullViewEventStartTime = strEventFullViewEventStartTime;
    }

    public String getStrEventFullViewEventEndDate() {
        return strEventFullViewEventEndDate;
    }

    public void setStrEventFullViewEventEndDate(String strEventFullViewEventEndDate) {
        this.strEventFullViewEventEndDate = strEventFullViewEventEndDate;
    }

    public String getStrEventFullViewEventEndTime() {
        return strEventFullViewEventEndTime;
    }

    public void setStrEventFullViewEventEndTime(String strEventFullViewEventEndTime) {
        this.strEventFullViewEventEndTime = strEventFullViewEventEndTime;
    }

    public int getIntEventFullViewGalleryImage() {
        return intEventFullViewGalleryImage;
    }

    public void setIntEventFullViewGalleryImage(int intEventFullViewGalleryImage) {
        this.intEventFullViewGalleryImage = intEventFullViewGalleryImage;
    }
}
