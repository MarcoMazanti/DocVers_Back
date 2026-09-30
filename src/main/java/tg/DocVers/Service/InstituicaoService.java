package tg.DocVers.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.DocVers.DTO.InstituicaoDTO;
import tg.DocVers.DTO.LoginDTO;
import tg.DocVers.DTO.ResetSenhaDTO;
import tg.DocVers.Entity.Instituicao;
import tg.DocVers.Exception.DadosInvalidosException;
import tg.DocVers.Exception.RegistroInexistenteException;
import tg.DocVers.Exception.SolicitacaoNegadaException;
import tg.DocVers.Repository.InstituicaoRepository;

import java.util.Optional;

import static tg.DocVers.Security.ManagementHash.encriptarHash;
import static tg.DocVers.Security.ManagementHash.validarHash;
import static tg.DocVers.Security.TokenGenerator.gerarToken;

@Service
public class InstituicaoService {
    @Autowired
    private InstituicaoRepository instituicaoRepository;

    public InstituicaoDTO getPoloInstituicao(Long idAlvo, Long idInstituicao) {
        Optional<Instituicao> instituicaoOpcional = instituicaoRepository.findById(idAlvo);
        if (instituicaoOpcional.isEmpty()) throw new RegistroInexistenteException("Empresa não encontrada.");
        Instituicao instituicao = instituicaoOpcional.get();

        if (!instituicao.getIdMatriz().equals(idInstituicao)) throw new SolicitacaoNegadaException("Apenas a Instituição Matriz pode verificar os dados de seus Polos.");

        return new InstituicaoDTO(instituicaoOpcional.get());
    }

    public InstituicaoDTO login(LoginDTO loginDTO) {
        if (loginDTO == null || !loginDTO.exist()) throw new DadosInvalidosException("Insira cnpj e senha devidamente.");

        Optional<Instituicao> instituicaoOpcional = instituicaoRepository.findByCnpj(loginDTO.cnpj());
        if (instituicaoOpcional.isEmpty()) throw new RegistroInexistenteException("Instituição não encontrada.");

        if (!validarHash(loginDTO.senha(), instituicaoOpcional.get().getSenha())) throw new DadosInvalidosException("Senha incorreta.");

        return new InstituicaoDTO(instituicaoOpcional.get());
    }

    public InstituicaoDTO resetSenha(Long idInstituicao, String token, ResetSenhaDTO resetSenhaDTO) {
        Optional<Instituicao> instituicaoOpcional = instituicaoRepository.findById(idInstituicao);
        if (instituicaoOpcional.isEmpty()) throw new RegistroInexistenteException("Instituição não encontrada.");
        Instituicao instituicao = instituicaoOpcional.get();

        if (!validarHash(token, instituicao.getToken())) throw new DadosInvalidosException("Token inválido.");
        if (instituicao.getCnpj().equals(resetSenhaDTO.cnpj())) throw new DadosInvalidosException("CNPJ inválido.");

        instituicao.setSenha(encriptarHash(resetSenhaDTO.senha()));
        instituicaoRepository.save(instituicao);

        return new InstituicaoDTO(instituicao);
    }

    public String resetToken(Long idInstituicao, String tokenPrefix) {
        if (tokenPrefix == null) throw new DadosInvalidosException("Token prefix não informado.");

        Optional<Instituicao> instituicaoOpcional = instituicaoRepository.findById(idInstituicao);
        if (instituicaoOpcional.isEmpty()) throw new RegistroInexistenteException("Instituição não encontrada.");
        Instituicao instituicao = instituicaoOpcional.get();

        String tokenPuro = gerarToken(tokenPrefix);
        instituicao.setToken(encriptarHash(tokenPuro));
        instituicao.setTokenPrefix(tokenPrefix);
        instituicaoRepository.save(instituicao);

        return instituicao.getToken();
    }
}
