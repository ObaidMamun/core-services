package org.egov.filestore.persistence.repository;

import java.util.List;
import org.egov.filestore.persistence.entity.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStoreJpaRepository extends JpaRepository<Artifact, Long> {
  Artifact findByFileStoreIdAndTenantId(String paramString1, String paramString2);
  
  List<Artifact> findByTagAndTenantId(String paramString1, String paramString2);
  
  @Query(value = "SELECT * FROM eg_filestoremap T WHERE T.tenantId = (?1) AND T.fileStoreId IN (?2)", nativeQuery = true)
  List<Artifact> findByTenantIdAndFileStoreIdList(String paramString, List<String> paramList);
}


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\persistence\repository\FileStoreJpaRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */