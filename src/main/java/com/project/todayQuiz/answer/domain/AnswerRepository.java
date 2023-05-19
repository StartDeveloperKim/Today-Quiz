package com.project.todayQuiz.answer.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("select count(a) > 0 from Answer a where YEAR(a.correctTime)=:year and MONTH(a.correctTime)=:month and DAY(a.correctTime)=:day " +
            "and a.user.id=:userId and a.quiz.id=:quizId")
    Boolean existsAnswerByUserAndQuiz(@Param("year") int year,
                                      @Param("month") int month,
                                      @Param("day") int day,
                                      @Param("userId") Long userId,
                                      @Param("quizId") Long quizId);
}
