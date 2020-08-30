package ru.sfedu.textile;

public class Constants {

    // Configuration
    public static final String CONFIG_PATH = "src/main/resources/config.properties";
    public static final String CONFIG_JAR = "config.properties";

    public static final String CONFIG_LOG_KEY = "log4j2.configurationFile";
    public static final String CONFIG_LOG_PATH = "src/main/resources/log4j2.properties";
    public static final String CONFIG_LOG_JAR = "log4j2.properties";


    // FOR MAIN CLI

    public static final String HELP =
            "\n\nAll parameters must be separated by spaces. You can't enter string values with spaces.\n"+
                    "1.data provider (csv, xml, db)\n"+
                    "2.action (add, get, del, change, showall, showzero, showbyname, doc)\n"+
                    "3.data if needed\n"+
            "\nData for different actions:\n"+
                    "add: article, name, price, quantity, category, size\n"+
                    "add: article, name, price, quantity, category, length, width, height\n"+
                    "get: article\n"+
                    "del: article\n"+
                    "change: article, what change, new value\n"+
                    "showall: no data\n"+
                    "showzero: no data\n"+
                    "showbyname: name or part of the name\n"+
                    "doc: article, quantity, type of document\n"+
            "\nCategories for clothes: bathrobe, shirt, socks, tights, underpants, linen"+
            "\nCategories for bedding: pillow, blanket, mattress, pillowcase, sheet, duvetcover"+
            "\nTypes of documents: sale, writeoff, posting"+
            "\nWhat you can change: name, price, quantity\n"+
            "\nExamples of commands:\n"+
                    "csv add 111 RedMattress 2000 5 mattress 200 160 10\n"+
                    "csv get 111\n"+
                    "csv del 111\n"+
                    "csv change 111 price 2300\n"+
                    "xml showall\n"+
                    "xml showzero\n"+
                    "xml showbyname red\n"+
                    "xml doc 111 3 posting\n";

    public static final String EMPTY_COMMAND = "You are trying to enter an empty command. Try again";
    public static final String WRONG_QTY_PARAMS = "Wrong quantity of parameters";
    public static final String WRONG_DP = "Wrong data provider";
    public static final String WRONG_ACTION = "Wrong action";

    public static final String BE_CAREFUL = "Please be careful when entering data";
    public static final String WRONG_ARTICLE = "Wrong article";
    public static final String WRONG_NAME = "Wrong name";
    public static final String WRONG_PRICE = "Wrong price";
    public static final String WRONG_QTY = "Wrong quantity";
    public static final String WRONG_SIZE = "Wrong size";
    public static final String WRONG_LENGTH = "Wrong length";
    public static final String WRONG_WIDTH = "Wrong width";
    public static final String WRONG_HEIGHT = "Wrong height";
    public static final String WRONG_CATEGORY_CLO = "Wrong category for clothes";
    public static final String WRONG_CATEGORY_BED = "Wrong category for bedding";
    public static final String WRONG_TYPE = "Wrong type of document";
    public static final String WRONG_WHATCHANGE = "Wrong 'what change' value";


    public static final String CSV = "CSV";
    public static final String XML = "XML";
    public static final String DB = "DB";

    public static final String ADD = "ADD";
    public static final String GET = "GET";
    public static final String DEL = "DEL";
    public static final String CHANGE = "CHANGE";
    public static final String SHOWALL = "SHOWALL";
    public static final String SHOWZERO = "SHOWZERO";
    public static final String SHOWBYNAME = "SHOWBYNAME";
    public static final String DOC = "DOC";

    public static final String PATTERN_ARTICLE = "[\\d]{1,8}";
    public static final String PATTERN_NAME = "[^\\s]{1,50}";
    public static final String PATTERN_PRICE = "[\\d]{1,7}";
    public static final String PATTERN_QTY = "[\\d]{1,4}";
    public static final String PATTERN_SIZE = ".{1,10}";
    public static final String PATTERN_LENGTH = "[\\d]{1,3}";
    public static final String PATTERN_WIDTH = "[\\d]{1,3}";
    public static final String PATTERN_HEIGHT = "[\\d]{1,3}";


