package tg.DocVers.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tg.DocVers.DTO.FullDocDTO;
import tg.DocVers.DTO.NewDocDTO;
import tg.DocVers.Entity.Documentacao;
import tg.DocVers.Service.DocumentacaoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documentacao")
public class DocumentacaoController {
    @Autowired
    private DocumentacaoService documentacaoService;

    @GetMapping("/{id}")
    public ResponseEntity<Documentacao> getDocumentacaoById(String id, Long idEmpresa) {
        return ResponseEntity.ok(documentacaoService.getDocumentacaoById(id, idEmpresa));
    }

    @GetMapping("/list/{idDocInfo}")
    public ResponseEntity<List<Documentacao>> getDocumentacoesByIdDocInfo(Long idDocInfo, Long idEmpresa) {
        return ResponseEntity.ok(documentacaoService.getDocumentacoesByIdDocInfo(idDocInfo, idEmpresa));
    }

    @PostMapping("/create/full")
    public ResponseEntity<FullDocDTO> createFullDocumentacao(String nomeDocumento, NewDocDTO newDocDTO, Long idEmpresa) {
        return ResponseEntity.ok(documentacaoService.createFullDocumentacao(nomeDocumento, newDocDTO, idEmpresa));
    }

    @PostMapping("/create")
    public ResponseEntity<Documentacao> createDocumentacao(NewDocDTO newDocDTO, Long idEmpresa) {
        return ResponseEntity.ok(documentacaoService.createDocumentacao(newDocDTO, idEmpresa));
    }

    @DeleteMapping()
    public void deleteListOfDocumentacao(List<UUID> listId, Long idDocInfo, Long idEmpresa) {
        documentacaoService.deleteListOfDocumentacao(listId, idDocInfo, idEmpresa);
    }
}
