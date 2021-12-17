package org.egov.filestore.repository;

import java.util.List;
import java.util.Map;
import org.egov.filestore.domain.model.Artifact;

public interface CloudFilesManager {
  void saveFiles(List<Artifact> paramList);
  
  Map<String, String> getFiles(Map<String, String> paramMap);
}


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\repository\CloudFilesManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */