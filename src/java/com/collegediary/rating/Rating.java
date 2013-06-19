package com.collegediary.rating;

import java.io.Serializable;

/**
 * поля класса содержат информаци об успевамости учащегося
 * @author Vinceal
 */
public class Rating implements Serializable{
    
    public String  Lesson_Type, Rating, coupleDate;

    public String getLesson_Type() {
        return Lesson_Type;
    }

    public void setLesson_Type(String Lesson_Type) {
        this.Lesson_Type = Lesson_Type;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String Rating) {
        this.Rating = Rating;
    }

    public String getCoupleDate() {
        return coupleDate;
    }

    public void setCoupleDate(String coupleDate) {
        this.coupleDate = coupleDate;
    }

    public Rating(String Lesson_Type, String Rating, String coupleDate) {
        this.Lesson_Type = Lesson_Type;
        this.Rating = Rating;
        this.coupleDate = coupleDate;
    }
    
}
