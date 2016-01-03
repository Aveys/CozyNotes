package cpe.lesbarbus.cozynotes.models;

import java.util.List;

/**
 * Created by arthurveys on 03/01/16.
 */
public class Notebook {

    private String _id;
    private String _rev;
    private String name;
    private List<Note> notes;

    public Notebook() {
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
