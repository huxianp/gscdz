package com.gscdz.dto;

import com.gscdz.model.Answer;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * @author Hu
 * @date 2021-09-22 17:47
 * @description:
 */
@Data
@Builder
public class UnSolvedProblemDTO {
    private Long problemId;
    private String subject;
    private String analysis;
    private String createTime;
    private String firstSolvedTime;
    private String answerNumAndVisitNum;
    private String relevantKps;
    private List<Answer> answerList;

    //每个知识点有多少个对应的题目
    private HashMap<String,Integer> kpStatistic=new HashMap<>();
    //每天回答量的统计
    private HashMap<String,Integer> answerStatistic=new HashMap<>();
    //每天提出的问题量统计
    private HashMap<String,Integer> timeStatic=new HashMap<>();
}
