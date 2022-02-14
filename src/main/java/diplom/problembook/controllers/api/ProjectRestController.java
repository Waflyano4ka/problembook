package diplom.problembook.controllers.api;

import diplom.problembook.models.Project;
import diplom.problembook.models.User;
import diplom.problembook.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/projects")
public class ProjectRestController {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectRestController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public Iterable<Project> get(){
        return projectRepository.findAll();
    }

    @PostMapping
    public Project create(@RequestBody Project project, @AuthenticationPrincipal User user) {
        project.setUser(user);
        return projectRepository.save(project);
    }
}
