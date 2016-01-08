package cpe.lesbarbus.cozynotes.synctasks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cpe.lesbarbus.cozynotes.models.Note;
import cpe.lesbarbus.cozynotes.models.Notebook;

/**
 * Created by arthurveys on 07/01/16.
 */
public class DBHolder implements Serializable{
    private List<Note> listNotes;
    private List<Notebook> listNotebook;

    public DBHolder(List<Note> listNotes, List<Notebook> listNotebook) {
        this.listNotes = listNotes;
        this.listNotebook = listNotebook;
    }

    public List<Note> getListNotes() {
        return listNotes;
    }

    public void setListNotes(ArrayList<Note> listNotes) {
        this.listNotes = listNotes;
    }

    public List<Notebook> getListNotebook() {
        return listNotebook;
    }

    public void setListNotebook(ArrayList<Notebook> listNotebook) {
        this.listNotebook = listNotebook;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DBHolder{");
        sb.append("listNotes=").append(listNotes);
        sb.append(", listNotebook=").append(listNotebook);
        sb.append('}');
        return sb.toString();
    }
}
