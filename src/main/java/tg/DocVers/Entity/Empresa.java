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
    private String cnpj; // Campo único para identificar a empresa
    private String senha;
    private String token; // token gerado de forma aleatória e armazenado em hash, o token puro deve ter no mínimo 45 caracteres
    private String tokenPrefix; // prefixo determinado pela empresa
    private SituacaoEmpresa situacao;
    private Date dataContrato;
    private Date dataLimiteContrato;

    // O token não deve sair do sistema se não for para apresentar ele ao respectivo cliente ao efetuar o login no site
    // Senha e o token serão armazenados no sistema em hash, se for esquecido o token, o usuário poderá solicitar um novo token
    // Se ultrapassar o limite de tempo de contrato, a conta fica bloqueada até segundas alterações - Efetuar via scheduler
    // Empresa e os seus dados são deletados após 1 mês com o status como INATIVO - Efetuar via scheduler
    // Possível funcionalidade: enviar um e-mail para a empresa informando a troca do token via KAFKA
}
