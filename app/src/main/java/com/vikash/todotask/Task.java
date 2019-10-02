package com.vikash.todotask;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Task {



    String title;
    boolean completed;
    int completionRate;
    ArrayList<SubTask> subTasks;

    public Task(String title, boolean completed, int completionRate) {
        this.title = title;
        this.completed = completed;
        this.completionRate = completionRate;
    }

    public String getTitle() {
        return title;

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(int completionRate) {
        this.completionRate = completionRate;
    }





    public static class SubTask {
        String startDate;
        String EndDate;
        String subtaskTitle;
        boolean completed;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return EndDate;
        }

        public void setEndDate(String endDate) {
            EndDate = endDate;
        }

        public String getTaskTitle() {
            return subtaskTitle;
        }

        public void setTaskTitle(String taskTitle) {
            this.subtaskTitle = taskTitle;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

       //



        public SubTask(String startDate, String endDate, String taskTitle, boolean completed) {
            this.startDate = startDate;
            EndDate = endDate;
            this.subtaskTitle = taskTitle;
            this.completed = completed;
        }
    }
}
