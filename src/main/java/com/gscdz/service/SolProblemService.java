package com.gscdz.service;

import com.gscdz.dto.SolProblemDTO;
import com.gscdz.dto.SolvedProblemExcel;
import com.gscdz.model.Answer;
import com.gscdz.model.KnowledgePoint;
import com.gscdz.model.SolvedProblem;
import com.gscdz.model.Tips;
import com.gscdz.repository.KnowledgePointRepository;
import com.gscdz.repository.SolvedProblemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Hu
 * @date 2021-09-21 21:40
 * @description:
 */
@Service
@Slf4j
public class SolProblemService {
    @Autowired
    SolvedProblemRepository repo;
    @Autowired
    KnowledgePointRepository kpRepo;
    public List<SolProblemDTO> generateSolProblemDTOList(){
        List<SolvedProblem> solvedProblemList=repo.findAll();
        List<SolProblemDTO> dtoList=new ArrayList<>();
    solvedProblemList.forEach(
        solvedProblem -> {
            String solvedTimesAndVisitTimes=solvedProblem.getSolvedTimes()+"/"+solvedProblem.getVisitTimes();
            Set<KnowledgePoint> kps= solvedProblem.getRelevantKps();
            String knowledgePoints="";
            for(KnowledgePoint kp:kps){
                knowledgePoints+=kp.getTitle()+"  ";
            }

            SolProblemDTO dto =
              SolProblemDTO.builder()
                  .solvedProblemId(solvedProblem.getId())
                  .subject(solvedProblem.getSubject())
                  .analysis(solvedProblem.getAnalysis())
                  .answers(solvedProblem.getAnswers())
                  .customUserSet(solvedProblem.getCustomUserSet())
                  .knowledgePoints(knowledgePoints)
                  .solvedTimesAndVisitTimes(solvedTimesAndVisitTimes)
                  .build();
            dtoList.add(dto);
        });
    return dtoList;
    }

    public void insertBatch(List<SolvedProblemExcel> problems) {
        List<SolvedProblem> solvedProblemList=new ArrayList<>();
         problems.forEach(
             problem -> {
                 log.info("题干:[{}]-分析：[{}]-相关知识点：[{}]-解答：[{}]-提示：[{}]",problem.getSubject(),problem.getAnalysis(),problem.getRelevantKnowledgePoint(),problem.getAnswer(),problem.getTip());
                SolvedProblem solvedProblem = new SolvedProblem();
                solvedProblem.setSubject(problem.getSubject());
                solvedProblem.setAnalysis(problem.getAnalysis());
                solvedProblem.setVisitTimes(0);

                 Answer answer = new Answer();
                answer.setContent(problem.getAnswer());
                answer.setProblem(solvedProblem);
                answer.setPros(0);
                answer.setCons(0);
                answer.setCreateDate(new Date());
                solvedProblem.getAnswers().add(answer);

                 KnowledgePoint point = kpRepo.findKnowledgePointByTitle(problem.getRelevantKnowledgePoint()).get();
                 solvedProblem.getRelevantKps().add(point);
                 solvedProblem.setSolvedTimes(0);

                 Tips tip = new Tips();
                 tip.setTip(problem.getTip());
                 tip.setSolvedProblem(solvedProblem);
                 solvedProblem.getTips().add(tip);
                 solvedProblemList.add(solvedProblem);
        });
         repo.saveAll(solvedProblemList);
    }

    public SolProblemDTO generateSolProblemDTO(Long problemId) {
        SolvedProblem solvedProblem=repo.findById(problemId).get();

        String solvedTimesAndVisitTimes=solvedProblem.getSolvedTimes()+"/"+solvedProblem.getVisitTimes();
        Set<KnowledgePoint> kps= solvedProblem.getRelevantKps();
        String knowledgePoints="";
        for(KnowledgePoint kp:kps){
            knowledgePoints+=kp.getTitle()+"  ";
        }

        SolProblemDTO dto =
                SolProblemDTO.builder()
                        .solvedProblemId(solvedProblem.getId())
                        .subject(solvedProblem.getSubject())
                        .analysis(solvedProblem.getAnalysis())
                        .answers(solvedProblem.getAnswers())
                        .customUserSet(solvedProblem.getCustomUserSet())
                        .knowledgePoints(knowledgePoints)
                        .solvedTimesAndVisitTimes(solvedTimesAndVisitTimes)
                        .build();

        return dto;
    }
}
