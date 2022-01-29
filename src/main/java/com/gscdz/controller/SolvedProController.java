package com.gscdz.controller;

import java.util.*;

import com.gscdz.Utils.ExcelUtil;
import com.gscdz.dto.SolProblemDTO;
import com.gscdz.dto.UnSolvedProblemDTO;
import com.gscdz.dto.UserExcelDTO;
import com.gscdz.model.*;
import com.gscdz.service.SolProblemService;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.gscdz.dto.SolvedProblemExcel;
import com.gscdz.repository.SolvedProblemRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/solvedProblem")
@SessionAttributes(value = {"solvedProList","userInf"})
@Slf4j
public class SolvedProController {
    @Autowired
    SolvedProblemRepository solvedProRepo;
    @Autowired
    SolProblemService service;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String  getTestPage(Model model){
        List<SolvedProblem> solvedProblems=solvedProRepo.findAll();
        int number=(int)(Math.random()*solvedProblems.size());
        System.out.print(number);
        SolvedProblem solpro=solvedProblems.get(number);
        System.out.print(solpro.getSubject());
        System.out.print("");
        model.addAttribute("solvedProList",solvedProblems);
        model.addAttribute("randomSolvedPro",solpro);
        return "UserViews/test";
    }

    @RequestMapping(value = "/offer",method = RequestMethod.GET)
    public String offerProPage(SolvedProblemExcel solvedProDto, Model model){
    	return "UserViews/offer";
    }
    @RequestMapping(value = "/offer",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> offerPro(@RequestBody Map<String,String> values,@SessionAttribute("userInf")CustomUser customUser) {
        Map<String,Object> map=new HashMap<>();
        try {
            String subject=values.get("subject");
            String solution=values.get("solution");
            String analysis=values.get("analysis");
            String tipsContent=values.get("tips");
            SolvedProblem problem=new SolvedProblem();
            problem.setSubject(subject);
            problem.setSolvedTimes(0);
            problem.setVisitTimes(0);
            problem.setAnalysis(analysis);
            problem.getCustomUserSet().add(customUser);

            Answer answer=new Answer();
            answer.setPros(0);
            answer.setCons(0);
            answer.setCreateDate(new Date());
            answer.setProblem(problem);
            answer.setContent(solution);
            answer.setAnalysis("");
            answer.setUser(customUser);

            problem.getAnswers().add(answer);
            Tips tip=new Tips();
            tip.setTip(tipsContent);
            tip.setSolvedProblem(problem);
            problem.getTips().add(tip);

            solvedProRepo.save(problem);
            map.put("code",1);
            map.put("msg","新增成功，感谢您对我们网站的支持");
        } catch (Exception e) {
            log.info(e.toString(),e);
            map.put("code",0);
            map.put("msg","出错了，请稍后重试");
        }
        return  map;
    }

    @RequestMapping(value = "/ctj",method = RequestMethod.GET)
    public String offerCTJPage(Model model, @SessionAttribute("userInf")CustomUser customUser, @SessionAttribute("chapters")List<Chapter> chapters){
        Set<SolvedProblem> problems=customUser.getCtjSet();
        model.addAttribute("problems",problems);
        model.addAttribute("chapters",chapters);
        return "UserViews/ctj";
    }

    @RequestMapping(value = "/addToctj",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addToCtj(@RequestBody Map<String,String> params, @SessionAttribute("solvedProList") List<SolvedProblem> solvedProblems, @SessionAttribute("userInf")CustomUser customUser,Model model){
        Map<String,Object> map=new HashMap<>();
        String proId=params.get("problemId");
        System.out.println(proId);
        if (proId!=null){
            Long problemId=Long.parseLong(proId);
            System.out.println(problemId);
            SolvedProblem sp=null;
            for(SolvedProblem solvedProblem:solvedProblems){
                if(solvedProblem.getId()==problemId)
                    sp=solvedProblem;
            }
            customUser.getCtjSet().add(sp);
            model.addAttribute("userInf",customUser);
            map.put("msg",true);
        }else{
            map.put("msg",false);
        }
        return map;
    }
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.GET)
    public String delete( @SessionAttribute("userInf")CustomUser customUser,@PathVariable Long id){
        solvedProRepo.findById(id).get().getCustomUserSet().remove(customUser);
        customUser.getCtjSet().remove(solvedProRepo.findById(id).get());
        return "redirect:/solvedProblem/ctj";
    }

    @RequestMapping(value = "/getSolvedProblemInfo",method = RequestMethod.GET)
    public String  getSolvedProblem(){
        return "AdminViews/solvedProblemInfo";
    }

    @RequestMapping(value = "/getAllSolvedProblemInfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getAllSolvedProblemInfo(){
        Map<String,Object> result=new HashMap<>();
        List<SolProblemDTO> solvedProblemList=service.generateSolProblemDTOList();
        result.put("msg","success");
        result.put("code","0");
        result.put("count",solvedProblemList.size());
        result.put("data",solvedProblemList);
        return result;
    }

    @RequestMapping(value = "/getSolProblemById/{problemId}")
    @ResponseBody
    public Map<String,Object> getSolProblemById(@PathVariable Long problemId){
        Map<String,Object> result=new HashMap<>();
        try {
            SolProblemDTO dto=service.generateSolProblemDTO(problemId);
            result.put("data",dto);
            result.put("code",1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code" ,0);
        }
        return result;
    }
    @RequestMapping(value = "/insertBatch",method=RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> uploadFile(@RequestParam("file") MultipartFile file){
        Map<String,Object> map = new HashMap();
        try {
            List<SolvedProblemExcel> problems = ExcelUtil.importFromFile(SolvedProblemExcel.class, file);
            log.info("listSize:[{}]",problems.size());
            service.insertBatch(problems);
            map.put("msg","OK");
            map.put("code",200);
        } catch (Exception e) {
            log.info(e.toString(),e);
            map.put("msg","error");
            map.put("code",0);
        }
        return map;
    }
    @RequestMapping(value = "/getAllAnswerAndProblem" ,method = RequestMethod.GET)
     public  Map<String,Object> getAllAnswerAndProblem(){
        Map<String,Object> result=new HashMap<>();

        return result;
    }

    @RequestMapping(value = "/deleteById/{problemId}",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> deleteById(@PathVariable Long problemId){
        Map<String,Object> resultMap=new HashMap<>();
        log.info("userId:[{}]",problemId);
        try {
            solvedProRepo.deleteById(problemId);
            resultMap.put("isSuccess",true);
            resultMap.put("msg","删除成功");
        } catch (Exception e) {
            log.info(e.toString(),e);
            resultMap.put("isSuccess",false);
            resultMap.put("msg","服务器内部出错，请稍后重试");
        }
        return resultMap;
    }

}
