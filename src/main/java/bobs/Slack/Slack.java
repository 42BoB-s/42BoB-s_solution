package bobs.Slack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Slack implements SlackApiHandler {
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

    @Override
    public ResponseEntity<String> getResponseEntity(String apiName)
    {
        return restTemplate.exchange(url + apiName, HttpMethod.GET, entity, String.class);
    }

    @Override
    public JSONArray parsing(ResponseEntity<String> response)   //slack에서 받아온 값을 json으로 변경
    {
        JSONArray member_list = new JSONArray();
        JSONParser parser = new JSONParser();
        Object obj;
        JSONObject jsonObj;
        try {
            obj = parser.parse(response.getBody());
            jsonObj = (JSONObject) obj;
            member_list = (JSONArray)jsonObj.get("members"); //json에서 members에 해당하는 것만 가져오기
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return member_list;
    }

    @Override
    public void sendMsg(String memberId, String text)
    {
        jsonObject.put("username", "Bobs");  // 송신자 이름 지정
        jsonObject.put("channel", memberId);  // 수신자 id 지정
        jsonObject.put("text", text); // 보낼 내용
        entity = new HttpEntity<String>(jsonObject.toString(), header);
        String log = restTemplate.postForObject(url + "chat.postMessage", entity, String.class);
        System.out.println("send msg to " + memberId + " : " + text + "\n" + log);
    }

    @Override
    public Map<String,String> getMembersId(List<String> membersName)        // 추후 gmail기능에 사용가능
    {
        HashMap<String,String> result = new HashMap<>();
        for (String name : membersName)             //약속에 참여하는 모든 인원 result에 대입(value값을 null로 초기화)
            result.put(name, null);
        JSONArray member_list = parsing(getResponseEntity("users.list"));
        for (Object memberInfo : member_list)       //cuckoo채널에 존재하는 모든 user 순회
        {
            JSONObject jsonLineItem = (JSONObject) memberInfo;
            String name = (String) jsonLineItem.get("real_name");       // user 이름 추출
            if (membersName.contains(name)) {       // if 약속이 존재하는 user
                result.put(name, (String) jsonLineItem.get("id"));      // value값을 null에서 실제 id로 변경
            }
        }
        return result;
    }

    @Override
    public String joinNames(List<String> membersName, String myName)
    {
        String friends = membersName.stream().filter(name->!name.equals(myName)).collect(Collectors.joining(", "));
        if (membersName.size() != 1) //혼자하는 약속이 아닐 경우
            friends += "님과 함께하는 ";
        return friends;
    }

    public void sendSuccessMsg(List<String> membersName)
    {
        Map<String,String> memberIds = getMembersId(membersName);  // key : 닉네임( ex mhong), value : id( ex U025KJ31MGS)
        for (Map.Entry<String, String> elem : memberIds.entrySet())
        {
            if (elem.getValue() != null) { // cuckoo채널에 없는 약속대상자에게 슬랙 보낼 필요 없음
                String friends = joinNames(membersName, elem.getKey());
                sendMsg(elem.getValue(), friends + "약속시간이 다가오고 있습니다!");
            }
        }
    }

    public void sendCancelMsg(List<String> membersName)
    {
        Map<String,String> memberIds = getMembersId(membersName);
        for (Map.Entry<String, String> elem : memberIds.entrySet())
        {
            if (elem.getValue() != null) { // cuckoo채널에 없는 약속대상자에게 슬랙 보낼 필요 없음
                String friends = joinNames(membersName, elem.getKey());
                sendMsg(elem.getValue(), friends + "약속이 취소되었습니다.");
            }
        }
    }
}
