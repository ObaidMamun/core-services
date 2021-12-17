/*    */ package org.egov.filestore.web.common;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.egov.filestore.domain.exception.ArtifactNotFoundException;
/*    */ import org.egov.filestore.domain.exception.EmptyFileUploadRequestException;
/*    */ import org.egov.tracer.model.CustomException;
/*    */ import org.egov.tracer.model.Error;
/*    */ import org.egov.tracer.model.ErrorRes;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.http.ResponseEntity;
/*    */ import org.springframework.web.bind.annotation.ControllerAdvice;
/*    */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*    */ import org.springframework.web.bind.annotation.ResponseStatus;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ @ControllerAdvice
/*    */ @RestController
/*    */ public class GlobalExceptionHandler {
/* 23 */   private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
/*    */ 
/*    */   
/*    */   @ExceptionHandler({ArtifactNotFoundException.class})
/*    */   @ResponseStatus(HttpStatus.NOT_FOUND)
/*    */   public String handleFileNotFoundException(Exception e) {
/* 29 */     log.error(e.getMessage());
/* 30 */     return e.getMessage();
/*    */   }
/*    */   
/*    */   @ExceptionHandler({EmptyFileUploadRequestException.class})
/*    */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*    */   public String handleEmptyFileUploadRequestException(Exception e) {
/* 36 */     log.error(e.getMessage());
/* 37 */     return e.getMessage();
/*    */   }
/*    */ 
/*    */   
/*    */   @ExceptionHandler({CustomException.class})
/*    */   @ResponseStatus(HttpStatus.NOT_FOUND)
/*    */   public ResponseEntity<?> customException(CustomException e) {
/* 44 */     ErrorRes errorRes = new ErrorRes();
/* 45 */     List<Error> errors = new ArrayList<>();
/* 46 */     populateCustomErrros(e, errors);
/* 47 */     errorRes.setErrors(errors);
/* 48 */     return new ResponseEntity(errorRes, HttpStatus.BAD_REQUEST);
/*    */   }
/*    */   
/*    */   private void populateCustomErrros(CustomException customException, List<Error> errors) {
/* 52 */     Map<String, String> map = customException.getErrors();
/* 53 */     if (map != null && !map.isEmpty()) {
/* 54 */       for (Map.Entry<String, String> entry : map.entrySet()) {
/* 55 */         Error error = new Error();
/* 56 */         error.setCode(entry.getKey());
/* 57 */         error.setMessage(entry.getValue());
/* 58 */         errors.add(error);
/*    */       } 
/*    */     } else {
/* 61 */       Error error = new Error();
/* 62 */       error.setCode(customException.getCode());
/* 63 */       error.setMessage(customException.getMessage());
/* 64 */       errors.add(error);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\web\common\GlobalExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */