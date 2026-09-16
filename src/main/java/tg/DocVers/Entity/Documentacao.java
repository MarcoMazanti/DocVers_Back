package tg.DocVers.Entity;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Documentacao {
    private UUID id;
    private Long idDocInfo;
    private String nomeArquivo;
    private TipoDocumento tipo;
    private String extensao;
    private String texto;
    private Date dataCriacao;
}
