package tg.DocVers.Entity;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "documentacao")
public class Documentacao {
    @Id
    private UUID id;
    private Long idDocInfo;
    private int versao;
    private String nomeArquivo;
    private TipoDocumento tipo;
    private String texto;
    private Date dataCriacao;

    // Executa-se a função antes de persistir o objeto no banco de dados
    @PrePersist
    public void prePersist() {
        if (id == null) {
            this.id = UUID.randomUUID();
        }

        if (dataCriacao == null) {
            this.dataCriacao = new Date();
        }

        this.nomeArquivo = (tipo != TipoDocumento.TXT) ? id + "." + tipo.toString().toLowerCase() : null;
    }

    public Documentacao(Long idDocInfo, String nomeArquivo, TipoDocumento tipo, String texto) {
        this.idDocInfo = idDocInfo;
        this.nomeArquivo = nomeArquivo;
        this.tipo = tipo;
        this.texto = texto;
    }

    public Documentacao(Long idDocInfo, String nomeArquivo, TipoDocumento tipo, String texto, int versao) {
        this.idDocInfo = idDocInfo;
        this.nomeArquivo = nomeArquivo;
        this.tipo = tipo;
        this.texto = texto;
        this.versao = versao;
    }

    public Documentacao(Long idDocInfo, String nomeArquivo, TipoDocumento tipo, String texto, int versao, Date dataCriacao) {
        this.idDocInfo = idDocInfo;
        this.nomeArquivo = nomeArquivo;
        this.tipo = tipo;
        this.texto = texto;
        this.versao = versao;
        this.dataCriacao = dataCriacao;
    }
}
