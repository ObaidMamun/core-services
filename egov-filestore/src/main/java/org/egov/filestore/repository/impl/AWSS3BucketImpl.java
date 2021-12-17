/*     */ package org.egov.filestore.repository.impl;
/*     */ 
/*     */ import com.amazonaws.services.s3.AmazonS3;
/*     */ import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
/*     */ import com.amazonaws.services.s3.model.ObjectMetadata;
/*     */ import com.amazonaws.services.s3.model.PutObjectRequest;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.imageio.ImageIO;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.egov.filestore.config.FileStoreConfig;
/*     */ import org.egov.filestore.domain.model.Artifact;
/*     */ import org.egov.filestore.repository.AWSClientFacade;
/*     */ import org.egov.filestore.repository.CloudFilesManager;
/*     */ import org.egov.tracer.model.CustomException;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.stereotype.Service;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ @Service
/*     */ @ConditionalOnProperty(value = {"isS3Enabled"}, havingValue = "true", matchIfMissing = true)
/*     */ public class AWSS3BucketImpl implements CloudFilesManager {
/*  36 */   private static final Logger log = LoggerFactory.getLogger(AWSS3BucketImpl.class);
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private FileStoreConfig configs;
/*     */ 
/*     */   
/*     */   private AmazonS3 s3Client;
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private AWSClientFacade awsFacade;
/*     */   
/*     */   @Autowired
/*     */   private CloudFileMgrUtils util;
/*     */   
/*     */   @Value("${aws.key}")
/*     */   private String key;
/*     */   
/*     */   @Value("${aws.secretkey}")
/*     */   private String secretKey;
/*     */   
/*     */   @Value("${aws.region}")
/*     */   private String awsRegion;
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
/*     */   @Value("${is.bucket.fixed}")
/*     */   private Boolean isBucketFixed;
/*     */   
/*     */   @Value("${presigned.url.expiry.time.in.secs}")
/*     */   private Long presignedUrlExpirytime;
/*     */   
/*  76 */   private List<String> scalableImageTypes = Arrays.asList(new String[] { "jpg", "png", "jpeg" });
/*     */ 
/*     */   
/*     */   public void saveFiles(List<Artifact> artifacts) {
/*  80 */     if (null == this.s3Client) {
/*  81 */       this.s3Client = this.awsFacade.getS3Client();
/*     */     }
/*  83 */     artifacts.forEach(artifact -> {
/*     */           String completeName = artifact.getFileLocation().getFileName();
/*     */           
/*     */           int index = completeName.indexOf('/');
/*     */           
/*     */           String bucketName = completeName.substring(0, index);
/*     */           
/*     */           String fileNameWithPath = completeName.substring(index + 1, completeName.length());
/*     */           if (!this.isBucketFixed.booleanValue() && !this.s3Client.doesBucketExistV2(bucketName)) {
/*     */             this.s3Client.createBucket(bucketName);
/*     */           }
/*     */           Long contentLength = Long.valueOf(artifact.getMultipartFile().getSize());
/*     */           try {
/*     */             String imagetype = FilenameUtils.getExtension(artifact.getMultipartFile().getOriginalFilename());
/*     */             String inputStreamAsString = artifact.getFileContentInString();
/*     */             InputStream inputStreamForUpload = IOUtils.toInputStream(inputStreamAsString, this.configs.getImageCharsetType());
/*     */             if (this.scalableImageTypes.contains(imagetype)) {
/*     */               InputStream ipStreamForImg = IOUtils.toInputStream(inputStreamAsString, this.configs.getImageCharsetType());
/*     */               String extension = FilenameUtils.getExtension(artifact.getMultipartFile().getOriginalFilename());
/*     */               Map<String, BufferedImage> mapOfImagesAndPaths = this.util.createVersionsOfImage(ipStreamForImg, fileNameWithPath);
/*     */               writeImage(mapOfImagesAndPaths, bucketName, extension);
/*     */             } 
/*     */             writeFile(inputStreamForUpload, bucketName, fileNameWithPath, contentLength);
/* 106 */           } catch (IOException e) {
/*     */             log.error("EG_FILESTORE_INPUT_ERROR", e);
/*     */             throw new CustomException("EG_FILESTORE_INPUT_ERROR", "Failed to read input stream from multipart file");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getFiles(Map<String, String> mspOfIdAndFilePath) {
/* 134 */     Map<String, String> urlMap = new HashMap<>();
/* 135 */     if (null == this.s3Client) {
/* 136 */       this.s3Client = this.awsFacade.getS3Client();
/*     */     }
/* 138 */     mspOfIdAndFilePath.keySet().forEach(fileStoreId -> {
/*     */           String completeName = (String)mspOfIdAndFilePath.get(fileStoreId);
/*     */           
/*     */           int index = completeName.indexOf('/');
/*     */           String bucketName = completeName.substring(0, index);
/*     */           String fileNameWithPath = completeName.substring(index + 1, completeName.length());
/*     */           String replaceString = fileNameWithPath.substring(fileNameWithPath.lastIndexOf('.'), fileNameWithPath.length());
/*     */           if (this.util.isFileAnImage((String)mspOfIdAndFilePath.get(fileStoreId)).booleanValue()) {
/*     */             StringBuilder url = new StringBuilder();
/*     */             url.append(generateSignedURL(bucketName, fileNameWithPath));
/*     */             String[] imageFormats = { this._large, this._medium, this._small };
/*     */             for (String format : Arrays.<String>asList(imageFormats)) {
/*     */               url.append(",");
/*     */               String path = fileNameWithPath;
/*     */               path = path.replaceAll(replaceString, format + replaceString);
/*     */               url.append(generateSignedURL(bucketName, path));
/*     */             } 
/*     */             urlMap.put(fileStoreId, url.toString());
/*     */           } else {
/*     */             urlMap.put(fileStoreId, generateSignedURL(bucketName, fileNameWithPath));
/*     */           } 
/*     */         });
/* 160 */     return urlMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String generateSignedURL(String bucketName, String fileName) {
/* 171 */     Date time = new Date(System.currentTimeMillis() + this.presignedUrlExpirytime.longValue() * 1000L);
/* 172 */     GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName);
/* 173 */     generatePresignedUrlRequest.setExpiration(time);
/* 174 */     return this.s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
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
/*     */   private void writeFile(InputStream content, String bucketName, String fileName, Long contentLength) {
/* 186 */     ObjectMetadata objMd = new ObjectMetadata();
/* 187 */     objMd.setContentLength(contentLength.longValue());
/* 188 */     this.s3Client.putObject(bucketName, fileName, content, objMd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeImage(Map<String, BufferedImage> mapOfImagesAndPaths, String bucketName, String extension) {
/* 199 */     Map<String, String> errorMap = new HashMap<>();
/* 200 */     for (String key : mapOfImagesAndPaths.keySet()) {
/*     */       try {
/* 202 */         this.s3Client.putObject(getPutObjectRequest(bucketName, key, mapOfImagesAndPaths.get(key), extension));
/* 203 */         ((BufferedImage)mapOfImagesAndPaths.get(key)).flush();
/* 204 */       } catch (Exception e) {
/* 205 */         errorMap.put("AWS_UPLOAD_FAILED", e.getMessage());
/*     */       } 
/*     */     } 
/* 208 */     if (!CollectionUtils.isEmpty(errorMap.keySet())) {
/* 209 */       throw new CustomException(errorMap);
/*     */     }
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
/*     */   private PutObjectRequest getPutObjectRequest(String bucketName, String key, BufferedImage originalImage, String extension) {
/* 223 */     ByteArrayOutputStream os = new ByteArrayOutputStream();
/*     */     try {
/* 225 */       ImageIO.write(originalImage, extension, os);
/* 226 */     } catch (IOException e) {
/* 227 */       log.error(" error while writing image to stream : {}", e);
/* 228 */       throw new CustomException("IMAGE_PROCESSING_FAILED", "Failed to process the image to be uploaded");
/*     */     } 
/* 230 */     ObjectMetadata metadata = new ObjectMetadata();
/* 231 */     metadata.setContentLength(os.size());
/* 232 */     return new PutObjectRequest(bucketName, key, new ByteArrayInputStream(os.toByteArray()), metadata);
/*     */   }
/*     */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\repository\impl\AWSS3BucketImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */