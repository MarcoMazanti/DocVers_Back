package tg.DocVers.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tg.DocVers.Entity.TipoDocumento;

public record NewDocDTO(Long idDocInfo, String nomeArquivo, TipoDocumento tipo, String texto) {
    @JsonCreator
    public NewDocDTO(@JsonProperty("idDocInfo") Long idDocInfo,
                     @JsonProperty("nomeArquivo") String nomeArquivo,
                     @JsonProperty("tipo") TipoDocumento tipo,
                     @JsonProperty("texto") String texto) {
        this.idDocInfo = idDocInfo;
        this.nomeArquivo = nomeArquivo;
        this.tipo = tipo;
        this.texto = texto;
    }
}
