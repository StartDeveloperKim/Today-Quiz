package com.project.todayQuiz.quiz.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    // TODO : 2023-05-01 일단 DB에서 오늘 날짜와 클라이언트가 제출한 정답을 비교하여 True or False로 가져오는 쿼리를 쓰지만
    //  나중에 스프링 스케쥴러를 활용해서 Redis에 오늘의 퀴즈 정답을 가져와서 사용하는 전략으로 리팩토링하자.
    @Query("select count(q) > 0 from Quiz q where YEAR(q.postDate)=:year and MONTH(q.postDate)=:month and DAY(q.postDate)=:day " +
            "and q.answer = :answer")
    Boolean checkAnswer(@Param("year") int year,
                        @Param("month") int month,
                        @Param("day") int day,
                        @Param("answer") String answer);
}
