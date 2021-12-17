/*    */ package org.egov.filestore.repository;
/*    */ import com.microsoft.azure.storage.CloudStorageAccount;
/*    */ import com.microsoft.azure.storage.blob.CloudBlobClient;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.boot.ApplicationArguments;
/*    */ import org.springframework.boot.ApplicationRunner;
/*    */ import org.springframework.core.annotation.Order;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ @Order(2)
/*    */ public class AzureClientFacade implements ApplicationRunner {
/* 15 */   private static final Logger log = LoggerFactory.getLogger(AzureClientFacade.class);
/*    */ 
/*    */   
/*    */   @Value("${azure.defaultEndpointsProtocol}")
/*    */   private String defaultEndpointsProtocol;
/*    */ 
/*    */   
/*    */   @Value("${azure.accountName}")
/*    */   private String accountName;
/*    */   
/*    */   @Value("${azure.accountKey}")
/*    */   private String accountKey;
/*    */   
/*    */   @Value("${isAzureStorageEnabled}")
/*    */   private Boolean isAzureEnabled;
/*    */   
/*    */   private static CloudBlobClient cloudBlobClient;
/*    */ 
/*    */   
/*    */   public void run(ApplicationArguments arg0) throws Exception {
/* 35 */     if (this.isAzureEnabled.booleanValue()) {
/* 36 */       initializeAzureClient();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void initializeAzureClient() {
/* 44 */     StringBuilder storageConnectionString = new StringBuilder();
/* 45 */     storageConnectionString.append("DefaultEndpointsProtocol=").append(this.defaultEndpointsProtocol).append(";")
/* 46 */       .append("AccountName=").append(this.accountName).append(";").append("AccountKey=").append(this.accountKey);
/* 47 */     CloudStorageAccount storageAccount = null;
/* 48 */     CloudBlobClient blobClient = null;
/*    */     try {
/* 50 */       storageAccount = CloudStorageAccount.parse(storageConnectionString.toString());
/* 51 */       if (null != storageAccount) {
/* 52 */         blobClient = storageAccount.createCloudBlobClient();
/*    */       }
/* 54 */     } catch (Exception e) {
/* 55 */       log.error("Exception while intializing client: ", e);
/*    */     } 
/* 57 */     cloudBlobClient = blobClient;
/*    */   }
/*    */   
/*    */   public CloudBlobClient getAzureClient() {
/* 61 */     return cloudBlobClient;
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\repository\AzureClientFacade.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */