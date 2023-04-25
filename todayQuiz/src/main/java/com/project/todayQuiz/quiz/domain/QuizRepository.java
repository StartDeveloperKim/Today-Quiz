package com.project.todayQuiz.quiz.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query("select q from Quiz q where YEAR(q.postDate)=:year and MONTH(q.postDate)=:month and DAY(q.postDate)=:day")
    Optional<Quiz> findQuizByPostDate(@Param("year") int year,
                                      @Param("month") int month,
                                      @Param("day") int day);

    @Query("select count(q) > 0 from Quiz q where YEAR(q.postDate)=:year and MONTH(q.postDate)=:month and DAY(q.postDate)=:day")
    Boolean existsQuizByPostDate(@Param("year") int year,
                                 @Param("month") int month,
                                 @Param("day") int day);
}
