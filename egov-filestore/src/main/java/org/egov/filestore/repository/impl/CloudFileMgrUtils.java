package org.egov.filestore.repository.impl;

import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.SharedAccessBlobPolicy;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import org.egov.tracer.model.CustomException;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CloudFileMgrUtils {
  private static final Logger log = LoggerFactory.getLogger(CloudFileMgrUtils.class);
  
  @Value("${image.small}")
  private String _small;
  
  @Value("${image.medium}")
  private String _medium;
  
  @Value("${image.large}")
  private String _large;
  
  @Value("${image.small.width}")
  private Integer smallWidth;
  
  @Value("${image.medium.width}")
  private Integer mediumWidth;
  
  @Value("${image.large.width}")
  private Integer largeWidth;
  
  @Value("${azure.sas.expiry.time.in.secs}")
  private Integer azureSASExpiryinSecs;
  
  public Map<String, BufferedImage> createVersionsOfImage(InputStream inputStream, String fileName) {
    Map<String, BufferedImage> mapOfImagesAndPaths = new HashMap<>();
    try {
      BufferedImage originalImage = ImageIO.read(inputStream);
      if (null == originalImage) {
        Map<String, String> map = new HashMap<>();
        map.put("Image Source Unavailable", "Image File present in upload request is Invalid/Not Readable");
        throw new CustomException(map);
      } 
      BufferedImage largeImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, this.mediumWidth.intValue(), new BufferedImageOp[] { null, Scalr.OP_ANTIALIAS });
      BufferedImage mediumImg = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, this.mediumWidth.intValue(), new BufferedImageOp[] { null, Scalr.OP_ANTIALIAS });
      BufferedImage smallImg = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, this.smallWidth.intValue(), new BufferedImageOp[] { null, Scalr.OP_ANTIALIAS });
      int lastIndex = fileName.length();
      String replaceString = fileName.substring(fileName.lastIndexOf('.'), lastIndex);
      mapOfImagesAndPaths.put(fileName.replace(replaceString, this._large + replaceString), largeImage);
      mapOfImagesAndPaths.put(fileName.replace(replaceString, this._medium + replaceString), mediumImg);
      mapOfImagesAndPaths.put(fileName.replace(replaceString, this._small + replaceString), smallImg);
      log.info("Different versions of the image created!");
    } catch (Exception e) {
      log.error("Error while creating different versions of the image: ", e);
    } 
    return mapOfImagesAndPaths;
  }
  
  public String generateSASToken(CloudBlobClient azureBlobClient, String absolutePath) {
    String sasUrl = null;
    try {
      int index = absolutePath.indexOf('/');
      String containerName = absolutePath.substring(0, index);
      String fileNameWithPath = absolutePath.substring(index + 1, absolutePath.length());
      CloudBlobContainer container = azureBlobClient.getContainerReference(containerName);
      CloudBlockBlob blob = (CloudBlockBlob)container.getBlobReferenceFromServer(fileNameWithPath);
      SharedAccessBlobPolicy sasConstraints = new SharedAccessBlobPolicy();
      sasConstraints.setSharedAccessStartTime(new Date(System.currentTimeMillis()));
      sasConstraints
        .setSharedAccessExpiryTime(new Date(System.currentTimeMillis() + (this.azureSASExpiryinSecs.intValue() * 1000)));
      sasConstraints.setPermissionsFromString("r");
      String sasBlobToken = blob.generateSharedAccessSignature(sasConstraints, null);
      sasUrl = sasBlobToken;
    } catch (Exception e) {
      log.error("Error while generating sas token: ", e);
      log.error("Exception while generating SAS token: ", e);
    } 
    return sasUrl;
  }
  
  private static String getHMAC256(String key, String input) {
    Mac sha256_HMAC = null;
    String hash = null;
    try {
      sha256_HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
      sha256_HMAC.init(secret_key);
      Base64.Encoder encoder = Base64.getEncoder();
      hash = new String(encoder.encode(sha256_HMAC.doFinal(input.getBytes("UTF-8"))));
    } catch (Exception e) {
      log.error("Exception while generating hash for the SAS token: ", e);
    } 
    return hash;
  }
  
  public Boolean isFileAnImage(String filePath) {
    Boolean isFileAnImage = Boolean.valueOf(false);
    String[] imageFormats = { "png", "jpeg", "jpg" };
    if ((filePath.split("[\\.]")).length > 1) {
      String extension = filePath.substring(filePath.lastIndexOf('.') + 1, filePath.length());
      if (Arrays.<String>asList(imageFormats).contains(extension))
        isFileAnImage = Boolean.valueOf(true); 
    } 
    return isFileAnImage;
  }
}
