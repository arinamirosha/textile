package ru.sfedu.textile.classes;

import com.opencsv.bean.CsvBindByPosition;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Document")
public class Document {

  // Fields
  @Attribute(name = "id")
  @CsvBindByPosition(position = 0)
  private int id;
  @Element(name = "product")
  @CsvBindByPosition(position = 1)
  private int product;
  @Element(name = "qty")
  @CsvBindByPosition(position = 2)
  private int qty;
  @Element(name = "date")
  @CsvBindByPosition(position = 3)
  private String date;
  @Element(name = "type")
  @CsvBindByPosition(position = 4)
  private String type;


  // Methods
  public void setId(int newVar) {
    id = newVar;
  }

  public int getId() {
    return id;
  }

  public void setProduct(int newVar) {
    product = newVar;
  }

  public int getProduct() {
    return product;
  }

  public void setQty(int newVar) {
    qty = newVar;
  }

  public int getQty() {
    return qty;
  }

  public void setDate(String newVar) {
    date = newVar;
  }

  public String getDate() {
    return date;
  }

  public void setType(String newVar) {
    type = newVar;
  }

  public String getType() {
    return type;
  }

  public String toString() {
    return "Id:" + getId() +
            ", Product:" + getProduct() +
            ", Qty:" + getQty() +
            ", Date:" + getDate() +
            ", Type:" + getType();
  }

}
