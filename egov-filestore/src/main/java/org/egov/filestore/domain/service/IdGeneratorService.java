/*    */ package org.egov.filestore.domain.service;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class IdGeneratorService
/*    */ {
/*    */   public String getId() {
/* 11 */     return UUID.randomUUID().toString();
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\domain\service\IdGeneratorService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */