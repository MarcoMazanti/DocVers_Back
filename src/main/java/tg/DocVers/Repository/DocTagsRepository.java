package tg.DocVers.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.DocVers.Entity.DocTags;

@Repository
public interface DocTagsRepository extends JpaRepository<DocTags, Integer> {
}
