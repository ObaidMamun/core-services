/*     */ package org.egov.filestore.persistence.repository;
/*     */ 
/*     */ import com.amazonaws.auth.AWSCredentials;
/*     */ import com.amazonaws.auth.AWSCredentialsProvider;
/*     */ import com.amazonaws.auth.AWSStaticCredentialsProvider;
/*     */ import com.amazonaws.auth.BasicAWSCredentials;
/*     */ import com.amazonaws.regions.Regions;
/*     */ import com.amazonaws.services.s3.AmazonS3;
/*     */ import com.amazonaws.services.s3.AmazonS3ClientBuilder;
/*     */ import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
/*     */ import com.amazonaws.services.s3.model.GetObjectRequest;
/*     */ import com.amazonaws.services.s3.model.ObjectMetadata;
/*     */ import com.amazonaws.services.s3.model.PutObjectRequest;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.BufferedImageOp;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.imageio.ImageIO;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ import org.egov.filestore.domain.model.FileLocation;
/*     */ import org.egov.filestore.persistence.entity.Artifact;
/*     */ import org.egov.tracer.model.CustomException;
/*     */ import org.imgscalr.Scalr;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.core.io.FileSystemResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.stereotype.Repository;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ @Repository
/*     */ public class AwsS3Repository {
/*  43 */   private static final Logger log = LoggerFactory.getLogger(AwsS3Repository.class);
/*     */ 
/*     */   
/*     */   @Value("${aws.key}")
/*     */   private String key;
/*     */ 
/*     */   
/*     */   @Value("${aws.secretkey}")
/*     */   private String secretKey;
/*     */ 
/*     */   
/*     */   @Value("${aws.region}")
/*     */   private String awsRegion;
/*     */ 
/*     */   
/*     */   @Value("${image.small}")
/*     */   private String _small;
/*     */ 
/*     */   
/*     */   @Value("${image.medium}")
/*     */   private String _medium;
/*     */ 
/*     */   
/*     */   @Value("${image.large}")
/*     */   private String _large;
/*     */   
/*     */   @Value("${image.small.width}")
/*     */   private Integer smallWidth;
/*     */   
/*     */   @Value("${image.medium.width}")
/*     */   private Integer mediumWidth;
/*     */   
/*     */   @Value("${image.large.width}")
/*     */   private Integer largeWidth;
/*     */   
/*     */   @Value("${is.bucket.fixed}")
/*     */   private Boolean isBucketFixed;
/*     */   
/*     */   @Value("${presigned.url.expiry.time.in.secs}")
/*     */   private Long presignedUrlExpirytime;
/*     */   
/*     */   private AmazonS3 s3Client;
/*     */   
/*     */   private static final String TEMP_FILE_PATH_NAME = "TempFolder/localFile";
/*     */ 
/*     */   
/*     */   public void writeToS3(MultipartFile file, FileLocation fileLocation) {
/*  90 */     if (null == this.s3Client)
/*  91 */       getS3Client(); 
/*  92 */     String completeName = fileLocation.getFileName();
/*  93 */     int index = completeName.indexOf('/');
/*  94 */     String bucketName = completeName.substring(0, index);
/*  95 */     String fileNameWithPath = completeName.substring(index + 1, completeName.length());
/*     */     
/*  97 */     if (!this.isBucketFixed.booleanValue() && !this.s3Client.doesBucketExistV2(bucketName)) {
/*  98 */       this.s3Client.createBucket(bucketName);
/*     */     }
/* 100 */     if (file.getContentType().startsWith("image/")) {
/* 101 */       writeImage(file, bucketName, fileNameWithPath);
/*     */     } else {
/* 103 */       writeFile(file, bucketName, fileNameWithPath);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeFile(MultipartFile file, String bucketName, String fileName) {
/* 109 */     InputStream is = null;
/* 110 */     long contentLength = file.getSize();
/*     */     try {
/* 112 */       is = file.getInputStream();
/* 113 */     } catch (IOException e) {
/* 114 */       log.error(" exception occured while reading input stream from file {}", e);
/* 115 */       throw new RuntimeException(e);
/*     */     } 
/* 117 */     ObjectMetadata objMd = new ObjectMetadata();
/* 118 */     objMd.setContentLength(contentLength);
/*     */     
/* 120 */     this.s3Client.putObject(bucketName, fileName, is, objMd);
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeImage(MultipartFile file, String bucketName, String fileName) {
/*     */     try {
/* 126 */       log.info(" the file name " + file.getName());
/* 127 */       log.info(" the file size " + file.getSize());
/* 128 */       log.info(" the file content " + file.getContentType());
/*     */       
/* 130 */       BufferedImage originalImage = ImageIO.read(file.getInputStream());
/*     */       
/* 132 */       if (null == originalImage) {
/* 133 */         Map<String, String> map = new HashMap<>();
/* 134 */         map.put("Image Source Unavailable", "Image File present in upload request is Invalid/Not Readable");
/* 135 */         throw new CustomException(map);
/*     */       } 
/*     */       
/* 138 */       BufferedImage largeImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, this.mediumWidth.intValue(), new BufferedImageOp[] { null, Scalr.OP_ANTIALIAS });
/*     */       
/* 140 */       BufferedImage mediumImg = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, this.mediumWidth.intValue(), new BufferedImageOp[] { null, Scalr.OP_ANTIALIAS });
/*     */       
/* 142 */       BufferedImage smallImg = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, this.smallWidth.intValue(), new BufferedImageOp[] { null, Scalr.OP_ANTIALIAS });
/*     */ 
/*     */       
/* 145 */       int lastIndex = fileName.length();
/* 146 */       String replaceString = fileName.substring(fileName.lastIndexOf('.'), lastIndex);
/* 147 */       String extension = FilenameUtils.getExtension(file.getOriginalFilename());
/* 148 */       String largePath = fileName.replace(replaceString, this._large + replaceString);
/* 149 */       String mediumPath = fileName.replace(replaceString, this._medium + replaceString);
/* 150 */       String smallPath = fileName.replace(replaceString, this._small + replaceString);
/*     */       
/* 152 */       this.s3Client.putObject(getPutObjectRequest(bucketName, fileName, originalImage, extension));
/* 153 */       this.s3Client.putObject(getPutObjectRequest(bucketName, largePath, largeImage, extension));
/* 154 */       this.s3Client.putObject(getPutObjectRequest(bucketName, mediumPath, mediumImg, extension));
/* 155 */       this.s3Client.putObject(getPutObjectRequest(bucketName, smallPath, smallImg, extension));
/*     */       
/* 157 */       smallImg.flush();
/* 158 */       mediumImg.flush();
/* 159 */       originalImage.flush();
/*     */     }
/* 161 */     catch (Exception ioe) {
/*     */       
/* 163 */       Map<String, String> map = new HashMap<>();
/* 164 */       log.error("Exception while uploading the image: ", ioe);
/* 165 */       map.put("ERROR_AWS_S3_UPLOAD", "An error has occured while trying to upload image to S3 bucket.");
/* 166 */       throw new CustomException(map);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Resource getObject(String completeName) {
/* 172 */     long startTime = (new Date()).getTime();
/* 173 */     if (null == this.s3Client) {
/* 174 */       getS3Client();
/*     */     }
/* 176 */     int index = completeName.indexOf('/');
/* 177 */     String bucketName = completeName.substring(0, index);
/* 178 */     String fileNameWithPath = completeName.substring(index + 1, completeName.length());
/*     */     
/* 180 */     GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileNameWithPath);
/*     */     
/* 182 */     long beforeCalling = (new Date()).getTime();
/*     */     
/* 184 */     File localFile = new File("TempFolder/localFile");
/* 185 */     this.s3Client.getObject(getObjectRequest, localFile);
/*     */     
/* 187 */     long afterAws = (new Date()).getTime();
/*     */     
/* 189 */     FileSystemResource fileSystemResource = new FileSystemResource(Paths.get("TempFolder/localFile", new String[0]).toFile());
/*     */     
/* 191 */     long generateResource = (new Date()).getTime();
/*     */     
/* 193 */     log.info(" the time to prep Obj : " + (beforeCalling - startTime));
/* 194 */     log.info(" the time to get object from aws " + (afterAws - beforeCalling));
/* 195 */     log.info(" the time for creating resource form file : " + (generateResource - afterAws));
/* 196 */     return (Resource)fileSystemResource;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getUrlMap(Map<String, Artifact> fileMap) {
/* 201 */     Map<String, String> urlMap = new HashMap<>();
/* 202 */     if (null == this.s3Client) {
/* 203 */       getS3Client();
/*     */     }
/* 205 */     fileMap.keySet().forEach(fileStoreId -> {
/*     */           Artifact artifact = (Artifact)fileMap.get(fileStoreId);
/*     */           
/*     */           String completeName = artifact.getFileName();
/*     */           
/*     */           int index = completeName.indexOf('/');
/*     */           
/*     */           String bucketName = completeName.substring(0, index);
/*     */           
/*     */           String fileNameWithPath = completeName.substring(index + 1, completeName.length());
/*     */           
/*     */           String replaceString = fileNameWithPath.substring(fileNameWithPath.lastIndexOf('.'), fileNameWithPath.length());
/*     */           
/*     */           Date time = new Date();
/*     */           
/*     */           long msec = time.getTime();
/*     */           
/*     */           msec += this.presignedUrlExpirytime.longValue();
/*     */           time.setTime(msec);
/*     */           if (artifact.getContentType().startsWith("image/")) {
/*     */             List<String> urlList = new ArrayList<>();
/*     */             for (int i = 0; i < 4; i++) {
/*     */               String currentname = fileNameWithPath;
/*     */               if (1 == i) {
/*     */                 currentname = fileNameWithPath.replace(replaceString, this._large + replaceString);
/*     */               } else if (2 == i) {
/*     */                 currentname = fileNameWithPath.replace(replaceString, this._medium + replaceString);
/*     */               } else if (3 == i) {
/*     */                 currentname = fileNameWithPath.replace(replaceString, this._small + replaceString);
/*     */               } 
/*     */               GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, currentname);
/*     */               generatePresignedUrlRequest.setExpiration(time);
/*     */               urlList.add(this.s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString());
/*     */             } 
/*     */             urlMap.put(fileStoreId, urlList.toString().replaceFirst("\\[", "").replaceFirst("\\]", "").replaceAll(", ", ","));
/*     */           } else {
/*     */             GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileNameWithPath);
/*     */             generatePresignedUrlRequest.setExpiration(time);
/*     */             urlMap.put(fileStoreId, this.s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString());
/*     */           } 
/*     */         });
/* 246 */     return urlMap;
/*     */   }
/*     */   
/*     */   private AmazonS3 getS3Client() {
/* 250 */     if (null == this.s3Client)
/* 251 */       this
/*     */         
/* 253 */         .s3Client = (AmazonS3)((AmazonS3ClientBuilder)((AmazonS3ClientBuilder)AmazonS3ClientBuilder.standard().withCredentials((AWSCredentialsProvider)new AWSStaticCredentialsProvider((AWSCredentials)new BasicAWSCredentials(this.key, this.secretKey)))).withRegion(Regions.valueOf(this.awsRegion))).build(); 
/* 254 */     return this.s3Client;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private PutObjectRequest getPutObjectRequest(String bucketName, String key, BufferedImage originalImage, String extension) {
/* 260 */     ByteArrayOutputStream os = new ByteArrayOutputStream();
/*     */     try {
/* 262 */       ImageIO.write(originalImage, extension, os);
/* 263 */     } catch (IOException e) {
/* 264 */       log.error(" error while writing image to stream : {}", e);
/* 265 */       throw new RuntimeException(e);
/*     */     } 
/* 267 */     ObjectMetadata metadata = new ObjectMetadata();
/* 268 */     metadata.setContentLength(os.size());
/* 269 */     return new PutObjectRequest(bucketName, key, new ByteArrayInputStream(os.toByteArray()), metadata);
/*     */   }
/*     */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\persistence\repository\AwsS3Repository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */