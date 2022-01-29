package com.gscdz.controller;

import com.gscdz.model.Chapter;
import com.gscdz.model.KnowledgePoint;
import com.gscdz.repository.ChapterRepository;
import com.gscdz.repository.KnowledgePointRepository;
import com.gscdz.service.ChapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(value = "/chapter")
@Slf4j
public class ChapterController {
	@Autowired
	ChapterRepository chapterRepo;
	@Autowired
	KnowledgePointRepository kpRepo;
	@Autowired
	ChapterService chapterService;

	@RequestMapping(value = "/{chapterId}",method =RequestMethod.GET)
	public String getAllKps(@PathVariable Long chapterId,Model model) {
		Chapter chapter=chapterRepo.findById(chapterId).get();
		String text="";
		for(KnowledgePoint kp:chapter.getKpSet()){
			if(kp.getMaterials()!=null) {text+=kp.getMaterials();}
		}
		System.out.println(text);
		model.addAttribute("chapter",chapter);
		model.addAttribute("text",text);
		return "UserViews/kpDetails";
	}
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addChapter(@RequestBody Map<String,String> values){
		Map<String,Object> results=new HashMap<>();
		try {
			String chapterName=values.get("chapterName");
			Chapter chapter=new Chapter();
			chapter.setTitle(chapterName);
			chapterRepo.save(chapter);
			results.put("code",1);
			results.put("msg","删除成功");
		} catch (Exception e) {
			results.put("code",0);
			results.put("msg","删除失败");
		}
		return  results;
	}

	@RequestMapping(value = "/knowledgePoint/{chapterId}/{kpId}",method =RequestMethod.GET)
	public String getKp(@PathVariable Long chapterId,@PathVariable Long kpId,Model model) {
		Chapter chapter=chapterRepo.findById(chapterId).get();
		String text=kpRepo.findById(kpId).get().getMaterials();
		model.addAttribute("chapter",chapter);
		model.addAttribute("text",text);
		return "UserViews/kpDetails";
	}
	//根据chapterId返回json数据
	@RequestMapping(value = "/getKnowledgePoints",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getKnowledgePoints(@SessionAttribute List<Chapter> chapters,@RequestBody Map<String,String> params){
		Map<String, Object> map = new HashMap<String, Object>();
		long chapterId=Long.parseLong(params.get("chapterId"));
		Set<KnowledgePoint> kps=null;
		//找对应的knowledge的集合
		for (Chapter chapter:chapters){
			if(chapter.getId()==chapterId){
				kps=chapter.getKpSet();
			}
		}
		//减少传输量
		for (KnowledgePoint knowledgePoint:kps) {
			knowledgePoint.setMaterials(null);
			knowledgePoint.setChapter(null);
			knowledgePoint.setRelevantPros(null);
		}
		map.put("kps",kps);
		return map;
	}

	@RequestMapping(value="/getManagerPage",method=RequestMethod.GET)
	public String getManagerPage(Model model){
		List<Chapter> chapterList=chapterRepo.findAll();
		model.addAttribute("chapterList",chapterList);
		return "AdminViews/chapterManage";
	}

	@RequestMapping(value="/knowledgePoints/add")
	public String addKnowledgePoints(HttpServletRequest request){
		try {
			String newSubtitle=request.getParameter("newSubtitle");
			String title=request.getParameter("title");
			log.info("newSubtitle:[{}]-title:[{}]",newSubtitle,title);
			Chapter chapter=chapterRepo.findChapterByTitle(title).get();
			chapterService.addKPToChapter(newSubtitle,chapter);
		} catch (Exception e) {
			log.info(e.toString(),e);
		}
		return "redirect:/chapter/getManagerPage";
	}
	@RequestMapping(value ="/knowledgePoints/delete",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteKnowledgePoints(@RequestBody Map<String,String> values){
		Map<String,Object> results=new HashMap<>();
		try {
			String subTitle=values.get("subTitle");
			KnowledgePoint knowledgePoint=kpRepo.findKnowledgePointByTitle(subTitle).get();
			log.info("subTitle:[{}]",subTitle);
			log.info("isEmpty;[{}]",kpRepo.findKnowledgePointByTitle(subTitle).isEmpty());
			kpRepo.delete(knowledgePoint);
			results.put("code",1);
			results.put("msg","删除成功");
		} catch (Exception e) {
			results.put("code",0);
			results.put("msg","删除失败");
		}
		return results;
	}
	@RequestMapping(value="/knowledgePoints/getMaterials/{kpId}")
	@ResponseBody
	public Map<String,Object> getkpMaterials(@PathVariable Long kpId){
		Map<String,Object> resultMap=new HashMap<>();
		try {
			Optional<KnowledgePoint> optionalKnowledgePoint = kpRepo.findById(kpId);
			KnowledgePoint knowledgePoint=optionalKnowledgePoint.get();
			String materials=knowledgePoint.getMaterials();
			resultMap.put("code",1);
			resultMap.put("materials",materials);
		} catch (Exception e) {
			log.info(e.toString(),e);
			resultMap.put("code",0);
		}
		return resultMap;
	}
	@RequestMapping(value="/knowledgePoints/updateMaterials",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updatekpMaterials(@RequestBody Map<String,String> values) {
		Map<String,Object> resultMap=new HashMap<>();
		try {
			Long kpId=Long.parseLong(values.get("kpId"));
			String materials=values.get("Materials");
			KnowledgePoint kp=kpRepo.findById(kpId).get();
			kp.setMaterials(materials);
			kpRepo.save(kp);
			resultMap.put("msg","update okay");
			resultMap.put("code",1);
		} catch (Exception e) {
			log.info(e.toString(),e);
			resultMap.put("code",0);
			resultMap.put("msg","Oops");
		}

		return resultMap;
		}
	}


