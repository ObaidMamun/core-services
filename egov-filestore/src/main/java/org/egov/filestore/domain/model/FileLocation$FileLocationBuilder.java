/*    */ package org.egov.filestore.domain.model;
/*    */ 
/*    */ public class FileLocationBuilder
/*    */ {
/*    */   private String fileStoreId;
/*    */   private String module;
/*    */   private String tag;
/*    */   private String tenantId;
/*    */   private String fileName;
/*    */   private String fileSource;
/*    */   
/*    */   public FileLocationBuilder fileStoreId(String fileStoreId) {
/* 13 */     this.fileStoreId = fileStoreId; return this; } public FileLocationBuilder module(String module) { this.module = module; return this; } public FileLocationBuilder tag(String tag) { this.tag = tag; return this; } public FileLocationBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; } public FileLocationBuilder fileName(String fileName) { this.fileName = fileName; return this; } public FileLocationBuilder fileSource(String fileSource) { this.fileSource = fileSource; return this; } public FileLocation build() { return new FileLocation(this.fileStoreId, this.module, this.tag, this.tenantId, this.fileName, this.fileSource); } public String toString() { return "FileLocation.FileLocationBuilder(fileStoreId=" + this.fileStoreId + ", module=" + this.module + ", tag=" + this.tag + ", tenantId=" + this.tenantId + ", fileName=" + this.fileName + ", fileSource=" + this.fileSource + ")"; }
/*    */ 
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\domain\model\FileLocation$FileLocationBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */