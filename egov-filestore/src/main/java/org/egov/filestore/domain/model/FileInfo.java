/*    */ package org.egov.filestore.domain.model;public class FileInfo {
/*    */   private String contentType;
/*    */   private FileLocation fileLocation;
/*    */   private String tenantId;
/*    */   
/*    */   public FileInfo(String contentType, FileLocation fileLocation, String tenantId) {
/*  7 */     this.contentType = contentType; this.fileLocation = fileLocation; this.tenantId = tenantId;
/*    */   }
/*  9 */   public String getContentType() { return this.contentType; }
/* 10 */   public FileLocation getFileLocation() { return this.fileLocation; } public String getTenantId() {
/* 11 */     return this.tenantId;
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\domain\model\FileInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */