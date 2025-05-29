package com.subh.recorder.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class profileResponseDto {
    private Long userId;
    private String username;
    private String email;

}
