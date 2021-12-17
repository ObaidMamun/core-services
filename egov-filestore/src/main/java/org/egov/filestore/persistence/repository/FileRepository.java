/*    */ package org.egov.filestore.persistence.repository;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.awt.image.BufferedImageOp;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import javax.imageio.ImageIO;
/*    */ import org.apache.commons.io.FileUtils;
/*    */ import org.apache.commons.io.FilenameUtils;
/*    */ import org.imgscalr.Scalr;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.core.io.FileSystemResource;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ import org.springframework.web.multipart.MultipartFile;
/*    */ 
/*    */ @Service
/*    */ public class FileRepository
/*    */ {
/* 22 */   private static final Logger log = LoggerFactory.getLogger(FileRepository.class);
/*    */ 
/*    */   
/*    */   public void write(MultipartFile file, Path path) {
/* 26 */     writeToFileSystem(file, path);
/*    */   }
/*    */ 
/*    */   
/*    */   private void writeToFileSystem(MultipartFile file, Path path) {
/*    */     try {
/* 32 */       FileUtils.forceMkdirParent(path.toFile());
/*    */       
/* 34 */       if (file.getContentType().startsWith("image/")) {
/*    */         
/* 36 */         log.debug("its an image");
/*    */         try {
/* 38 */           writeImage(file, path);
/* 39 */         } catch (RuntimeException e) {
/*    */           
/* 41 */           file.transferTo(path.toFile());
/*    */         } 
/*    */       } else {
/*    */         
/* 45 */         log.debug("its other format");
/* 46 */         file.transferTo(path.toFile());
/*    */       } 
/* 48 */       log.info("Successfully wrote file to disk. File name: " + file.getOriginalFilename() + " File Size: " + file
/* 49 */           .getSize());
/* 50 */     } catch (IOException e) {
/* 51 */       log.error(" exception occured while write file ", e);
/* 52 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private void writeImage(MultipartFile file, Path path) {
/*    */     try {
/* 59 */       BufferedImage originalImage = ImageIO.read(file.getInputStream());
/* 60 */       BufferedImage mediumImg = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, 75, 75, new BufferedImageOp[] { Scalr.OP_ANTIALIAS });
/*    */       
/* 62 */       BufferedImage smallImg = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, 50, 50, new BufferedImageOp[] { Scalr.OP_ANTIALIAS });
/*    */ 
/*    */       
/* 65 */       String originalPath = path.toFile().toString();
/* 66 */       int lastIndex = originalPath.length();
/* 67 */       String replaceString = originalPath.substring(originalPath.lastIndexOf('.'), lastIndex);
/* 68 */       String extension = FilenameUtils.getExtension(file.getOriginalFilename());
/* 69 */       String mediumPath = path.toFile().toString().replace(replaceString, "_medium" + replaceString);
/* 70 */       String smallPath = path.toFile().toString().replace(replaceString, "_small" + replaceString);
/*    */ 
/*    */       
/* 73 */       File f2 = new File(path.toFile().toString());
/* 74 */       ImageIO.write(originalImage, extension, f2);
/*    */       
/* 76 */       log.info(" the medium path : {}", mediumPath);
/* 77 */       File f3 = new File(mediumPath);
/* 78 */       ImageIO.write(mediumImg, extension, f3);
/*    */       
/* 80 */       log.info(" the small path : {}", smallPath);
/* 81 */       File f = new File(smallPath);
/* 82 */       ImageIO.write(smallImg, extension, f);
/*    */     }
/* 84 */     catch (IOException ioe) {
/* 85 */       log.error("IO exception occurred while trying to read image.");
/* 86 */       throw new RuntimeException(ioe);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Resource read(Path path) {
/* 91 */     return (Resource)new FileSystemResource(path.toFile());
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\persistence\repository\FileRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */