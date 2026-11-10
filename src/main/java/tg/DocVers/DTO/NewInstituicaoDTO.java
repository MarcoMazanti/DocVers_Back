package tg.DocVers.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record NewInstituicaoDTO(Long idMatriz, String nome, String cnpj, String senha, String tokenPrefix) {
    @JsonCreator
    public NewInstituicaoDTO(@JsonProperty("idMatriz") Long idMatriz,
                             @JsonProperty("nome") String nome,
                             @JsonProperty("cnpj") String cnpj,
                             @JsonProperty("senha") String senha,
                             @JsonProperty("tokenPrefix") String tokenPrefix) {
        this.idMatriz = idMatriz;
        this.nome = nome;
        this.cnpj = cnpj;
        this.senha = senha;
        this.tokenPrefix = tokenPrefix;
    }

    public boolean exist() {
        return idMatriz != null && nome != null && cnpj != null && senha != null && tokenPrefix != null;
    }
}
