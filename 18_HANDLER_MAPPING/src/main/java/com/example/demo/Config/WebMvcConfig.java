package com.example.demo.Config;

import com.example.demo.Handler.CustomHandler;
import com.example.demo.Interceptor.MemoInterceptor;
import com.example.demo.Listener.C01CustomContextRefreshedListener;
import com.example.demo.Listener.C02RequestHandledEventListener;
import com.example.demo.Listener.MemoAddEventListener;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Configuration
//@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer, WebMvcRegistrations {


    //MULTIPART CONFIG
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofGigabytes(1));       // 파일 1개 최대(1G)
        factory.setMaxRequestSize(DataSize.ofGigabytes(1));    // 요청 전체 최대(1G)
        factory.setFileSizeThreshold(DataSize.ofGigabytes(1)); // 메모리 임계치(유사: maxInMemorySize)
        return factory.createMultipartConfig();
    }




//ViewResolver 설정(JSP 용)
//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        registry.jsp("/WEB-INF/views",".jsp");
//    }

    //JSP ViewResolver(직접 객체 생성)
//    @Bean
//    public ViewResolver viewResolver(){
//        InternalResourceViewResolver viewResolver
//                =new InternalResourceViewResolver();
//        viewResolver.setPrefix("/WEB-INF/views");
//        viewResolver.setSuffix(".jsp");
//        return viewResolver;
//    }


    // Thymeleaf 옵션 설정
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates/"); // Thymeleaf 템플릿 위치
//        resolver.setPrefix("classpath:/");
        resolver.setSuffix(".html");                 // 확장자
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false); // 개발 중에는 캐시 끄기
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        engine.setEnableSpringELCompiler(true);
        return engine;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setOrder(1);
        return viewResolver;
    }


    //정적자원 관리(classpath:/static 이 기본경로)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/css/**").addResourceLocations("classpath:/css/");
        registry.addResourceHandler("/resources/js/**").addResourceLocations("classpath:/js/");
    }

    @Autowired
    MemoInterceptor memoInterceptor;

    //인터셉터 추가
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(memoInterceptor)
//                    .addPathPatterns("/**")// '/memo 의 모든요청'
//                    .excludePathPatterns("/resources/css/**","/resources/js/**"); //인터셉터 제외
//
//    }
    //리스너 등록 -!!!----!!!!
    @Bean
    public C01CustomContextRefreshedListener customContextRefreshedListener(){
        return new C01CustomContextRefreshedListener();
    }
    @Bean
    public C02RequestHandledEventListener requestHandledEventListener(){
        return new C02RequestHandledEventListener();
    }

    @Bean
    public MemoAddEventListener memoAddEventListener(){
        return new MemoAddEventListener();
    }

    //핸들러 매핑
    //1) BEAN MAPPING(BeanNameUrlHandlerMapping)
    // 요청 URL을 동일한 이름을 가진 Bean 에 매핑
    @Bean
    BeanNameUrlHandlerMapping beanNameUrlHandlerMapping(){
        System.out.println("BeanNameUrlHandlerMapping bean init!");
        return new BeanNameUrlHandlerMapping();
    }
    @Bean(name="/custom_01")
    public CustomHandler customHandler(){
        return new CustomHandler();
    }
    //2)SimpleUrlHandlerMapping: 정적자원에 대한 매핑정보 설정(기본값)
    //+개발자가 매핑정보를 추가설정가능
    @Bean
    SimpleUrlHandlerMapping simpleUrlHandlerMapping(){
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();

        Map<String,Object> map = new HashMap();
        map.put("/custom_02",new CustomHandler());

        handlerMapping.setUrlMap(map);
        handlerMapping.setOrder(0);
        return handlerMapping;
    }

    //03 RequestMappingHandlerMapping
    //Controller와 매핑되는 URL을 찾아내고 해당 URL에 대한 요청 처리
//    @Bean(name="requestMappingHandlerMapping")
//    public RequestMappingHandlerMapping requestMappingHandlerMapping() throws NoSuchMethodException {
//        RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
//
//        //어떤 함수를 실행할건지 설정(Reflection)
//        Method method=
//        CustomHandler.class.getMethod("HelloWorld",null);
//
//        //어떤 요청에 대한 매핑 설정가능(GET/POST/PUT/DELETE...)
//        RequestMappingInfo info = RequestMappingInfo
//                .paths("/custom_03")
//                .methods(RequestMethod.GET)
//                .build();
//
//        //핸들러에 등록
//        handlerMapping.registerMapping(info,new CustomHandler(),method);
//
//        return handlerMapping;
//    }


    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();

        //어떤 함수를 실행할건지 설정(Reflection)
        Method method=
                null;
        try {
            method = CustomHandler.class.getMethod("HelloWorld",null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        //어떤 요청에 대한 매핑 설정가능(GET/POST/PUT/DELETE...)
        RequestMappingInfo info = RequestMappingInfo
                .paths("/custom_03")
                .methods(RequestMethod.GET)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .build();

        //핸들러에 등록
        handlerMapping.registerMapping(info,new CustomHandler(),method);

        return handlerMapping;

    }
}
