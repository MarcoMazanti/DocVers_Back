package tg.DocVers.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tg.DocVers.DTO.InstituicaoDTO;
import tg.DocVers.DTO.LoginDTO;
import tg.DocVers.DTO.ResetSenhaDTO;
import tg.DocVers.Service.InstituicaoService;

@RestController
@RequestMapping("/api/instituicao")
public class InstituicaoController {
    @Autowired
    private InstituicaoService instituicaoService;

    @GetMapping("/polo/{idAlvo}")
    public ResponseEntity<InstituicaoDTO> getPoloInstituicao(@PathVariable Long idAlvo, @RequestAttribute("idInstituicao") Long idInstituicao) {
        return ResponseEntity.ok(instituicaoService.getPoloInstituicao(idAlvo, idInstituicao));
    }

    @PostMapping("/login")
    public ResponseEntity<InstituicaoDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(instituicaoService.login(loginDTO));
    }

    @PostMapping("/reset/senha")
    public ResponseEntity<InstituicaoDTO> resetSenha(@RequestAttribute("idInstituicao") Long idInstituicao,
                                                     @RequestHeader("token") String token,
                                                     @RequestBody ResetSenhaDTO resetSenhaDTO) {
        return ResponseEntity.ok(instituicaoService.resetSenha(idInstituicao, token, resetSenhaDTO));
    }

    @PostMapping("/reset/token")
    public ResponseEntity<String> resetToken(@RequestAttribute("idInstituicao") Long idInstituicao,
                                                  @RequestParam("tokenPrefix") String tokenPrefix) {
        return ResponseEntity.ok(instituicaoService.resetToken(idInstituicao, tokenPrefix));
    }
}
