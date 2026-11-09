package tg.DocVers.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "instituicao")
public class Instituicao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long idMatriz;
    private String nome;
    private String cnpj; // Campo único para identificar a empresa
    private String senha; // senha salva em hash
    private String token; // token gerado de forma aleatória e armazenado em hash, o token puro deve ter no mínimo 45 caracteres
    private String tokenPrefix; // prefixo determinado pela empresa
    @Enumerated(EnumType.STRING)
    private SituacaoEmpresa situacao;
    private LocalDate dataContrato;
    private LocalDate dataLimiteContrato;

    // O token não deve sair do sistema se não for para apresentar ele ao respectivo cliente ao efetuar o login no site
    // Senha e o token serão armazenados no sistema em hash, se for esquecido o token, o usuário poderá solicitar um novo token
    // Se ultrapassar o limite de tempo de contrato, a conta fica bloqueada até segundas alterações - Efetuar via scheduler
    // Empresa e os seus dados são deletados após 1 mês com o status como INATIVO - Efetuar via scheduler
    // Possível funcionalidade: enviar um e-mail para a empresa informando a troca do token via KAFKA


    public Instituicao(Long idMatriz, String nome, String cnpj, String senha, String tokenPrefix) {
        this.idMatriz = idMatriz;
        this.nome = nome;
        this.cnpj = cnpj;
        this.senha = senha;
        this.tokenPrefix = tokenPrefix;
    }
}
