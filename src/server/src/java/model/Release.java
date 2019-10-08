package java.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Release implements Serializable {
    @Expose
    private String id;
    @Expose
    private String name;

    public Release(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
