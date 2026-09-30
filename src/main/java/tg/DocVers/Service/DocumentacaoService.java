package tg.DocVers.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tg.DocVers.DTO.FullDocDTO;
import tg.DocVers.DTO.NewDocDTO;
import tg.DocVers.Entity.DocInfo;
import tg.DocVers.Entity.Documentacao;
import tg.DocVers.Entity.Instituicao;
import tg.DocVers.Entity.TipoDocumento;
import tg.DocVers.Exception.DadosInvalidosException;
import tg.DocVers.Exception.FileException;
import tg.DocVers.Exception.RegistroInexistenteException;
import tg.DocVers.Exception.SolicitacaoNegadaException;
import tg.DocVers.Repository.DocumentacaoRepository;
import tg.DocVers.Repository.InstituicaoRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class DocumentacaoService {
    @Autowired
    private DocumentacaoRepository documentacaoRepository;
    @Autowired
    private InstituicaoRepository instituicaoRepository;
    @Autowired
    private DocInfoService docInfoService;
    @Autowired
    private DockerService dockerService;

    // Responsável por apontar qual classe está gerando o log
    private static final Logger logger = Logger.getLogger(DocumentacaoService.class.getName());

    // =============================================================================
    //  APENAS ESTÁ A SER DESENVOLVIDO PARA DOCUMENTOS COM TIPAGEM REGISTRO_INTERNO
    // =============================================================================

    // Obter documentação pelo id
    public Documentacao getDocumentacaoById(String id, Long idInstituicao) {
        Optional<Documentacao> documentacaoOptional = documentacaoRepository.findById(UUID.fromString(id));

        if (documentacaoOptional.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado um documento com o id " + id);
        Documentacao documentacao = documentacaoOptional.get();

        // Valida se a informação da documentação pertence a respectiva empresa informada
        docInfoService.getDocInfo(documentacao.getIdDocInfo(), idInstituicao);

        return documentacao;
    }

    // obter todas as documentações por idDocInfo
    public List<Documentacao> getDocumentacoesByIdDocInfo(Long idDocInfo, Long idInstituicao) {
        // Valida se a informação da documentação pertence a respectiva empresa informada
        docInfoService.getDocInfo(idDocInfo, idInstituicao);

        List<Documentacao> documentacaoList = documentacaoRepository.findAllByIdDocInfo(idDocInfo);

        if (documentacaoList.isEmpty()) throw new RegistroInexistenteException("Não foi encontrado nenhuma documentação com o ID informado.");

        return documentacaoList;
    }

    public File getFileFromDocker(String nomeArquivo, Long idInstituicao) {
        if (nomeArquivo.contains(idInstituicao.toString()))
            return dockerService.getFileFromDocker(nomeArquivo);
        else
            return null;
    }

    // criar nova documentação do zero, com o DocInfo
    public FullDocDTO createFullDocumentacao(String nomeDocumento, NewDocDTO newDocDTO, Long idInstituicao, MultipartFile arquivo) {
        DocInfo docInfo = docInfoService.create(nomeDocumento, idInstituicao);

        Documentacao documentacao = documentacaoRepository.save(
                new Documentacao(
                        newDocDTO.idDocInfo(),
                        newDocDTO.tipo(),
                        newDocDTO.texto()));

        documentacao.setNomeArquivo(setNomeArquivo(documentacao, idInstituicao));

        // Verificar se deve mandar o arquivo para o DOCKER
        if (!newDocDTO.tipo().equals(TipoDocumento.REGISTRO_INTERNO)) {
            try {
                // Tratativa do arquivo para salvar
                File arquivoDocker = new File(documentacao.getNomeArquivo());
                arquivo.transferTo(arquivoDocker);

                // Envio do arquivoDocker ao docker
                dockerService.sendFileToDocker(arquivoDocker);
            } catch (IOException e) {
                throw new FileException("Erro ao tratar o arquivo para o Docker");
            }
        }

        return new FullDocDTO(docInfo, List.of(documentacao));
    }

    // criar nova documentação
    public Documentacao createDocumentacao(NewDocDTO newDocDTO, Long idInstituicao, MultipartFile arquivo) {
        // Valida se a informação da documentação pertence a respectiva empresa informada
        docInfoService.getDocInfo(newDocDTO.idDocInfo(), idInstituicao);

        // Valida se a extensão informada bate com a do arquivo
        String nomeOriginal = arquivo.getOriginalFilename();
        String extensao = nomeOriginal != null ? nomeOriginal.substring(nomeOriginal.lastIndexOf(".")) : ""; // previne a pessoa inserir nome_arquivo.pdf.exe

        if (!extensao.equals("." + newDocDTO.tipo().toString().toLowerCase())) throw new SolicitacaoNegadaException("Extensão do arquivo não corresponde ao tipo informado.");

        // Deve adicionar mais 1 na versão do DocInfo
        docInfoService.incrementDocInfoVersion(newDocDTO.idDocInfo(), idInstituicao);

        Documentacao documentacao = new Documentacao(
                newDocDTO.idDocInfo(),
                newDocDTO.tipo(),
                newDocDTO.texto());

        documentacao.setNomeArquivo(setNomeArquivo(documentacao, idInstituicao));

        // Verificar se deve mandar o arquivo para o DOCKER
        if (!newDocDTO.tipo().equals(TipoDocumento.REGISTRO_INTERNO)) {
            try {
                // Tratativa do arquivo para salvar
                File arquivoDocker = new File(documentacao.getNomeArquivo());
                arquivo.transferTo(arquivoDocker);

                // Envio do arquivoDocker ao docker
                dockerService.sendFileToDocker(arquivoDocker);
            } catch (IOException e) {
                throw new FileException("Erro ao tratar o arquivo para o Docker");
            }
        }

        return documentacaoRepository.save(documentacao);
    }

    // deletar documentações do mesmo DocInfo, não todos
    public void deleteListOfDocumentacao(List<UUID> listId, Long idDocInfo, Long idInstituicao) {
        List<Documentacao> documentacaoList = documentacaoRepository.findAllByIdDocInfoAndIdEmpresaAndId(listId, idDocInfo, idInstituicao);

        documentacaoList.forEach(documentacao -> {
            if (!documentacao.getTipo().equals(TipoDocumento.REGISTRO_INTERNO)) {
                dockerService.deleteFileFromDocker(documentacao.getNomeArquivo());
            }
        });

        documentacaoRepository.deleteAll(documentacaoList);

        // Confirmo se passou algum ID para passar em LOG
        documentacaoList.forEach(documentacao -> {
            if (listId.contains(documentacao.getId())) listId.remove(documentacao.getId());
        });

        if (!listId.isEmpty()) logger.warning("Não foram deletados os seguintes IDs: " + listId);

        // Deve Ajustar a versão máxima do DocInfo
        docInfoService.decrementDocInfoVersion(idDocInfo, idInstituicao);
    }

    private String setNomeArquivo(Documentacao documentacao, Long idInstituicao) {
        if (documentacao.getTipo() != TipoDocumento.REGISTRO_INTERNO) {
            DocInfo docInfo = docInfoService.getDocInfo(documentacao.getIdDocInfo(), idInstituicao);
            Instituicao instituicao = instituicaoRepository.findById(idInstituicao).orElseThrow(() -> new RegistroInexistenteException("Não foi encontrado nenhuma instituição com o ID informado."));

            return instituicao.getIdMatriz() + "/" +docInfo.getIdInstituicao().toString() + "/" + documentacao.getIdDocInfo().toString() + "/" + documentacao.getId() + "." + documentacao.getTipo().toString().toLowerCase();
        } else {
            return null;
        }
    }
}
