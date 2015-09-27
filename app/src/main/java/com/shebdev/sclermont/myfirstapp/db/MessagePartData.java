package com.shebdev.sclermont.myfirstapp.db;

/**
 * Created by sclermont on 21/08/15.
 */
public class MessagePartData {

    private long _id;
    private String partId;
    private String text;
    private boolean isGreeting;
    private String audioFileName;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isGreeting() {
        return isGreeting;
    }

    public void setIsGreeting(boolean isGreeting) {
        this.isGreeting = isGreeting;
    }

    public String getAudioFileName() {
        return audioFileName;
    }

    public void setAudioFileName(String audioFileName) {
        this.audioFileName = audioFileName;
    }
}
