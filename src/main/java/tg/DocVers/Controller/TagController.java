package tg.DocVers.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tg.DocVers.Entity.Tags;
import tg.DocVers.Service.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/{id}")
    public Tags getTagById(@PathVariable int id, @RequestAttribute("idInstituicao") Long idInstituicao) {
        return tagService.getById(id, idInstituicao);
    }

    @GetMapping()
    public List<Tags> getAllTags(@RequestAttribute("idInstituicao") Long idInstituicao) {
        return tagService.getAllByInstituicao(idInstituicao);
    }

    @PostMapping()
    public Tags createTag(@RequestBody Tags tag, @RequestAttribute("idInstituicao") Long idInstituicao) {
        return tagService.create(tag, idInstituicao);
    }

    @PutMapping("/{id}/{tag}")
    public Tags updateTag(@PathVariable("id") int id, @PathVariable("tag") String tag, @RequestAttribute("idInstituicao") Long idInstituicao) {
        return tagService.update(tag, id, idInstituicao);
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable("id") int id, @RequestAttribute("idInstituicao") Long idInstituicao) {
        tagService.delete(id, idInstituicao);
    }
}
