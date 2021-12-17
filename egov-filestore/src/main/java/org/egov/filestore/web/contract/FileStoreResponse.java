/*    */ package org.egov.filestore.web.contract;
/*    */ 
/*    */ public class FileStoreResponse {
/*    */   private String id;
/*    */   private String url;
/*    */   
/*    */   public FileStoreResponse() {}
/*    */   
/*  9 */   public FileStoreResponse(String id, String url) { this.id = id; this.url = url; }
/* 10 */   public void setId(String id) { this.id = id; } public void setUrl(String url) { this.url = url; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof FileStoreResponse)) return false;  FileStoreResponse other = (FileStoreResponse)o; if (!other.canEqual(this)) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  Object this$url = getUrl(), other$url = other.getUrl(); return !((this$url == null) ? (other$url != null) : !this$url.equals(other$url)); } protected boolean canEqual(Object other) { return other instanceof FileStoreResponse; } public int hashCode() { int PRIME = 59; result = 1; Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); Object $url = getUrl(); return result * 59 + (($url == null) ? 43 : $url.hashCode()); } public String toString() { return "FileStoreResponse(id=" + getId() + ", url=" + getUrl() + ")"; }
/* 11 */   public static FileStoreResponseBuilder builder() { return new FileStoreResponseBuilder(); } public static class FileStoreResponseBuilder { private String id; public FileStoreResponseBuilder id(String id) { this.id = id; return this; } private String url; public FileStoreResponseBuilder url(String url) { this.url = url; return this; } public FileStoreResponse build() { return new FileStoreResponse(this.id, this.url); } public String toString() { return "FileStoreResponse.FileStoreResponseBuilder(id=" + this.id + ", url=" + this.url + ")"; }
/*    */      }
/*    */   public String getId() {
/* 14 */     return this.id;
/*    */   } public String getUrl() {
/* 16 */     return this.url;
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\web\contract\FileStoreResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */