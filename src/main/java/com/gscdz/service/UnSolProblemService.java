package com.gscdz.service;

import com.gscdz.Utils.TimeUtil;
import com.gscdz.dto.UnSolvedProblemDTO;
import com.gscdz.model.Answer;
import com.gscdz.model.KnowledgePoint;
import com.gscdz.model.SolvedProblem;
import com.gscdz.model.UnsolvedProblem;
import com.gscdz.repository.AnswerRepository;
import com.gscdz.repository.SolvedProblemRepository;
import com.gscdz.repository.UnsolvedProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Hu
 * @date 2021-09-22 11:18
 * @description:
 */
@Service
public class UnSolProblemService {
    @Autowired
    UnsolvedProblemRepository repo;
    @Autowired
    AnswerRepository answerRepo;
    @Autowired
    SolvedProblemRepository solRepo;
    public List<UnSolvedProblemDTO> generateUnsolvedProblems(){
        ArrayList<UnSolvedProblemDTO> problemDTOList = new ArrayList<>();
        List<UnsolvedProblem> problems=repo.findAll();
        problems.forEach(
            problem -> {
                String kps = "";
                String answerNumAndVisitNum=problem.getAnswerNum()+"/"+problem.getVisitTimes();
                for (KnowledgePoint Kp : problem.getRelevantKps()) {
                    kps+=Kp.getTitle();
                }
                UnSolvedProblemDTO dto=
                            UnSolvedProblemDTO.builder()
                                    .problemId(problem.getId())
                                    .subject(problem.getSubject())
                                    .analysis(problem.getAnalysis())
                                    .createTime(TimeUtil.getSimpleDateFormat(problem.getCreateDate()))
                                    .firstSolvedTime(TimeUtil.getOnlyDate(problem.getCreateDate()))
                                    .relevantKps(kps)
                                    .answerNumAndVisitNum(answerNumAndVisitNum)
                                    .answerList(problem.getAnswers())
                                    .build();

          problemDTOList.add(dto);
        });
        return  problemDTOList;
    }

    public UnSolvedProblemDTO generateSolProblemDTO(Long problemId) {
        UnsolvedProblem problem=repo.findById(problemId).get();
        String kps = "";
        String answerNumAndVisitNum=problem.getAnswerNum()+"/"+problem.getVisitTimes();
        for (KnowledgePoint Kp : problem.getRelevantKps()) {
            kps+=Kp.getTitle();
        }
        UnSolvedProblemDTO dto =
                UnSolvedProblemDTO.builder()
                        .problemId(problem.getId())
                        .subject(problem.getSubject())
                        .analysis(problem.getAnalysis())
                        .createTime(TimeUtil.getSimpleDateFormat(problem.getCreateDate()))
                        .firstSolvedTime(TimeUtil.getSimpleDateFormat(problem.getFirstSolvedDate()))
                        .relevantKps(kps)
                        .answerNumAndVisitNum(answerNumAndVisitNum)
                        .answerList(problem.getAnswers())
                        .build();
        return dto;
    }

    public UnSolvedProblemDTO getStaticInfo(){
        List<Answer> answerList = answerRepo.findAll();
        List<UnsolvedProblem> unsolvedProblemList=repo.findAll();

        //每个知识点有多少个对应的题目
        HashMap<String,Integer> kpStatistic=new HashMap<>();
        Set<String> kpKeySet = kpStatistic.keySet();
        //每天回答量的统计
        HashMap<String,Integer> answerStatistic=new HashMap<>();
        Set<String> answerKeySet = answerStatistic.keySet();
        //每天提出的问题量统计
        HashMap<String,Integer> timeStatic=new HashMap<>();
        Set<String> timeSet = timeStatic.keySet();


        //每天回答量的统计
        answerList.forEach(answer -> {
            String date = TimeUtil.getOnlyDate(answer.getCreateDate());
            if(answerKeySet.contains(date)){
                answerStatistic.put(date,answerStatistic.get(date)+1);
            }
            else {
                answerStatistic.put(date,1);
            }
        });

        unsolvedProblemList.forEach(unsolvedProblem -> {
            //每天提出的问题量统计
            String date=TimeUtil.getOnlyDate(unsolvedProblem.getCreateDate());
            if(timeSet.contains(date)){
                timeStatic.put(date,timeStatic.get(date)+1);
            }
            else {
                timeStatic.put(date,1);
            }
            //问题相关知识点的统计
            unsolvedProblem.getRelevantKps().forEach(knowledgePoint -> {
                String kp=knowledgePoint.getTitle();
                if(kpKeySet.contains(kp)){
                    kpStatistic.put(kp,kpStatistic.get(kp)+1);
                }else{
                    kpStatistic.put(kp,1);
                }
            });
        });
        UnSolvedProblemDTO dto= UnSolvedProblemDTO.builder()
                .answerStatistic(answerStatistic)
                .timeStatic(timeStatic)
                .kpStatistic(kpStatistic)
                .build();
        return  dto;

    }

    public void addToProblemBank(UnsolvedProblem problem){
        UnsolvedProblem unsolvedProblem =problem;
        SolvedProblem solvedProblem=new SolvedProblem();
        solvedProblem.setId(unsolvedProblem.getId());
        solvedProblem.setSubject(unsolvedProblem.getSubject());
        solvedProblem.setAnalysis(unsolvedProblem.getAnalysis());
        solvedProblem.setVisitTimes(unsolvedProblem.getVisitTimes());
        solvedProblem.setAnswers(unsolvedProblem.getAnswers());
        solvedProblem.setRelevantKps(unsolvedProblem.getRelevantKps());
        solvedProblem.setSolvedTimes(20);
        solRepo.save(solvedProblem);
    }

}
