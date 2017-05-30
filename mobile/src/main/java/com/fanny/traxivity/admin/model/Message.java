package com.fanny.traxivity.admin.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fanny.traxivity.admin.view.activities.MessagesManager;


/**
 * Created by Alexandre on 18/04/2017.
 */

public class Message implements Parcelable{

    String content;

    protected Quote quote;

    //protected MessagesManager.BCTCategory bctCategory;
    protected String strBctCategory;

    protected MessagesManager.Achievement achievement;

    public Quote getQuote() {
        return quote;
    }

    public String getContent() {
        return content;
    }

    public String getStrBctCategory() {
        return strBctCategory;
    }

    public MessagesManager.Achievement getAchievement() {
        return achievement;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public void setStrBctCategory(String strBctCategory) {
        this.strBctCategory = strBctCategory;
    }

    public void setAchievement(MessagesManager.Achievement achievement) {
        this.achievement = achievement;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeParcelable(quote, flags);
        dest.writeString(strBctCategory);
        dest.writeString(achievement.name());
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    // "De-parcel object
    public Message(Parcel in) {
        content = in.readString();
        quote = in.readParcelable(Quote.class.getClassLoader());
        strBctCategory = in.readString();
        achievement = MessagesManager.Achievement.valueOf(in.readString());
    }

    public Message(){
        quote = null;
        content = "";
        strBctCategory = "";
        achievement = MessagesManager.Achievement.Low;
    }

    public Message(Message message){
        this.content = message.getContent();
        if(message.getQuote() != null)
            this.quote = new Quote(message.getQuote().getAuthor(), message.getQuote().getContent());
        this.achievement = message.getAchievement();
        this.strBctCategory = message.getStrBctCategory();
    }

    public Message(String content, Quote quote, String strBctCategory, MessagesManager.Achievement achievement) {
        this.content = content;
        this.quote = quote;
        this.strBctCategory = strBctCategory;
        this.achievement = achievement;
    }
}

