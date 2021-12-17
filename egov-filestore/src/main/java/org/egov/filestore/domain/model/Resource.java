/*    */ package org.egov.filestore.domain.model;
/*    */ public class Resource { private String contentType;
/*    */   private String fileName;
/*    */   private org.springframework.core.io.Resource resource;
/*    */   private String tenantId;
/*    */   private String fileSize;
/*    */   
/*  8 */   public void setContentType(String contentType) { this.contentType = contentType; } public void setFileName(String fileName) { this.fileName = fileName; } public void setResource(org.springframework.core.io.Resource resource) { this.resource = resource; } public void setTenantId(String tenantId) { this.tenantId = tenantId; } public void setFileSize(String fileSize) { this.fileSize = fileSize; } public Resource(String contentType, String fileName, org.springframework.core.io.Resource resource, String tenantId, String fileSize) {
/*  9 */     this.contentType = contentType; this.fileName = fileName; this.resource = resource; this.tenantId = tenantId; this.fileSize = fileSize;
/*    */   }
/* 11 */   public String getContentType() { return this.contentType; }
/* 12 */   public String getFileName() { return this.fileName; }
/* 13 */   public org.springframework.core.io.Resource getResource() { return this.resource; }
/* 14 */   public String getTenantId() { return this.tenantId; } public String getFileSize() {
/* 15 */     return this.fileSize;
/*    */   } }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\domain\model\Resource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */