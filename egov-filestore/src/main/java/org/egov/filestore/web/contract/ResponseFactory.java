/*    */ package org.egov.filestore.web.contract;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import org.egov.filestore.domain.model.FileInfo;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class ResponseFactory
/*    */ {
/*    */   private static final String FORMAT = "%s/v1/files/id?fileStoreId=%s&tenantId=%s";
/*    */   private String contextPath;
/*    */   
/*    */   public ResponseFactory(@Value("${server.contextPath}") String contextPath) {
/* 17 */     this.contextPath = contextPath;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GetFilesByTagResponse getFilesByTagResponse(List<FileInfo> listOfFileInfo) {
/* 26 */     List<FileRecord> fileRecords = (List<FileRecord>)listOfFileInfo.stream().map(fileInfo -> { String url = String.format("%s/v1/files/id?fileStoreId=%s&tenantId=%s", new Object[] { this.contextPath, fileInfo.getFileLocation().getFileStoreId(), fileInfo.getTenantId() }); return new FileRecord(url, fileInfo.getContentType()); }).collect(Collectors.toList());
/*    */     
/* 28 */     return new GetFilesByTagResponse(fileRecords);
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\web\contract\ResponseFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */