package com.akvelon.hk.gameserver.rest;

import com.akvelon.hk.gameserver.dto.CommandsInDto;
import com.akvelon.hk.gameserver.dto.CommandsOutDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CommandsController {

    @PutMapping("/commands")
    public CommandsOutDto simpleInputCommands(@RequestBody CommandsInDto commandsInDto){
        log.debug("Putted command: "+ commandsInDto.toString());
        return new CommandsOutDto(commandsInDto.getId(), commandsInDto.getCmd());
    }

    @GetMapping("/simple-commands")
    public CommandsOutDto simpleOutputCommands(){
        return new CommandsOutDto("first", "left");
    }

}
