package tg.DocVers.Entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocInfo {
    private Long id;
    private Long idEmpresa;
    private String nome;
    private int versao;
    private Date criadoEm;
    private Date atualizadoEm;
}
