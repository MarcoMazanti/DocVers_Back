package tg.DocVers.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.DocVers.DTO.FullDocDTO;
import tg.DocVers.Entity.DocInfo;
import tg.DocVers.Entity.Documentacao;
import tg.DocVers.Entity.Instituicao;
import tg.DocVers.Exception.RegistroInexistenteException;
import tg.DocVers.Exception.SolicitacaoNegadaException;
import tg.DocVers.Repository.DocInfoRepository;
import tg.DocVers.Repository.DocumentacaoRepository;
import tg.DocVers.Repository.InstituicaoRepository;

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
    @Autowired
    private InstituicaoRepository instituicaoRepository;

    // Obter todos os DocInfos da Instituição

    // Obter os dados gerais
    public DocInfo getDocInfo(Long idInstituicao, Long idDoc) {
        Optional<DocInfo> docInfoOptional = docInfoRepository.findById(idDoc);

        if (docInfoOptional.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado nenhuma informação de documentação com o ID informado.");
        DocInfo docInfo = docInfoOptional.get();

        Instituicao instituicaoDoc = instituicaoRepository.findById(docInfo.getIdInstituicao()).orElseThrow(() -> new RegistroInexistenteException("Não foi encontrado nenhuma instituição com o ID informado."));

        if (!Objects.equals(docInfo.getIdInstituicao(), idInstituicao) || !Objects.equals(instituicaoDoc.getIdMatriz(), idInstituicao)) throw new SolicitacaoNegadaException("Não é possível obter os dados de documentação de uma instituição diferente da que foi informada.");

        return docInfo;
    }

    // Obter os dados de uma versão específica
    public FullDocDTO getDocInfoByVersion(Long idInstituicao, Long idDoc, int versao) {
        DocInfo docInfo = getDocInfo(idInstituicao, idDoc);

        Optional<Documentacao> documentacaoOptional = documentacaoRepository.findByIdDocInfoAndVersao(idDoc, versao);
        if (documentacaoOptional.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado nenhuma documentação com o ID informado e versão informada.");
        Documentacao documentacao = documentacaoOptional.get();

        return new FullDocDTO(docInfo, List.of(documentacao));
    }

    // Obter os dados de todas as versões
    public FullDocDTO getAllDocInfo(Long idInstituicao, Long idDoc) {
        DocInfo docInfo = getDocInfo(idInstituicao, idDoc);

        List<Documentacao> documentacoes = documentacaoRepository.findAllByIdDocInfo(idDoc);
        if (documentacoes.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado nenhuma documentação com o ID informado.");

        return new FullDocDTO(docInfo, documentacoes);
    }

    // Obter os dados de todas as versões através do nome


    // Criar centro de informações de documentação
    // Apenas o Polo pode criar uma documentação para eles
    public DocInfo create(String nome, Long idInstituicao) {
        Instituicao instituicao = instituicaoRepository.findById(idInstituicao).orElseThrow(() -> new RegistroInexistenteException("Não foi encontrado nenhuma instituição com o ID informado."));

        DocInfo docInfo = new DocInfo(instituicao.getId(), instituicao.getIdMatriz(), nome, 0, ZonedDateTime.now(), ZonedDateTime.now());

        return docInfoRepository.save(docInfo);
    }

    // Atualizar nome
    // Apenas o Polo pode atualizar o nome da sua documentação
    public DocInfo updateNome(Long idInstituicao, Long idDoc, String nome) {
        DocInfo docInfo = getDocInfo(idInstituicao, idDoc);
        docInfo.setNome(nome);
        docInfo.setAtualizadoEm(ZonedDateTime.now());

        return docInfoRepository.save(docInfo);
    }

    // Deletar documentações
    // Apenas o Polo pode deletar a suas documentações
    public void delete(Long idInstituicao, Long idDoc) {
        FullDocDTO fullDocDTO = getAllDocInfo(idInstituicao, idDoc);

        documentacaoRepository.deleteAll(fullDocDTO.documentacoes());
        docInfoRepository.deleteById(fullDocDTO.docInfo().getId());
    }

    // Incrementar versão de documentações
    public void incrementDocInfoVersion(Long idDocInfo, Long idInstituicao) {
        DocInfo docInfo = getDocInfo(idInstituicao, idDocInfo);
        docInfo.setVersaoMax(docInfo.getVersaoMax() + 1);
        docInfo.setAtualizadoEm(ZonedDateTime.now());

        docInfoRepository.save(docInfo);
    }

    // Diminui para a versão mais recente
    public void decrementDocInfoVersion(Long idDocInfo, Long idInstituicao) {
        Optional<Documentacao> documentacaoOptional = documentacaoRepository.findLastVersionByIdDocInfoAndIdEmpresa(idDocInfo, idInstituicao);

        if (documentacaoOptional.isEmpty()) throw new RegistroInexistenteException("Documentação mais recente não encontrada");
        Documentacao documentacao = documentacaoOptional.get();

        DocInfo docInfo = getDocInfo(idInstituicao, idDocInfo);
        docInfo.setVersaoMax(documentacao.getVersao());
        docInfo.setAtualizadoEm(ZonedDateTime.now());

        docInfoRepository.save(docInfo);
    }
}
