package bobs.Slack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class Slack {
    public static final String url = "https://slack.com/api/";
    private static final String token = "{slack-token}";
    public HttpHeaders header;
    public RestTemplate restTemplate;
    public HttpEntity<String> entity;
    public JSONObject jsonObject;

    public Slack()
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

    public List<String> getMembersId(List<String> membersName)
    {
        List<String> result = new ArrayList<>();
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
            String key = (String) jsonLineItem.get("name");
            for (String curName : membersName)
            {
                if (key.equals(curName))
                {
                    result.add((String) jsonLineItem.get("id"));
                }
            }
        }
        return result;
    }

    public void sendSuccessMsg(List<String> membersName)
    {
        List<String> memberIds = getMembersId(membersName);
        jsonObject.put("text", "약속시간이 다가오고 있습니다!");
        jsonObject.put("username", "bobs");
        for (String memberId : memberIds)
        {
            jsonObject.put("channel",memberId);
            entity = new HttpEntity<String>(jsonObject.toString(),header);
            String log = restTemplate.postForObject(url + "chat.postMessage", entity, String.class);
            System.out.println("send success msg to " + memberId +" : " + log);
        }
    }

    public void sendCancelMsg(List<String> membersName)
    {
        List<String> memberIds = getMembersId(membersName);
        jsonObject.put("text", "약속이 취소되었습니다!");
        jsonObject.put("username", "bobs");
        for (String memberId : memberIds)
        {
            jsonObject.put("channel",memberId);
            entity = new HttpEntity<String>(jsonObject.toString(),header);
            String log = restTemplate.postForObject(url + "chat.postMessage", entity, String.class);
            System.out.println("send success msg to " + memberId +" : " + log);
        }
    }
}
