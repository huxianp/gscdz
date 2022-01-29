package com.gscdz.controller;


import com.gscdz.Utils.MathOCRUtil;
import com.gscdz.model.Chapter;
import com.gscdz.model.CustomUser;
import com.gscdz.model.User;
import com.gscdz.repository.ChapterRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
@SessionAttributes(value = {"chapters"})
public class AppController {
	@Autowired
	ChapterRepository chapterRepo;
    @GetMapping("/login")
    public ModelAndView login(String error){
         ModelAndView modelAndView = new ModelAndView("Login");
         modelAndView.addObject("error", error);
        return modelAndView;
    }
	@GetMapping("/registerPage")
	public String signIn() {
		return "registerPage";
	}
	@GetMapping("/getUserHome")
	public String getUserHome(Model model,@SessionAttribute("userInf") CustomUser customUser) {
		if(customUser==null){
			log.info("customUser NUll");
			return "redirect:/login";
		}else{
			List<Chapter> chapters=chapterRepo.findAll();
			model.addAttribute("nickName",customUser.getNickName());
			model.addAttribute("solvedProblemNum",customUser.getSolvedProblemNum());
			model.addAttribute("unSolvedProblemNum",customUser.getUnsolvedProblems().size());
			model.addAttribute("chapters", chapters);
		}
		return "UserViews/UserHomePage";
	}
	@GetMapping("/getAdminHome")
	public String getAdminHome(Model model,@SessionAttribute("adminInfo")User admin) {
		model.addAttribute("admin",admin);
    	return "AdminViews/adminHomePage";
	}
	@GetMapping("/uerInfoPage")
	public String getUserInfo(){
    	return "AdminViews/userInfo";
	}
	@GetMapping("/getSurfaceManage")
	public String getSurfaceManage(){
    	return "AdminViews/surfaceManage";
	}
	@PostMapping("/upload")
	@ResponseBody
	public Map<String,Object> upload(@RequestParam("file") MultipartFile file) throws FileNotFoundException {
		Map<String,Object> result=new HashMap<>();
    	if (file.isEmpty()) {
    		result.put("code","0");
    		result.put("msg","上传失败，请选择文件");
		}
		String fileName = file.getOriginalFilename();
		String  path= ResourceUtils.getFile("classpath:").getPath();
    	String filePath = path+"\\static\\uploadFile\\";
		File dest = new File(filePath + fileName);
		try {
			file.transferTo(dest);
			log.info("上传成功");
			String latexStr=MathOCRUtil.getMathCodeFromImage(dest);
			result.put("code","1");
			result.put("msg","上传成功");
			result.put("data",latexStr);
		} catch (IOException e) {
			result.put("code","0");
			result.put("msg","出错了，请重试");
			log.error(e.toString(), e);
		}
		return result;
	}
	@GetMapping("/download/{fileName}")
	@ResponseBody
	public String downloadFile(HttpServletResponse response,@PathVariable("fileName") String fileName) throws FileNotFoundException {
    	log.info("fileName:[{}]",fileName);
    	if (fileName != null) {
			//设置文件路径
			String  path= ResourceUtils.getFile("classpath:").getPath();
			String filePath = path+"\\static\\uploadFile\\";
			File file = new File(filePath+fileName);
			if (file.exists()) {
				response.setContentType("application/force-download");
				response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
					return "下载成功";
				} catch (Exception e) {
					e.printStackTrace();
				} finally { // 做关闭操作
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return "下载失败";
	}
	@RequestMapping(value = "getPage",method = RequestMethod.GET)
	public String getPage(){
    	return "myscriptPage";
	}

}
