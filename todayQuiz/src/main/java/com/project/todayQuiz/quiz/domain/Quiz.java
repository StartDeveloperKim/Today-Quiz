package com.project.todayQuiz.quiz.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "quiz_id")
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    private LocalDateTime createDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime postDate;

    @Builder
    public Quiz(String question, String answer, LocalDateTime createDate, LocalDateTime postDate) {
        this.question = question;
        this.answer = answer;
        this.createDate = createDate;
        this.postDate = postDate;
    }

    public void updateQuiz(String question, String answer, LocalDateTime postDate) {
        this.question = question;
        this.answer = answer;
        this.postDate = postDate;
    }
}
