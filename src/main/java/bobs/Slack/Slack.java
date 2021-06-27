package bobs.Slack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Slack {
    public static final String url = "https://slack.com/api/";
    private static final String token = "xoxb-2213307174208-2189616646066-f47Uw2TXJjRzb6Dog4lgmWQZ";
    public HttpHeaders header;
    public RestTemplate restTemplate;
    public HttpEntity<String> entity;
    public JSONObject jsonObject;

    public Slack ()
    {
        header = new HttpHeaders();
        restTemplate = new RestTemplate();
        jsonObject = new JSONObject();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("Authorization", "Bearer " + token);
        jsonObject.put("Content-Type", "application/x-www-form-urlencoded");
        entity = new HttpEntity<String>(jsonObject.toString(),header);
    }
    public String getAllMembersInfo()
    {
        return restTemplate.postForObject(url + "users.list", entity, String.class);
    }
    public Map<String,String> getMembersId(List<String> membersName)
    {
        HashMap<String,String> result = new HashMap<>();
        JSONArray member_list = new JSONArray();
        JSONParser parser = new JSONParser();
        Object obj;
        JSONObject jsonObj;
        try {
            obj = parser.parse(getAllMembersInfo());
            jsonObj = (JSONObject) obj;
            member_list = (JSONArray)jsonObj.get("members");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (Object memberInfo : member_list)
        {
            JSONObject jsonLineItem = (JSONObject) memberInfo;
            String name = (String) jsonLineItem.get("real_name");
            for (String curName : membersName)
            {
                if (curName.equals(name))
                    result.put(name,(String) jsonLineItem.get("id"));
            }
            result.putIfAbsent(name, "");
        }
        return result;
    }
    public void sendSuccessMsg(List<String> membersName)
    {
        Map<String,String> memberIds = getMembersId(membersName);
        for (Map.Entry<String, String> elem : memberIds.entrySet())
        {
            if (!elem.getValue().equals("")) {
                String text = "";
                for (int idx = 0; idx < membersName.size(); idx++) {
                    if (!elem.getKey().equals(membersName.get(idx)))
                    {
                        text += membersName.get(idx);
                        if (idx != membersName.size() - 1)
                            text += ", ";
                    }
                }
                text += "님과 함께하는 약속시간이 다가오고 있습니다!";
                jsonObject.put("username", "Bobs");
                jsonObject.put("channel", elem.getValue());
                jsonObject.put("text", text);
                entity = new HttpEntity<String>(jsonObject.toString(), header);
                String log = restTemplate.postForObject(url + "chat.postMessage", entity, String.class);
                System.out.println("send success msg to " + elem.getKey() + " : " + log);
            }
        }
    }
    public void sendCancelMsg(List<String> membersName)
    {
        Map<String,String> memberIds = getMembersId(membersName);
        for (Map.Entry<String, String> elem : memberIds.entrySet())
        {
            if (!elem.getValue().equals("")) {
                String text = "";
                for (int idx = 0; idx < membersName.size(); idx++) {
                    if (!elem.getKey().equals(membersName.get(idx)))
                    {
                        text += membersName.get(idx);
                        if (idx != membersName.size() - 1)
                            text += ", ";
                    }
                }
                text += "님과 함께하는 약속이 취소되었습니다.";
                jsonObject.put("username", "Bobs");
                jsonObject.put("channel", elem.getValue());
                jsonObject.put("text", text);
                entity = new HttpEntity<String>(jsonObject.toString(), header);
                String log = restTemplate.postForObject(url + "chat.postMessage", entity, String.class);
                System.out.println("send success msg to " + elem.getKey() + " : " + log);
            }
        }
    }
}
