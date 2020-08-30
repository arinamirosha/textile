package ru.sfedu.textile.api;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.textile.classes.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static ru.sfedu.textile.ConfigUtil.getConfigurationEntry;
import static ru.sfedu.textile.Constants.*;

public class DataProviderCsv implements IDataProvider {

    private static Logger log = LogManager.getLogger(DataProviderCsv.class);

    @Override
    public void addProduct(int article, String name, int price, int quantity, String category, String size) {

        // If no such product
        if (getProductByArticle(article)==null) {

            // Create clothes bean
            Clothes cloth = new Clothes();
            cloth.setArticle(article);
            cloth.setName(name);
            cloth.setPrice(price);
            cloth.setQuantity(quantity);
            cloth.setCategory(category.toLowerCase());
            cloth.setSize(size);

            // Write to file
            save(cloth);
            log.debug(SUCCESS_ADD_CLOTHES);
        }
        else log.debug(NOT_ADDED);
    }

    @Override
    public void addProduct(int article, String name, int price, int quantity, String category, int length, int width, int height) {

        // If no such product
        if (getProductByArticle(article)==null) {

            // Create bedding bean
            Bedding bed = new Bedding();
            bed.setArticle(article);
            bed.setName(name);
            bed.setPrice(price);
            bed.setQuantity(quantity);
            bed.setCategory(category.toLowerCase());
            bed.setLength(length);
            bed.setWidth(width);
            bed.setHeight(height);

            // Write to file
            save(bed);
            log.debug(SUCCESS_ADD_BEDDING);
        }
        else log.debug(NOT_ADDED);
    }

    @Override
    public Object getProductByArticle(int article) {

        // Search in clothes
        List<Clothes> cloth = getList(Clothes.class);
        if (!cloth.isEmpty())
            if (cloth.stream().anyMatch(a -> a.getArticle() == article))
                return cloth.stream().filter(a -> a.getArticle() == article).findFirst().get();

        // Search in bedding
        List<Bedding> bed = getList(Bedding.class);
        if (!bed.isEmpty())
            if (bed.stream().anyMatch(a -> a.getArticle() == article))
                return bed.stream().filter(a -> a.getArticle() == article).findFirst().get();

        // If not found a product
        log.debug(NO_PRODUCT+article);
        return null;
    }

    @Override
    public void deleteProductByArticle(int article) {
        try {
            // If product exists
            if (getProductByArticle(article)!=null){
                boolean linkExists = getList(Document.class).stream().anyMatch(a -> ((Document) a).getProduct()==article);
                // If no records in documents with this product
                if (!linkExists) {

                    // Search in clothes
                    List<Clothes> cloth = getList(Clothes.class);
                    int size = cloth.size();
                    // Delete if product is found in clothes
                    cloth.removeIf(a -> a.getArticle() == article);
                    if (size!=cloth.size()){
                        String csv = getConfigurationEntry(KEY_CSV_CLO);
                        rewriteCsv(csv,cloth);
                        log.debug(SUCCESS_DEL_CLOTHES); return;
                    }

                    // Search in bedding
                    List<Bedding> bed = getList(Bedding.class);
                    // Delete if product is found in bedding
                    bed.removeIf(a -> a.getArticle() == article);
                    String csv = getConfigurationEntry(KEY_CSV_BED);
                    rewriteCsv(csv,bed);
                    log.debug(SUCCESS_DEL_BEDDING); return;

                } else log.debug(USED_IN_DOC);
            }
            log.debug(NOT_DELETED);
        } catch (IOException | NullPointerException e) { log.error(e); }
    }

    @Override
    public void changeProductByArticle(int article, String whatChange, String newValue) {

        // If product exists
        if (getProductByArticle(article)!=null) {
            try {
                boolean check = false;
                // Search in clothes
                List<Clothes> cloth = getList(Clothes.class);
                if (!cloth.isEmpty())
                    for (Clothes c : cloth)
                        // If found
                        if (c.getArticle() == article) {
                            switch (whatChange.toLowerCase()){
                                case NAME: c.setName(newValue); break;
                                case PRICE: c.setPrice(Integer.parseInt(newValue)); break;
                                case QUANTITY: c.setQuantity(Integer.parseInt(newValue)); break;
                            }
                            String csv = getConfigurationEntry(KEY_CSV_CLO);
                            rewriteCsv(csv,cloth);
                            check=true; break;
                        }
                if (!check){
                    // Search in bedding
                    List<Bedding> bed = getList(Bedding.class);
                    if (!bed.isEmpty())
                        for (Bedding b : bed)
                            // If found
                            if (b.getArticle() == article) {
                                switch (whatChange.toLowerCase()){
                                    case NAME: b.setName(newValue); break;
                                    case PRICE: b.setPrice(Integer.parseInt(newValue)); break;
                                    case QUANTITY: b.setQuantity(Integer.parseInt(newValue)); break;
                                }
                                String csv = getConfigurationEntry(KEY_CSV_BED);
                                rewriteCsv(csv,bed);
                                break;
                            }
                }
            } catch (IOException | NullPointerException e) { log.error(e); }
            log.debug(PROD_ARTICLE+article+UPDATED+whatChange.toUpperCase()+CHANGED);
        }
        else log.debug(NOT_CHANGED);
    }

    @Override
    public void showAllLists() {
        try {
            // Get clothes records
            String str = LIST_CLOTHES;
            List list = getList(Clothes.class);
            if (!list.isEmpty()) for (Object o : list) str+="\n"+o;
            else str+=NO_DATA;

            // Get bedding records
            str += LIST_BEDDING;
            list = getList(Bedding.class);
            if (!list.isEmpty()) for (Object o : list) str+="\n"+o;
            else str+=NO_DATA;

            // Get documents records
            str += LIST_DOCUMENTS;
            list = getList(Document.class);
            if (!list.isEmpty())
                for (Object o : list) {
                    TextileProduct product = (TextileProduct) getProductByArticle(((Document) o).getProduct());
                    str+="\n"+"Id:"+((Document) o).getId()+", Product:"+((Document) o).getProduct()+", Name:"+product.getName()+", Price:"+product.getPrice()+
                            ", Qty:"+((Document) o).getQty()+", Date:"+((Document) o).getDate()+", Type:"+((Document) o).getType();
                }
            else str+=NO_DATA;

            // Show data
            log.debug(str+"\n");
        } catch (NullPointerException e) {log.error(e);}

    }

    @Override
    public void showProductsWithZeroBalance() {
        List list = new ArrayList();

        // Search in clothes with zero balance
        List<Clothes> cloth = getList(Clothes.class);
        if (!cloth.isEmpty()){
            cloth.removeIf(a -> a.getQuantity() != 0);
            list.addAll(cloth);
        }
        // Search in bedding with zero balance
        List<Bedding> bed = getList(Bedding.class);
        if (!bed.isEmpty()){
            bed.removeIf(a -> a.getQuantity() != 0);
            list.addAll(bed);
        }

        // Show list of products with zero balance
        if (!list.isEmpty()) for (Object o : list) { log.debug(o); }
        else log.debug(NO_ZERO);
    }

    @Override
    public void showListProductsByName(String name) {
        List list = new ArrayList();

        // Search in clothes
        List<Clothes> cloth = getList(Clothes.class);
        if (!cloth.isEmpty())
            // Remove if product do not match
            cloth.removeIf(a -> !a.getName().toLowerCase().matches(".*"+name.toLowerCase()+".*"));
        list.addAll(cloth);

        // Search in bedding
        List<Bedding> bed = getList(Bedding.class);
        if (!bed.isEmpty())
            // Remove if product do not match
            bed.removeIf(a -> !a.getName().toLowerCase().matches(".*"+name.toLowerCase()+".*"));
        list.addAll(bed);

        // Show list with found products
        if (!list.isEmpty()) for (Object o : list) { log.debug(o); }
        else log.debug(NO_PRODUCT_NAME);
    }

    @Override
    public void issueSaleWriteoffPosting(int product, int qty, String type) {

        // Create id
        int id;
        List<Document> list = getList(Document.class);
        if (list.isEmpty())id=1;
        else id = list.get(list.size()-1).getId()+1;

        // Create current date
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat(PATTERN_DATE);
        String date = formatForDateNow.format(dateNow);

        // If product exists
        if (getProductByArticle(product)!=null) {

            // Create document bean
            Document doc = new Document();
            doc.setId(id);
            doc.setProduct(product);
            doc.setQty(qty);
            doc.setDate(date);
            doc.setType(type.toLowerCase());

            // Increase or decrease quantity of product depending on the type of document
            if (type.toLowerCase().equals(TypeOfDoc.posting.toString())) increaseQty(product, qty);
            else if (!decreaseQty(product, qty)) { log.debug(NOT_ENOUGH_QTY); return; }

            // Write to file
            save(doc);
            log.debug(SUCCESS_ADD_DOCUMENT);
        }
        else log.debug(NOT_ISSUED);
    }


