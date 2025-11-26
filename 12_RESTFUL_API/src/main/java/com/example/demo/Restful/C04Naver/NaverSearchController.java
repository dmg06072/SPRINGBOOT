package com.example.demo.Restful.C04Naver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
@RequestMapping("/naver/search")
public class NaverSearchController {

    private String CLIENT_ID;
    private String CLIENT_SECRET;

    @GetMapping("/book/{keyword}")
    public void search_book(@PathVariable String keyword)
    {
        log.info("GET /naver/search/book..keyword : " + keyword);

        String url = "https://openapi.naver.com/v1/search/book.json?query="+keyword;

        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders header = new HttpHeaders();
        header.add("X-Naver-Client-Id",CLIENT_ID);
        header.add("X-Naver-Client-Secret",CLIENT_SECRET);
        // 요청 바디 파라미터(X)

        HttpEntity entity = new HttpEntity(header);

        // 요청 후 응답 확인
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);
        System.out.println(response.getBody());
    }

}
