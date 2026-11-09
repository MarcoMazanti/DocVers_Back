package tg.DocVers.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tg.DocVers.Entity.Tags;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tags, Integer> {
    @Query(value = "SELECT t.* FROM tags t " +
            "WHERE t.idInstituicao = :idInstituicao " +
            "OR t.idInstituicao = (SELECT i.idMatriz FROM instituicao i WHERE i.id = :idInstituicao)" +
            "ORDER BY t.tag",
            nativeQuery = true)
    List<Tags> findAllByIdInstituicao(@Param("idInstituicao") Long idInstituicao);

    @Query(value = "SELECT t.tag FROM doc_info di " +
            "INNER JOIN doc_tags dt ON dt.idDocInfo = di.id " +
            "INNER JOIN tags t ON t.id = dt.idTag " +
            "WHERE di.id = :idDocInfo " +
            "AND (t.idInstituicao = :idInstituicao " +
            "OR t.idInstituicao = (SELECT i.idMatriz FROM instituicao i WHERE i.id = :idInstituicao))" +
            "ORDER BY t.tag",
            nativeQuery = true)
    List<String> findTagsByIdDocInfoAndIdInstituicao(
            @Param("idDocInfo") Long idDocInfo,
            @Param("idInstituicao") Long idInstituicao);
}
