package tg.DocVers.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tg.DocVers.Entity.DocInfo;

import java.util.List;

@Repository
public interface DocInfoRepository extends JpaRepository<DocInfo, Long> {
    @Query(value = "SELECT di.* FROM doc_info di " +
            "INNER JOIN instituicao i ON di.idInstituicao = i.id " +
            "WHERE di.idInstituicao = :idInstituicao OR i.idMatriz = :idInstituicao", nativeQuery = true)
    List<DocInfo> findAllByIdInstituicao(
            @Param("idInstituicao") Long idInstituicao
    );
}