    // Actions with data

    @Override
    public <T> List<T> getList(Class clazz){
        try {
            String csv = "";
            switch (clazz.getSimpleName()){
                case CLOTHES: csv = getConfigurationEntry(KEY_CSV_CLO); break;
                case BEDDING: csv = getConfigurationEntry(KEY_CSV_BED); break;
                case DOCUMENT: csv = getConfigurationEntry(KEY_CSV_DOC); break;
            }
            File file = new File(csv);
            if (file.length() != 0) return (List<T>) new CsvToBeanBuilder(new FileReader(csv)).withType(clazz).build().parse();
            else return Collections.emptyList();

        } catch (IOException | NullPointerException e) { log.error(e); return Collections.emptyList();}
    }

    @Override
    public void save(Clothes bean){
        try {
            String csv = getConfigurationEntry(KEY_CSV_CLO);
            CSVWriter writer = new CSVWriter(new FileWriter(csv, true));
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(bean);
            writer.close();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | NullPointerException e) { log.error(e); }
    }

    @Override
    public void save(Bedding bean){
        try {
            String csv = getConfigurationEntry(KEY_CSV_BED);
            CSVWriter writer = new CSVWriter(new FileWriter(csv, true));
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(bean);
            writer.close();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | NullPointerException e) { log.error(e); }
    }

    @Override
    public void save(Document bean){
        try {
            String csv = getConfigurationEntry(KEY_CSV_DOC);
            CSVWriter writer = new CSVWriter(new FileWriter(csv, true));
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(bean);
            writer.close();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | NullPointerException e) { log.error(e); }
    }

    @Override
    public void increaseQty(int article, int qty){
        try {
            // Search in clothes
            String csv = getConfigurationEntry(KEY_CSV_CLO);
            List<Clothes> cloth = getList(Clothes.class);
            if (!cloth.isEmpty())
                for (Clothes c : cloth)
                    if (c.getArticle() == article) {
                        // Increasing
                        c.setQuantity(c.getQuantity()+qty);
                        rewriteCsv(csv,cloth);
                        return;
                    }
            // Search in bedding
            csv = getConfigurationEntry(KEY_CSV_BED);
            List<Bedding> bed = getList(Bedding.class);
            if (!bed.isEmpty())
                for (Bedding b : bed)
                    if (b.getArticle() == article) {
                        // Increasing
                        b.setQuantity(b.getQuantity()+qty);
                        rewriteCsv(csv,bed);
                        return;
                    }
        } catch (IOException | NullPointerException e) { log.error(e); }
    }

    @Override
    public boolean decreaseQty(int article, int qty){
        try {
            // Search in clothes
            String csv = getConfigurationEntry(KEY_CSV_CLO);
            List<Clothes> cloth = getList(Clothes.class);
            if (!cloth.isEmpty())
                for (Clothes clothes : cloth)
                    if (clothes.getArticle() == article) {
                        if (clothes.getQuantity()-qty >= 0) {
                            // Decreasing
                            clothes.setQuantity(clothes.getQuantity()-qty);
                            rewriteCsv(csv,cloth);
                            return true;
                        }
                        else return false;
                    }
            // Search in bedding
            csv = getConfigurationEntry(KEY_CSV_BED);
            List<Bedding> bed = getList(Bedding.class);
            if (!bed.isEmpty())
                for (Bedding bedding : bed)
                    if (bedding.getArticle() == article) {
                        if (bedding.getQuantity()-qty >= 0) {
                            // Decreasing
                            bedding.setQuantity(bedding.getQuantity()-qty);
                            rewriteCsv(csv,bed);
                            return true;
                        }
                        else return false;
                    }
        } catch (IOException | NullPointerException e) { log.error(e); }
        return false;
    }


    // Private methods

    private <T> void rewriteCsv(String csv, List<T> list){
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(csv, false));
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(list);
            writer.close();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | NullPointerException e) { log.error(e); }

    }

}
