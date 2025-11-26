package com.example.demo.Restful.C01Opendata;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

//대구광역시_돌발 교통정보 조회 서비스(신)

@RestController
@Slf4j
@RequestMapping("/BUS")
public class OepnData03Controller {
    private String server ="https://apis.data.go.kr/6270000/dbmsapi02/getRealtime02";
    private String serviceKey;
    private String bsId;
    private String routeNo;

//정류소 ID : 7001001600 (반월당역(2번출구))
//버스번호 : 649
//버스노선ID : 3000649000
    @GetMapping(value = "/{bsId}/{routeNo}" )
    public void get(
            @PathVariable(value = "bsId",required = true) String bsId,
            @PathVariable(value = "routeNo",required = true) String routeNo,
            Model model
    )
    {
        log.info("GET /INCIDENT....bsId : "+bsId+" routeNo : " + routeNo );
        this.bsId = bsId;
        this.routeNo = routeNo;

        // 파라미터 설정( service Key 포함)
//        String url = UriComponentsBuilder.fromHttpUrl(server)
//                .queryParam("serviceKey",serviceKey)
//                .queryParam("pageNo",pageNo)
//                .queryParam("numOfRows",numOfRows)
//                .toUriString();
//        System.out.println(url);

        String url = server;
        url+="?serviceKey=" + serviceKey;
        url+="&bsId=" + bsId;
        url+="&routeNo="+routeNo;


        RestTemplate restTemplate = new RestTemplate();

//        // 요청 헤더 설정(x)
//        HttpHeaders header = new HttpHeaders();
//        header.add("key","value");
//        // 요청 바디 설정(x)
//        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
//        params.add("","");
//
//        HttpEntity< MultiValueMap<String,String>  > entity = new HttpEntity<>(params,header);

        // 요청 후 응답 확인
        ResponseEntity<Root> response =
        restTemplate.exchange(url, HttpMethod.GET,null,Root.class);

        //REST TYPE -> Class Type 변환
        System.out.println(response.getBody());

        //확인
        Root root = response.getBody();
        Body body = root.getBody();
        ArrayList<Item> items =  body.getItems();
        items.forEach((item)->{
            System.out.println(item);
        });



    }


    // RESPONSE CLASS
    @Data
    private static class ArrList{
        public String routeId;
        public String routeNo;
        public String moveDir;
        public int bsGap;
        public String bsNm;
        public String vhcNo2;
        public String busTCd2;
        public String busTCd3;
        public String busAreaCd;
        public String arrState;
        public int prevBsGap;
        public int arrTime;
    }
    @Data
    private static class Body{
        public ArrayList<Item> items;
        public int totalCount;
    }
    @Data
    private static class Header{
        public boolean success;
        public String resultCode;
        public String resultMsg;
    }
    @Data
    private static class Item{
        public String routeNo;
        public ArrayList<ArrList> arrList;
    }
    @Data
    private static class Root{
        public Header header;
        public Body body;
    }




}
