package cpe.lesbarbus.cozynotes.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arthurveys on 03/01/16.
 */
public class Notebook {

    private String _id;
    private String _rev;
    private String type = "notebook";
    private String name;

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


    /***
     * Get the Map format of a Note
     *
     * @return Map containing all the filled fields of the Note
     */
    public Map<String, Object> getMapFormat() {
        Map<String, Object> map = new HashMap<>();
        if (type != null) map.put("_type", type);
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
            sb.append(", _type='").append(type).append('\'');
        if (name != null)
            sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
