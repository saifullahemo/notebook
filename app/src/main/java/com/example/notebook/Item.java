package com.example.notebook;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Item {
    private String title;
    private String text;

    private Date dueDate;
    private Status status;
    private String dateColor;
    private String categorie;
    public enum Status {TODO, DONE}


    public Item(String title, String text, Date dueDate)
    {
        this.title = title;
        this.text = text;
        this.dueDate = dueDate;
        this.status = Status.TODO;
        this.dateColor = "#AFAFAF";
        this.categorie = "none";

    }

    public void setDateColor(String c)
    {
        this.dateColor = c;
    }


    public String getDateColor()
    {
        return this.dateColor;
    }


    public String getTitle()
    {
        return (this.title);
    }


    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getText()
    {
        return (this.text);
    }


    public void setText(String text)
    {
        this.text = text;
    }

    public String getDueDate()
    {
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy\nHH:mm");
        String MySDate = "due till " + newDateFormat.format(this.dueDate);
        return MySDate;
    }

    public String getDate()
    {
        SimpleDateFormat newDateFormat = new SimpleDateFormat("EE d MMM yyyy");
        String MySDate = newDateFormat.format(this.dueDate);
        return MySDate;
    }


    public String getTime()
    {
        SimpleDateFormat newDateFormat = new SimpleDateFormat("HH:mm");
        String MySDate = newDateFormat.format(this.dueDate);
        return MySDate;
    }


    public String getMonth()
    {
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM");
        String MySDate = newDateFormat.format(this.dueDate);
        return MySDate;
    }


    public String getYear()
    {
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy");
        String MySDate = newDateFormat.format(this.dueDate);
        return MySDate;
    }


    public Date getRealDate()
    {
        return this.dueDate;
    }


    public void setDueDate(Date dueDate)
    {
        this.dueDate = dueDate;
    }


    public Status getStatus()
    {
        return (this.status);
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }


    public void setCategorie(String cat)
    {
        this.categorie = cat;
    }


    public String getCategorie()
    {
        return this.categorie;
    }
}