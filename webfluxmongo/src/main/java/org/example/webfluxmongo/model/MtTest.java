package org.example.webfluxmongo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class MtTest {

    private Integer id;
    private String name;

    public MtTest(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
