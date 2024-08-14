package com.example.sbb.question;

import jakarta.validation.constraints.NotEmpty; // null 또는 빈 문자열 X
import jakarta.validation.constraints.Size; // 문자 길이 제한 

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm {
    @NotEmpty(message="제목은 필수항목입니다.") // message -> 검증 실패시 메세지 
    @Size(max=200)
    private String subject;

    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;
}
