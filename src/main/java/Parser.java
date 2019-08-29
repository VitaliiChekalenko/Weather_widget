import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static Document getPage() throws IOException {
        String url = "http://pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");
    private static String getDataFromString (String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if(matcher.find()){
            return matcher.group();
        }
        throw new Exception("Can't extract date from string!");
    }

    private static void printFourValues(Elements values, int index){

        if(index == 0 ){
            Element valueLine = values.get(0);
            for(Element td: valueLine.select("td")){

                System.out.print(td.text() + "    ");
            }
        }

        for(int i = 0; i<4; i++){
            Element valueLine = values.get(index);
            for(Element td: valueLine.select("td")){
                System.out.print(td.text() + "    ");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        //css query lang
        Element tableWth = page.select("table[class=wt]").first();
        System.out.println(tableWth);
        Elements names = tableWth.select("tr[class=wth]");
       Elements values = tableWth.select("tr[valign=top]");
       int index=0;

       for(Element name: names){
           String dateString = name.select("th[id=dt]").text();
           String date = getDataFromString(dateString);
           System.out.println(date + "      Явления     Температура      Давл       Влажность    Ветер ");
           printFourValues(values, index);
       }


    }
}
