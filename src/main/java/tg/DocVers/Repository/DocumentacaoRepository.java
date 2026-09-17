package tg.DocVers.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.DocVers.Entity.Documentacao;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentacaoRepository extends JpaRepository<Documentacao, Integer> {
    Optional<Documentacao> findByIdDocInfoAndVersao(Long idDocInfo, int versao);
    List<Documentacao> findByIdDocInfo(Long idDocInfo);
}
