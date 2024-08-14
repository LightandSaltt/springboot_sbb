package com.example.sbb.question; 

import java.time.LocalDateTime; 
import java.util.List;

import com.example.sbb.answer.Answer;

import jakarta.persistence.CascadeType; 
import jakarta.persistence.Column; 
import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue; 
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id; 
import jakarta.persistence.OneToMany; 

import lombok.Getter; 
import lombok.Setter; 

// author(글쓴이) 엔티티 추가를 위한 
import jakarta.persistence.ManyToOne;
import com.example.sbb.user.SiteUser;

import java.util.Set;
import jakarta.persistence.ManyToMany;

@Getter 
@Setter 
@Entity 
public class Question { 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer id; 

    @Column(length = 200) 
    private String subject; 

    @Column(columnDefinition = "TEXT") 
    private String content; 

    private LocalDateTime createDate; 

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE) 
    private List<Answer> answerList; 
    // 질문에서 답변을 참조하려면 question.getAnswerList()를 호출하면 된다.
    
    // 글쓴이 표시 
    @ManyToOne
    private SiteUser author;
    
    // 수정 일시
    private LocalDateTime modifyDate;
    
    //추천
    @ManyToMany
    Set<SiteUser> voter;
}
