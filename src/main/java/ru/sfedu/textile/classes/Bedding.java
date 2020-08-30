package ru.sfedu.textile.classes;

import com.opencsv.bean.CsvBindByPosition;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Bedding")
public class Bedding extends TextileProduct {

  // Fields
  @Element(name = "length")
  @CsvBindByPosition(position = 5)
  private int length;
  @Element(name = "width")
  @CsvBindByPosition(position = 6)
  private int width;
  @Element(name = "height")
  @CsvBindByPosition(position = 7)
  private int height;


  // Methods
  public void setLength(int newVar) {
    length = newVar;
  }

  public int getLength() {
    return length;
  }

  public void setWidth(int newVar) {
    width = newVar;
  }

  public int getWidth() {
    return width;
  }

  public void setHeight(int newVar) {
    height = newVar;
  }

  public int getHeight() {
    return height;
  }

  @Override
  public String toString() {
      return super.toString() +
              ", Length:" + getLength() +
              ", Width:" + getWidth() +
              ", Height:" + getHeight();
  }

}
