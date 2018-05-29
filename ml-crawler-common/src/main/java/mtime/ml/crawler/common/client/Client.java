package mtime.ml.crawler.common.client;

import com.google.common.collect.Maps;
import io.prediction.Event;
import io.prediction.EventClient;
import mtime.lark.pb.utils.StringUtils;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class Client {


    private static final String accessKey = "2qW6zOyj0AHS6arN2SJA5EcC5erMXiwLq6cKPGxxkqafTVFRJ0x0VN3h83rteKdA";
    //private static final String accessKey =  "VD3sYCHkOB5mFpLnmf2C9O_HkUFh2GWZajtNqHvMuwTJZwvm9Wr6BYCMqqR7EWQ5";
    //private static final String accessKey = "ZgnbYC8DVxVtTOp7PKvkBLfL0MN4M2mvnRwHck5-JHN1ln_QH0QlH-zDILR13c_7";
    //private static final String url = "http://192.167.13.145:7070";
    //混合云
    private static final String url = "http://10.0.0.33:7070";


    public static EventClient instance(String acKey, String eventUrl) {
        if (StringUtils.isEmpty(acKey) || StringUtils.isEmpty(eventUrl)) {
            return new EventClient(accessKey, url);
        }
        return new EventClient(acKey, eventUrl);
    }

    public static void invoker(Consumer<Event> function, EventClient customClient) {
        EventClient client = null;
        try {

            if (customClient == null) {
                client = instance(null, null);
            }
            client = customClient;
            Event event = new Event();
            event.eventTime(new DateTime());
            function.accept(event);

            System.out.println(client.createEvent(event));
        } catch (Exception e) {

        } finally {
            client.close();
        }

    }


    public static String createEvent(long userId, Map<String, Object> properties) {

        Event event = new Event()
                .event("$set")
                .entityType("user")
                .entityId(userId + "")
                //.targetEntityType("item")
                //.targetEntityId("")
                .properties(properties)
                .eventTime(new DateTime());
        try (EventClient client = instance(null, null);) {
            String result = client.createEvent(event);
            System.out.println(result);
            return result;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    public static String createItemEvent(long userId, Map<String, Object> properties) {

        Event event = new Event()
                .event("$set")
                .entityType("item")
                .entityId(userId + "")
                //.targetEntityType("item")
                //.targetEntityId("")
                .properties(properties)
                .eventTime(new DateTime());
        try (EventClient client = new EventClient(accessKey, url);) {
            String result = client.createEvent(event);
            System.out.println(result);
            return result;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String createUserEvent(long userId) {

        Event event = new Event()
                .event("$set")
                .entityType("user")
                .entityId(userId + "")
                //.targetEntityType("item")
                //.targetEntityId("")
                .eventTime(new DateTime());
        try (EventClient client = new EventClient(accessKey, url);) {
            String result = client.createEvent(event);
            System.out.println(result);
            return result;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String createUserItemRelationEvent(long userId, long ItemId) {

        Event event = new Event()
                .event("view")
                .entityType("user")
                .entityId(userId + "")
                .targetEntityType("item")
                .targetEntityId(ItemId + "")
                .eventTime(new DateTime());
        try (EventClient client = new EventClient(accessKey, url);) {
            String result = client.createEvent(event);
            System.out.println(result);
            return result;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void main (String [] args){

        Map<String,Object> map = Maps.newHashMap();
        map.put("text","i love you");
        try {
            Event event = new Event()
                    .event("$set")
                    .entityType("user")
                    .entityId(1 + "").properties(map)
                    //.targetEntityType("item")
                    //.targetEntityId("")
                    .eventTime(new DateTime());
            instance("1234","http://192.167.13.144:7070").createEvent(event);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }

    }


}
