package com.project.todayQuiz.answer.domain;

import com.project.todayQuiz.quiz.domain.Quiz;
import com.project.todayQuiz.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "answer_id")
    private Long id;

    @ManyToOne(targetEntity = Quiz.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime correctTime;

    public Answer(Quiz quiz, User user) {
        this.quiz = quiz;
        this.user = user;
        this.correctTime = LocalDateTime.now();
    }
}
