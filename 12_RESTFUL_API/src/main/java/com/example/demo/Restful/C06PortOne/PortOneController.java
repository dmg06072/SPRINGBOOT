package com.example.demo.Restful.C06PortOne;

import com.example.demo.Restful.C03Kakao.C02KakaoLoginController;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

@Controller
@Slf4j
@RequestMapping("/portOne")
public class PortOneController {

    private String HOSTNAME = "https://api.iamport.kr";
    private String APIKEY;
    private String SECRET;
    private TokenResponse tokenResponse;

    @GetMapping("/index")
    public void index(){
        log.info("GET /portOne/index....");
    }
    // AccessToken 발급 요청
    @GetMapping("/getAccessToken")
    @ResponseBody
    public void getToken(){
        //파라미터
        //Header
        //Body
        //Entity
        //Req->Resp

        String url = HOSTNAME+"/users/getToken";

        RestTemplate restTemplate = new RestTemplate();

//
        HttpHeaders header = new HttpHeaders();

//        header.add("Content-Type","application/json");
//        JSONObject params = new JSONObject(); // {}
//        params.put("imp_key",APIKEY);
//        params.put("imp_secret",SECRET);
//        HttpEntity< String  > entity = new HttpEntity<>(params.toJSONString(),header);

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("imp_key",APIKEY);
        params.add("imp_secret",SECRET);
        HttpEntity< MultiValueMap<String,String>  > entity = new HttpEntity<>(params,header);

        // 요청 후 응답 확인
        ResponseEntity<TokenResponse> response = restTemplate.exchange(url, HttpMethod.POST,entity, TokenResponse.class);
        System.out.println(response.getBody());
        this.tokenResponse = response.getBody();
    }
    @GetMapping("/payments")
    @ResponseBody
    public void getPayments(){
        String url = HOSTNAME+"/payments?imp_uid[]=&merchant_uid[]=test_1758786594793";

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type","application/json");
        header.add("Authorization","Bearer "+tokenResponse.getItem().getAccess_token());

        HttpEntity entity = new HttpEntity(header);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(url,HttpMethod.GET,entity,String.class);
        System.out.println(response.getBody());
    }
    @GetMapping("/pay/cancel")
    @ResponseBody
    public void pay_cancel(){
        log.info("GET /portOne/pay/cancel..");

        String url = HOSTNAME+"/payments/cancel";

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type","application/json");
        header.add("Authorization","Bearer "+tokenResponse.getItem().getAccess_token());

        JSONObject params = new JSONObject();
        params.put("merchant_uid","test_1758786594793");

        HttpEntity<String> entity = new HttpEntity(params.toJSONString(), header);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(url,HttpMethod.POST,entity,String.class);
        System.out.println(response.getBody());
    }

    @GetMapping("/certifications/{imp_uid}")
    @ResponseBody
    public ResponseEntity<String> certifications(@PathVariable("imp_uid")String imp_uid){
        //AccessToken 발급
        getToken();

        log.info("GET /portOne/certifications...");
        String url = HOSTNAME + "/certifications/"+imp_uid;
        //요청 헤더 설정
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+this.tokenResponse.getItem().getAccess_token());
        header.add("Content-Type","application/json");

        HttpEntity entity = new HttpEntity<>(header);

        //요청 후 응답확인
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(url, HttpMethod.GET,entity, String.class);

        System.out.println(response);

        return new ResponseEntity<>(response.getBody() , HttpStatus.OK);

    }
    //----------------------------
    //PORTONE ACCESSTOKEN CLASS
    //----------------------------
    @Data
    private static class Item{
        public String access_token;
        public int now;
        public int expired_at;
    }
    @Data
    private static class TokenResponse{
        public int code;
        public Object message;
        @JsonProperty("response")
        public Item item;
    }
    //----------------------------


}
