package com.example.demo.Restful.C03Kakao;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/kakao")
public class C02KakaoLoginController {

    private String CLIENT_ID;
    private String REDIRECT_URI="http://localhost:8099/kakao/getCode";
    private String LOGOUT_REDIRECT_URI="http://localhost:8099/kakao";


//    private String REDIRECT_URI="http://192.168.5.4:8099/kakao/getCode";
//    private String LOGOUT_REDIRECT_URI="http://192.168.5.4:8099/kakao";

    private String code;
    private KakaoTokenResponse kakaoTokenResponse;
    private KakaoFriendsResponse kakaoFriendsResponse;

    @GetMapping("/login")
    public String login(){
        log.info("GET /kakao/login...");
        return "redirect:https://kauth.kakao.com/oauth/authorize?client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"&response_type=code";
    }

    @GetMapping("/getCode")
    public String getCode(String code){
        log.info("GET /kakao/getCode...code : " + code);
        this.code = code;
        return "forward:/kakao/getAccessToken";
    }
    @GetMapping("/getAccessToken")
    public String getAccessToken(){
        log.info("GET /kakao/getAccessToken....");

        String url = "https://kauth.kakao.com/oauth/token";

        RestTemplate restTemplate = new RestTemplate();

//
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
//
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id",CLIENT_ID);
        params.add("redirect_uri",REDIRECT_URI);
        params.add("code",code);
//
        HttpEntity< MultiValueMap<String,String>  > entity = new HttpEntity<>(params,header);

        // 요청 후 응답 확인
        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(url, HttpMethod.POST,entity,KakaoTokenResponse.class);
        System.out.println(response.getBody());
        this.kakaoTokenResponse = response.getBody();

        //main으로 리다이렉트
        return "redirect:/kakao";

    }
    @GetMapping
    public String main(Model model){
        log.info("GET /kakao/index...");

        String url = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+ kakaoTokenResponse.getAccess_token());
        header.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
        // 요청 바디 파라미터(X)

        HttpEntity entity = new HttpEntity(header);

        // 요청 후 응답 확인
        ResponseEntity<KakaoProfileResponse> response = restTemplate.exchange(url, HttpMethod.POST,entity,KakaoProfileResponse.class);
        System.out.println(response.getBody());

        // 뷰로 전달
        model.addAttribute("profile",response.getBody());
        String nickname = response.getBody().getProperties().getNickname();
        String image_url = response.getBody().getProperties().getThumbnail_image();
        String email = response.getBody().getKakao_account().getEmail();

        model.addAttribute("nickname",nickname);
        model.addAttribute("image_url",image_url);
        model.addAttribute("email",email);

