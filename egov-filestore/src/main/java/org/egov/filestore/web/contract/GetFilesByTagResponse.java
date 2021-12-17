/*    */ package org.egov.filestore.web.contract;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class GetFilesByTagResponse {
/*    */   private List<FileRecord> files;
/*    */   
/*    */   public GetFilesByTagResponse(List<FileRecord> files) {
/*  9 */     this.files = files;
/*    */   } public List<FileRecord> getFiles() {
/* 11 */     return this.files;
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\web\contract\GetFilesByTagResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */