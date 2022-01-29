package com.gscdz.service;

import com.gscdz.model.Chapter;
import com.gscdz.model.KnowledgePoint;
import com.gscdz.repository.ChapterRepository;
import com.gscdz.repository.KnowledgePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Hu
 * @date 2021-09-21 15:48
 * @description:
 */
@Service
public class ChapterService {
    @Autowired
    ChapterRepository chapterRepo;
    @Autowired
    KnowledgePointRepository kpRepo;
    public void addKPToChapter(String newTitle, Chapter chapter){
        KnowledgePoint knowledgePoint=new KnowledgePoint();
        knowledgePoint.setChapter(chapter);
        knowledgePoint.setTitle(newTitle);
        kpRepo.save(knowledgePoint);
    }
}
