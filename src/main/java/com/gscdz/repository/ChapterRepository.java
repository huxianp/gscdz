package com.gscdz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gscdz.model.Chapter;

import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long>{
    public Optional<Chapter>  findChapterByTitle(String title);

}
