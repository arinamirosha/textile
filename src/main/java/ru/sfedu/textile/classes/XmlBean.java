package ru.sfedu.textile.classes;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "beans")
public class XmlBean<T> {

    // Fields
    @ElementList(inline = true, required = false)
    private List<T> list;


    // Methods
    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
