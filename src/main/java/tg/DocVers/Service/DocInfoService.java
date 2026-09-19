package tg.DocVers.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.DocVers.DTO.FullDocDTO;
import tg.DocVers.Entity.DocInfo;
import tg.DocVers.Entity.Documentacao;
import tg.DocVers.Exception.RegistroInexistenteException;
import tg.DocVers.Exception.SolicitacaoNegadaException;
import tg.DocVers.Repository.DocInfoRepository;
import tg.DocVers.Repository.DocumentacaoRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DocInfoService {
    @Autowired
    private DocInfoRepository docInfoRepository;
    @Autowired
    private DocumentacaoRepository documentacaoRepository;

    // Obter todos os DocInfos da Empresa

    // Obter os dados gerais
    public DocInfo getDocInfo(Long idEmpresa, Long idDoc) {
        Optional<DocInfo> docInfoOptional = docInfoRepository.findById(idDoc);

        if (docInfoOptional.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado nenhuma informação de documentação com o ID informado.");
        DocInfo docInfo = docInfoOptional.get();

        if (!Objects.equals(docInfo.getIdEmpresa(), idEmpresa)) throw new SolicitacaoNegadaException("Não é possível obter os dados de documentação de uma empresa diferente da que foi informada.");

        return docInfo;
    }

    // Obter os dados de uma versão específica
    public FullDocDTO getDocInfoByVersion(Long idEmpresa, Long idDoc, int versao) {
        DocInfo docInfo =getDocInfo(idEmpresa, idDoc);

        Optional<Documentacao> documentacaoOptional = documentacaoRepository.findByIdDocInfoAndVersao(idDoc, versao);
        if (documentacaoOptional.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado nenhuma documentação com o ID informado e versão informada.");
        Documentacao documentacao = documentacaoOptional.get();

        return new FullDocDTO(docInfo, List.of(documentacao));
    }

    // Obter os dados de todas as versões
    public FullDocDTO getAllDocInfo(Long idEmpresa, Long idDoc) {
        DocInfo docInfo =getDocInfo(idEmpresa, idDoc);

        List<Documentacao> documentacoes = documentacaoRepository.findAllByIdDocInfo(idDoc);
        if (documentacoes.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado nenhuma documentação com o ID informado.");

        return new FullDocDTO(docInfo, documentacoes);
    }

    // Obter os dados de todas as versões através do nome


    // Criar centro de informações de documentação
    public DocInfo create(String nome, Long idEmpresa) {
        DocInfo docInfo = new DocInfo(idEmpresa, nome, 0, ZonedDateTime.now(), ZonedDateTime.now());

        return docInfoRepository.save(docInfo);
    }

    // Atualizar nome
    public DocInfo updateNome(Long idEmpresa, Long idDoc, String nome) {
        DocInfo docInfo = getDocInfo(idEmpresa, idDoc);
        docInfo.setNome(nome);
        docInfo.setAtualizadoEm(ZonedDateTime.now());

        return docInfoRepository.save(docInfo);
    }

    // Deletar documentações
    public void delete(Long idEmpresa, Long idDoc) {
        FullDocDTO fullDocDTO = getAllDocInfo(idEmpresa, idDoc);

        documentacaoRepository.deleteAll(fullDocDTO.documentacoes());
        docInfoRepository.deleteById(fullDocDTO.docInfo().getId());
    }

    // Incrementar versão de documentações
    public void incrementDocInfoVersion(Long idDocInfo, Long idEmpresa) {
        DocInfo docInfo = getDocInfo(idEmpresa, idDocInfo);
        docInfo.setVersaoMax(docInfo.getVersaoMax() + 1);
        docInfo.setAtualizadoEm(ZonedDateTime.now());

        docInfoRepository.save(docInfo);
    }

    // Diminui para a versão mais recente
    public void decrementDocInfoVersion(Long idDocInfo, Long idEmpresa) {
        Optional<Documentacao> documentacaoOptional = documentacaoRepository.findLastVersionByIdDocInfoAndIdEmpresa(idDocInfo, idEmpresa);

        if (documentacaoOptional.isEmpty()) throw new RegistroInexistenteException("Documentação mais recente não encontrada");
        Documentacao documentacao = documentacaoOptional.get();

        DocInfo docInfo = getDocInfo(idEmpresa, idDocInfo);
        docInfo.setVersaoMax(documentacao.getVersao());
        docInfo.setAtualizadoEm(ZonedDateTime.now());

        docInfoRepository.save(docInfo);
    }
}
