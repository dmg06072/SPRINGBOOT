package com.example.demo.Restful.C02OpenWeatherMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@Slf4j
@RequestMapping("/OPEN_WEATHER")
public class OpenWeatherMapController {
    private String server ="https://api.openweathermap.org/data/2.5/weather";
    private String appid;

    @GetMapping("/{lat}/{lon}")
    public void get(
        @PathVariable("lat") String lat,
        @PathVariable("lon") String lon,
        Model model
    ) throws UnsupportedEncodingException {
        log.info("GET /OPEN_WEATHER...");
        //파라미터 설정( service Key 포함)
        String url = UriComponentsBuilder.fromHttpUrl(server)
                .queryParam("appid",URLEncoder.encode(appid,"UTF-8"))
                .queryParam("lat",lat)
                .queryParam("lon",lon)
                .toUriString();
        System.out.println(url);
        //요청 헤더 x
        //요청 바디 x

        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response =
        rt.exchange(url,HttpMethod.GET,null,String.class);
        System.out.println(response.getBody());
    }
}
