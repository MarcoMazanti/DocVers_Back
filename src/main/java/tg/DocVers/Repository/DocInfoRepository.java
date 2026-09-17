package tg.DocVers.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.DocVers.Entity.DocInfo;

@Repository
public interface DocInfoRepository extends JpaRepository<DocInfo, Integer> {
}
