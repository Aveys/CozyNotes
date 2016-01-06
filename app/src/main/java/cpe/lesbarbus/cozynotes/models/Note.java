package cpe.lesbarbus.cozynotes.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;


public class Note implements Serializable{

    private String _id;
    private String _rev;
    private String type = "note";
    private String notebookId;
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

    public String getnotebookId() {
        return notebookId;
    }

    public void setNotebookId(String notebookId) {
        this.notebookId = notebookId;
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

    public void setCurrentDatetime() {
        this.datetime = new Date();
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public String getNotebookId() {
        return notebookId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormattedDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(this.getDatetime());
    }

    /***
     * Get the Map format of a Note
     *
     * @return Map containing all the filled fields of the Note
     */
    public Map<String, Object> getMapFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Map<String, Object> map = new HashMap<>();
        if (type != null) map.put("type", type);
        if (notebookId != null) map.put("notebookId", notebookId);
        if (title != null) map.put("title", title);
        if (content != null) map.put("content", content);
        if (datetime != null) map.put("datetime", sdf.format(datetime));
        if (endDatetime != null) map.put("endDatetime", endDatetime);

        return map;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Note{");
        if (_id != null)
            sb.append("_id='").append(_id).append('\'');
        if (_rev != null)
            sb.append(", _rev='").append(_rev).append('\'');
        if (type != null)
            sb.append(", _type='").append(type).append('\'');
        if (notebookId != null)
            sb.append(", _notebookId='").append(notebookId).append('\'');
        if (title != null)
            sb.append(", title='").append(title).append('\'');
        if (content != null)
            sb.append(", content='").append(content).append('\'');
        if (media != null)
            sb.append(", media='").append(media).append('\'');
        if (datetime != null)
            sb.append(", datetime=").append(datetime);
        if (endDatetime != null)
            sb.append(", endDatetime=").append(endDatetime);
        sb.append('}');
        return sb.toString();
    }
}
