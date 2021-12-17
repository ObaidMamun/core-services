/*     */ package org.egov.filestore.repository.impl;
/*     */ 
/*     */ import com.microsoft.azure.storage.blob.CloudBlobClient;
/*     */ import com.microsoft.azure.storage.blob.CloudBlobContainer;
/*     */ import com.microsoft.azure.storage.blob.CloudBlockBlob;
/*     */ import com.microsoft.azure.storage.blob.SharedAccessBlobPolicy;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.BufferedImageOp;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Base64;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.crypto.Mac;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import javax.imageio.ImageIO;
/*     */ import org.egov.tracer.model.CustomException;
/*     */ import org.imgscalr.Scalr;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class CloudFileMgrUtils
/*     */ {
/*  32 */   private static final Logger log = LoggerFactory.getLogger(CloudFileMgrUtils.class);
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${image.small}")
/*     */   private String _small;
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${image.medium}")
/*     */   private String _medium;
/*     */ 
/*     */   
/*     */   @Value("${image.large}")
/*     */   private String _large;
/*     */ 
/*     */   
/*     */   @Value("${image.small.width}")
/*     */   private Integer smallWidth;
/*     */ 
/*     */   
/*     */   @Value("${image.medium.width}")
/*     */   private Integer mediumWidth;
/*     */ 
/*     */   
/*     */   @Value("${image.large.width}")
/*     */   private Integer largeWidth;
/*     */ 
/*     */   
/*     */   @Value("${azure.sas.expiry.time.in.secs}")
/*     */   private Integer azureSASExpiryinSecs;
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, BufferedImage> createVersionsOfImage(InputStream inputStream, String fileName) {
/*  67 */     Map<String, BufferedImage> mapOfImagesAndPaths = new HashMap<>();
/*     */     
/*     */     try {
/*  70 */       BufferedImage originalImage = ImageIO.read(inputStream);
/*     */       
/*  72 */       if (null == originalImage) {
/*     */         
/*  74 */         Map<String, String> map = new HashMap<>();
/*  75 */         map.put("Image Source Unavailable", "Image File present in upload request is Invalid/Not Readable");
/*  76 */         throw new CustomException(map);
/*     */       } 
/*     */       
/*  79 */       BufferedImage largeImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, this.mediumWidth.intValue(), new BufferedImageOp[] { null, Scalr.OP_ANTIALIAS });
/*     */       
/*  81 */       BufferedImage mediumImg = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, this.mediumWidth.intValue(), new BufferedImageOp[] { null, Scalr.OP_ANTIALIAS });
/*     */       
/*  83 */       BufferedImage smallImg = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, this.smallWidth.intValue(), new BufferedImageOp[] { null, Scalr.OP_ANTIALIAS });
/*     */ 
/*     */       
/*  86 */       int lastIndex = fileName.length();
/*  87 */       String replaceString = fileName.substring(fileName.lastIndexOf('.'), lastIndex);
/*     */       
/*  89 */       mapOfImagesAndPaths.put(fileName.replace(replaceString, this._large + replaceString), largeImage);
/*  90 */       mapOfImagesAndPaths.put(fileName.replace(replaceString, this._medium + replaceString), mediumImg);
/*  91 */       mapOfImagesAndPaths.put(fileName.replace(replaceString, this._small + replaceString), smallImg);
/*     */       
/*  93 */       log.info("Different versions of the image created!");
/*  94 */     } catch (Exception e) {
/*  95 */       log.error("Error while creating different versions of the image: ", e);
/*     */     } 
/*     */     
/*  98 */     return mapOfImagesAndPaths;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String generateSASToken(CloudBlobClient azureBlobClient, String absolutePath) {
/* 118 */     String sasUrl = null;
/*     */     try {
/* 120 */       int index = absolutePath.indexOf('/');
/* 121 */       String containerName = absolutePath.substring(0, index);
/* 122 */       String fileNameWithPath = absolutePath.substring(index + 1, absolutePath.length());
/* 123 */       CloudBlobContainer container = azureBlobClient.getContainerReference(containerName);
/* 124 */       CloudBlockBlob blob = (CloudBlockBlob)container.getBlobReferenceFromServer(fileNameWithPath);
/* 125 */       SharedAccessBlobPolicy sasConstraints = new SharedAccessBlobPolicy();
/* 126 */       sasConstraints.setSharedAccessStartTime(new Date(System.currentTimeMillis()));
/* 127 */       sasConstraints
/* 128 */         .setSharedAccessExpiryTime(new Date(System.currentTimeMillis() + (this.azureSASExpiryinSecs.intValue() * 1000)));
/* 129 */       sasConstraints.setPermissionsFromString("r");
/* 130 */       String sasBlobToken = blob.generateSharedAccessSignature(sasConstraints, null);
/* 131 */       sasUrl = sasBlobToken;
/* 132 */     } catch (Exception e) {
/* 133 */       log.error("Error while generating sas token: ", e);
/* 134 */       log.error("Exception while generating SAS token: ", e);
/*     */     } 
/* 136 */     return sasUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getHMAC256(String key, String input) {
/* 147 */     Mac sha256_HMAC = null;
/* 148 */     String hash = null;
/*     */     try {
/* 150 */       sha256_HMAC = Mac.getInstance("HmacSHA256");
/* 151 */       SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
/* 152 */       sha256_HMAC.init(secret_key);
/* 153 */       Base64.Encoder encoder = Base64.getEncoder();
/* 154 */       hash = new String(encoder.encode(sha256_HMAC.doFinal(input.getBytes("UTF-8"))));
/* 155 */     } catch (Exception e) {
/* 156 */       log.error("Exception while generating hash for the SAS token: ", e);
/*     */     } 
/*     */     
/* 159 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean isFileAnImage(String filePath) {
/* 170 */     Boolean isFileAnImage = Boolean.valueOf(false);
/* 171 */     String[] imageFormats = { "png", "jpeg", "jpg" };
/* 172 */     if ((filePath.split("[\\.]")).length > 1) {
/* 173 */       String extension = filePath.substring(filePath.lastIndexOf('.') + 1, filePath.length());
/* 174 */       if (Arrays.<String>asList(imageFormats).contains(extension))
/* 175 */         isFileAnImage = Boolean.valueOf(true); 
/*     */     } 
/* 177 */     return isFileAnImage;
/*     */   }
/*     */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\repository\impl\CloudFileMgrUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */