package tg.DocVers.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tg.DocVers.Entity.Instituicao;
import tg.DocVers.Entity.SituacaoEmpresa;

import java.util.Date;

public record InstituicaoDTO(Long id, Long idMatriz, String nome, String cnpj, String tokenPrefix, SituacaoEmpresa situacao, Date dataContrato, Date dataLimiteContrato) {
    @JsonCreator
    public InstituicaoDTO(@JsonProperty("id") Long id,
                          @JsonProperty("idMatriz") Long idMatriz,
                          @JsonProperty("nome") String nome,
                          @JsonProperty("cnpj") String cnpj,
                          @JsonProperty("tokenPrefix") String tokenPrefix,
                          @JsonProperty("situacao") SituacaoEmpresa situacao,
                          @JsonProperty("dataContrato") Date dataContrato,
                          @JsonProperty("dataLimiteContrato") Date dataLimiteContrato) {
        this.id = id;
        this.idMatriz = idMatriz;
        this.nome = nome;
        this.cnpj = cnpj;
        this.tokenPrefix = tokenPrefix;
        this.situacao = situacao;
        this.dataContrato = dataContrato;
        this.dataLimiteContrato = dataLimiteContrato;
    }

    public InstituicaoDTO(Instituicao instituicao) {
        this(
                instituicao.getId(),
                instituicao.getIdMatriz(),
                instituicao.getNome(),
                instituicao.getCnpj(),
                instituicao.getTokenPrefix(),
                instituicao.getSituacao(),
                instituicao.getDataContrato(),
                instituicao.getDataLimiteContrato());
    }
}
