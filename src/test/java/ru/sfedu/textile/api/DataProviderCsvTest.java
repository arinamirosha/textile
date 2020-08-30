package ru.sfedu.textile.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Random;

import static ru.sfedu.textile.Constants.*;

public class DataProviderCsvTest {

    private static Logger log = LogManager.getLogger(DataProviderCsvTest.class);
    private static IDataProvider iDataProvider = new DataProviderCsv();

    private static int article, price, quantity, length, width, height, product, qty;
    private static String name, category, size, type, whatChange, newValue;

    private static void setRandomProduct(){
        int articleArr[] = {1234,2345,3456,4567,5678,6789,7890,8901};
        String nameArr[] = {"SoftPillow","WarmBlanket","FloweryBathrobe","WinterTights","OrthopedicMattress","WhiteSocks","RedUnderpants","RosePillowcase"};
        int priceArr[] = {600,1500,1200,150,2300,60,200,100};
        int quantityArr[] = {8,3,10,30,2,14,22,10};
        String categoryArr[] = {"pillow","blanket","bathrobe","tights","mattress","socks","underpants","pillowCase"};
        String sizeArr[] = {"44","S","38-40","M-L","36","37-39","L","noSize"};
        int lengthArr[] = {60,220,90,110,200,50,60,80};
        int widthArr[] = {40,60,70,80,90,30,40,50};
        int heightArr[] = {0,10,0,15,30,5,20,0};

        Random random = new Random();
        int num = random.nextInt(8);
        article = articleArr[num];
        name = nameArr[num];
        price = priceArr[num];
        quantity = quantityArr[num];
        category = categoryArr[num];
        size = sizeArr[num];
        length = lengthArr[num];
        width = widthArr[num];
        height = heightArr[num];
    }

    private static void setRandomDocument(){
        int productArr[] = {1234,2345,3456,4567,5678,6789,7890,8901};
        int qtyArr[] = {3,1,2,6,3,4,1,5};
        String typeArr[] = {"posting","sale","writeoff","posting","posting","sale","sale","writeoff"};

        Random random = new Random();
        int num = random.nextInt(8);
        product = productArr[num];
        qty = qtyArr[num];
        type = typeArr[num];
    }

    private static void setRandomArticle(){
        int articleArr[] = {1234,2345,3456,4567,5678,6789,7890,8901};
        Random random = new Random();
        int num = random.nextInt(8);
        article = articleArr[num];
    }

    private static void setRandomChange(){
        int articleArr[] = {1234,2345,3456,4567,5678,6789,7890,8901};
        String whatChangeArr[] = {PRICE,QUANTITY,NAME,QUANTITY,QUANTITY,PRICE,PRICE,NAME};
        String newValueArr[] = {"500","17","RedWhiteBlanket","11","13","150","1700","RoseStripesPillowcase"};

        Random random = new Random();
        int num = random.nextInt(8);
        article = articleArr[num];
        whatChange = whatChangeArr[num];
        newValue = newValueArr[num];
    }

    private static void setRandomName(){
        String nameArr[] = {"pillow","blanket","tights","Orthopedic","White","RosePillowcase"};
        Random random = new Random();
        int num = random.nextInt(6);
        name = nameArr[num];
    }


    // TESTS

    @Test
    public void addProduct() {
        setRandomProduct();
        iDataProvider.addProduct(article,name, price, quantity, category, size);
        setRandomProduct();
        iDataProvider.addProduct(article,name, price, quantity, category, length, width, height);
    }

    @Test
    public void getProductByArticle() {
        setRandomArticle();
        log.debug(iDataProvider.getProductByArticle(article));
    }

    @Test
    public void deleteProductByArticle() {
        setRandomArticle();
        iDataProvider.deleteProductByArticle(article);
    }

    @Test
    public void changeProductByArticle() {
        setRandomChange();
        iDataProvider.changeProductByArticle(article,whatChange,newValue);
    }

    @Test
    public void showAllLists() {
        iDataProvider.showAllLists();
    }

    @Test
    public void showProductsWithZeroBalance() {
        iDataProvider.showProductsWithZeroBalance();
    }

    @Test
    public void showListProductsByName() {
        setRandomName();
        iDataProvider.showListProductsByName(name);
    }

    @Test
    public void issueSaleWriteoffPosting() {
        setRandomDocument();
        iDataProvider.issueSaleWriteoffPosting(product,qty, type);
    }

}