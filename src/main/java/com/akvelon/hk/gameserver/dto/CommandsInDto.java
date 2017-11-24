package com.akvelon.hk.gameserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandsInDto {
    private String id;
    private String pwd;
    private String cmd;
}
