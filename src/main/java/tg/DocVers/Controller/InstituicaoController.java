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

    @PostMapping("/login")
    public ResponseEntity<InstituicaoDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(instituicaoService.login(loginDTO));
    }

    @PostMapping("/reset/senha")
    public ResponseEntity<InstituicaoDTO> resetSenha(@RequestAttribute("id") Long id,
                                                     @RequestHeader("token") String token,
                                                     @RequestBody ResetSenhaDTO resetSenhaDTO) {
        return ResponseEntity.ok(instituicaoService.resetSenha(id, token, resetSenhaDTO));
    }

    @PostMapping("/reset/token")
    public ResponseEntity<String> resetToken(@RequestAttribute("id") Long id,
                                                  @RequestParam("tokenPrefix") String tokenPrefix) {
        return ResponseEntity.ok(instituicaoService.resetToken(id, tokenPrefix));
    }
}
