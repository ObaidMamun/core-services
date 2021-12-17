/*     */ package org.egov.filestore.repository.impl;
/*     */ 
/*     */ import com.microsoft.azure.storage.OperationContext;
/*     */ import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
/*     */ import com.microsoft.azure.storage.blob.BlobRequestOptions;
/*     */ import com.microsoft.azure.storage.blob.CloudBlobClient;
/*     */ import com.microsoft.azure.storage.blob.CloudBlobContainer;
/*     */ import com.microsoft.azure.storage.blob.CloudBlockBlob;
/*     */ import com.microsoft.azure.storage.blob.ListBlobItem;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.imageio.ImageIO;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ import org.egov.filestore.domain.model.Artifact;
/*     */ import org.egov.filestore.repository.AzureClientFacade;
/*     */ import org.egov.filestore.repository.CloudFilesManager;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ @Service
/*     */ @ConditionalOnProperty(value = {"isAzureStorageEnabled"}, havingValue = "true")
/*     */ public class AzureBlobStorageImpl
/*     */   implements CloudFilesManager {
/*  35 */   private static final Logger log = LoggerFactory.getLogger(AzureBlobStorageImpl.class);
/*     */ 
/*     */   
/*     */   private CloudBlobClient azureBlobClient;
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private AzureClientFacade azureFacade;
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private CloudFileMgrUtils util;
/*     */ 
/*     */   
/*     */   @Value("${is.container.fixed}")
/*     */   private Boolean isContainerFixed;
/*     */ 
/*     */   
/*     */   @Value("${fixed.container.name}")
/*     */   private String fixedContainerName;
/*     */ 
/*     */   
/*     */   @Value("${azure.blob.host}")
/*     */   private String azureBlobStorageHost;
/*     */ 
/*     */   
/*     */   @Value("${azure.accountName}")
/*     */   private String azureAccountName;
/*     */   
/*     */   @Value("${azure.accountKey}")
/*     */   private String azureAccountKey;
/*     */   
/*     */   @Value("${image.small}")
/*     */   private String _small;
/*     */   
/*     */   @Value("${image.medium}")
/*     */   private String _medium;
/*     */   
/*     */   @Value("${image.large}")
/*     */   private String _large;
/*     */ 
/*     */   
/*     */   public void saveFiles(List<Artifact> artifacts) {
/*  78 */     if (null == this.azureBlobClient) {
/*  79 */       this.azureBlobClient = this.azureFacade.getAzureClient();
/*     */     }
/*  81 */     artifacts.forEach(artifact -> {
/*     */           CloudBlobContainer container = null;
/*     */           
/*     */           String completeName = artifact.getFileLocation().getFileName();
/*     */           int index = completeName.indexOf('/');
/*     */           String containerName = completeName.substring(0, index);
/*     */           String fileNameWithPath = completeName.substring(index + 1, completeName.length());
/*     */           try {
/*     */             if (this.isContainerFixed.booleanValue()) {
/*     */               container = this.azureBlobClient.getContainerReference(this.fixedContainerName);
/*     */             } else {
/*     */               container = this.azureBlobClient.getContainerReference(containerName);
/*     */             } 
/*     */             container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());
/*     */             Long contentLength = Long.valueOf(artifact.getMultipartFile().getSize());
/*     */             BufferedInputStream inputStream = new BufferedInputStream(artifact.getMultipartFile().getInputStream());
/*     */             if (artifact.getMultipartFile().getContentType().startsWith("image/")) {
/*     */               String extension = FilenameUtils.getExtension(artifact.getMultipartFile().getOriginalFilename());
/*     */               Map<String, BufferedImage> mapOfImagesAndPaths = this.util.createVersionsOfImage(inputStream, fileNameWithPath);
/*     */               for (String key : mapOfImagesAndPaths.keySet()) {
/*     */                 upload(container, key, null, null, mapOfImagesAndPaths.get(key), extension);
/*     */                 ((BufferedImage)mapOfImagesAndPaths.get(key)).flush();
/*     */               } 
/*     */             } 
/*     */             upload(container, fileNameWithPath, inputStream, contentLength, null, null);
/*     */             for (ListBlobItem blobItem : container.listBlobs()) {
/*     */               log.info("URI of blob is: " + blobItem.getStorageUri().getPrimaryUri());
/*     */             }
/* 109 */           } catch (Exception e) {
/*     */             log.error("Exceptione while creating the container: ", e);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getFiles(Map<String, String> mapOfIdAndFilePath) {
/* 128 */     if (null == this.azureBlobClient)
/* 129 */       this.azureBlobClient = this.azureFacade.getAzureClient(); 
/* 130 */     Map<String, String> mapOfIdAndSASUrls = new HashMap<>();
/* 131 */     mapOfIdAndFilePath.keySet().forEach(id -> {
/*     */           if (this.util.isFileAnImage((String)mapOfIdAndFilePath.get(id)).booleanValue()) {
/*     */             StringBuilder url = new StringBuilder();
/*     */             
/*     */             String[] imageFormats = { this._large, this._medium, this._small };
/*     */             
/*     */             url.append(getSASURL((String)mapOfIdAndFilePath.get(id), this.util.generateSASToken(this.azureBlobClient, (String)mapOfIdAndFilePath.get(id))));
/*     */             
/*     */             String replaceString = ((String)mapOfIdAndFilePath.get(id)).substring(((String)mapOfIdAndFilePath.get(id)).lastIndexOf('.'), ((String)mapOfIdAndFilePath.get(id)).length());
/*     */             for (String format : Arrays.<String>asList(imageFormats)) {
/*     */               url.append(",");
/*     */               String path = (String)mapOfIdAndFilePath.get(id);
/*     */               path = path.replaceAll(replaceString, format + replaceString);
/*     */               url.append(getSASURL(path, this.util.generateSASToken(this.azureBlobClient, path)));
/*     */             } 
/*     */             mapOfIdAndSASUrls.put(id, url.toString());
/*     */           } else {
/*     */             mapOfIdAndSASUrls.put(id, getSASURL((String)mapOfIdAndFilePath.get(id), this.util.generateSASToken(this.azureBlobClient, (String)mapOfIdAndFilePath.get(id))));
/*     */           } 
/*     */         });
/* 151 */     return mapOfIdAndSASUrls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getSASURL(String path, String sasToken) {
/* 163 */     StringBuilder sasURL = new StringBuilder();
/* 164 */     String host = this.azureBlobStorageHost.replace("$accountName", this.azureAccountName);
/* 165 */     sasURL.append(host).append("/").append(path).append("?").append(sasToken);
/* 166 */     return sasURL.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upload(CloudBlobContainer container, String completePath, InputStream inputStream, Long contentLength, BufferedImage image, String extension) {
/*     */     try {
/* 181 */       if (null == inputStream && null != image) {
/* 182 */         ByteArrayOutputStream os = new ByteArrayOutputStream();
/* 183 */         ImageIO.write(image, extension, os);
/* 184 */         CloudBlockBlob blob = container.getBlockBlobReference(completePath);
/* 185 */         blob.upload(new ByteArrayInputStream(os.toByteArray()), 8388608L);
/*     */       } else {
/* 187 */         CloudBlockBlob blob = container.getBlockBlobReference(completePath);
/* 188 */         blob.upload(inputStream, contentLength.longValue());
/*     */       }
/*     */     
/* 191 */     } catch (Exception e) {
/* 192 */       log.error("Exception while uploading the file: ", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\repository\impl\AzureBlobStorageImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */