/*   */ package org.egov.filestore.domain.exception;
/*   */ 
/*   */ public class EmptyFileUploadRequestException
/*   */   extends RuntimeException {
/*   */   private static final long serialVersionUID = 469769329321629532L;
/*   */   private static final String MESSAGE = "No files present in upload request for module: %s, tag: %s, tenantId: %s";
/*   */   
/*   */   public EmptyFileUploadRequestException(String module, String tag, String tenantId) {
/* 9 */     super(String.format("No files present in upload request for module: %s, tag: %s, tenantId: %s", new Object[] { module, tag, tenantId }));
/*   */   }
/*   */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\domain\exception\EmptyFileUploadRequestException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */