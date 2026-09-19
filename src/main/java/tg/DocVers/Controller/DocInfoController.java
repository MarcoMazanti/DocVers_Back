package tg.DocVers.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tg.DocVers.DTO.FullDocDTO;
import tg.DocVers.Entity.DocInfo;
import tg.DocVers.Service.DocInfoService;

@RestController
@RequestMapping("/api/docInfo")
public class DocInfoController {
    @Autowired
    private DocInfoService docInfoService;

    @GetMapping("/{idDoc}")
    public ResponseEntity<DocInfo> getDocInfo(@PathVariable Long idDoc, @RequestAttribute("id") Long idEmpresa) {
        return ResponseEntity.ok(docInfoService.getDocInfo(idEmpresa, idDoc));
    }

    @GetMapping("/{idDoc}/versao{versao}")
    public ResponseEntity<FullDocDTO> getFullDocInfoVersao(@PathVariable Long idDoc, @PathVariable("versao") int versao, @RequestAttribute("id") Long idEmpresa) {
        return ResponseEntity.ok(docInfoService.getDocInfoByVersion(idEmpresa, idDoc, versao));
    }

    @GetMapping("/{idDoc}/all")
    public ResponseEntity<FullDocDTO> getAllFullDocInfo(@PathVariable Long idDoc, @RequestAttribute("id") Long idEmpresa) {
        return ResponseEntity.ok(docInfoService.getAllDocInfo(idEmpresa, idDoc));
    }

    @PostMapping("/create")
    public ResponseEntity<DocInfo> createDocInfo(@RequestParam("nome") String nome, @RequestAttribute("id") Long idEmpresa) {
        return ResponseEntity.ok(docInfoService.create(nome, idEmpresa));
    }

    @PutMapping("/update/{idDoc}")
    public ResponseEntity<DocInfo> updateNomeDocInfo(@PathVariable Long idDoc, @RequestParam("nome") String nome, @RequestAttribute("id") Long idEmpresa) {
        return ResponseEntity.ok(docInfoService.updateNome(idEmpresa, idDoc, nome));
    }

    @DeleteMapping("/{idDoc}")
    public ResponseEntity<Void> deleteDocInfo(@PathVariable Long idDoc, @RequestAttribute("id") Long idEmpresa) {
        docInfoService.delete(idEmpresa, idDoc);
        return ResponseEntity.ok().build();
    }
}
