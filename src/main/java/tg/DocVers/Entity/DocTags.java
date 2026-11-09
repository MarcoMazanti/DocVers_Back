package tg.DocVers.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "doc_tags")
public class DocTags {
    @Id
    private int id;
    private int idDocInfo;
    private int idTag;

    public DocTags(int idDocInfo, int idTag) {
        this.idDocInfo = idDocInfo;
        this.idTag = idTag;
    }
}
