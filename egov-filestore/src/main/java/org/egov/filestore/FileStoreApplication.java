/*    */ package org.egov.filestore;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.MapperFeature;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Locale;
/*    */ import java.util.TimeZone;
/*    */ import javax.annotation.PostConstruct;
/*    */ import org.egov.tracer.config.TracerConfiguration;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.boot.SpringApplication;
/*    */ import org.springframework.boot.autoconfigure.SpringBootApplication;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Import;
/*    */ import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @SpringBootApplication
/*    */ @Import({TracerConfiguration.class})
/*    */ public class FileStoreApplication
/*    */ {
/*    */   private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
/*    */   @Value("${app.timezone}")
/*    */   private String timeZone;
/*    */   
/*    */   @PostConstruct
/*    */   public void initialize() {
/* 30 */     TimeZone.setDefault(TimeZone.getTimeZone(this.timeZone));
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public MappingJackson2HttpMessageConverter jacksonConverter() {
/* 35 */     MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
/* 36 */     ObjectMapper mapper = new ObjectMapper();
/* 37 */     mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
/* 38 */     mapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH));
/* 39 */     mapper.setTimeZone(TimeZone.getTimeZone(this.timeZone));
/* 40 */     converter.setObjectMapper(mapper);
/* 41 */     return converter;
/*    */   }
/*    */ 
/*    */   
/*    */   @Bean
/*    */   public ObjectMapper getObjectMapper() {
/* 47 */     ObjectMapper objectMapper = new ObjectMapper();
/* 48 */     objectMapper.setTimeZone(TimeZone.getTimeZone(this.timeZone));
/* 49 */     return objectMapper;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void main(String[] args) {
/* 54 */     SpringApplication.run(FileStoreApplication.class, args);
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\FileStoreApplication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */