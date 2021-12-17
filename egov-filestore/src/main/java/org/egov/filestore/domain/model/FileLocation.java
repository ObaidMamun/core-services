/*    */ package org.egov.filestore.domain.model;public class FileLocation { private String fileStoreId;
/*    */   private String module;
/*    */   private String tag;
/*    */   private String tenantId;
/*    */   private String fileName;
/*    */   private String fileSource;
/*    */   
/*    */   public FileLocation(String fileStoreId, String module, String tag, String tenantId, String fileName, String fileSource) {
/*  9 */     this.fileStoreId = fileStoreId; this.module = module; this.tag = tag; this.tenantId = tenantId; this.fileName = fileName; this.fileSource = fileSource;
/*    */   }
/* 11 */   public void setFileStoreId(String fileStoreId) { this.fileStoreId = fileStoreId; } public void setModule(String module) { this.module = module; } public void setTag(String tag) { this.tag = tag; } public void setTenantId(String tenantId) { this.tenantId = tenantId; } public void setFileName(String fileName) { this.fileName = fileName; } public void setFileSource(String fileSource) { this.fileSource = fileSource; }
/*    */    public FileLocation() {}
/* 13 */   public static FileLocationBuilder builder() { return new FileLocationBuilder(); } public static class FileLocationBuilder { private String fileStoreId; private String module; private String tag; public FileLocationBuilder fileStoreId(String fileStoreId) { this.fileStoreId = fileStoreId; return this; } private String tenantId; private String fileName; private String fileSource; public FileLocationBuilder module(String module) { this.module = module; return this; } public FileLocationBuilder tag(String tag) { this.tag = tag; return this; } public FileLocationBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; } public FileLocationBuilder fileName(String fileName) { this.fileName = fileName; return this; } public FileLocationBuilder fileSource(String fileSource) { this.fileSource = fileSource; return this; } public FileLocation build() { return new FileLocation(this.fileStoreId, this.module, this.tag, this.tenantId, this.fileName, this.fileSource); } public String toString() { return "FileLocation.FileLocationBuilder(fileStoreId=" + this.fileStoreId + ", module=" + this.module + ", tag=" + this.tag + ", tenantId=" + this.tenantId + ", fileName=" + this.fileName + ", fileSource=" + this.fileSource + ")"; }
/*    */      }
/* 15 */   public String getFileStoreId() { return this.fileStoreId; }
/* 16 */   public String getModule() { return this.module; }
/* 17 */   public String getTag() { return this.tag; }
/* 18 */   public String getTenantId() { return this.tenantId; }
/* 19 */   public String getFileName() { return this.fileName; } public String getFileSource() {
/* 20 */     return this.fileSource;
/*    */   } }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\domain\model\FileLocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */