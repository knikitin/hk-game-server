package com.akvelon.hk.gameserver.rest;

import com.akvelon.hk.gameserver.dto.CommandsInDto;
import com.akvelon.hk.gameserver.dto.CommandsOutDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@RestController
public class CommandsController {
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public final ApplicationEventPublisher eventPublisher;

    public CommandsController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PutMapping("/commands")
    public CommandsOutDto simpleInputCommands(@RequestBody CommandsInDto commandsInDto){
        log.debug("Putted command: "+ commandsInDto.toString());
        CommandsOutDto cmd = new CommandsOutDto(commandsInDto.getId(), commandsInDto.getCmd());
        this.eventPublisher.publishEvent(cmd);
        return cmd;
    }

    @GetMapping("/simple-commands")
    public CommandsOutDto simpleOutputCommands(){
        return new CommandsOutDto("first", "left");
    }

    @GetMapping("/commands")
    public SseEmitter handle() {

        SseEmitter emitter = new SseEmitter(60_000L);
        this.emitters.add(emitter);

        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));

        return emitter;
    }

    @EventListener
    public void onAddCommand(CommandsOutDto commandsOutDto) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        this.emitters.forEach(emitter -> {
            try {
                emitter.send(commandsOutDto);
            }
            catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });

        this.emitters.remove(deadEmitters);
    }
}