        return "kakao/index";
    }

    // 로그아웃(엑세스토큰만 만료)
    @GetMapping("/logout1")
    @ResponseBody
    public void logout1(){

        log.info("GET /kakao/logout1");
        String url = "https://kapi.kakao.com/v1/user/logout";

        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+ kakaoTokenResponse.getAccess_token());
        // 요청 바디 파라미터(X)

        HttpEntity entity = new HttpEntity(header);

        // 요청 후 응답 확인
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,entity,String.class);
        System.out.println(response.getBody());

    }
    // 연결해제(엑세스토큰 만료 / 리프레시토큰 만료 / 동의 철회)
    @GetMapping("/logout2")
    @ResponseBody
    public void logout2(){

        log.info("GET /kakao/logout2");
        String url = "https://kapi.kakao.com/v1/user/unlink";

        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+ kakaoTokenResponse.getAccess_token());
        // 요청 바디 파라미터(X)

        HttpEntity entity = new HttpEntity(header);

        // 요청 후 응답 확인
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,entity,String.class);
        System.out.println(response.getBody());
    }
    @GetMapping("/logout3")
    public String logout3(){
        // 카카오계정과 함께 로그아웃
        log.info("GET /kakao/logout3");
        return "redirect:https://kauth.kakao.com/oauth/logout?client_id="+CLIENT_ID+"&logout_redirect_uri="+LOGOUT_REDIRECT_URI;
    }

    @GetMapping("/getMsgCode")
    public String getMsgCode(){
        log.info("GET /kakao/getMsgCode...");
        return "redirect:https://kauth.kakao.com/oauth/authorize?client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"&response_type=code&scope=talk_message,friends";
    }
    @GetMapping("/message/me/{message}")
    @ResponseBody
    public void message_me(@PathVariable String message){
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+ kakaoTokenResponse.getAccess_token());
        header.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        // 요청 바디 파라미터
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();

        JSONObject template_object = new JSONObject();       // {}
        template_object.put("object_type","text");
        template_object.put("text",message);
        template_object.put("link",new JSONObject());
        template_object.put("button_title","-");

        params.add("template_object",template_object.toString());


        HttpEntity< MultiValueMap<String,String>  > entity = new HttpEntity(params,header);


        // 요청 후 응답 확인
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,entity,String.class);
        System.out.println(response.getBody());
    }

    @GetMapping("/friends")
    @ResponseBody
    public void getFriends(){
        log.info("GET /kakao/friends....");

        String url = "https://kapi.kakao.com/v1/api/talk/friends";

        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+ kakaoTokenResponse.getAccess_token());

        // 요청 바디 파라미터
        HttpEntity entity = new HttpEntity(header);

        // 요청 후 응답 확인
        ResponseEntity<KakaoFriendsResponse> response = restTemplate.exchange(url, HttpMethod.GET,entity,KakaoFriendsResponse.class);
        System.out.println(response.getBody());

        this.kakaoFriendsResponse = response.getBody();

    }
    @GetMapping("/message/friend/{message}")
    @ResponseBody
    public void message_friend(@PathVariable String message){
        String url = "https://kapi.kakao.com/v1/api/talk/friends/message/default/send";

        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+ kakaoTokenResponse.getAccess_token());
        header.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        // 요청 바디 파라미터
        List<Element> list =  kakaoFriendsResponse.getElements();
        JSONArray array = new JSONArray(); // []
        for(int i=0;i<list.size();i++){
            array.add(list.get(i).getUuid());
        }

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();

        JSONObject template_object = new JSONObject();       // {}
        template_object.put("object_type","text");
        template_object.put("text",message);
        template_object.put("link",new JSONObject());
        template_object.put("button_title","-");

        params.add("template_object",template_object.toString());
        params.add("receiver_uuids",array.toString());

        HttpEntity< MultiValueMap<String,String>  > entity = new HttpEntity(params,header);


        // 요청 후 응답 확인
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,entity,String.class);
        System.out.println(response.getBody());
    }

    //---------------------------------------
    //KAKAO ACCESS TOKEN CLASS
    //---------------------------------------
    @Data
    private static class KakaoTokenResponse{
        public String access_token;
        public String token_type;
        public String refresh_token;
        public int expires_in;
        public String scope;
        public int refresh_token_expires_in;
    }
    //---------------------------------------
    //KAKAO PROFILE CLASS
    //---------------------------------------
    @Data
    private static class KakaoAccount{
        public boolean profile_nickname_needs_agreement;
        public boolean profile_image_needs_agreement;
        public Profile profile;
        public boolean has_email;
        public boolean email_needs_agreement;
        public boolean is_email_valid;
        public boolean is_email_verified;
        public String email;
    }
    @Data
    private static class Profile{
        public String nickname;
        public String thumbnail_image_url;
        public String profile_image_url;
        public boolean is_default_image;
        public boolean is_default_nickname;
    }
    @Data
    private static class Properties{
        public String nickname;
        public String profile_image;
        public String thumbnail_image;
    }
    @Data
    private static class KakaoProfileResponse{
        public long id;
        public Date connected_at;
        public Properties properties;
        public KakaoAccount kakao_account;
    }
    //----------------------------------------
    // KAKAO FRIENDS CLASS
    //---------------------------------------
    @Data
    private static class Element{
        public String profile_nickname;
        public String profile_thumbnail_image;
        public boolean allowed_msg;
        public long id;
        public String uuid;
        public boolean favorite;
    }
    @Data
    private static class KakaoFriendsResponse{
        public ArrayList<Element> elements;
        public int total_count;
        public Object after_url;
        public int favorite_count;
    }

}
