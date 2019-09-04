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
        Document page = Jsoup.parse( new URL(url), 3000);
        return page;
    }

    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static String getDateFromString (String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()){
            return matcher.group();
        }
        throw new Exception("Can't extract date from String");
    }
        private static int printPartValues(Elements values, int index){
        int iterationCount = 4;

            if(index == 0){
                Element valueLn = values.get(3);
//                switch (valueLn.text()){
//                    case "Утро": iterationCount = 3; break;
//                    case "День": iterationCount = 2; break;
//                    case  "Вечер": iterationCount = 1; break;
//                }
                boolean isMorning = valueLn.text().contains("Утро");
                boolean isDay = valueLn.text().contains("День");
                boolean isEvening = valueLn.text().contains("Вечер");
                if(isMorning){
                    iterationCount = 3;
                }
                if(isDay){
                    iterationCount = 2;
                }
                if(isEvening){
                    iterationCount = 1;
                }


            }
            for(int i = 0; i <iterationCount; i++){
                Element valueLine = values.get(index + i);
                for (Element td : valueLine.select("td")){
                    System.out.print(td.text() + "    ");
                }
                System.out.println();

            }
            return iterationCount;

    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        //css query language
        Element tableWth = page.select("table[class=wt]").first();
        Elements names =  tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;

        for(Element name : names){
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date +"    Явление Температура Давоение Влажность Ветер");
            int iterationCount = printPartValues(values, index);
            index += iterationCount;
        }
    }
}
