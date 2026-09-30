package tg.DocVers.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tg.DocVers.DTO.FullDocDTO;
import tg.DocVers.DTO.NewDocDTO;
import tg.DocVers.Entity.Documentacao;
import tg.DocVers.Service.DocumentacaoService;

import java.io.File;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documentacao")
public class DocumentacaoController {
    @Autowired
    private DocumentacaoService documentacaoService;

    // ===========================================================================
    //  FOCO PARA DESENVOLVIMENTO DOS ENDPOINTS POST, DEVEM INTEGRAR COM O DOCKER
    // ===========================================================================

    @GetMapping("/{id}")
    public ResponseEntity<Documentacao> getDocumentacaoById(
            @PathVariable String id,
            @RequestAttribute("idInstituicao") Long idInstituicao) {
        return ResponseEntity.ok(documentacaoService.getDocumentacaoById(id, idInstituicao));
    }

    @GetMapping("/list/{idDocInfo}")
    public ResponseEntity<List<Documentacao>> getDocumentacoesByIdDocInfo(
            @PathVariable Long idDocInfo,
            @RequestAttribute("idInstituicao") Long idInstituicao) {
        return ResponseEntity.ok(documentacaoService.getDocumentacoesByIdDocInfo(idDocInfo, idInstituicao));
    }

    @GetMapping("/doc/{nomeArquivo}")
    public ResponseEntity<File> getFileFromDocker(
            @PathVariable String nomeArquivo,
            @RequestAttribute("idInstituicao") Long idInstituicao) {
        return ResponseEntity.ok(documentacaoService.getFileFromDocker(nomeArquivo, idInstituicao));
    }

    @PostMapping(path = "/create/full/{nomeDocumento}", consumes = {"multipart/form-data"})
    public ResponseEntity<FullDocDTO> createFullDocumentacao(
            @PathVariable String nomeDocumento,
            @RequestPart("arquivo") MultipartFile arquivo,
            @RequestPart("dados") NewDocDTO newDocDTO,
            @RequestAttribute("idInstituicao") Long idInstituicao) {
        return ResponseEntity.ok(documentacaoService.createFullDocumentacao(nomeDocumento, newDocDTO, idInstituicao, arquivo));
    }

    @PostMapping(path = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<Documentacao> createDocumentacao(
            @RequestPart("arquivo") MultipartFile arquivo,
            @RequestPart("dados") NewDocDTO newDocDTO,
            @RequestAttribute("idInstituicao") Long idInstituicao) {
        return ResponseEntity.ok(documentacaoService.createDocumentacao(newDocDTO, idInstituicao, arquivo));
    }

    @DeleteMapping("/{idDocInfo}")
    public void deleteListOfDocumentacao(
            @RequestBody List<UUID> listId,
            @PathVariable Long idDocInfo,
            @RequestAttribute("idInstituicao") Long idInstituicao) {
        documentacaoService.deleteListOfDocumentacao(listId, idDocInfo, idInstituicao);
    }
}
