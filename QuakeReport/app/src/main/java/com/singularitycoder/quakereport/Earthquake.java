package com.singularitycoder.quakereport;

public class Earthquake {

    public Earthquake(String magnitude, String location, long timeInMilliseconds) {
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = timeInMilliseconds;
    }

    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }
}
