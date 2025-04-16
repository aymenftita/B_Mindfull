package tn.esprit.mindfull.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Service.ICoachingProgramService;
import tn.esprit.mindfull.Service.IProgramContentService;
import tn.esprit.mindfull.entity.ProgramContent;

import java.util.List;

@RestController
@RequestMapping("/program-contents")
public class ProgramContentController {

    @Autowired
    private IProgramContentService iProgramContentService;

    @GetMapping("/programcontents")
    public List<ProgramContent> getAllContent() {

        return iProgramContentService.getAllContent();
    }

    @GetMapping("/{id}")
    public ProgramContent getContentById(@PathVariable Long id) {

        return iProgramContentService.getContentById(id);
    }

    @PostMapping("/programcontent")
    public ProgramContent create(@RequestBody ProgramContent content) {
        return iProgramContentService.saveContent(content);
    }

    // Pour mettre à jour un contenu
    @PutMapping("/programcontent/{contentId}")
    public ProgramContent updateContent(@PathVariable Long contentId, @RequestBody ProgramContent programContent) {
        return iProgramContentService.updateContent(contentId, programContent);
    }
    @DeleteMapping("/programcontent/{id}")
    public void deleteContent(@PathVariable Long id) {

        iProgramContentService.deleteContent(id);
    }
}
