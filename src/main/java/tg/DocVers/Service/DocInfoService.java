package tg.DocVers.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.DocVers.DTO.DocInfoDTO;
import tg.DocVers.DTO.FullDocDTO;
import tg.DocVers.Entity.DocInfo;
import tg.DocVers.Entity.Documentacao;
import tg.DocVers.Entity.Instituicao;
import tg.DocVers.Exception.RegistroInexistenteException;
import tg.DocVers.Exception.SolicitacaoNegadaException;
import tg.DocVers.Repository.DocInfoRepository;
import tg.DocVers.Repository.DocumentacaoRepository;
import tg.DocVers.Repository.InstituicaoRepository;
import tg.DocVers.Repository.TagRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocInfoService {
    @Autowired
    private DocInfoRepository docInfoRepository;
    @Autowired
    private DocumentacaoRepository documentacaoRepository;
    @Autowired
    private InstituicaoRepository instituicaoRepository;
    @Autowired
    private TagRepository tagRepository;

    // Obter todos os DocInfos da Instituição
    public List<DocInfoDTO> getAllDocInfos(Long idInstituicao) {
        List<DocInfo> docInfo = docInfoRepository.findAllByIdInstituicao(idInstituicao);
        List<DocInfoDTO> docInfoDTO = new ArrayList<>();

        docInfo.forEach(doc -> {
            List<String> tags = getTagsDoc(doc.getId(), idInstituicao);
            docInfoDTO.add(new DocInfoDTO(doc, tags));
        });

        return docInfoDTO;
    }

    // Obter os dados gerais
    public DocInfoDTO getDocInfo(Long idInstituicao, Long idDoc) {
        Optional<DocInfo> docInfoOptional = docInfoRepository.findById(idDoc);

        if (docInfoOptional.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado nenhuma informação de documentação com o ID informado.");
        DocInfo docInfo = docInfoOptional.get();

        Instituicao instituicaoDoc = instituicaoRepository.findById(docInfo.getIdInstituicao()).orElseThrow(() -> new RegistroInexistenteException("Não foi encontrado nenhuma instituição com o ID informado."));
        Instituicao instituicaoUser = instituicaoRepository.findById(idInstituicao).orElseThrow(() -> new RegistroInexistenteException("Não foi encontrado nenhuma instituição com o ID informado."));


        if (!Objects.equals(docInfo.getIdInstituicao(), idInstituicao) ||
                !Objects.equals(instituicaoDoc.getIdMatriz(), idInstituicao) ||
                !(docInfo.isPublicoInstituicao() && instituicaoDoc.getIdMatriz().equals(instituicaoUser.getIdMatriz()))) throw new SolicitacaoNegadaException("Não é possível obter os dados de documentação de uma instituição diferente da que foi informada.");

        List<String> tags = getTagsDoc(docInfo.getId(), idInstituicao);

        return new DocInfoDTO(docInfo, tags);
    }

    // Obter os dados de uma versão específica
    public FullDocDTO getDocInfoByVersion(Long idInstituicao, Long idDoc, int versao) {
        DocInfoDTO docInfoDTO = getDocInfo(idInstituicao, idDoc);

        Optional<Documentacao> documentacaoOptional = documentacaoRepository.findByIdDocInfoAndVersao(idDoc, versao);
        if (documentacaoOptional.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado nenhuma documentação com o ID informado e versão informada.");
        Documentacao documentacao = documentacaoOptional.get();

        return new FullDocDTO(docInfoDTO, List.of(documentacao));
    }

    // Obter os dados de todas as versões
    public FullDocDTO getAllDocInfo(Long idInstituicao, Long idDoc) {
        DocInfoDTO docInfoDTO = getDocInfo(idInstituicao, idDoc);

        List<Documentacao> documentacoes = documentacaoRepository.findAllByIdDocInfo(idDoc);
        if (documentacoes.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado nenhuma documentação com o ID informado.");

        return new FullDocDTO(docInfoDTO, documentacoes);
    }

    // Obter os dados de todas as versões através do nome


    // Criar centro de informações de documentação
    // Apenas o Polo pode criar uma documentação para eles
    public DocInfoDTO create(String nome, Long idInstituicao) {
        Instituicao instituicao = instituicaoRepository.findById(idInstituicao).orElseThrow(() -> new RegistroInexistenteException("Não foi encontrado nenhuma instituição com o ID informado."));

        DocInfo docInfo = new DocInfo(instituicao.getId(), instituicao.getIdMatriz(), nome, 0, ZonedDateTime.now(), ZonedDateTime.now());
        docInfo = docInfoRepository.save(docInfo);
        List<String> tags = getTagsDoc(docInfo.getId(), idInstituicao);
        return new DocInfoDTO(docInfo, tags);
    }

    // Atualizar nome
    // Apenas o Polo pode atualizar o nome da sua documentação
    public DocInfoDTO updateNome(Long idInstituicao, Long idDoc, String nome) {
        DocInfoDTO docInfoDTO = getDocInfo(idInstituicao, idDoc);
        DocInfo docInfo = new DocInfo(docInfoDTO);

        docInfo.setNome(nome);
        docInfo.setAtualizadoEm(ZonedDateTime.now());

        docInfo = docInfoRepository.save(docInfo);
        List<String> tags = getTagsDoc(docInfo.getId(), idInstituicao);
        return new DocInfoDTO(docInfo, tags);
    }

    // Deletar documentações
    // Apenas o Polo pode deletar a suas documentações
    public void delete(Long idInstituicao, Long idDoc) {
        FullDocDTO fullDocDTO = getAllDocInfo(idInstituicao, idDoc);

        documentacaoRepository.deleteAll(fullDocDTO.documentacoes());
        docInfoRepository.deleteById(fullDocDTO.docInfoDTO().id());
    }

    // Incrementar versão de documentações
    public void incrementDocInfoVersion(Long idDocInfo, Long idInstituicao) {
        DocInfoDTO docInfoDTO = getDocInfo(idInstituicao, idDocInfo);
        DocInfo docInfo = new DocInfo(docInfoDTO);

        docInfo.setVersaoMax(docInfo.getVersaoMax() + 1);
        docInfo.setAtualizadoEm(ZonedDateTime.now());

        docInfoRepository.save(docInfo);
    }

    // Diminui para a versão mais recente
    public void decrementDocInfoVersion(Long idDocInfo, Long idInstituicao) {
        Optional<Documentacao> documentacaoOptional = documentacaoRepository.findLastVersionByIdDocInfoAndIdEmpresa(idDocInfo, idInstituicao);

        if (documentacaoOptional.isEmpty()) throw new RegistroInexistenteException("Documentação mais recente não encontrada");
        Documentacao documentacao = documentacaoOptional.get();

        DocInfoDTO docInfoDTO = getDocInfo(idInstituicao, idDocInfo);
        DocInfo docInfo = new DocInfo(docInfoDTO);

        docInfo.setVersaoMax(documentacao.getVersao());
        docInfo.setAtualizadoEm(ZonedDateTime.now());

        docInfoRepository.save(docInfo);
    }

    private List<String> getTagsDoc(Long idDocInfo, Long idInstituicao) {
        return tagRepository.findTagsByIdDocInfoAndIdInstituicao(idDocInfo, idInstituicao);
    }
}
