/*    */ package org.egov.filestore.domain.model;
/*    */ public class Artifact {
/*    */   private String fileContentInString;
/*    */   
/*    */   public Artifact(String fileContentInString, MultipartFile multipartFile, FileLocation fileLocation) {
/*  6 */     this.fileContentInString = fileContentInString; this.multipartFile = multipartFile; this.fileLocation = fileLocation;
/*    */   }
/*    */   private MultipartFile multipartFile; private FileLocation fileLocation;
/*    */   public String getFileContentInString() {
/* 10 */     return this.fileContentInString;
/*    */   } public MultipartFile getMultipartFile() {
/* 12 */     return this.multipartFile;
/*    */   } public FileLocation getFileLocation() {
/* 14 */     return this.fileLocation;
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\domain\model\Artifact.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */