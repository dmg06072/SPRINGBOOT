package com.example.demo.Restful.C03Kakao;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
@RequestMapping("/kakao/pay")
public class C04KakaoPayController {


    private String SECRET_KEY;
    //단건결제 요청

    @GetMapping("/req")
    @ResponseBody
    public void req(){
        log.info("GET /kakao/pay/req....");

        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";

        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","SECRET_KEY "+SECRET_KEY);
        header.add("Content-Type","application/json");

        // 파라미터
        JSONObject params = new JSONObject(); // {}
        params.put("cid","TC0ONETIME");
        params.put("partner_order_id","1001");
        params.put("partner_user_id","user10");
        params.put("item_name","초코파이[정]");
        params.put("quantity","2");
        params.put("total_amount","4400");
        params.put("vat_amount","400");
        params.put("tax_free_amount","0");
        params.put("approval_url","http://localhost:8099/kakao/pay/success");  //결제 성공시 REDIRECT_URL
        params.put("fail_url","http://localhost:8099/kakao/pay/fail");      //결제 실패시 REDIRECT_URL
        params.put("cancel_url","http://localhost:8099/kakao/pay/cancel");    //결제 취소시 REDIRECT_URL

        // 요청 바디 파라미터(O)
        HttpEntity<JSONObject> entity = new HttpEntity(params,header);
        // 요청 후 응답 확인
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST,entity, String.class);
        System.out.println(response.getBody());
    }
    @GetMapping("/success") @ResponseBody
    public void success(){ log.info("GET /kakao/pay/success....");}
    @GetMapping("/fail") @ResponseBody
    public void fail(){log.info("GET /kakao/pay/fail....");}
    @GetMapping("/cancel") @ResponseBody
    public void cancel(){log.info("GET /kakao/pay/cancel....");}
    
}
