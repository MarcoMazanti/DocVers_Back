package tg.DocVers.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empresa {
    private Long id;
    private String nome;
    private String cnpj;
    private String token;
    private SituacaoEmpresa situacao;
    private Date dataLimiteCadastro;
}
