package com.project.todayQuiz.answer.service;

import com.project.todayQuiz.answer.domain.Answer;
import com.project.todayQuiz.answer.domain.AnswerRedisDao;
import com.project.todayQuiz.answer.domain.AnswerRepository;
import com.project.todayQuiz.quiz.domain.Quiz;
import com.project.todayQuiz.quiz.domain.QuizRepository;
import com.project.todayQuiz.user.domain.User;
import com.project.todayQuiz.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@EnableScheduling
@Service
public class AnswerServiceImpl implements AnswerService{

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    private final AnswerRedisDao answerRedisDao;

    @Transactional
    @Override
    public Boolean checkAnswer(String submitAnswer, LocalDateTime todayDate, String email) {
        String todayAnswer = answerRedisDao.getAnswer();
        Boolean isCorrect = todayAnswer.equals(submitAnswer);

        if (isCorrect) {
            Quiz quiz = quizRepository.findQuizByPostDate(todayDate.getYear(), todayDate.getMonthValue(), todayDate.getDayOfMonth()).get();
            User user = userRepository.findByEmail(email).get();
            Answer answer = new Answer(quiz, user);
            answerRepository.save(answer);
        }

        return isCorrect;
    }

    @Scheduled(cron = "0 * * * * *") // 매 분 0초에 실행
    public void saveAnswerAtRedis() {
        LocalDateTime now = LocalDateTime.now();
        Optional<Quiz> todayQuiz = quizRepository.findQuizByPostDate(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        if (todayQuiz.isPresent()) {
            answerRedisDao.saveAnswer(todayQuiz.get().getAnswer(), Duration.ofMinutes(10));
        }else{
            answerRedisDao.saveAnswer(null, Duration.ofMinutes(10));
        }
    }
}
