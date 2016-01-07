package cpe.lesbarbus.cozynotes.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Notebook implements Serializable{

    private String _id;
    private String _rev;
    private String type = "notebook";
    private String name;

    public Notebook() {
    }

    public Notebook(String name) {
        this.name = name;
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

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    /***
     * Get the Map format of a Notebook
     *
     * @return Map containing all the filled fields of the Note
     */
    public Map<String, Object> getMapFormat() {
        Map<String, Object> map = new HashMap<>();
        if (type != null) map.put("type", type);
        if (name != null) map.put("name", name);
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
            sb.append(", type='").append(type).append('\'');
        if (name != null)
            sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
