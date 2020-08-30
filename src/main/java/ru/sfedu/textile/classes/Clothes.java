package ru.sfedu.textile.classes;

import com.opencsv.bean.CsvBindByPosition;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Clothes")
public class Clothes extends TextileProduct {

  // Fields
  @Element(name = "size")
  @CsvBindByPosition(position = 5)
  private String size;


  // Methods
  public void setSize(String newVar) {
    size = newVar;
  }

  public String getSize() {
    return size;
  }

  @Override
  public String toString() {
        return super.toString() + ", Size:" + getSize();
    }

}
