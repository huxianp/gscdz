package com.gscdz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Hu
 * @date 2021-09-20 13:57
 * @description:Infomation of CustomerUser
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserInfoDTO {
    private Long userId;
    private String username;
    private String password;
    private Integer solvedProblemNum;
    private Integer AnswerPros;
    private Integer AnswerCons;
    private HashMap<String,Integer> submitedUnSolvedProblemTypes=new HashMap<>();
    private HashMap<String,Integer> answeredProblemTypes=new HashMap<>();
}
