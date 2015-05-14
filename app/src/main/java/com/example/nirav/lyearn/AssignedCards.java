package com.example.nirav.lyearn;

/**
 * Created by nirav on 4/27/15.
 */
public class AssignedCards {

    private String courseTitle;
    private String numberOfCards;
    private String estimatedTime;
    private String dueDateTime;
    private String courseAssigner;

    public AssignedCards(String courseTitle, String numberOfCards, String estimatedTime, String dueDateTime, String assigner) {
        this.courseTitle = courseTitle;
        this.numberOfCards = numberOfCards;
        this.estimatedTime = estimatedTime;
        this.dueDateTime = dueDateTime;
        this.courseAssigner = assigner;
     }



    public String getNumberOfCards() {
        return numberOfCards;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public String getDueDateTime() {
        return dueDateTime;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseAssigner() {
        return courseAssigner;
    }
}
