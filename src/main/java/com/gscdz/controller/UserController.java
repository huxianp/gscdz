package com.gscdz.controller;

import com.gscdz.Utils.ExcelUtil;
import com.gscdz.Utils.PhotoConverUtil;
import com.gscdz.dto.CustomUserInfoDTO;
import com.gscdz.dto.UserExcelDTO;
import com.gscdz.model.Answer;
import com.gscdz.model.CustomUser;
import com.gscdz.repository.CustomUserRepository;
import com.gscdz.service.CustomUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {


    @Autowired
    CustomUserService customUserService;
    @Autowired
    CustomUserRepository customUserRepo;

    @RequestMapping(value = "/checkUsername",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> checkUserName(@RequestBody Map<String,String> params) {
        Map<String, Object> map = new HashMap<String, Object>();
        String username=params.get("username");
        CustomUser user=customUserService.findByUsername(username);
        map.put("isExist",true);
        if(user==null){
            map.put("isExist",false);
        }
        return map;
    }
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> register(@RequestBody Map<String,String> params) throws FileNotFoundException {
        Map<String, Object> map = new HashMap<String, Object>();
        String username=params.get("username");
        String password=params.get("password");
        String encryPassword=new BCryptPasswordEncoder().encode(password);
        String  path= ResourceUtils.getFile("classpath:").getPath();
        String filePath = path+"\\static\\uploadFile\\";
        byte[] avatarByte=PhotoConverUtil.imageToBytes(filePath+"default-avatar.png");
        if(username!=null&&password!=null){
            CustomUser cmUser=CustomUser
                    .builder()
                    .username(username)
                    .nickName(username)
                    .password(encryPassword)
                    .role("ROLE_USER")
                    .avatar(avatarByte)
                    .build();
            customUserRepo.save(cmUser);
            map.put("msg",true);
        }else{
            map.put("msg",false);
        }
            return map;
    }

    @RequestMapping(value = "/insertBatch",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Map<String,Object> uploadFile(@RequestParam("file") MultipartFile file){
        Map map = new HashMap();
        try {
            List<UserExcelDTO> userInfoList = ExcelUtil.importFromFile(UserExcelDTO.class, file);
            log.info("listSize:[{}]",userInfoList.size());
            customUserService.insertBatch(userInfoList);
            map.put("msg","OK");
            map.put("code",200);
        } catch (Exception e) {
            log.info(e.toString(),e);
            map.put("msg","error");
            map.put("code",0);
        }
        return map;
    }


    @RequestMapping(value = "/getSelfInformation",method = RequestMethod.GET)
    public String getSelfInformation(Model model, @SessionAttribute("userInf") CustomUser customUser){
        if(customUser==null){
            return "redirect:/login";
        }else{
            Integer pros=0;
            List<Answer> answers=customUser.getAnswers();
            for (Answer an:answers){
                pros+=an.getPros();
            }
            model.addAttribute("pros",pros);
            model.addAttribute("customUser",customUser);
        }
        return "UserViews/SelfInformation";
    }

    @RequestMapping(value = "/getAvatar",method = RequestMethod.GET)
    public void getAvatar(@SessionAttribute("userInf") CustomUser customUser, HttpServletResponse response) throws IOException {
        byte[] imgByte=customUser.getAvatar();//获取byte数组
        ServletOutputStream os=response.getOutputStream();//获取输出流
        PhotoConverUtil.byteToImage(imgByte,os);
    }

    @RequestMapping(value = "/getAllUserInfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getAllUserInfo(){
        Map<String,Object> result=new HashMap<>();
        List<CustomUserInfoDTO> allUserInfo=customUserService.getAllCustomUserInfo();
        result.put("msg","success");
        result.put("code","0");
        result.put("data",allUserInfo);
        return result;
    }

    @RequestMapping(value="/deleteCustomUserById/{userId}" ,method=RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> deleteCustomUserByUserId(@PathVariable Long userId){
        Map<String,Object> resultMap=new HashMap<>();
         log.info("userId:[{}]",userId);
        try {
            customUserRepo.deleteById(userId);
            resultMap.put("isSuccess",true);
            resultMap.put("msg","删除成功");
        } catch (Exception e) {
            log.info(e.toString(),e);
            resultMap.put("isSuccess",false);
            resultMap.put("msg","服务器内部出错，请稍后重试");
        }
        return resultMap;
    }

    @RequestMapping(value="/updateUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateUserInfo(@RequestBody Map<String,String> values){
        Map<String,Object> resultMap=new HashMap<>();
        try {
            customUserService.updateUserInformation(values);
            resultMap.put("isSuccess",true);
            resultMap.put("msg","修改成功");
        } catch (Exception e) {
            resultMap.put("isSuccess",false);
            resultMap.put("msg","服务器内部错误，请稍后重试");
            log.info(e.toString(),e);
        }
        return resultMap;

    }
}
