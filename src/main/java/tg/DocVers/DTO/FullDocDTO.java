package tg.DocVers.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tg.DocVers.Entity.DocInfo;
import tg.DocVers.Entity.Documentacao;

import java.util.List;

public record FullDocDTO(DocInfoDTO docInfoDTO, List<Documentacao> documentacoes) {
    @JsonCreator
    public FullDocDTO(@JsonProperty("docInfoDTO") DocInfoDTO docInfoDTO,
                      @JsonProperty("documentacoes") List<Documentacao> documentacoes) {
        this.docInfoDTO = docInfoDTO;
        this.documentacoes = documentacoes;
    }
}
