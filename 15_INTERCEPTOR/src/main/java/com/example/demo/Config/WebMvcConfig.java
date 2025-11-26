package com.example.demo.Config;

import com.example.demo.Interceptor.MemoInterceptor;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

@Configuration
//@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {


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
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memoInterceptor)
                    .addPathPatterns("/**")// '/memo 의 모든요청'
                    .excludePathPatterns("/resources/css/**","/resources/js/**"); //인터셉터 제외

    }

}
