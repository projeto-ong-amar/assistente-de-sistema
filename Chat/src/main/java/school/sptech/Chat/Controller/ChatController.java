package school.sptech.Chat.Controller;

import org.springframework.web.bind.annotation.*;
import school.sptech.Chat.Service.IaService;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final IaService iaService;

    public ChatController(IaService iaService) {
        this.iaService = iaService;
    }

    @PostMapping
    public String chat(@RequestBody String pergunta) {
        return iaService.responder(pergunta);
    }
}
