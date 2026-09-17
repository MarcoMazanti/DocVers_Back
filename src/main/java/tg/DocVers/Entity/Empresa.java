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
    private String senha;
    private String token; // token gerado de forma aleatória e armazenado em hash, o token puro deve ter no mínimo 25 caracteres
    private String tokenPrefix; // salva as 5 primeiras letras do token puro
    private SituacaoEmpresa situacao;
    private Date dataContrato;
    private Date dataLimiteContrato;

    // O token não deve sair do sistema se não for para apresentar ele ao respectivo cliente ao efetuar o login no site
    // Senha e o token serão armazenados no sistema em hash, se for esquecido o token, o usuário poderá solicitar um novo token
    // Se ultrapassar o limite de tempo de contrato, a conta fica bloqueada até segundas alterações
    // Possível funcionalidade: enviar um e-mail para a empresa informando a troca do token via KAFKA
}
