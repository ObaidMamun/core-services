/*    */ package org.egov.filestore.web.contract;
/*    */ public class File {
/*    */   private String fileStoreId;
/*    */   private String tenantId;
/*    */   
/*    */   public File(String fileStoreId, String tenantId) {
/*  7 */     this.fileStoreId = fileStoreId; this.tenantId = tenantId;
/*    */   }
/*  9 */   public String getFileStoreId() { return this.fileStoreId; } public String getTenantId() {
/* 10 */     return this.tenantId;
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\web\contract\File.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */