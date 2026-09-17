package tg.DocVers.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.DocVers.DTO.EmpresaDTO;
import tg.DocVers.DTO.LoginDTO;
import tg.DocVers.Entity.Empresa;
import tg.DocVers.Exception.DadosInvalidosException;
import tg.DocVers.Exception.RegistroInexistenteException;
import tg.DocVers.Repository.EmpresaRepository;
import tg.DocVers.Security.ManagementHash;

import java.util.Optional;

import static tg.DocVers.Security.ManagementHash.encriptarHash;
import static tg.DocVers.Security.TokenGenerator.gerarToken;

@Service
public class EmpresaService {
    @Autowired
    private EmpresaRepository empresaRepository;

    public EmpresaDTO login(LoginDTO loginDTO) {
        if (loginDTO == null || !loginDTO.exist()) throw new DadosInvalidosException("Insira cnpj e senha devidamente.");

        Optional<Empresa> empresaOptional = empresaRepository.findByCnpj(loginDTO.cnpj());
        if (empresaOptional.isEmpty()) throw new RegistroInexistenteException("Empresa não encontrada.");

        if (!ManagementHash.validarHash(loginDTO.senha(), empresaOptional.get().getSenha())) throw new DadosInvalidosException("Senha incorreta.");

        return new EmpresaDTO(empresaOptional.get());
    }

    public String resetToken(int id, String tokenPrefix) {
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
