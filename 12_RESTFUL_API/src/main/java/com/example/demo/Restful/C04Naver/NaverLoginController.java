package com.example.demo.Restful.C04Naver;

import com.example.demo.Restful.C03Kakao.C02KakaoLoginController;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
@RequestMapping("/naver")
public class NaverLoginController {

    private String CLIENT_ID;
    private String CLIENT_SECRET;
    private String REDIRECT_URL="http://localhost:8099/naver/getCode";

    private String code;
    private String state;
    private NaverTokenResponse naverTokenResponse;
    private NaverProfileResponse naverProfileResponse;

    @GetMapping("/login")
    public String login(){
        log.info("GET /naver/login...");
        return "redirect:https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id="+CLIENT_ID+"&state=STATE_STRING&redirect_uri="+REDIRECT_URL;

    }
    @GetMapping("/getCode")
    public String getCode(String code,String state){
        log.info("GET /naver/getCode...code : " + code+" state : " + state);
        this.code = code;
        this.state = state;
        return "forward:/naver/getAccessToken";
    }
    @GetMapping("/getAccessToken")
    public String getAccessToken(){
        log.info("GET /naver/getAccessToken....");

        String url = "https://nid.naver.com/oauth2.0/token";
        RestTemplate restTemplate = new RestTemplate();
        //
        HttpHeaders header = new HttpHeaders();
        //
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id",CLIENT_ID);
        params.add("client_secret",CLIENT_SECRET);
        params.add("code",code);
        params.add("state",state);
        //
        HttpEntity< MultiValueMap<String,String>  > entity = new HttpEntity<>(params,header);
        // 요청 후 응답 확인
        ResponseEntity<NaverTokenResponse> response = restTemplate.exchange(url, HttpMethod.POST,entity, NaverTokenResponse.class);
        System.out.println(response.getBody());

        this.naverTokenResponse = response.getBody();

        //main으로 리다이렉트
        return "redirect:/naver";
    }

    @GetMapping
    public String main(Model model){
        log.info("GET /naver/index...");

        String url = "https://openapi.naver.com/v1/nid/me";

        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+ naverTokenResponse.getAccess_token());
        // 요청 바디 파라미터(X)

        HttpEntity entity = new HttpEntity(header);

        // 요청 후 응답 확인
        ResponseEntity<NaverProfileResponse> response = restTemplate.exchange(url, HttpMethod.POST,entity, NaverProfileResponse.class);
        System.out.println(response.getBody());

        this.naverProfileResponse =response.getBody();
        // 뷰로 전달
        model.addAttribute("profile",naverProfileResponse.getProfileInfo());
        return "naver/index";

    }
    @GetMapping("/logout1")
    public String logout1(){
        log.info("GET /naver/logout1...");

        String url = "https://nid.naver.com/oauth2.0/token";

        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders header = new HttpHeaders();
//        header.add("Authorization","Bearer "+ naverTokenResponse.getAccess_token());

        //요청 본문 파라미터
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("client_id",CLIENT_ID);
        params.add("client_secret",CLIENT_SECRET);
        params.add("access_token", naverTokenResponse.getAccess_token());
        params.add("grant_type","delete");
        //
        HttpEntity< MultiValueMap<String,String>  > entity = new HttpEntity<>(params,header);

        // 요청 후 응답 확인
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,entity, String.class);
        System.out.println(response.getBody());

        return "redirect:/naver";
    }
    @GetMapping("/logout2")
    public String logout2(){
        log.info("GET /naver/logout2....");
        return "redirect:https://nid.naver.com/nidlogin.logout?returl=https://www.naver.com/";
    }
    //https://nid.naver.com/nidlogin.logout?returl=https://www.naver.com/


    //NAVER TOKEN RESPONSE CLASS
    @Data
    private static class NaverTokenResponse{
        public String access_token;
        public String refresh_token;
        public String token_type;
        public String expires_in;
    }

    //NAVER PROFILE RESPONSE CLASS
    @Data
    private static class ProfileInfo{
        public String id;
        public String nickname;
        public String profile_image;
        public String email;
        public String name;
    }
    @Data
    private static class NaverProfileResponse{
        public String resultcode;
        public String message;
        @JsonProperty(value = "response")
        public ProfileInfo profileInfo;
    }

}
