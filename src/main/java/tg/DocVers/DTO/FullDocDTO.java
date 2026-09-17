package tg.DocVers.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tg.DocVers.Entity.DocInfo;
import tg.DocVers.Entity.Documentacao;

import java.util.List;

public record FullDocDTO(DocInfo docInfo, List<Documentacao> documentacoes) {
    @JsonCreator
    public FullDocDTO(@JsonProperty("docInfo") DocInfo docInfo,
                      @JsonProperty("documentacoes") List<Documentacao> documentacoes) {
        this.docInfo = docInfo;
        this.documentacoes = documentacoes;
    }
}
