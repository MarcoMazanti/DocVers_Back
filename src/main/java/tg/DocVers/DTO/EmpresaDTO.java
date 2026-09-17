package tg.DocVers.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tg.DocVers.Entity.Empresa;
import tg.DocVers.Entity.SituacaoEmpresa;

import java.util.Date;

public record EmpresaDTO(Long id, String nome, String cnpj, String tokenPrefix, SituacaoEmpresa situacao, Date dataContrato, Date dataLimiteContrato) {
    @JsonCreator
    public EmpresaDTO(@JsonProperty("id") Long id,
                      @JsonProperty("nome") String nome,
                      @JsonProperty("cnpj") String cnpj,
                      @JsonProperty("tokenPrefix") String tokenPrefix,
                      @JsonProperty("situacao") SituacaoEmpresa situacao,
                      @JsonProperty("dataContrato") Date dataContrato,
                      @JsonProperty("dataLimiteContrato") Date dataLimiteContrato) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.tokenPrefix = tokenPrefix;
        this.situacao = situacao;
        this.dataContrato = dataContrato;
        this.dataLimiteContrato = dataLimiteContrato;
    }

    public EmpresaDTO(Empresa empresa) {
        this(
                empresa.getId(),
                empresa.getNome(),
                empresa.getCnpj(),
                empresa.getTokenPrefix(),
                empresa.getSituacao(),
                empresa.getDataContrato(),
                empresa.getDataLimiteContrato());
    }
}
