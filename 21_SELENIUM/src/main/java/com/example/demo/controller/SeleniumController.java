package com.example.demo.controller;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class SeleniumController {

    @GetMapping("/crawl")
    public String crawlNaverSearch() {
        WebDriver driver = null;
        String resultMessage;

        // 1. WebDriverManager 설정 및 Headless 옵션 설정
        // 이 과정은 서버 환경에서 브라우저 드라이버를 쉽게 사용할 수 있게 합니다.
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");             // 필수: GUI 없이 실행
//        options.addArguments("--no-sandbox");           // 리눅스 환경에서 권장
//        options.addArguments("--disable-dev-shm-usage"); // 메모리 관련 최적화
//        options.addArguments("--disable-gpu");
//        options.addArguments("--window-size=1920,1080");

        try {
            // 2. WebDriver 인스턴스 생성 (옵션 적용)
            driver = new ChromeDriver(options);

            // 3. 네이버 접속
            driver.get("https://www.instagram.com/");

            // 4. 검색창 요소 찾기 및 검색어 입력
            // 네이버의 검색창 요소는 일반적으로 'query'라는 name을 가집니다.
            WebElement loginForm = driver.findElement(By.id("loginForm"));
            // 2. 부모 요소 내부에서 모든 'input' 태그를 찾음
            List<WebElement> inputElements = loginForm.findElements(By.tagName("input"));

            // 3. 찾은 input 요소들을 순회하며 작업 수행 (예: 속성 출력)
            for (WebElement input : inputElements) {
                String type = input.getAttribute("type");
                String name = input.getAttribute("name");
                System.out.println("Input Found - Type: " + type + ", Name: " + name);
                if ("username".equals(name)) {
                    input.sendKeys("abcd@naver.com");
                    // 다른 작업을 할 필요가 없다면, 여기서 루프를 종료해도 됩니다.
                    // break;
                }
            }

        } catch (Exception e) {
            // 크롤링 중 오류 발생 시
            e.printStackTrace();
            resultMessage = "크롤링 중 오류가 발생했습니다: " + e.getMessage();

        } finally {
            // 6. WebDriver 종료 (매우 중요: 자원 누수 방지)
            if (driver != null) {
//                driver.quit();
            }
        }

        return null;
    }
}