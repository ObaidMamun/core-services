/*    */ package org.egov.filestore.web.contract;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileStoreResponseBuilder
/*    */ {
/*    */   private String id;
/*    */   private String url;
/*    */   
/*    */   public FileStoreResponseBuilder id(String id) {
/* 11 */     this.id = id; return this; } public FileStoreResponseBuilder url(String url) { this.url = url; return this; } public FileStoreResponse build() { return new FileStoreResponse(this.id, this.url); } public String toString() { return "FileStoreResponse.FileStoreResponseBuilder(id=" + this.id + ", url=" + this.url + ")"; }
/*    */ 
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\web\contract\FileStoreResponse$FileStoreResponseBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */