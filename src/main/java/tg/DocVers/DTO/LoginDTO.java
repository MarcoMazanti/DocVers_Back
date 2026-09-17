package tg.DocVers.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginDTO(String cnpj, String senha) {
    @JsonCreator
    public LoginDTO(@JsonProperty("cnpj") String cnpj,
                    @JsonProperty("senha") String senha) {
        this.cnpj = cnpj;
        this.senha = senha;
    }

    public boolean exist() {
        return cnpj != null && senha != null;
    }
}
