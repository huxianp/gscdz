package com.gscdz.dto;

import com.gscdz.model.Answer;
import com.gscdz.model.CustomUser;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author Hu
 * @date 2021-09-21 21:31
 * @description:
 */
@Data
@Builder
public class SolProblemDTO {
    private Long solvedProblemId;
    private String subject;
    private String solvedTimesAndVisitTimes;
    private String knowledgePoints;
    private String analysis;
    private List<Answer> answers;
    private Set<CustomUser> customUserSet;
}
