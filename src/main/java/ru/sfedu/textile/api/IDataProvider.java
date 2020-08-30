package ru.sfedu.textile.api;

import ru.sfedu.textile.classes.Bedding;
import ru.sfedu.textile.classes.Clothes;
import ru.sfedu.textile.classes.Document;

import java.util.List;

public interface IDataProvider {

    // Methods for user

    void addProduct(int article, String name, int price, int quantity, String category, String size);
    void addProduct(int article, String name, int price, int quantity, String category, int length, int width, int height);

    Object getProductByArticle(int article);
    void deleteProductByArticle(int article);
    void changeProductByArticle(int article, String whatChange, String newValue);

    void showAllLists();
    void showProductsWithZeroBalance();
    void showListProductsByName(String name);

    void issueSaleWriteoffPosting(int product, int qty, String type);


    // Actions with data

    <T> List<T> getList(Class clazz);
    void save(Clothes bean);
    void save(Bedding bean);
    void save(Document bean);
    void increaseQty(int article, int qty);
    boolean decreaseQty(int article, int qty);

}
