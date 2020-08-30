package ru.sfedu.textile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.textile.api.DataProviderCsv;
import ru.sfedu.textile.api.DataProviderJdbc;
import ru.sfedu.textile.api.DataProviderXml;
import ru.sfedu.textile.api.IDataProvider;
import ru.sfedu.textile.classes.Category;
import ru.sfedu.textile.classes.TypeOfDoc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static ru.sfedu.textile.Constants.*;

public class Main {
    private static IDataProvider dataProvider;
    private static Logger log = LogManager.getLogger(Main.class);
//    private static Logger log;
//    static {
//        if (Files.exists(Paths.get(CONFIG_LOG_JAR)))
//            System.setProperty(CONFIG_LOG_KEY,CONFIG_LOG_JAR);
//        else System.setProperty(CONFIG_LOG_KEY,CONFIG_LOG_PATH);
//        log = LogManager.getLogger(Main.class);
//    }

    public static void main(String[] args) {

        if (args.length==0) { log.info(EMPTY_COMMAND); return; }
        if (args.length<2) { log.info(WRONG_QTY_PARAMS); return; }

        switch (args[0].toUpperCase()){
            case CSV: dataProvider = new DataProviderCsv(); break;
            case XML: dataProvider = new DataProviderXml(); break;
            case DB: dataProvider = new DataProviderJdbc(); break;
            default: log.info(WRONG_DP); return;
        }

        switch (args[1].toUpperCase()){
            case ADD:
                switch (args.length){
                    case 8:if (checkClothes(args[2],args[3],args[4],args[5],args[6],args[7]))
                        dataProvider.addProduct(Integer.parseInt(args[2]),args[3],Integer.parseInt(args[4]),Integer.parseInt(args[5]),args[6],args[7]); break;
                    case 10: if (checkBedding(args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9]))
                        dataProvider.addProduct(Integer.parseInt(args[2]),args[3],Integer.parseInt(args[4]),Integer.parseInt(args[5]),args[6],Integer.parseInt(args[7]),Integer.parseInt(args[8]),Integer.parseInt(args[9])); break;
                    default: log.info(WRONG_QTY_PARAMS);
                } break;

            case GET:
                if (args.length != 3) { log.info(WRONG_QTY_PARAMS); return; }
                if (check(ARTICLE,args[2])) log.debug(dataProvider.getProductByArticle(Integer.parseInt(args[2]))); break;

            case DEL:
                if (args.length != 3) { log.info(WRONG_QTY_PARAMS); return; }
                if (check(ARTICLE,args[2])) dataProvider.deleteProductByArticle(Integer.parseInt(args[2])); break;

            case CHANGE:
                if (args.length != 5) { log.info(WRONG_QTY_PARAMS); return; }
                if (checkChange(args[2],args[3],args[4])) dataProvider.changeProductByArticle(Integer.parseInt(args[2]),args[3],args[4]); break;

            case SHOWALL:
                if (args.length != 2) { log.info(WRONG_QTY_PARAMS); return; }
                dataProvider.showAllLists(); break;

            case SHOWZERO:
                if (args.length != 2) { log.info(WRONG_QTY_PARAMS); return; }
                dataProvider.showProductsWithZeroBalance(); break;

            case SHOWBYNAME:
                if (args.length != 3) { log.info(WRONG_QTY_PARAMS); return; }
                if (check(NAMEPART,args[2])) dataProvider.showListProductsByName(args[2]); break;

            case DOC:
                if (args.length != 5) { log.info(WRONG_QTY_PARAMS); return; }
                if (checkDoc(args[2],args[3],args[4])) dataProvider.issueSaleWriteoffPosting(Integer.parseInt(args[2]),Integer.parseInt(args[3]),args[4]); break;

            default: log.info(WRONG_ACTION);
        }
    }

    private static boolean check(String key, String value) {
        switch (key){
            case CATEGORY_CLO:
                if (value.toLowerCase().equals(Category.bathrobe.toString()) || value.toLowerCase().equals(Category.shirt.toString()) ||
                        value.toLowerCase().equals(Category.socks.toString()) || value.toLowerCase().equals(Category.tights.toString()) ||
                        value.toLowerCase().equals(Category.underpants.toString()) || value.toLowerCase().equals(Category.linen.toString())) return true;
                log.info(WRONG_CATEGORY_CLO); break;

            case CATEGORY_BED:
                if (value.toLowerCase().equals(Category.pillow.toString()) || value.toLowerCase().equals(Category.blanket.toString()) ||
                        value.toLowerCase().equals(Category.mattress.toString()) || value.toLowerCase().equals(Category.pillowcase.toString()) ||
                        value.toLowerCase().equals(Category.sheet.toString()) || value.toLowerCase().equals(Category.duvetcover.toString())) return true;
                log.info(WRONG_CATEGORY_BED); break;

            case TYPE:
                if(Arrays.stream(TypeOfDoc.values()).anyMatch(t -> t.toString().equals(value.toLowerCase()))) return true;
                log.info(WRONG_TYPE); break;

            case WHATCHANGE:
                switch (value.toLowerCase()){
                    case NAME: return true;
                    case PRICE: return true;
                    case QUANTITY: return true;
                }
                log.info(WRONG_WHATCHANGE); break;

            case ARTICLE:
                if (value.trim().matches(PATTERN_ARTICLE)) return true;
                log.info(WRONG_ARTICLE); break;

            case PRICE:
                if (value.trim().matches(PATTERN_PRICE)) return true;
                log.info(WRONG_PRICE); break;

            case QUANTITY:
                if (value.trim().matches(PATTERN_QTY)) return true;
                log.info(WRONG_QTY); break;

            case NAME:
                if (value.trim().matches(PATTERN_NAME)&&!value.trim().matches(PATTERN_ARTICLE)) return true;
                log.info(WRONG_NAME); break;

            case NAMEPART:
                if (value.trim().matches(PATTERN_NAME)) return true;
                log.info(WRONG_NAME); break;

            case SIZE:
                if (value.trim().matches(PATTERN_SIZE)) return true;
                log.info(WRONG_SIZE); break;

            case LENGTH:
                if (value.trim().matches(PATTERN_LENGTH)) return true;
                log.info(WRONG_LENGTH); break;

            case WIDTH:
                if (value.trim().matches(PATTERN_WIDTH)) return true;
                log.info(WRONG_WIDTH); break;

            case HEIGHT:
                if (value.trim().matches(PATTERN_HEIGHT)) return true;
                log.info(WRONG_HEIGHT); break;
        }
        return false;
    }
    private static boolean checkClothes(String article, String name, String price, String quantity, String category, String size) {
        boolean check=true;
        if (!check(ARTICLE,article)) check=false;
        if (!check(NAME,name)) check=false;
        if (!check(PRICE,price)) check=false;
        if (!check(QUANTITY,quantity)) check=false;
        if (!check(CATEGORY_CLO,category)) check=false;
        if (!check(SIZE,size)) check=false;
        return check;
    }
    private static boolean checkBedding(String article, String name, String price, String quantity, String category, String length, String width, String height) {
        boolean check=true;
        if (!check(ARTICLE,article)) check=false;
        if (!check(NAME,name)) check=false;
        if (!check(PRICE,price)) check=false;
        if (!check(QUANTITY,quantity)) check=false;
        if (!check(CATEGORY_BED,category)) check=false;
        if (!check(LENGTH,length)) check=false;
        if (!check(WIDTH,width)) check=false;
        if (!check(HEIGHT,height)) check=false;
        return check;
    }
    private static boolean checkChange(String article, String whatChange, String newValue) {
        boolean check = true;
        if (!check(ARTICLE,article)) check = false;
        if (!check(WHATCHANGE,whatChange)) return false;
        switch (whatChange.toLowerCase()){
            case NAME: if (!check(NAME,newValue)) check = false;
            case PRICE: if (!check(PRICE,newValue)) check = false;
            case QUANTITY: if (!check(QUANTITY,newValue)) check = false;
        }
        return check;
    }
    private static boolean checkDoc(String article, String qty, String type) {
        boolean check = true;
        if (!check(ARTICLE,article)) check=false;
        if (!check(QUANTITY,qty)) check=false;
        if (!check(TYPE,type)) check=false;
        return check;
    }

}