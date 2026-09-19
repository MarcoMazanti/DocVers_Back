package tg.DocVers.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tg.DocVers.Entity.Documentacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentacaoRepository extends JpaRepository<Documentacao, UUID> {
    Optional<Documentacao> findByIdDocInfoAndVersao(Long idDocInfo, int versao);
    List<Documentacao> findAllByIdDocInfo(Long idDocInfo);

    @Query(value = "SELECT d.* FROM documentacao d " +
            "INNER JOIN doc_info di ON d.idDocInfo = di.id " +
            "WHERE d.id IN (:listId) AND di.id = :idDocInfo AND di.idEmpresa = :idEmpresa",
            nativeQuery = true)
    List<Documentacao> findAllByIdDocInfoAndIdEmpresaAndId(
            @Param("listId") List<UUID> listId,
            @Param("idDocInfo") Long idDocInfo,
            @Param("idEmpresa") Long idEmpresa
    );

    @Query(value = "SELECT d.* FROM documentacao d " +
            "INNER JOIN doc_info di ON d.idDocInfo = di.id " +
            "WHERE di.id = :idDocInfo AND di.idEmpresa = :idEmpresa " +
            "ORDER BY d.versao DESC LIMIT 1",
            nativeQuery = true)
    Optional<Documentacao> findLastVersionByIdDocInfoAndIdEmpresa(
            @Param("idDocInfo") Long idDocInfo,
            @Param("idEmpresa") Long idEmpresa
    );
}
