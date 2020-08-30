package ru.sfedu.textile.classes;

import com.opencsv.bean.CsvBindByPosition;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "TextileProduct")
public class TextileProduct {

  // Fields
  @Attribute(name = "article")
  @CsvBindByPosition(position = 0)
  protected int article;
  @Element(name = "name")
  @CsvBindByPosition(position = 1)
  protected String name;
  @Element(name = "price")
  @CsvBindByPosition(position = 2)
  protected int price;
  @Element(name = "quantity")
  @CsvBindByPosition(position = 3)
  protected int quantity;
  @Element(name = "category")
  @CsvBindByPosition(position = 4)
  protected String category;


  // Methods
  public void setArticle(int newVar) {
    article = newVar;
  }

  public int getArticle() {
    return article;
  }

  public void setName(String newVar) {
    name = newVar;
  }

  public String getName() { return name; }

  public void setPrice(int newVar) {
    price = newVar;
  }

  public int getPrice() {
    return price;
  }

  public void setQuantity(int newVar) {
    quantity = newVar;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setCategory(String newVar) {
    category = newVar;
  }

  public String getCategory() {
    return category;
  }

  public String toString() {
    return "Article:" + getArticle() +
            ", Name:" + getName() +
            ", Price:" + getPrice() +
            ", Quantity:" + getQuantity() +
            ", Category:" + getCategory();
  }
}
