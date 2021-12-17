/*    */ package org.egov.filestore.persistence.entity;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import javax.persistence.Column;
/*    */ import javax.persistence.Entity;
/*    */ import javax.persistence.GeneratedValue;
/*    */ import javax.persistence.GenerationType;
/*    */ import javax.persistence.Id;
/*    */ import javax.persistence.SequenceGenerator;
/*    */ import javax.persistence.Table;
/*    */ import org.egov.filestore.domain.model.FileLocation;
/*    */ import org.hibernate.validator.constraints.NotBlank;
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
/*    */ @Table(name = "eg_filestoremap")
/*    */ @Entity
/*    */ @SequenceGenerator(name = "SEQ_EG_FILESTOREMAP", sequenceName = "SEQ_EG_FILESTOREMAP", allocationSize = 1)
/*    */ public class Artifact
/*    */   extends AbstractPersistable<Long>
/*    */ {
/*    */   public static final String SEQ_FILESTOREMAP = "SEQ_EG_FILESTOREMAP";
/*    */   private static final long serialVersionUID = -2997164207274266823L;
/*    */   @Id
/*    */   @GeneratedValue(generator = "SEQ_EG_FILESTOREMAP", strategy = GenerationType.SEQUENCE)
/*    */   private Long id;
/*    */   @NotBlank
/*    */   @Column(length = 36, unique = true, nullable = false)
/*    */   private String fileStoreId;
/*    */   @NotBlank
/*    */   private String fileName;
/*    */   private String contentType;
/*    */   private String module;
/*    */   private String tag;
/*    */   @Column(name = "tenantid")
/*    */   private String tenantId;
/*    */   @Column(name = "filesource")
/*    */   private String fileSource;
/*    */   
/*    */   public void setId(Long id) {
/* 58 */     this.id = id; } public void setFileStoreId(String fileStoreId) { this.fileStoreId = fileStoreId; } public void setFileName(String fileName) { this.fileName = fileName; } public void setContentType(String contentType) { this.contentType = contentType; } public void setModule(String module) { this.module = module; } public void setTag(String tag) { this.tag = tag; } public void setTenantId(String tenantId) { this.tenantId = tenantId; } public void setFileSource(String fileSource) { this.fileSource = fileSource; }
/*    */   
/*    */   public Artifact() {}
/*    */   
/* 62 */   public Artifact(Long id, String fileStoreId, String fileName, String contentType, String module, String tag, String tenantId, String fileSource) { this.id = id; this.fileStoreId = fileStoreId; this.fileName = fileName; this.contentType = contentType; this.module = module; this.tag = tag; this.tenantId = tenantId; this.fileSource = fileSource; }
/* 63 */   public static ArtifactBuilder builder() { return new ArtifactBuilder(); } public static class ArtifactBuilder { private Long id; private String fileStoreId; private String fileName; private String contentType; public ArtifactBuilder id(Long id) { this.id = id; return this; } private String module; private String tag; private String tenantId; private String fileSource; public ArtifactBuilder fileStoreId(String fileStoreId) { this.fileStoreId = fileStoreId; return this; } public ArtifactBuilder fileName(String fileName) { this.fileName = fileName; return this; } public ArtifactBuilder contentType(String contentType) { this.contentType = contentType; return this; } public ArtifactBuilder module(String module) { this.module = module; return this; } public ArtifactBuilder tag(String tag) { this.tag = tag; return this; } public ArtifactBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; } public ArtifactBuilder fileSource(String fileSource) { this.fileSource = fileSource; return this; } public Artifact build() { return new Artifact(this.id, this.fileStoreId, this.fileName, this.contentType, this.module, this.tag, this.tenantId, this.fileSource); } public String toString() { return "Artifact.ArtifactBuilder(id=" + this.id + ", fileStoreId=" + this.fileStoreId + ", fileName=" + this.fileName + ", contentType=" + this.contentType + ", module=" + this.module + ", tag=" + this.tag + ", tenantId=" + this.tenantId + ", fileSource=" + this.fileSource + ")"; }
/*    */      }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Long getId() {
/* 70 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getFileStoreId() {
/* 74 */     return this.fileStoreId;
/*    */   }
/*    */   
/* 77 */   public String getFileName() { return this.fileName; } public String getContentType() {
/* 78 */     return this.contentType;
/*    */   }
/* 80 */   public String getModule() { return this.module; } public String getTag() {
/* 81 */     return this.tag;
/*    */   }
/*    */   public String getTenantId() {
/* 84 */     return this.tenantId;
/*    */   }
/*    */   public String getFileSource() {
/* 87 */     return this.fileSource;
/*    */   }
/*    */   public FileLocation getFileLocation() {
/* 90 */     return new FileLocation(this.fileStoreId, this.module, this.tag, this.tenantId, this.fileName, this.fileSource);
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\persistence\entity\Artifact.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */