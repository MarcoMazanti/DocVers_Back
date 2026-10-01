package tg.DocVers.Entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tg.DocVers.DTO.DocInfoDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "doc_info")
public class DocInfo {
    @Id
    private Long id;
    private Long idInstituicao;
    private Long idMatriz;
    private String nome;
    private int versaoMax;
    private ZonedDateTime criadoEm;
    private ZonedDateTime atualizadoEm;
    private boolean publicoInstituicao = false;

    public DocInfo(Long idInstituicao, Long idMatriz, String nome, int versaoMax, ZonedDateTime criadoEm, ZonedDateTime atualizadoEm) {
        this.idInstituicao = idInstituicao;
        this.idMatriz = idMatriz;
        this.nome = nome;
        this.versaoMax = versaoMax;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public DocInfo(DocInfoDTO docInfoDTO) {
        this.id = docInfoDTO.id();
        this.idInstituicao = docInfoDTO.idInstituicao();
        this.idMatriz = docInfoDTO.idMatriz();
        this.nome = docInfoDTO.nome();
        this.versaoMax = docInfoDTO.versaoMax();
        this.criadoEm = docInfoDTO.criadoEm();
        this.atualizadoEm = docInfoDTO.atualizadoEm();
        this.publicoInstituicao = docInfoDTO.publicoInstituicao();
    }
}
