package com.gscdz.controller;

import com.gscdz.Utils.TimeUtil;
import com.gscdz.dto.SolProblemDTO;
import com.gscdz.dto.UnSolvedProblemDTO;
import com.gscdz.model.*;
import com.gscdz.repository.AnswerRepository;
import com.gscdz.repository.KnowledgePointRepository;
import com.gscdz.repository.UnsolvedProblemRepository;
import com.gscdz.service.UnSolProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(value = "/quiz")
@SessionAttributes(value = {"userInf"})
@Slf4j
public class UnsolvedProController {

    @Autowired
    UnsolvedProblemRepository usProRepo;
    @Autowired
    KnowledgePointRepository kpRepo;
    @Autowired
    AnswerRepository answerRepo;
    @Autowired
    UnSolProblemService service;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String getQuizPage(Model model, @SessionAttribute List<Chapter> chapters){
        Integer page=0;
        Integer size=5;
        Pageable pageable= PageRequest.of(page, size);
        Page<UnsolvedProblem> usPros=usProRepo.findAll(pageable);
        model.addAttribute("pageNums",usPros.getTotalPages()+1);
        model.addAttribute("currentPageNum",page+1);
        model.addAttribute("itemNum",usPros.getTotalElements());
        model.addAttribute("usPros",usPros);
        return "UserViews/quizPage";
    }
    @RequestMapping(value = "/{page}",method = RequestMethod.GET)
    public String getQuizPage(Model model,@PathVariable Integer page){
        Integer size=5;
        Integer realPage=page-1;
        Pageable pageable= PageRequest.of(realPage, size);
        Page<UnsolvedProblem> usPros=usProRepo.findAll(pageable);
        model.addAttribute("pageNums",usPros.getTotalPages()+1);
        model.addAttribute("currentPageNum",realPage+1);
        model.addAttribute("itemNum",usPros.getTotalElements());
        model.addAttribute("usPros",usPros);
        return "UserViews/quizPage";
    }

    @RequestMapping(value = "/getProDetails/{usProId}" ,method = RequestMethod.GET)
    public String getProDetails(Model model, @PathVariable Long usProId){
        UnsolvedProblem usPro=usProRepo.findById(usProId).get();
        usPro.setVisitTimes(usPro.getVisitTimes()+1);
        usProRepo.save(usPro);
        model.addAttribute("unsolvedProblem",usPro);
        model.addAttribute("createdTime", TimeUtil.getSimpleDateFormat(usPro.getCreateDate()));
        if(usPro.getFirstSolvedDate()!=null){
        model.addAttribute("firstSolvedTime", TimeUtil.getSimpleDateFormat(usPro.getFirstSolvedDate()));
        }else{
            model.addAttribute("firstSolvedTime",null);
        }
        model.addAttribute("customUserName",usPro.getCustomUser().getNickName());
        return "UserViews/unsolvedProDetails";
    }


    @RequestMapping(value = "/deletePro/{usProId}",method = RequestMethod.GET)
    public String deletePro( @PathVariable Long usProId,@SessionAttribute("userInf") CustomUser customUser,Model model){
        System.out.println("id:"+usProId);
       Set<UnsolvedProblem> problems= customUser.getUnsolvedProblems();
       System.out.println("size1:"+problems.size());
        Iterator<UnsolvedProblem> iterator= problems.iterator();
        while(iterator.hasNext()){
            UnsolvedProblem usp=iterator.next();
            if(usp.getId()==usProId){
                iterator.remove();
            }
        }
        System.out.println("size2:"+problems.size());
        customUser.setUnsolvedProblems(problems);
        model.addAttribute("userInf",customUser);
        return "redirect:/user/getSelfInformation";
    }
    @RequestMapping(value = "/addUnsolvedPro",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addUnsolvedPro(@RequestBody Map<String,String> params,@SessionAttribute("userInf") CustomUser customUser){
        Map<String,Object> map=new HashMap<>();
        if(params.get("content")!=null&&params.get("kpId")!=null){
            String content="$"+params.get("content")+"$";
            Long kpId=Long.parseLong(params.get("kpId"));
            KnowledgePoint kp=kpRepo.findById(kpId).get();
            HashSet<KnowledgePoint> kps=new HashSet<>();
            kps.add(kp);
            UnsolvedProblem unsolvedProblem=UnsolvedProblem.builder()
                    .customUser(customUser)
                    .subject(content)
                    .createDate(new Date())
                    .isSolved(false)
                    .visitTimes(0)
                    .answerNum(0)
                    .RelevantKps(kps)
                    .build();
            usProRepo.save(unsolvedProblem);//保存
            customUser.getUnsolvedProblems().add(unsolvedProblem);//userInf对象中添加题目，避免再次读取
            map.put("msg",true);
        }else{
            map.put("msg",false);
        }

        return map;
    }
    @RequestMapping(value = "/prosAndCons",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addprosAndCons(@RequestBody Map<String,String> params){
        Map<String,Object> map=new HashMap<>();
        Long answerId=Long.parseLong(params.get("answerId"));
        String action=params.get("action");
        Answer answer=answerRepo.findById(answerId).get();
        if(action.equals("pros")){
            answer.setPros(answer.getPros()+1);
        }else if(action.equals("cons")){
            answer.setCons(answer.getCons()+1);
        }
        answerRepo.save(answer);
        map.put("msg",true);
        return map;
    }
    @RequestMapping(value = "/addAnswer",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addAnswer(@RequestBody Map<String,String> params,@SessionAttribute("userInf") CustomUser customUser,Model model) {
        Map<String, Object> map = new HashMap<>();
        //添加回答
        String analysis= "$"+params.get("analysis")+"$";
        String content="$"+params.get("content")+"$";
        Long problemId=Long.parseLong(params.get("problemId"));
        UnsolvedProblem usp=usProRepo.findById(problemId).get();
        Answer answer=Answer.builder()
                .user(customUser)
                .analysis(analysis)
                .problem(usp)
                .content(content)
                .createDate(new Date())
                .cons(0)
                .pros(0)
                .build();
        answerRepo.save(answer);
        //修改user中值并保存
        customUser.setSolvedProblemNum(customUser.getSolvedProblemNum()+1);
        CustomUser userInf=customUser;
        model.addAttribute("userInf",userInf);
        //map中设置返回值
        map.put("msg",true);
        return map;
    }

    @RequestMapping(value="/getUnsoledRroblemInfo",method=RequestMethod.GET)
    public String getUnsoledRroblemInfo(){
        return "AdminViews/unsolvedProblemInfo";
    }

    @RequestMapping(value = "/getAllProblems",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getAllProblems(){
        Map<String,Object> result=new HashMap<>();
        List<UnSolvedProblemDTO> problemList=service.generateUnsolvedProblems();
        result.put("msg","success");
        result.put("code","0");
        result.put("count",problemList.size());
        result.put("data",problemList);
        return result;
    }
    @RequestMapping(value = "/getSolProblemById/{problemId}",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getSolProblemById(@PathVariable Long problemId){
        Map<String,Object> result=new HashMap<>();
        try {
            UnSolvedProblemDTO dto=service.generateSolProblemDTO(problemId);
            result.put("data",dto);
            result.put("code",1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code" ,0);
        }
        return result;
    }

    @RequestMapping(value = "/getStaticInfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getStaticInfo(){
        Map<String,Object> result=new HashMap<>();
        try {
            UnSolvedProblemDTO staticInfo = service.getStaticInfo();
            result.put("info",staticInfo);
            result.put("code",1);
            result.put("msg","get Information Successfully");
        } catch (Exception e) {
            result.put("code",0);
            result.put("msg","error");
            log.info(e.toString(),e);
        }
        return result;
    }
    @RequestMapping(value = "/addToQuestionBank",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addToQuestionBank(@RequestBody Map<String,String> values){
        Map<String,Object> resultMap=new HashMap<>();
        try {
            Long problemId=Long.parseLong(values.get("problemId"));
            UnsolvedProblem problem=usProRepo.findById(problemId).get();
            service.addToProblemBank(problem);
            resultMap.put("code",1);
            resultMap.put("msg","添加成功");
        } catch (NumberFormatException e) {
           log.info(e.toString(),e);
            resultMap.put("code",0);
            resultMap.put("msg","添加失败，稍后重试");
        }
       return  resultMap;
    }
}
