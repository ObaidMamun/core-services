/*    */ package org.egov.filestore.web.contract;public class FileRecord {
/*    */   private String url;
/*    */   private String contentType;
/*    */   
/*    */   public FileRecord(String url, String contentType) {
/*  6 */     this.url = url; this.contentType = contentType;
/*    */   }
/*    */   
/*  9 */   public String getUrl() { return this.url; } public String getContentType() {
/* 10 */     return this.contentType;
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\web\contract\FileRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */