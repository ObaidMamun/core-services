/*     */ package org.egov.filestore.persistence.repository;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import org.egov.filestore.domain.exception.ArtifactNotFoundException;
/*     */ import org.egov.filestore.domain.model.Artifact;
/*     */ import org.egov.filestore.domain.model.FileInfo;
/*     */ import org.egov.filestore.domain.model.FileLocation;
/*     */ import org.egov.filestore.domain.model.Resource;
/*     */ import org.egov.filestore.persistence.entity.Artifact;
/*     */ import org.egov.filestore.repository.CloudFilesManager;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class ArtifactRepository
/*     */ {
/*     */   private FileStoreJpaRepository fileStoreJpaRepository;
/*     */   @Autowired
/*     */   private AwsS3Repository s3Repository;
/*     */   @Autowired
/*     */   private CloudFilesManager cloudFilesManager;
/*     */   @Value("${isS3Enabled}")
/*     */   private Boolean isS3Enabled;
/*     */   @Value("${isAzureStorageEnabled}")
/*     */   private Boolean isAzureStorageEnabled;
/*     */   @Value("${source.s3}")
/*     */   private String awsS3Source;
/*     */   @Value("${source.azure.blob}")
/*     */   private String azureBlobSource;
/*     */   @Value("${source.disk}")
/*     */   private String diskFileStorage;
/*     */   
/*     */   public ArtifactRepository(FileStoreJpaRepository fileStoreJpaRepository) {
/*  45 */     this.fileStoreJpaRepository = fileStoreJpaRepository;
/*     */   }
/*     */   
/*     */   public List<String> save(List<Artifact> artifacts) {
/*  49 */     this.cloudFilesManager.saveFiles(artifacts);
/*  50 */     List<Artifact> artifactEntities = new ArrayList<>();
/*  51 */     artifacts.forEach(artifact -> artifactEntities.add(mapToEntity(artifact)));
/*     */ 
/*     */     
/*  54 */     return (List<String>)this.fileStoreJpaRepository.save(artifactEntities).stream().map(Artifact::getFileStoreId)
/*  55 */       .collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Artifact mapToEntity(Artifact artifact) {
/*  66 */     FileLocation fileLocation = artifact.getFileLocation();
/*     */ 
/*     */ 
/*     */     
/*  70 */     Artifact entityArtifact = Artifact.builder().fileStoreId(fileLocation.getFileStoreId()).fileName(fileLocation.getFileName()).contentType(artifact.getMultipartFile().getContentType()).module(fileLocation.getModule()).tag(fileLocation.getTag()).tenantId(fileLocation.getTenantId()).fileSource(fileLocation.getFileSource()).build();
/*  71 */     if (this.isAzureStorageEnabled.booleanValue())
/*  72 */       entityArtifact.setFileSource(this.azureBlobSource); 
/*  73 */     if (this.isS3Enabled.booleanValue()) {
/*  74 */       entityArtifact.setFileSource(this.awsS3Source);
/*     */     }
/*  76 */     return entityArtifact;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource find(String fileStoreId, String tenantId) throws IOException {
/* 106 */     Artifact artifact = this.fileStoreJpaRepository.findByFileStoreIdAndTenantId(fileStoreId, tenantId);
/* 107 */     if (artifact == null) {
/* 108 */       throw new ArtifactNotFoundException(fileStoreId);
/*     */     }
/* 110 */     Resource resource = null;
/* 111 */     if (artifact.getFileLocation().getFileSource().equals(this.diskFileStorage)) {
/*     */       
/* 113 */       DiskFileStoreRepository diskfileStore = (DiskFileStoreRepository)this.cloudFilesManager;
/* 114 */       resource = diskfileStore.read(artifact.getFileLocation());
/* 115 */     } else if (artifact.getFileLocation().getFileSource().equals(this.awsS3Source)) {
/*     */ 
/*     */       
/* 118 */       resource = this.s3Repository.getObject(artifact.getFileLocation().getFileName());
/*     */     } 
/*     */     
/* 121 */     if (null != resource) {
/* 122 */       return new Resource(artifact.getContentType(), artifact.getFileName(), resource, artifact.getTenantId(), "" + resource
/* 123 */           .getFile().length() + " bytes");
/*     */     }
/* 125 */     return null;
/*     */   }
/*     */   
/*     */   public List<FileInfo> findByTag(String tag, String tenantId) {
/* 129 */     return (List<FileInfo>)this.fileStoreJpaRepository.findByTagAndTenantId(tag, tenantId).stream().map(this::mapArtifactToFileInfo)
/* 130 */       .collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */   
/*     */   private FileInfo mapArtifactToFileInfo(Artifact artifact) {
/* 135 */     FileLocation fileLocation = new FileLocation(artifact.getFileStoreId(), artifact.getModule(), artifact.getTag(), artifact.getTenantId(), artifact.getFileName(), artifact.getFileSource());
/*     */     
/* 137 */     return new FileInfo(artifact.getContentType(), fileLocation, artifact.getTenantId());
/*     */   }
/*     */   
/*     */   public List<Artifact> getByTenantIdAndFileStoreIdList(String tenantId, List<String> fileStoreIds) {
/* 141 */     return this.fileStoreJpaRepository.findByTenantIdAndFileStoreIdList(tenantId, fileStoreIds);
/*     */   }
/*     */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\persistence\repository\ArtifactRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */