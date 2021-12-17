/*     */ package org.egov.filestore.web.controller;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.tika.Tika;
/*     */ import org.egov.filestore.config.FileStoreConfig;
/*     */ import org.egov.filestore.domain.model.FileInfo;
/*     */ import org.egov.filestore.domain.model.Resource;
/*     */ import org.egov.filestore.domain.service.StorageService;
/*     */ import org.egov.filestore.web.contract.File;
/*     */ import org.egov.filestore.web.contract.FileStoreResponse;
/*     */ import org.egov.filestore.web.contract.GetFilesByTagResponse;
/*     */ import org.egov.filestore.web.contract.ResponseFactory;
/*     */ import org.egov.filestore.web.contract.StorageResponse;
/*     */ import org.egov.tracer.model.CustomException;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.stereotype.Controller;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
/*     */ import org.springframework.web.bind.annotation.ResponseStatus;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Controller
/*     */ @RequestMapping({"/v1/files"})
/*     */ public class StorageController
/*     */ {
/*  44 */   private static final Logger log = LoggerFactory.getLogger(StorageController.class);
/*     */   
/*     */   private StorageService storageService;
/*     */   
/*     */   private ResponseFactory responseFactory;
/*     */   private FileStoreConfig fileStoreConfig;
/*     */   
/*     */   @Autowired
/*     */   public StorageController(StorageService storageService, ResponseFactory responseFactory, FileStoreConfig fileStoreConfig) {
/*  53 */     this.storageService = storageService;
/*  54 */     this.responseFactory = responseFactory;
/*  55 */     this.fileStoreConfig = fileStoreConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   @GetMapping({"/id"})
/*     */   @ResponseBody
/*     */   public ResponseEntity<Resource> getFile(@RequestParam("tenantId") String tenantId, @RequestParam("fileStoreId") String fileStoreId) {
/*  62 */     Resource resource = null;
/*     */     try {
/*  64 */       resource = this.storageService.retrieve(fileStoreId, tenantId);
/*  65 */     } catch (IOException e) {
/*     */       
/*  67 */       e.printStackTrace();
/*     */     } 
/*  69 */     String fileName = resource.getFileName().substring(resource.getFileName().lastIndexOf('/') + 1, resource.getFileName().length());
/*  70 */     return ((ResponseEntity.BodyBuilder)((ResponseEntity.BodyBuilder)ResponseEntity.ok()
/*  71 */       .header("Content-Disposition", new String[] { "attachment; filename=\"" + fileName + "\""
/*  72 */         })).header("Content-Type", new String[] { resource.getContentType() })).body(resource.getResource());
/*     */   }
/*     */ 
/*     */   
/*     */   @GetMapping({"/metadata"})
/*     */   @ResponseBody
/*     */   public ResponseEntity<Resource> getMetaData(@RequestParam("tenantId") String tenantId, @RequestParam("fileStoreId") String fileStoreId) {
/*  79 */     Resource resource = null;
/*     */     try {
/*  81 */       resource = this.storageService.retrieve(fileStoreId, tenantId);
/*  82 */     } catch (IOException e) {
/*     */       
/*  84 */       e.printStackTrace();
/*     */     } 
/*  86 */     resource.setResource(null);
/*  87 */     return new ResponseEntity(resource, HttpStatus.OK);
/*     */   }
/*     */ 
/*     */   
/*     */   @GetMapping(value = {"/tag"}, produces = {"application/json;charset=UTF-8"})
/*     */   @ResponseBody
/*     */   public GetFilesByTagResponse getUrlListByTag(@RequestParam("tenantId") String tenantId, @RequestParam("tag") String tag) {
/*  94 */     List<FileInfo> fileInfoList = this.storageService.retrieveByTag(tag, tenantId);
/*  95 */     return this.responseFactory.getFilesByTagResponse(fileInfoList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(produces = {"application/json;charset=UTF-8"})
/*     */   @ResponseStatus(HttpStatus.CREATED)
/*     */   @ResponseBody
/*     */   public StorageResponse storeFiles(@RequestParam("file") List<MultipartFile> files, @RequestParam("tenantId") String tenantId, @RequestParam(value = "module", required = true) String module, @RequestParam(value = "tag", required = false) String tag) {
/* 106 */     Map<String, List<String>> allowedFormatsMap = this.fileStoreConfig.getAllowedFormatsMap();
/* 107 */     Set<String> keySet = this.fileStoreConfig.getAllowedKeySet();
/* 108 */     String inputStreamAsString = null;
/* 109 */     String inputFormat = null;
/* 110 */     for (MultipartFile file : files) {
/*     */       
/* 112 */       String extension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
/* 113 */       if (!allowedFormatsMap.containsKey(extension)) {
/* 114 */         throw new CustomException("EG_FILESTORE_INVALID_INPUT", "Inalvid input provided for file : " + extension + ", please upload any of the allowed formats : " + keySet);
/*     */       }
/* 116 */       Tika tika = new Tika();
/*     */ 
/*     */       
/*     */       try {
/* 120 */         inputStreamAsString = IOUtils.toString(file.getInputStream(), this.fileStoreConfig.getImageCharsetType());
/* 121 */         InputStream ipStreamForValidation = IOUtils.toInputStream(inputStreamAsString, this.fileStoreConfig.getImageCharsetType());
/* 122 */         inputFormat = tika.detect(ipStreamForValidation);
/* 123 */         log.info(" the file format is : " + inputFormat);
/* 124 */         ipStreamForValidation.close();
/* 125 */       } catch (IOException e) {
/* 126 */         throw new CustomException("EG_FILESTORE_PARSING_ERROR", "not able to parse the input please upload a proper file of allowed type : " + e.getMessage());
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 131 */       if (!((List)allowedFormatsMap.get(extension)).contains(inputFormat)) {
/* 132 */         throw new CustomException("EG_FILESTORE_INVALID_INPUT", "Inalvid input provided for file, the extension does not match the file format. Please upload any of the allowed formats : " + keySet);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 137 */     List<String> fileStoreIds = this.storageService.save(files, module, tag, tenantId, inputStreamAsString);
/* 138 */     return getStorageResponse(fileStoreIds, tenantId);
/*     */   }
/*     */   
/*     */   private StorageResponse getStorageResponse(List<String> fileStorageIds, String tenantId) {
/* 142 */     List<File> files = new ArrayList<>();
/* 143 */     for (String fileStorageId : fileStorageIds) {
/* 144 */       File f = new File(fileStorageId, tenantId);
/* 145 */       files.add(f);
/*     */     } 
/* 147 */     return new StorageResponse(files);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GetMapping({"/url"})
/*     */   @ResponseBody
/*     */   public ResponseEntity<Map<String, Object>> getUrls(@RequestParam("tenantId") String tenantId, @RequestParam("fileStoreIds") List<String> fileStoreIds) {
/* 155 */     Map<String, Object> responseMap = new HashMap<>();
/* 156 */     if (fileStoreIds.isEmpty())
/* 157 */       return new ResponseEntity(new HashMap<>(), HttpStatus.OK); 
/* 158 */     Map<String, String> maps = this.storageService.getUrls(tenantId, fileStoreIds);
/*     */     
/* 160 */     List<FileStoreResponse> responses = new ArrayList<>();
/* 161 */     for (Map.Entry<String, String> entry : maps.entrySet())
/*     */     {
/* 163 */       responses.add(FileStoreResponse.builder().id(entry.getKey()).url(entry.getValue()).build());
/*     */     }
/* 165 */     responseMap.putAll(maps);
/* 166 */     responseMap.put("fileStoreIds", responses);
/*     */     
/* 168 */     return new ResponseEntity(responseMap, HttpStatus.OK);
/*     */   }
/*     */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\web\controller\StorageController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */