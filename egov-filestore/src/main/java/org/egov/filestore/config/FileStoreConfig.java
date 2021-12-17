/*    */ package org.egov.filestore.config;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import javax.annotation.PostConstruct;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ 
/*    */ @Configuration
/*    */ public class FileStoreConfig {
/*    */   @Value("${image.charset.type}")
/*    */   private String imageCharsetType;
/*    */   @Value("#{${allowed.formats.map}}")
/*    */   private Map<String, List<String>> allowedFormatsMap;
/*    */   private Set<String> allowedKeySet;
/*    */   
/*    */   public String getImageCharsetType() {
/* 19 */     return this.imageCharsetType;
/*    */   }
/*    */   public Map<String, List<String>> getAllowedFormatsMap() {
/* 22 */     return this.allowedFormatsMap;
/*    */   } public Set<String> getAllowedKeySet() {
/* 24 */     return this.allowedKeySet;
/*    */   }
/*    */   @PostConstruct
/*    */   private void enrichKeysetForFormats() {
/* 28 */     this.allowedKeySet = this.allowedFormatsMap.keySet();
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\config\FileStoreConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */