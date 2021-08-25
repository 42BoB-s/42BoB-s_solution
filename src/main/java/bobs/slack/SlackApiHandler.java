package bobs.slack;
import org.json.simple.JSONArray;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface SlackApiHandler {
    // https://slack.com/api/{apiName} 에서 get으로 요청한 정보를 반환하는 함수
    ResponseEntity<String> getResponseEntity(String apiName);
    // getResponseEntity함수의 리턴값을 json으로 파싱하는 함수
    JSONArray parsing(ResponseEntity<String> response);
    // 슬랙 이름을 통해 슬랙 id를 알아내는 함수
    public Map<String,String> getMembersId(List<String> membersName);
    // memberId는 닉네임이 아니라 id가 들어가야함 ( ex U52036145)
    // memberId에 text를 보내는 함수
    public void sendMsg(String memberId, String text);
    // myName을 제외한 membersName의 string을 ,로 join하는 함수
    public String joinNames(List<String> membersName, String myName);
}
