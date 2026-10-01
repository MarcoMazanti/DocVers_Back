package tg.DocVers.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tg.DocVers.Entity.Tags;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tags, Integer> {
    @Query(value = "SELECT t.* FROM tags t " +
            "WHERE t.idInstituicao = :idInstituicao " +
            "OR t.idInstituicao = (SELECT i.idMatriz FROM instituicao i WHERE i.id = :idInstituicao)",
            nativeQuery = true)
    List<Tags> findAllByIdInstituicao(Long idInstituicao);
}
