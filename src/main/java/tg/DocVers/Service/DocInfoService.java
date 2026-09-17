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
import java.util.Optional;

@Service
public class DocInfoService {
    @Autowired
    private DocInfoRepository docInfoRepository;
    @Autowired
    private DocumentacaoRepository documentacaoRepository;

    // Obter os dados gerais
    public DocInfo getDocInfo(int idEmpresa, int idDoc) {
        Optional<DocInfo> docInfoOptional = docInfoRepository.findById(idDoc);

        if (docInfoOptional.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado nenhuma informação de documentação com o ID informado.");
        DocInfo docInfo = docInfoOptional.get();

        if (docInfo.getIdEmpresa() != idEmpresa) throw new SolicitacaoNegadaException("Não é possível obter os dados de documentação de uma empresa diferente da que foi informada.");

        return docInfo;
    }

    // Obter os dados de uma versão específica
    public FullDocDTO getDocInfoByVersion(int idEmpresa, int idDoc, int versao) {
        DocInfo docInfo =getDocInfo(idEmpresa, idDoc);

        Optional<Documentacao> documentacaoOptional = documentacaoRepository.findByIdDocInfoAndVersao((long) idDoc, versao);
        if (documentacaoOptional.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado nenhuma documentação com o ID informado e versão informada.");
        Documentacao documentacao = documentacaoOptional.get();

        return new FullDocDTO(docInfo, List.of(documentacao));
    }

    // Obter os dados de todas as versões
    public FullDocDTO getAllDocInfo(int idEmpresa, int idDoc) {
        DocInfo docInfo =getDocInfo(idEmpresa, idDoc);

        List<Documentacao> documentacoes = documentacaoRepository.findByIdDocInfo((long) idDoc);
        if (documentacoes.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado nenhuma documentação com o ID informado.");

        return new FullDocDTO(docInfo, documentacoes);
    }

    // Criar centro de informações de documentação
    public DocInfo create(String nome, int idEmpresa) {
        DocInfo docInfo = new DocInfo((long) idEmpresa, nome, 0, ZonedDateTime.now(), ZonedDateTime.now());

        return docInfoRepository.save(docInfo);
    }

    // Atualizar nome
    public DocInfo updateNome(int idEmpresa, int idDoc, String nome) {
        DocInfo docInfo = getDocInfo(idEmpresa, idDoc);
        docInfo.setNome(nome);
        docInfo.setAtualizadoEm(ZonedDateTime.now());

        return docInfoRepository.save(docInfo);
    }

    // Deletar documentações
    public void delete(int idEmpresa, int idDoc) {
        FullDocDTO fullDocDTO = getAllDocInfo(idEmpresa, idDoc);

        documentacaoRepository.deleteAll(fullDocDTO.documentacoes());
        docInfoRepository.deleteById(Integer.parseInt(String.valueOf(fullDocDTO.docInfo().getId())));
    }
}
