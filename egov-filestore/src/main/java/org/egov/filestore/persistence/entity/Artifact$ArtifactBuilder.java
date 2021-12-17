/*    */ package org.egov.filestore.persistence.entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArtifactBuilder
/*    */ {
/*    */   private Long id;
/*    */   private String fileStoreId;
/*    */   private String fileName;
/*    */   private String contentType;
/*    */   private String module;
/*    */   private String tag;
/*    */   private String tenantId;
/*    */   private String fileSource;
/*    */   
/*    */   public ArtifactBuilder id(Long id) {
/* 63 */     this.id = id; return this; } public ArtifactBuilder fileStoreId(String fileStoreId) { this.fileStoreId = fileStoreId; return this; } public ArtifactBuilder fileName(String fileName) { this.fileName = fileName; return this; } public ArtifactBuilder contentType(String contentType) { this.contentType = contentType; return this; } public ArtifactBuilder module(String module) { this.module = module; return this; } public ArtifactBuilder tag(String tag) { this.tag = tag; return this; } public ArtifactBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; } public ArtifactBuilder fileSource(String fileSource) { this.fileSource = fileSource; return this; } public Artifact build() { return new Artifact(this.id, this.fileStoreId, this.fileName, this.contentType, this.module, this.tag, this.tenantId, this.fileSource); } public String toString() { return "Artifact.ArtifactBuilder(id=" + this.id + ", fileStoreId=" + this.fileStoreId + ", fileName=" + this.fileName + ", contentType=" + this.contentType + ", module=" + this.module + ", tag=" + this.tag + ", tenantId=" + this.tenantId + ", fileSource=" + this.fileSource + ")"; }
/*    */ 
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\persistence\entity\Artifact$ArtifactBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */