package com.project.todayQuiz.rank.domain;

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
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rank_id")
    private Long id;

    @ManyToOne(targetEntity = Quiz.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime rankDate;

    public Rank(Quiz quiz, User user, LocalDateTime rankDate) {
        this.quiz = quiz;
        this.user = user;
        this.rankDate = rankDate;
    }
}
