package tg.DocVers.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tg.DocVers.Entity.DocInfo;

import java.time.ZonedDateTime;
import java.util.List;

public record DocInfoDTO(Long id,
                         Long idInstituicao,
                         Long idMatriz,
                         String nome,
                         int versaoMax,
                         ZonedDateTime criadoEm,
                         ZonedDateTime atualizadoEm,
                         boolean publicoInstituicao,
                         List<String> tags) {
    @JsonCreator
    public DocInfoDTO(@JsonProperty("id") Long id,
                      @JsonProperty("idInstituicao") Long idInstituicao,
                      @JsonProperty("idMatriz") Long idMatriz,
                      @JsonProperty("nome") String nome,
                      @JsonProperty("versaoMax") int versaoMax,
                      @JsonProperty("criadoEm") ZonedDateTime criadoEm,
                      @JsonProperty("atualizadoEm") ZonedDateTime atualizadoEm,
                      @JsonProperty("publicoInstituicao") boolean publicoInstituicao,
                      @JsonProperty("tags") List<String> tags) {
        this.id = id;
        this.idInstituicao = idInstituicao;
        this.idMatriz = idMatriz;
        this.nome = nome;
        this.versaoMax = versaoMax;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.publicoInstituicao = publicoInstituicao;
        this.tags = tags;
    }

    public DocInfoDTO(DocInfo docInfo, List<String> tags) {
        this(docInfo.getId(),
                docInfo.getIdInstituicao(),
                docInfo.getIdMatriz(),
                docInfo.getNome(),
                docInfo.getVersaoMax(),
                docInfo.getCriadoEm(),
                docInfo.getAtualizadoEm(),
                docInfo.isPublicoInstituicao(),
                tags);
    }
}
