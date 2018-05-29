package mtime.ml.crawler.service.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.prediction.EventClient;
import mtime.ml.crawler.common.client.Client;
import mtime.ml.crawler.service.dao.MlQuoteDao;
import mtime.ml.crawler.service.entity.MlQuote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class EventUtil {

    @Autowired
    private MlQuoteDao mlQuoteDao;

    private List<MlQuote> getSource() {
        List<Integer> ids = Lists.newArrayList();

        for (int i = 0; i < 1700; i++) {
            ids.add(i);
        }

        return mlQuoteDao.findByIds(ids.toArray());
    }

    public void execute() {
        insertWikiPage();

    }


    public static String defaultValue(String str, String defaultVal) {

        if (str == null) {
            return defaultVal;
        }
        return str;
    }

    public void insertWikiPage() {

        getSource().forEach(data -> {
            Map<String, Object> properties = Maps.newHashMap();
            StringBuilder sb = new StringBuilder();
            sb.append(defaultValue(data.getTopic(), ""));
            sb.append(defaultValue(data.getAuthor(), "") );
            sb.append(defaultValue(data.getInfo(), ""));
            sb.append(defaultValue(data.getKeywords(), ""));
            String category = sb.toString();
            properties.put("category", category);
            properties.put("content", data.getContent());
            EventClient eventClient = Client.instance("aygXEUgJg747cHITeoFYRE5lOuEF4iR_58a0Sk-yd6fo0iHypEBe4pxBvFVWErav", "http://192.167.13.145:7070");

            Client.invoker(event -> {

                event.entityId(data.getId() + "");
                event.event("train");
                event.entityType("wiki_page");
                event.properties(properties);


            }, eventClient);


        });
    }


    //700-1700
    public void insertLDAData() {
        List<Integer> ids = Lists.newArrayList();

        for (int i = 0; i < 1700; i++) {
            ids.add(i);
        }

        List<MlQuote> data = mlQuoteDao.findByIds(ids.toArray());
        data.forEach(i -> {
            Map<String, Object> properties = Maps.newHashMap();
            properties.put("text", i.getContent());
            Client.createEvent(i.getContent()
                                .length(), properties);
        });
    }

    public void insertSimiData() {
        List<Integer> ids = Lists.newArrayList();

        for (int i = 0; i < 1700; i++) {
            ids.add(i);
        }

        List<MlQuote> data = mlQuoteDao.findByIds(ids.toArray());
        data.forEach(i -> {
            Map<String, Object> properties = Maps.newHashMap();
            properties.put("categories", i.getKeywords()
                                          .split(", "));
            Client.createItemEvent(i.getId(), properties);
        });
    }


    //700-1700
    public void insertUserData() {
        for (int i = 700; i < 1700; i++) {
            Client.createUserEvent(i);
        }
    }

    //700-1700
    public void insertUserItemData() {
        Random random = new Random();
        for (int i = 700; i < 1700; i++) {
            Client.createUserItemRelationEvent(i, random.nextInt(1000) + 700);
        }
    }

    public void combond() {

        insertSimiData();
        insertUserData();
        insertUserItemData();
    }
}
