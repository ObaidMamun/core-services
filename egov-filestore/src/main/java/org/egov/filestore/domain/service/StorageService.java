/*     */ package org.egov.filestore.domain.service;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Calendar;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import org.egov.filestore.domain.exception.EmptyFileUploadRequestException;
/*     */ import org.egov.filestore.domain.model.Artifact;
/*     */ import org.egov.filestore.domain.model.FileInfo;
/*     */ import org.egov.filestore.domain.model.FileLocation;
/*     */ import org.egov.filestore.domain.model.Resource;
/*     */ import org.egov.filestore.persistence.entity.Artifact;
/*     */ import org.egov.filestore.persistence.repository.ArtifactRepository;
/*     */ import org.egov.filestore.repository.CloudFilesManager;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.stereotype.Service;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ @Service
/*     */ public class StorageService {
/*  27 */   private static final Logger log = LoggerFactory.getLogger(StorageService.class);
/*     */   
/*     */   @Value("${is.bucket.fixed}")
/*     */   private Boolean isBucketFixed;
/*     */   
/*     */   @Value("${fixed.bucketname}")
/*     */   private String fixedBucketName;
/*     */   
/*     */   @Value("${isS3Enabled}")
/*     */   private Boolean isS3Enabled;
/*     */   
/*     */   @Value("${isNfsStorageEnabled}")
/*     */   private Boolean isNfsStorageEnabled;
/*     */   
/*     */   @Value("${isAzureStorageEnabled}")
/*     */   private Boolean isAzureStorageEnabled;
/*     */   
/*     */   @Value("${source.disk}")
/*     */   private String diskStorage;
/*     */   
/*     */   @Value("${source.s3}")
/*     */   private String awsS3Source;
/*     */   
/*     */   @Value("${source.azure.blob}")
/*     */   private String azureBlobSource;
/*     */   
/*     */   @Autowired
/*     */   private CloudFilesManager cloudFilesManager;
/*     */   
/*     */   private static final String UPLOAD_MESSAGE = "Received upload request for jurisdiction: %s, module: %s, tag: %s with file count: %s";
/*     */   
/*     */   private ArtifactRepository artifactRepository;
/*     */   
/*     */   private IdGeneratorService idGeneratorService;
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public StorageService(ArtifactRepository artifactRepository, IdGeneratorService idGeneratorService) {
/*  65 */     this.artifactRepository = artifactRepository;
/*  66 */     this.idGeneratorService = idGeneratorService;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> save(List<MultipartFile> filesToStore, String module, String tag, String tenantId, String inputStreamAsString) {
/*  71 */     validateFilesToUpload(filesToStore, module, tag, tenantId);
/*  72 */     log.info("Received upload request for jurisdiction: %s, module: %s, tag: %s with file count: %s", new Object[] { module, tag, Integer.valueOf(filesToStore.size()) });
/*  73 */     List<Artifact> artifacts = mapFilesToArtifacts(filesToStore, module, tag, tenantId, inputStreamAsString);
/*  74 */     return this.artifactRepository.save(artifacts);
/*     */   }
/*     */   
/*     */   private void validateFilesToUpload(List<MultipartFile> filesToStore, String module, String tag, String tenantId) {
/*  78 */     if (CollectionUtils.isEmpty(filesToStore)) {
/*  79 */       throw new EmptyFileUploadRequestException(module, tag, tenantId);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Artifact> mapFilesToArtifacts(List<MultipartFile> files, String module, String tag, String tenantId, String inputStreamAsString) {
/*  86 */     String folderName = getFolderName(module, tenantId);
/*  87 */     return (List<Artifact>)files.stream().map(file -> {
/*     */           String fileName = folderName + System.currentTimeMillis() + file.getOriginalFilename();
/*     */           
/*     */           String id = this.idGeneratorService.getId();
/*     */           FileLocation fileLocation = new FileLocation(id, module, tag, tenantId, fileName, null);
/*     */           return new Artifact(inputStreamAsString, file, fileLocation);
/*  93 */         }).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */   
/*     */   private String getFolderName(String module, String tenantId) {
/*  98 */     Calendar calendar = Calendar.getInstance();
/*  99 */     return getBucketName(tenantId, calendar) + "/" + getFolderName(module, tenantId, calendar);
/*     */   }
/*     */   
/*     */   public Resource retrieve(String fileStoreId, String tenantId) throws IOException {
/* 103 */     return this.artifactRepository.find(fileStoreId, tenantId);
/*     */   }
/*     */   
/*     */   public List<FileInfo> retrieveByTag(String tag, String tenantId) {
/* 107 */     return this.artifactRepository.findByTag(tag, tenantId);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getUrls(String tenantId, List<String> fileStoreIds) {
/* 112 */     Map<String, String> urlMap = getUrlMap(this.artifactRepository
/* 113 */         .getByTenantIdAndFileStoreIdList(tenantId, fileStoreIds));
/* 114 */     if (this.isNfsStorageEnabled.booleanValue())
/* 115 */       for (String s : urlMap.keySet()) {
/* 116 */         urlMap.put(s, ((String)urlMap.get(s)).concat("&tenantId=").concat(tenantId));
/*     */       } 
/* 118 */     return urlMap;
/*     */   }
/*     */   
/*     */   private Map<String, String> getUrlMap(List<Artifact> artifactList) {
/* 122 */     String src = null;
/* 123 */     if (this.isAzureStorageEnabled.booleanValue())
/* 124 */       src = this.azureBlobSource; 
/* 125 */     if (this.isS3Enabled.booleanValue())
/* 126 */       src = this.awsS3Source; 
/* 127 */     if (this.isNfsStorageEnabled.booleanValue())
/* 128 */       src = this.diskStorage; 
/* 129 */     String source = src;
/*     */ 
/*     */ 
/*     */     
/* 133 */     Map<String, String> mapOfIdAndFile = (Map<String, String>)artifactList.stream().filter(a -> (null != a.getFileSource() && a.getFileSource().equals(source))).collect(Collectors.toMap(Artifact::getFileStoreId, Artifact::getFileName));
/*     */     
/* 135 */     return this.cloudFilesManager.getFiles(mapOfIdAndFile);
/*     */   }
/*     */   
/*     */   private String getFolderName(String module, String tenantId, Calendar calendar) {
/* 139 */     return tenantId + "/" + module + "/" + calendar.getDisplayName(2, 2, Locale.ENGLISH) + "/" + calendar
/* 140 */       .get(5) + "/";
/*     */   }
/*     */ 
/*     */   
/*     */   private String getBucketName(String tenantId, Calendar calendar) {
/* 145 */     if (this.isBucketFixed.booleanValue()) {
/* 146 */       return this.fixedBucketName;
/*     */     }
/* 148 */     return tenantId.split("\\.")[0] + calendar.get(1);
/*     */   }
/*     */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\domain\service\StorageService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */