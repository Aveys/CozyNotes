package cpe.lesbarbus.cozynotes.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Note {

    private String _id;
    private String _rev;
    private String title;
    private String content;
    private String media;
    private Date datetime;
    private Date endDatetime;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_rev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public void setCurrentDatetime(){
        this.datetime = new Date();
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    /***
     * Get the Map format of a Note
     * @return Map containing all the filled fields of the Note
     */
    public  Map<String, Object> getMapFormat(){
        Map<String, Object> map = new HashMap<>();
        if (title != null)          map.put("title", title);
        if (content != null)        map.put("content", content);
        if (datetime != null)       map.put("datetime", datetime);
        if (endDatetime != null)    map.put("endDatetime", endDatetime);

        return map;
    }
}
