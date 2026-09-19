package tg.DocVers.Entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "doc_info")
public class DocInfo {
    @Id
    private Long id;
    private Long idEmpresa;
    private String nome;
    private int versaoMax;
    private ZonedDateTime criadoEm;
    private ZonedDateTime atualizadoEm;

    public DocInfo(Long idEmpresa, String nome, int versaoMax, ZonedDateTime criadoEm, ZonedDateTime atualizadoEm) {
        this.idEmpresa = idEmpresa;
        this.nome = nome;
        this.versaoMax = versaoMax;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }
}
