/*     */ package org.egov.filestore.persistence.repository;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.egov.filestore.domain.model.Artifact;
/*     */ import org.egov.filestore.domain.model.FileLocation;
/*     */ import org.egov.filestore.persistence.entity.Artifact;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.stereotype.Service;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ @Service
/*     */ @ConditionalOnProperty(value = {"isNfsStorageEnabled"}, havingValue = "true")
/*     */ public class DiskFileStoreRepository implements CloudFilesManager {
/*  23 */   private static final Logger log = LoggerFactory.getLogger(DiskFileStoreRepository.class);
/*     */ 
/*     */   
/*     */   @Value("${disk.storage.host.url}")
/*     */   private String hostUrl;
/*     */ 
/*     */   
/*     */   @Value("${disk.storage.host.endpoint}")
/*     */   private String hostEndpoint;
/*     */ 
/*     */   
/*     */   @Value("${source.disk}")
/*     */   private String diskFileStorage;
/*     */   
/*     */   private FileRepository fileRepository;
/*     */   
/*     */   private String fileMountPath;
/*     */ 
/*     */   
/*     */   public DiskFileStoreRepository(FileRepository fileRepository, @Value("${file.storage.mount.path}") String fileMountPath) {
/*  43 */     this.fileRepository = fileRepository;
/*  44 */     if (null == fileMountPath || fileMountPath.isEmpty())
/*  45 */       fileMountPath = FileUtils.getUserDirectoryPath(); 
/*  46 */     this.fileMountPath = fileMountPath;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveFiles(List<Artifact> artifacts) {
/*  52 */     List<Artifact> persistList = new ArrayList<>();
/*  53 */     artifacts.forEach(artifact -> {
/*     */           MultipartFile multipartFile = artifact.getMultipartFile();
/*     */           FileLocation fileLocation = artifact.getFileLocation();
/*     */           Path path = getPath(fileLocation);
/*     */           long start = System.currentTimeMillis();
/*     */           this.fileRepository.write(multipartFile, path);
/*     */           fileLocation.setFileSource(this.diskFileStorage);
/*     */           persistList.add(mapToEntity(artifact));
/*     */           long time = System.currentTimeMillis() - start;
/*     */           log.info("File= " + multipartFile.getName() + " , Size= " + multipartFile.getSize() + " , Transfer time= " + time);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getFiles(Map<String, String> mapOfIdAndFilePath) {
/*  71 */     Map<String, String> mapOfIdAndSASUrls = new HashMap<>();
/*  72 */     for (String s : mapOfIdAndFilePath.keySet()) {
/*     */ 
/*     */       
/*  75 */       StringBuilder url = new StringBuilder(this.hostUrl);
/*  76 */       url.append(this.hostEndpoint);
/*  77 */       url.append(s);
/*  78 */       mapOfIdAndSASUrls.put(s, url.toString());
/*     */     } 
/*     */     
/*  81 */     return mapOfIdAndSASUrls;
/*     */   }
/*     */ 
/*     */   
/*     */   public Resource read(FileLocation fileLocation) {
/*  86 */     Resource resource = null;
/*     */     
/*  88 */     if (fileLocation.getFileSource() == null || fileLocation.getFileSource().equals(this.diskFileStorage)) {
/*  89 */       Path path = getPath(fileLocation);
/*  90 */       resource = this.fileRepository.read(path);
/*  91 */       if (resource == null)
/*  92 */         resource = this.fileRepository.read(getPathOldVersion(fileLocation)); 
/*     */     } 
/*  94 */     return resource;
/*     */   }
/*     */   
/*     */   private Path getPath(FileLocation fileLocation) {
/*  98 */     return Paths.get(this.fileMountPath, new String[] { fileLocation.getFileName() });
/*     */   }
/*     */   
/*     */   private Path getPathOldVersion(FileLocation fileLocation) {
/* 102 */     return Paths.get(this.fileMountPath, new String[] { fileLocation.getTenantId(), fileLocation.getModule(), fileLocation
/* 103 */           .getFileStoreId() });
/*     */   }
/*     */ 
/*     */   
/*     */   private Artifact mapToEntity(Artifact artifact) {
/* 108 */     FileLocation fileLocation = artifact.getFileLocation();
/* 109 */     return Artifact.builder().fileStoreId(fileLocation.getFileStoreId()).fileName(fileLocation.getFileName())
/* 110 */       .contentType(artifact.getMultipartFile().getContentType()).module(fileLocation.getModule())
/* 111 */       .tag(fileLocation.getTag()).tenantId(fileLocation.getTenantId())
/* 112 */       .fileSource(fileLocation.getFileSource()).build();
/*     */   }
/*     */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\persistence\repository\DiskFileStoreRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */