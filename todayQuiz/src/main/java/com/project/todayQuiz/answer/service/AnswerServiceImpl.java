package com.project.todayQuiz.answer.service;

import com.project.todayQuiz.answer.domain.Answer;
import com.project.todayQuiz.answer.domain.AnswerRepository;
import com.project.todayQuiz.quiz.domain.Quiz;
import com.project.todayQuiz.quiz.domain.QuizRepository;
import com.project.todayQuiz.user.domain.User;
import com.project.todayQuiz.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AnswerServiceImpl implements AnswerService{

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    @Override
    public Boolean checkAnswer(String submitAnswer, LocalDateTime todayDate, String email) {
        Quiz quiz = quizRepository.findQuizByPostDate(todayDate.getYear(), todayDate.getMonthValue(), todayDate.getDayOfMonth()).get();
        Boolean isCorrect = quiz.getAnswer().equals(submitAnswer);

        if (isCorrect) {
            User user = userRepository.findByEmail(email).get();
            Answer answer = new Answer(quiz, user);
            answerRepository.save(answer);
        }

        return isCorrect;
    }
}
