package ru.sfedu.textile.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.textile.classes.*;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static ru.sfedu.textile.ConfigUtil.getConfigurationEntry;
import static ru.sfedu.textile.Constants.*;

public class DataProviderJdbc implements IDataProvider {

    private static Logger log = LogManager.getLogger(DataProviderJdbc.class);

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

            // Write to database
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

            // Write to database
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

        // If product exists
        if (getProductByArticle(article)!=null){
            boolean linkExists = getList(Document.class).stream().anyMatch(a -> ((Document) a).getProduct()==article);
            // If no records in documents with this product
            if (!linkExists) {
                try {
                    // Database connection
                    String url = getConfigurationEntry(KEY_DB_URL);
                    String user = getConfigurationEntry(KEY_DB_USER);
                    String password = getConfigurationEntry(KEY_DB_PSWD);

                    try (Connection con = DriverManager.getConnection(url, user, password);
                         Statement st = con.createStatement()) {

                        // Delete product
                        String sql = DEL_CLO_W_ARTICLE+article;
                        st.executeUpdate(sql);
                        sql = DEL_BED_W_ARTICLE+article;
                        st.executeUpdate(sql);
                        log.debug(SUCCESS_DEL_DB); return;

                    } catch (SQLException e) { log.error(e); }
                } catch (IOException e) { log.error(e); }

            } else log.debug(USED_IN_DOC);
        }
        log.debug(NOT_DELETED);
    }

    @Override
    public void changeProductByArticle(int article, String whatChange, String newValue) {

        // If product exists
        if (getProductByArticle(article)!=null) {
            try {
                // Database connection
                String url = getConfigurationEntry(KEY_DB_URL);
                String user = getConfigurationEntry(KEY_DB_USER);
                String password = getConfigurationEntry(KEY_DB_PSWD);

                try (Connection con = DriverManager.getConnection(url, user, password);
                     Statement st = con.createStatement()) {

                    // Change product
                    if (whatChange.toLowerCase().equals(NAME)) {
                        String sql = UPD_CLO_SET+NAME_EQV+newValue+WHERE_ARTICLE+article;
                        st.executeUpdate(sql);
                        sql = UPD_BED_SET+NAME_EQV+newValue+WHERE_ARTICLE+article;
                        st.executeUpdate(sql);
                    }
                    else {
                        String sql = UPD_CLO_SET+whatChange.toLowerCase()+"="+newValue+WHERE_ARTICLE+article;
                        st.executeUpdate(sql);
                        sql = UPD_BED_SET+whatChange.toLowerCase()+"="+newValue+WHERE_ARTICLE+article;
                        st.executeUpdate(sql);
                    }
                } catch (SQLException e) { log.error(e); }
            } catch (IOException e) { log.error(e); }
            log.debug(PROD_ARTICLE+article+UPDATED+whatChange.toUpperCase()+CHANGED);
        }
        else log.debug(NOT_CHANGED);
    }

    @Override
    public void showAllLists() {
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
            if (TypeOfDoc.valueOf(type.toLowerCase()).equals(TypeOfDoc.posting)) increaseQty(product, qty);
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
            String url = getConfigurationEntry(KEY_DB_URL);
            String user = getConfigurationEntry(KEY_DB_USER);
            String password = getConfigurationEntry(KEY_DB_PSWD);
            String query = "SELECT * FROM "+clazz.getSimpleName().toLowerCase();
            boolean check = false;

            try (Connection con = DriverManager.getConnection(url, user, password);
                 PreparedStatement pst = con.prepareStatement(query);
                 ResultSet rs = pst.executeQuery()) {
                switch (clazz.getSimpleName()){
                    case CLOTHES:
                        List<Clothes> clo = new ArrayList<>();
                        while (rs.next()) {
                            Clothes clothes = new Clothes();
                            clothes.setArticle(rs.getInt(1));
                            clothes.setName(rs.getString(2));
                            clothes.setPrice(rs.getInt(3));
                            clothes.setQuantity(rs.getInt(4));
                            clothes.setCategory(rs.getString(5));
                            clothes.setSize(rs.getString(6));
                            clo.add(clothes);
                            check = true;
                        }
                        if (check) return (List<T>) clo; break;

                    case BEDDING:
                        List<Bedding> bed = new ArrayList<>();
                        while (rs.next()) {
                            Bedding bedding = new Bedding();
                            bedding.setArticle(rs.getInt(1));
                            bedding.setName(rs.getString(2));
                            bedding.setPrice(rs.getInt(3));
                            bedding.setQuantity(rs.getInt(4));
                            bedding.setCategory(rs.getString(5));
                            bedding.setLength(rs.getInt(6));
                            bedding.setWidth(rs.getInt(7));
                            bedding.setHeight(rs.getInt(8));
                            bed.add(bedding);
                            check = true;
                        }
                        if (check) return (List<T>) bed; break;

                    case DOCUMENT:
                        List<Document> doc = new ArrayList<>();
                        while (rs.next()) {
                            Document document = new Document();
                            document.setId(rs.getInt(1));
                            document.setProduct(rs.getInt(2));
                            document.setQty(rs.getInt(3));
                            document.setDate(rs.getString(4));
                            document.setType(rs.getString(5));
                            doc.add(document);
                            check = true;
                        }
                        if (check) return (List<T>) doc; break;
                }
                return Collections.emptyList();
            } catch (SQLException e){ log.error(e); return Collections.emptyList();}
        } catch (IOException e) { log.error(e); return Collections.emptyList();}
    }

    @Override
    public void save(Clothes bean){
        try {
            String url = getConfigurationEntry(KEY_DB_URL);
            String user = getConfigurationEntry(KEY_DB_USER);
            String password = getConfigurationEntry(KEY_DB_PSWD);

            try (Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement()) {
                String sql = "INSERT INTO clothes VALUES(";
                sql += bean.getArticle() + ",\"";
                sql += bean.getName() + "\",";
                sql += bean.getPrice() + ",";
                sql += bean.getQuantity() + ",\"";
                sql += bean.getCategory() + "\",\"";
                sql += bean.getSize() + "\")";
                st.executeUpdate(sql);
            } catch (SQLException e) { log.error(e); }
        } catch (IOException e) { log.error(e); }
    }

    @Override
    public void save(Bedding bean){
        try {
            String url = getConfigurationEntry(KEY_DB_URL);
            String user = getConfigurationEntry(KEY_DB_USER);
            String password = getConfigurationEntry(KEY_DB_PSWD);

            try (Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement()) {
                String sql = "INSERT INTO bedding VALUES(";
                sql += bean.getArticle() + ",\"";
                sql += bean.getName() + "\",";
                sql += bean.getPrice() + ",";
                sql += bean.getQuantity() + ",\"";
                sql += bean.getCategory() + "\",";
                sql += bean.getLength() + ",";
                sql += bean.getWidth() + ",";
                sql += bean.getHeight() + ")";
                st.executeUpdate(sql);
            } catch (SQLException e) { log.error(e); }
        } catch (IOException e) { log.error(e); }
    }

    @Override
    public void save(Document bean){
        try {
            String url = getConfigurationEntry(KEY_DB_URL);
            String user = getConfigurationEntry(KEY_DB_USER);
            String password = getConfigurationEntry(KEY_DB_PSWD);

            try (Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement()) {
                String sql = "INSERT INTO document VALUES(";
                sql += bean.getId() + ",";
                sql += bean.getProduct() + ",";
                sql += bean.getQty() + ",\"";
                sql += bean.getDate() + "\",\"";
                sql += bean.getType() + "\")";
                st.executeUpdate(sql);
            } catch (SQLException e) { log.error(e); }
        } catch (IOException e) { log.error(e); }
    }

    @Override
    public void increaseQty(int article, int qty){
        try {
            String url = getConfigurationEntry(KEY_DB_URL);
            String user = getConfigurationEntry(KEY_DB_USER);
            String password = getConfigurationEntry(KEY_DB_PSWD);
            String query = "SELECT quantity+"+qty+" FROM clothes WHERE article="+article;
            query+=" UNION SELECT quantity+"+qty+" FROM bedding WHERE article="+article;

            try (Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(query)) {
                if (rs.next())  {
                    int newQty=rs.getInt(1);
                    String sql = "UPDATE clothes SET quantity="+newQty+" WHERE article="+article;
                    st.executeUpdate(sql);
                    sql = "UPDATE bedding SET quantity="+newQty+" WHERE article="+article;
                    st.executeUpdate(sql);
                }
            } catch (SQLException e) { log.error(e); }
        } catch (IOException e) { log.error(e); }
    }

    @Override
    public boolean decreaseQty(int article, int qty){
        try {
            String url = getConfigurationEntry(KEY_DB_URL);
            String user = getConfigurationEntry(KEY_DB_USER);
            String password = getConfigurationEntry(KEY_DB_PSWD);
            String query = "SELECT quantity-"+qty+" FROM clothes WHERE article="+article;
            query+=" UNION SELECT quantity-"+qty+" FROM bedding WHERE article="+article;

            try (Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(query)) {
                if (rs.next())  {
                    int newQty=rs.getInt(1);
                    if (newQty>=0){
                        String sql = "UPDATE clothes SET quantity="+newQty+" WHERE article="+article;
                        st.executeUpdate(sql);
                        sql = "UPDATE bedding SET quantity="+newQty+" WHERE article="+article;
                        st.executeUpdate(sql);
                        return true;
                    }
                }
            } catch (SQLException e) { log.error(e); }
        } catch (IOException e) { log.error(e); }
        return false;
    }

}
