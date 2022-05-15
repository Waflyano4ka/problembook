package diplom.problembook.controllers.api;

import diplom.problembook.models.*;
import diplom.problembook.repositories.*;
import diplom.problembook.storage.StorageService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/task")
public class TaskRestController {
    private final ProjectUserRepository projectUserRepository;
    private final TaskRepository taskRepository;
    private final TaskUserRepository taskUserRepository;

    @Autowired
    public TaskRestController(
            ProjectUserRepository projectUserRepository,
            TaskRepository taskRepository,
            TaskUserRepository taskUserRepository) {
        this.projectUserRepository = projectUserRepository;
        this.taskRepository = taskRepository;
        this.taskUserRepository = taskUserRepository;
    }


    @GetMapping("/{id}")
    public ResponseEntity getTask(@PathVariable(value = "id") Task task,
                                   @AuthenticationPrincipal User user) {
        if (task.getArchive()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Задачи не существует");
        }
        List<ProjectUser> members = projectUserRepository.findByProject(task.getProject()).stream().filter(m -> m.getUser().getId().equals(user.getId())).toList();
        if (members.size() > 0){
            ProjectUser mem = members.get(0);
            HashMap<Object, Object> content = new HashMap<>();
            if (mem.getRole().getName().equals("READER")) {
                List<TaskUser> taskUsers = taskUserRepository.findByTask(task);
                for (TaskUser tu : taskUsers) {
                    if (tu.getReader().getUser().getId().equals(user.getId())){

                        content.put("task", task);
                        content.put("role", mem.getRole());
                        content.put("taskUsers", tu);

                        return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(content);
                    }
                }
            }
            else {
                content.put("task", task);
                content.put("role", mem.getRole());
                content.put("taskUsers", taskUserRepository.findByTask(task));

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(content);
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("У вас нет доступа к этой задаче");
    }

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping ("/{id}/complete")
    public ResponseEntity completeTask(@PathVariable(value = "id") TaskUser taskUser,
                                       @RequestParam("file") MultipartFile file,
                                       @AuthenticationPrincipal User user
    ) {
        if (taskUser.getReader().getUser().getId().equals(user.getId())){
            if (!file.isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                try {
                    String uuidFile = UUID.randomUUID().toString();
                    String resultFilename = uuidFile + '.' + file.getOriginalFilename();

                    file.transferTo(new File(uploadPath + '/' + resultFilename));

                    taskUser.setEnableFile(true);
                    taskUser.setFilename(resultFilename);
                    taskUser.setDatetime(LocalDateTime.now());
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
            else {
                taskUser.setEnableFile(false);
                taskUser.setDatetime(LocalDateTime.now());
            }
            taskUser.setComplete(true);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(taskUserRepository.save(taskUser));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("У вас нет доступа к этой задаче");
    }

    @GetMapping ("/{id}/complete")
    public ResponseEntity completeTask(@PathVariable(value = "id") TaskUser taskUser,
                                       @AuthenticationPrincipal User user
    ) {
        if (taskUser.getReader().getUser().getId().equals(user.getId())){
            taskUser.setEnableFile(false);
            if (taskUser.getComplete())
                taskUser.setComplete(false);
            else {
                taskUser.setDatetime(LocalDateTime.now());
                taskUser.setComplete(true);
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(taskUserRepository.save(taskUser));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("У вас нет доступа к этой задаче");
    }

    @GetMapping ("/file/{filename:.+}")
    public void downloadFile(@PathVariable String filename,
                             HttpServletResponse response
    ) {
        Path file = Paths.get(uploadPath, filename);
        if (Files.exists(file)){
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            response.setContentType("application/octet-stream");

            try {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException e) {
                throw new RuntimeException("IOError writing file to output stream");
            }
        }
    }

    /**
     * Удаление задачи
     *
     * @param task задача
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity Задача, которую удалили
     */
    @GetMapping("/{id}/delete")
    public ResponseEntity deleteTask(@PathVariable(value = "id") Task task,
                                  @AuthenticationPrincipal User user) {
        if (!task.getProject().getActive()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Проект находится в архиве");
        }
        List<ProjectUser> members = projectUserRepository.findByProject(task.getProject()).stream().filter(m -> m.getUser().getId().equals(user.getId())).toList();
        if (members.size() > 0){
            ProjectUser mem = members.get(0);
            if (!mem.getRole().getName().equals("READER")) {
                task.setArchive(true);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(taskRepository.save(task));
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("У вас нет доступа к этой задаче");
    }

    /**
     * Изменение задачи
     *
     * @param task Задача
     * @param request Данные для изменения задачи
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity Измененная задача
     */
    @PutMapping("/{id}/edit")
    public ResponseEntity editTask(@PathVariable(value = "id") Task task,
                                   @RequestBody String request,
                                   @AuthenticationPrincipal User user) {
        JSONObject data = new JSONObject(request);
        if (!task.getProject().getActive()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Проект находится в архиве");
        }
        List<ProjectUser> members = projectUserRepository.findByProject(task.getProject()).stream().filter(m -> m.getUser().getId().equals(user.getId())).toList();
        if (members.size() > 0){
            ProjectUser mem = members.get(0);
            if (!mem.getRole().getName().equals("READER")) {

                task.setName(data.getString("name"));
                task.setDescription(data.getString("description"));
                task.setEditDatetime(LocalDateTime.now());

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(taskRepository.save(task));
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("У вас нет доступа к этой задаче");
    }
}
