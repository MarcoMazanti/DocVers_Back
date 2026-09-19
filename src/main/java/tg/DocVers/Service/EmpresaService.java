package tg.DocVers.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.DocVers.DTO.EmpresaDTO;
import tg.DocVers.DTO.LoginDTO;
import tg.DocVers.DTO.ResetSenhaDTO;
import tg.DocVers.Entity.Empresa;
import tg.DocVers.Exception.DadosInvalidosException;
import tg.DocVers.Exception.RegistroInexistenteException;
import tg.DocVers.Repository.EmpresaRepository;

import java.util.Optional;

import static tg.DocVers.Security.ManagementHash.encriptarHash;
import static tg.DocVers.Security.ManagementHash.validarHash;
import static tg.DocVers.Security.TokenGenerator.gerarToken;

@Service
public class EmpresaService {
    @Autowired
    private EmpresaRepository empresaRepository;

    public EmpresaDTO login(LoginDTO loginDTO) {
        if (loginDTO == null || !loginDTO.exist()) throw new DadosInvalidosException("Insira cnpj e senha devidamente.");

        Optional<Empresa> empresaOptional = empresaRepository.findByCnpj(loginDTO.cnpj());
        if (empresaOptional.isEmpty()) throw new RegistroInexistenteException("Empresa não encontrada.");

        if (!validarHash(loginDTO.senha(), empresaOptional.get().getSenha())) throw new DadosInvalidosException("Senha incorreta.");

        return new EmpresaDTO(empresaOptional.get());
    }

    public EmpresaDTO resetSenha(Long id, String token, ResetSenhaDTO resetSenhaDTO) {
        Optional<Empresa> empresaOptional = empresaRepository.findById(id);
        if (empresaOptional.isEmpty()) throw new RegistroInexistenteException("Empresa não encontrada.");
        Empresa empresa = empresaOptional.get();

        if (!validarHash(token, empresa.getToken())) throw new DadosInvalidosException("Token inválido.");
        if (empresa.getCnpj().equals(resetSenhaDTO.cnpj())) throw new DadosInvalidosException("CNPJ inválido.");

        empresa.setSenha(encriptarHash(resetSenhaDTO.senha()));
        empresaRepository.save(empresa);

        return new EmpresaDTO(empresa);
    }

    public String resetToken(Long id, String tokenPrefix) {
        if (tokenPrefix == null) throw new DadosInvalidosException("Token prefix não informado.");

        Optional<Empresa> empresaOptional = empresaRepository.findById(id);
        if (empresaOptional.isEmpty()) throw new RegistroInexistenteException("Empresa não encontrada.");
        Empresa empresa = empresaOptional.get();

        String tokenPuro = gerarToken(tokenPrefix);
        empresa.setToken(encriptarHash(tokenPuro));
        empresa.setTokenPrefix(tokenPrefix);
        empresaRepository.save(empresa);

        return empresa.getToken();
    }
}
