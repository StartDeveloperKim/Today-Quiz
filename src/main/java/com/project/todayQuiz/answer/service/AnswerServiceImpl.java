package com.project.todayQuiz.answer.service;

import com.project.todayQuiz.answer.domain.Answer;
import com.project.todayQuiz.answer.domain.AnswerRedisDao;
import com.project.todayQuiz.answer.domain.AnswerRepository;
import com.project.todayQuiz.answer.dto.AnswerResponse;
import com.project.todayQuiz.answer.dto.AnswerState;
import com.project.todayQuiz.quiz.domain.Quiz;
import com.project.todayQuiz.quiz.domain.QuizRepository;
import com.project.todayQuiz.ranking.RankRedisDao;
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

import static com.project.todayQuiz.answer.dto.AnswerState.*;

@RequiredArgsConstructor
@EnableScheduling
@Service
public class AnswerServiceImpl implements AnswerService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    private final AnswerRedisDao answerRedisDao;
    private final RankRedisDao rankRedisDao;

    @Transactional
    @Override
    public AnswerResponse checkAnswer(String submitAnswer, LocalDateTime todayDate, String email) {
        String todayAnswer = answerRedisDao.getAnswer();
        boolean isCorrect = todayAnswer.equals(submitAnswer);
        int year = todayDate.getYear();
        int month = todayDate.getMonthValue();
        int day = todayDate.getDayOfMonth();

        if (isCorrect) {
            Quiz quiz = quizRepository.findQuizByPostDate(year, month, day).get();
            User user = userRepository.findByEmail(email).get();

            Boolean isExistAnswer = answerRepository.existsAnswerByUserAndQuiz(year, month, day, user.getId(), quiz.getId());
            if (isExistAnswer) {
                return getAnswerResponse(DUPLICATE, -1);
            } else {

                Answer answer = new Answer(quiz, user);
                answerRepository.save(answer);

                Integer count = rankRedisDao.getCount() + 1;
                rankRedisDao.saveCount(count, Duration.ofDays(1));

                return getAnswerResponse(CORRECT, count);
            }
        }
        return getAnswerResponse(WRONG, -1);
    }

    private AnswerResponse getAnswerResponse(AnswerState answerState, Integer count) {
        return new AnswerResponse(answerState.name(), answerState.getStateMessage(), count);
    }

    @Scheduled(cron = "0 * * * * *") // TODO :: 스케쥴러시간 조정 필요
    public void saveAnswerAtRedis() {
        LocalDateTime now = LocalDateTime.now();
        Optional<Quiz> todayQuiz = quizRepository.findQuizByPostDate(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        if (todayQuiz.isPresent()) {
            answerRedisDao.saveAnswer(todayQuiz.get().getAnswer(), Duration.ofMinutes(10));
        } else {
            answerRedisDao.saveAnswer("", Duration.ofMinutes(10));
        }
    }
}
