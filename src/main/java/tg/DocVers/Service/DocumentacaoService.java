package tg.DocVers.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.DocVers.DTO.FullDocDTO;
import tg.DocVers.DTO.NewDocDTO;
import tg.DocVers.Entity.DocInfo;
import tg.DocVers.Entity.Documentacao;
import tg.DocVers.Exception.RegistroInexistenteException;
import tg.DocVers.Repository.DocumentacaoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class DocumentacaoService {
    @Autowired
    private DocumentacaoRepository documentacaoRepository;
    @Autowired
    private DocInfoService docInfoService;

    // Responsável por apontar qual classe está gerando o log
    private static final Logger logger = Logger.getLogger(DocumentacaoService.class.getName());

    // =================================================================
    //  APENAS ESTÁ A SER DESENVOLVIDO PARA DOCUMENTOS COM TIPAGEM TXT
    // =================================================================

    // Obter documentação pelo id
    public Documentacao getDocumentacaoById(String id, Long idEmpresa) {
        Optional<Documentacao> documentacaoOptional = documentacaoRepository.findById(UUID.fromString(id));

        if (documentacaoOptional.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado um documento com o id " + id);
        Documentacao documentacao = documentacaoOptional.get();

        // Valida se a informação da documentação pertence a respectiva empresa informada
        docInfoService.getDocInfo(documentacao.getIdDocInfo(), idEmpresa);

        return documentacao;
    }

    // obter todas as documentações por idDocInfo
    public List<Documentacao> getDocumentacoesByIdDocInfo(Long idDocInfo, Long idEmpresa) {
        // Valida se a informação da documentação pertence a respectiva empresa informada
        docInfoService.getDocInfo(idDocInfo, idEmpresa);

        List<Documentacao> documentacaoList = documentacaoRepository.findAllByIdDocInfo(idDocInfo);

        if (documentacaoList.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado nenhuma documentação com o ID informado.");

        return documentacaoList;
    }

    // criar nova documentação do zero, com o DocInfo
    public FullDocDTO createFullDocumentacao(String nomeDocumento, NewDocDTO newDocDTO, Long idEmpresa) {
        DocInfo docInfo = docInfoService.create(nomeDocumento, idEmpresa);

        Documentacao documentacao = documentacaoRepository.save(
                new Documentacao(
                        newDocDTO.idDocInfo(),
                        newDocDTO.nomeArquivo(),
                        newDocDTO.tipo(),
                        newDocDTO.texto()));

        return new FullDocDTO(docInfo, List.of(documentacao));
    }

    // criar nova documentação
    public Documentacao createDocumentacao(NewDocDTO newDocDTO, Long idEmpresa) {
        // Valida se a informação da documentação pertence a respectiva empresa informada
        docInfoService.getDocInfo(newDocDTO.idDocInfo(), idEmpresa);

        // Deve adicionar mais 1 na versão do DocInfo
        docInfoService.incrementDocInfoVersion(newDocDTO.idDocInfo(), idEmpresa);

        return documentacaoRepository.save(
                new Documentacao(
                        newDocDTO.idDocInfo(),
                        newDocDTO.nomeArquivo(),
                        newDocDTO.tipo(),
                        newDocDTO.texto()));
    }

    // deletar documentações do mesmo DocInfo, não todos
    public void deleteListOfDocumentacao(List<UUID> listId, Long idDocInfo, Long idEmpresa) {
        List<Documentacao> documentacaoList = documentacaoRepository.findAllByIdDocInfoAndIdEmpresaAndId(listId, idDocInfo, idEmpresa);

        documentacaoRepository.deleteAll(documentacaoList);

        // Confirmo se passou algum ID para passar em LOG
        documentacaoList.forEach(documentacao -> {
            if (listId.contains(documentacao.getId())) listId.remove(documentacao.getId());
        });

        if (!listId.isEmpty()) logger.warning("Não foram deletados os seguintes IDs: " + listId);

        // Deve Ajustar a versão máxima do DocInfo
        docInfoService.decrementDocInfoVersion(idDocInfo, idEmpresa);
    }
}
