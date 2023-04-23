package com.project.todayQuiz.quiz.service;

import com.project.todayQuiz.quiz.domain.Quiz;
import com.project.todayQuiz.quiz.domain.QuizRepository;
import com.project.todayQuiz.quiz.dto.request.QuizPostRequest;
import com.project.todayQuiz.quiz.dto.response.AdminQuizResponse;
import com.project.todayQuiz.quiz.dto.response.TodayQuizResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class QuizServiceImpl implements QuizService{

    private final QuizRepository quizRepository;

    @Override
    public Long postQuiz(QuizPostRequest quizPostRequest) {
        if (!quizRepository.existsQuizByPostDate(quizPostRequest.getYear(), quizPostRequest.getMonth(), quizPostRequest.getDay())) {
            LocalDateTime postDate = LocalDateTime.of(quizPostRequest.getYear(), quizPostRequest.getMonth(), quizPostRequest.getDay(),
                    quizPostRequest.getHour(), quizPostRequest.getMinute());
            Quiz quiz = Quiz.builder()
                    .question(quizPostRequest.getQuestion())
                    .answer(quizPostRequest.getAnswer())
                    .postDate(postDate)
                    .createDate(LocalDateTime.now())
                    .build();
            return quizRepository.save(quiz).getId();
        }else {
            return -1L;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public TodayQuizResponse getTodayQuiz(LocalDate quizDate) {
        Quiz quiz = quizRepository.findQuizByPostDate(quizDate.getYear(), quizDate.getMonthValue(), quizDate.getDayOfMonth());

        return new TodayQuizResponse(quiz.getQuestion(), quiz.getPostDate());
    }

    @Transactional(readOnly = true)
    @Override
    public List<AdminQuizResponse> getQuizList(int page, int size) {
        PageRequest pageInfo = PageRequest.of(page - 1, size);
        List<Quiz> quizzes = quizRepository.findAll(pageInfo).getContent();

        return quizzes.stream().map(AdminQuizResponse::new).collect(Collectors.toList());
    }

    @Override
    public Long countQuiz() {
        return quizRepository.count();
    }

    @Override
    public void removeQuiz(Long id) {
        quizRepository.deleteById(id);
    }
}
