package lsg.graphics;

public class CSSFactory {

    private static String CSS_DIR = "css/" ;

    public static String getStyleSheet(String filename){
        return CSSFactory.class.getResource(CSS_DIR + filename).toExternalForm() ;
    }

}
