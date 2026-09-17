package tg.DocVers.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ResetSenhaDTO(String cnpj, String senha) {
    @JsonCreator
    public ResetSenhaDTO(@JsonProperty("cnpj") String cnpj,
                         @JsonProperty("senha") String senha) {
        this.cnpj = cnpj;
        this.senha = senha;
    }
}
