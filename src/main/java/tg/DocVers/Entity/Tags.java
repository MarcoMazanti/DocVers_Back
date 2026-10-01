package tg.DocVers.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "tags")
public class Tags {
    @Id
    private int id;
    private Long idInstituicao;
    private String tag;

    public Tags(Long idInstituicao, String tag) {
        this.idInstituicao = idInstituicao;
        this.setTag(tag);
    }

    public Tags(int id, Long idInstituicao, String tag) {
        this.id = id;
        this.idInstituicao = idInstituicao;
        this.setTag(tag);
    }

    public void setTag(String tag) {
        this.tag = tag.toUpperCase().replace(" ", "_");
    }
}
