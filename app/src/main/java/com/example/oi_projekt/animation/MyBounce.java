package com.example.oi_projekt.animation;

public class MyBounce implements android.view.animation.Interpolator {
    private double amp;
    private double freq;

    public MyBounce(double amplitude, double frequency) {
        amp = amplitude;
        freq = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ amp) *
                Math.cos(freq * time) + 1);
    }
}