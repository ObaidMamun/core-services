/*    */ package org.egov.filestore.repository;
/*    */ 
/*    */ import com.amazonaws.auth.AWSCredentials;
/*    */ import com.amazonaws.auth.AWSCredentialsProvider;
/*    */ import com.amazonaws.auth.AWSStaticCredentialsProvider;
/*    */ import com.amazonaws.auth.BasicAWSCredentials;
/*    */ import com.amazonaws.regions.Regions;
/*    */ import com.amazonaws.services.s3.AmazonS3;
/*    */ import com.amazonaws.services.s3.AmazonS3ClientBuilder;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.boot.ApplicationArguments;
/*    */ import org.springframework.boot.ApplicationRunner;
/*    */ import org.springframework.core.annotation.Order;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ @Order(1)
/*    */ public class AWSClientFacade
/*    */   implements ApplicationRunner
/*    */ {
/*    */   @Value("${aws.key}")
/*    */   private String key;
/*    */   @Value("${aws.secretkey}")
/*    */   private String secretKey;
/*    */   @Value("${aws.region}")
/*    */   private String awsRegion;
/*    */   @Value("${isS3Enabled}")
/*    */   private Boolean isS3Enabled;
/*    */   private static AmazonS3 amazonS3Client;
/*    */   
/*    */   public void run(ApplicationArguments arg0) throws Exception {
/* 35 */     if (this.isS3Enabled.booleanValue()) {
/* 36 */       intializeS3Client();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void intializeS3Client() {
/* 46 */     AmazonS3 client = (AmazonS3)((AmazonS3ClientBuilder)((AmazonS3ClientBuilder)AmazonS3ClientBuilder.standard().withCredentials((AWSCredentialsProvider)new AWSStaticCredentialsProvider((AWSCredentials)new BasicAWSCredentials(this.key, this.secretKey)))).withRegion(Regions.valueOf(this.awsRegion))).build();
/* 47 */     amazonS3Client = client;
/*    */   }
/*    */   
/*    */   public AmazonS3 getS3Client() {
/* 51 */     return amazonS3Client;
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\repository\AWSClientFacade.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */