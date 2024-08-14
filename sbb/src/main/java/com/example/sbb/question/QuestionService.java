package com.example.sbb.question;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;

// 최신순 조회 
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;

import com.example.sbb.user.SiteUser;
import com.example.sbb.answer.Answer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
@RequiredArgsConstructor
@Service // 서비스 클래스로 만들어 주는 어노테이션  / 모듈화 , 엔티티 객체를 DTO 객체로 변환  
public class QuestionService {

    private final QuestionRepository questionRepository; 
    // questionRepository 객체는 @RequiredArgsConstructor에 의해 생성자 방식으로 주입 

    public Page<Question> getList(int page, String kw) {
    	List<Sort.Order> sorts = new ArrayList<>(); // 정렬 
        sorts.add(Sort.Order.desc("createDate")); // 작성 일시(createDate) 역순 조회 desc / 정방향은 asc 
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); 
        Specification<Question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
    }
    
    public Question getQuestion(Integer id) {  
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }
    
    public void create(String subject, String content, SiteUser user) { // 질문 데이터 저장 
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        this.questionRepository.save(q);
    }
    
    // 수정 처리 
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }
    
    //삭제
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }
    
    // 추천인 저장 
    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }
    
    // 검색 
    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거 
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목 
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용 
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자 
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용 
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자 
            }
        };
    }
}