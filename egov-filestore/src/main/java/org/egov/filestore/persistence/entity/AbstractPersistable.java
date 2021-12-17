/*    */ package org.egov.filestore.persistence.entity;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import javax.persistence.MappedSuperclass;
/*    */ import javax.persistence.Version;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @MappedSuperclass
/*    */ public abstract class AbstractPersistable<PK extends Serializable>
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 7094572260034458544L;
/*    */   @Version
/*    */   private Long version;
/*    */   
/*    */   public abstract PK getId();
/*    */   
/*    */   protected abstract void setId(PK paramPK);
/*    */   
/*    */   public Long getVersion() {
/* 60 */     return this.version;
/*    */   }
/*    */   
/*    */   public boolean isNew() {
/* 64 */     return (null == getId());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 69 */     return String.format("Entity of type %s with id: %s", new Object[] { getClass().getName(), getId() });
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\persistence\entity\AbstractPersistable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */