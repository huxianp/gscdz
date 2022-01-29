package com.gscdz.service;

import com.gscdz.Utils.PhotoConverUtil;
import com.gscdz.dto.CustomUserInfoDTO;
import com.gscdz.dto.UserExcelDTO;
import com.gscdz.model.*;
import com.gscdz.repository.CustomUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.*;

@Service
@Slf4j
public class CustomUserService {
    @Autowired
    CustomUserRepository customUserRepo;
    public CustomUser findByUsername(String username){
        Optional<CustomUser> optUser=customUserRepo.findByUsername(username);
        if(optUser.isEmpty()){
            log.error("USER NOT EXISTS");
            return null;
        }
        return optUser.get();
    }
    public List<CustomUserInfoDTO> getAllCustomUserInfo(){
        List<CustomUser> allCutomUser=customUserRepo.findAll();
        List<CustomUserInfoDTO> allUserInfo=new ArrayList<>();


        allCutomUser.forEach(
            (customUser) -> {
                 CustomUserInfoDTO dto=new CustomUserInfoDTO();
                 dto.setUserId(customUser.getUserId());
                 dto.setUsername(customUser.getUsername());
                 dto.setPassword(customUser.getPassword());
                 dto.setSolvedProblemNum(customUser.getSolvedProblemNum());
                 HashMap<String,Integer> submitedUnSolvedProblemTypes=dto.getSubmitedUnSolvedProblemTypes();
                 Set<String> keySet = submitedUnSolvedProblemTypes.keySet();
                 Set<UnsolvedProblem> unsolvedProblems = customUser.getUnsolvedProblems();
                 unsolvedProblems.forEach(unsolvedProblem -> {
                     unsolvedProblem.getRelevantKps().forEach(knowledgePoint -> {
                         String title=knowledgePoint.getTitle();
                         if(keySet.contains(title)){
                             Integer num=submitedUnSolvedProblemTypes.get(title);
                             submitedUnSolvedProblemTypes.put(title,num+1);
                         }else{
                             submitedUnSolvedProblemTypes.put(title,1);
                         }
                     });
                 });
                  Integer pros=0;
                  Integer cons=0;
                  List<Answer> answers = customUser.getAnswers();
                  HashMap<String, Integer> answeredProblemTypes = dto.getAnsweredProblemTypes();
                  Set<String> stringSet = answeredProblemTypes.keySet();
                  for(Answer answer:answers){
                      Set<KnowledgePoint> relevantKps = answer.getProblem().getRelevantKps();
                      relevantKps.forEach(knowledgePoint -> {
                        String title=knowledgePoint.getTitle();
                        if(stringSet.contains(title)){
                            Integer num=answeredProblemTypes.get(title);
                            answeredProblemTypes.put(title,num+1);
                        }else{
                            answeredProblemTypes.put(title,1);
                        }
                    });
                      pros+=answer.getPros();
                      cons+=answer.getCons();

                  }
                 dto.setAnswerPros(pros);
                 dto.setAnswerCons(cons);
                 allUserInfo.add(dto);
        });

        return allUserInfo;
    }

    public void insertBatch(List<UserExcelDTO> customUserInfoList) throws FileNotFoundException {
        String  path= ResourceUtils.getFile("classpath:").getPath();
        String filePath = path+"\\static\\uploadFile\\";
        byte[] avatarByte= PhotoConverUtil.imageToBytes(filePath+"default-avatar.png");
        List<CustomUser> customUserList=new ArrayList<>();
        customUserInfoList.forEach(customUserInfo->{
            CustomUser customUser= CustomUser.builder()
                    .solvedProblemNum(0)
                    .username(customUserInfo.getUserName())
                    .nickName(customUserInfo.getNickName())
                    .avatar(avatarByte)
                    .password(new BCryptPasswordEncoder().encode(customUserInfo.getPassword()))
                    .role("ROLE_USER")
                    .build();
            customUserList.add(customUser);
        });
        customUserRepo.saveAll(customUserList);
    }
    public void updateUserInformation(Map<String,String>values){
        Long userId=Long.parseLong(values.get("userId"));
        Optional<CustomUser> optionalCustomUser = customUserRepo.findById(userId);
        CustomUser user=optionalCustomUser.get();
        String nickName=values.get("nickName");
        String password=values.get("password");
        if(nickName!=null&&nickName!=""&&user.getNickName()!=nickName){
            user.setNickName(nickName);
        }
        if(password!=null&&password!=""&&user.getPassword()!=password){
            user.setPassword(new BCryptPasswordEncoder().encode(password));
        }
    }
}
