/*   */ package org.egov.filestore.domain.exception;
/*   */ 
/*   */ public class ArtifactNotFoundException
/*   */   extends RuntimeException {
/*   */   public ArtifactNotFoundException(String fileStoreId) {
/* 6 */     super(String.format("Artifact with fileStoreId %s is not found", new Object[] { fileStoreId }));
/*   */   }
/*   */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\domain\exception\ArtifactNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */