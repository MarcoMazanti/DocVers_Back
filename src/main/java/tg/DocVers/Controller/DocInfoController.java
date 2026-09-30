package tg.DocVers.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tg.DocVers.DTO.FullDocDTO;
import tg.DocVers.Entity.DocInfo;
import tg.DocVers.Service.DocInfoService;

import java.util.List;

@RestController
@RequestMapping("/api/docInfo")
public class DocInfoController {
    @Autowired
    private DocInfoService docInfoService;

    @GetMapping("/all")
    public ResponseEntity<List<DocInfo>> getAllDocInfos(@RequestAttribute("idInstituicao") Long idInstituicao) {
        return ResponseEntity.ok(docInfoService.getAllDocInfos(idInstituicao));
    }

    @GetMapping("/{idDoc}")
    public ResponseEntity<DocInfo> getDocInfo(@PathVariable Long idDoc, @RequestAttribute("idInstituicao") Long idInstituicao) {
        return ResponseEntity.ok(docInfoService.getDocInfo(idInstituicao, idDoc));
    }

    @GetMapping("/{idDoc}/versao{versao}")
    public ResponseEntity<FullDocDTO> getFullDocInfoVersao(@PathVariable Long idDoc, @PathVariable("versao") int versao, @RequestAttribute("idInstituicao") Long idInstituicao) {
        return ResponseEntity.ok(docInfoService.getDocInfoByVersion(idInstituicao, idDoc, versao));
    }

    @GetMapping("/{idDoc}/all")
    public ResponseEntity<FullDocDTO> getAllFullDocInfo(@PathVariable Long idDoc, @RequestAttribute("idInstituicao") Long idInstituicao) {
        return ResponseEntity.ok(docInfoService.getAllDocInfo(idInstituicao, idDoc));
    }

    @PostMapping("/create")
    public ResponseEntity<DocInfo> createDocInfo(@RequestParam("nome") String nome, @RequestAttribute("idInstituicao") Long idInstituicao) {
        return ResponseEntity.ok(docInfoService.create(nome, idInstituicao));
    }

    @PutMapping("/update/{idDoc}")
    public ResponseEntity<DocInfo> updateNomeDocInfo(@PathVariable Long idDoc, @RequestParam("nome") String nome, @RequestAttribute("idInstituicao") Long idInstituicao) {
        return ResponseEntity.ok(docInfoService.updateNome(idInstituicao, idDoc, nome));
    }

    @DeleteMapping("/{idDoc}")
    public ResponseEntity<Void> deleteDocInfo(@PathVariable Long idDoc, @RequestAttribute("idInstituicao") Long idInstituicao) {
        docInfoService.delete(idInstituicao, idDoc);
        return ResponseEntity.ok().build();
    }
}