    // FOR DATA PROVIDERS

    public static final String KEY_CSV_CLO = "csvClothes";
    public static final String KEY_CSV_BED = "csvBedding";
    public static final String KEY_CSV_DOC = "csvDocument";
    public static final String KEY_XML_CLO = "xmlClothes";
    public static final String KEY_XML_BED = "xmlBedding";
    public static final String KEY_XML_DOC = "xmlDocument";
    public static final String KEY_DB_URL = "DBurl";
    public static final String KEY_DB_USER = "DBuser";
    public static final String KEY_DB_PSWD = "DBpasswd";

    public static final String SUCCESS_ADD_CLOTHES = "SUCCESSFULLY ADDED TO CLOTHES";
    public static final String SUCCESS_ADD_BEDDING = "SUCCESSFULLY ADDED TO BEDDING";
    public static final String SUCCESS_ADD_DOCUMENT = "SUCCESSFULLY ADDED TO DOCUMENT";
    public static final String SUCCESS_DEL_CLOTHES = "SUCCESSFULLY DELETED IN CLOTHES";
    public static final String SUCCESS_DEL_BEDDING = "SUCCESSFULLY DELETED IN BEDDING";
    public static final String SUCCESS_DEL_DB = "SUCCESSFULLY DELETED";

    public static final String PROD_ARTICLE = "PRODUCT WITH ARTICLE ";
    public static final String UPDATED = " UPDATED, ";
    public static final String CHANGED = " CHANGED";

    public static final String WHERE_ARTICLE = "\" WHERE article=";
    public static final String UPD_CLO_SET = "UPDATE clothes SET ";
    public static final String UPD_BED_SET = "UPDATE bedding SET ";
    public static final String NAME_EQV = "name=\"";
    public static final String DEL_CLO_W_ARTICLE = "DELETE FROM clothes WHERE article=";
    public static final String DEL_BED_W_ARTICLE = "DELETE FROM bedding WHERE article=";

    public static final String NOT_ADDED = "NOT ADDED, PRODUCT ALREADY EXISTS";
    public static final String NOT_DELETED = "NOT DELETED";
    public static final String USED_IN_DOC = "THIS PRODUCT IS USED IN DOCUMENTS";
    public static final String NOT_CHANGED = "NOT CHANGED";
    public static final String NOT_ISSUED = "NOT ISSUED";
    public static final String NOT_ENOUGH_QTY = "YOU HAVE NOT ENOUGH QUANTITY OF THE PRODUCT. DOCUMENT IS NOT ISSUED";

    public static final String NO_PRODUCT = "NO PRODUCT WITH ARTICLE ";
    public static final String NO_ZERO = "NO PRODUCTS WITH ZERO BALANCE";
    public static final String NO_PRODUCT_NAME = "NO PRODUCTS WITH SUCH NAME";

    public static final String PATTERN_DATE = "dd-MM-yyyy";

    public static final String LIST_CLOTHES = "\n\nCLOTHES:";
    public static final String LIST_BEDDING = "\n\nBEDDING:";
    public static final String LIST_DOCUMENTS = "\n\nDOCUMENTS:";
    public static final String NO_DATA = "\nNo data";

    public static final String CLOTHES = "Clothes";
    public static final String BEDDING = "Bedding";
    public static final String DOCUMENT = "Document";

    public static final String CATEGORY_CLO = "category clothes";
    public static final String CATEGORY_BED = "category bedding";
    public static final String TYPE = "type";
    public static final String WHATCHANGE = "whatchange";
    public static final String ARTICLE = "article";
    public static final String NAME = "name";
    public static final String NAMEPART = "namepart";
    public static final String PRICE = "price";
    public static final String QUANTITY = "quantity";
    public static final String SIZE = "size";
    public static final String LENGTH = "length";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";



}
