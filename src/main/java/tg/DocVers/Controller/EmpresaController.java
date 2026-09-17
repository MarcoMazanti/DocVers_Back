package tg.DocVers.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tg.DocVers.DTO.EmpresaDTO;
import tg.DocVers.DTO.LoginDTO;
import tg.DocVers.DTO.ResetSenhaDTO;
import tg.DocVers.Service.EmpresaService;

@RestController
@RequestMapping("/api/empresa")
public class EmpresaController {
    @Autowired
    private EmpresaService empresaService;

    @PostMapping("/login")
    public ResponseEntity<EmpresaDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(empresaService.login(loginDTO));
    }

    @PostMapping("/reset/senha")
    public ResponseEntity<EmpresaDTO> resetSenha(@RequestAttribute("id") int id,
                                                 @RequestHeader("token") String token,
                                                 @RequestBody ResetSenhaDTO resetSenhaDTO) {
        return ResponseEntity.ok(empresaService.resetSenha(id, token, resetSenhaDTO));
    }

    @PostMapping("/reset/token")
    public ResponseEntity<String> resetToken(@RequestAttribute("id") int id,
                                                  @RequestParam("tokenPrefix") String tokenPrefix) {
        return ResponseEntity.ok(empresaService.resetToken(id, tokenPrefix));
    }
}
