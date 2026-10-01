package tg.DocVers.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.DocVers.DTO.DocInfoDTO;
import tg.DocVers.Entity.DocInfo;
import tg.DocVers.Entity.Instituicao;
import tg.DocVers.Entity.Tags;
import tg.DocVers.Exception.RegistroInexistenteException;
import tg.DocVers.Exception.SolicitacaoNegadaException;
import tg.DocVers.Repository.InstituicaoRepository;
import tg.DocVers.Repository.TagRepository;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private InstituicaoRepository instituicaoRepository;
    @Autowired
    private DocInfoService docInfoService;

    public Tags getById(int id, Long idInstituicao) {
        List<Tags> listTags = getAllByInstituicao(idInstituicao);

        return listTags.stream().filter(t -> t.getId() == id).findFirst().orElseThrow(() -> new RegistroInexistenteException("Não foi encontrado a Tag com este ID."));
    }

    public List<Tags> getAllByInstituicao(Long idInstituicao) {
        return tagRepository.findAllByIdInstituicao(idInstituicao);
    }

    public Tags create(Tags tag, Long idInstituicao) {
        tag.setIdInstituicao(idInstituicao);
        Instituicao instituicao = instituicaoRepository.findById(tag.getIdInstituicao()).orElseThrow(() -> new RegistroInexistenteException("Não foi encontrado a Instituição com este ID."));
        if (!instituicao.getIdMatriz().equals(instituicao.getId())) throw new SolicitacaoNegadaException("A Instituição não é uma matriz.");

        return tagRepository.save(tag);
    }

    public Tags update(String tag, int id, Long idInstituicao) {
        Tags tagExistente = validarEhObterTag(id, idInstituicao);

        tagExistente.setTag(tag);
        return tagRepository.save(tagExistente);
    }

    public void delete(int id, Long idInstituicao) {
        Tags tag = validarEhObterTag(id, idInstituicao);

        tagRepository.delete(tag);
    }

    private Tags validarEhObterTag(int id, Long idInstituicao) {
        Instituicao instituicao = instituicaoRepository.findById(idInstituicao).orElseThrow(() -> new RegistroInexistenteException("Não foi encontrado a Instituição com este ID."));
        if (!instituicao.getIdMatriz().equals(instituicao.getId())) throw new SolicitacaoNegadaException("A Instituição não é uma matriz.");

        Tags tag = tagRepository.findById(id).orElseThrow(() -> new RegistroInexistenteException("Não foi encontrado a Tag com este ID."));
        if (!tag.getIdInstituicao().equals(instituicao.getIdMatriz())) throw new SolicitacaoNegadaException("A Tag não pertence a esta Instituição.");

        return tag;
    }

    public List<String> getTagsOfDocInfo(Long idDocInfo, Long idInstituicao) {
        Instituicao instituicao = instituicaoRepository.findById(idInstituicao).orElseThrow(() -> new RegistroInexistenteException("Não foi encontrado a Instituição com este ID."));

        DocInfoDTO docInfoDTO = docInfoService.getDocInfo(instituicao.getIdMatriz(), idDocInfo);
        DocInfo docInfo = new DocInfo(docInfoDTO);

        Instituicao instituicaoDoc = instituicaoRepository.findById(docInfo.getIdInstituicao()).orElseThrow(() -> new RegistroInexistenteException("Não foi encontrado a Instituição do DocInfo com este ID."));

        if (!instituicaoDoc.getIdMatriz().equals(instituicao.getIdMatriz()) ||
                !instituicaoDoc.getIdMatriz().equals(docInfo.getIdInstituicao()) ||
                !instituicaoDoc.getId().equals(instituicao.getId()) ||
                !instituicaoDoc.getId().equals(instituicao.getIdMatriz())) throw new SolicitacaoNegadaException("O DocInfo não pertence a esta Instituição.");

        return tagRepository.findTagsByIdDocInfoAndIdInstituicao(idDocInfo, idInstituicao);
    }
}
