package com.example.sbb.answer; 

import java.time.LocalDateTime;


import com.example.sbb.question.Question;

import jakarta.persistence.Column; 
import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id; 
import jakarta.persistence.ManyToOne;
import lombok.Getter; 
import lombok.Setter; 
import com.example.sbb.user.SiteUser;
import java.util.Set;
import jakarta.persistence.ManyToMany;
@Getter 
@Setter 
@Entity 
public class Answer { 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer id;

    @Column(columnDefinition = "TEXT") 
    private String content; 

    private LocalDateTime createDate; 

    @ManyToOne 
    private Question question; 
    
    @ManyToOne
    private SiteUser author; // 글쓴이 
    
    // 수정 일시
    private LocalDateTime modifyDate;
    
    // 추천
    @ManyToMany
    Set<SiteUser> voter;
}
