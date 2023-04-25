package com.project.todayQuiz.quiz.service;

import com.project.todayQuiz.quiz.domain.Quiz;
import com.project.todayQuiz.quiz.domain.QuizRepository;
import com.project.todayQuiz.quiz.dto.request.QuizRequest;
import com.project.todayQuiz.quiz.dto.request.QuizUpdateRequest;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class QuizServiceImpl implements QuizService{

    private final QuizRepository quizRepository;

    @Override
    public Long postQuiz(QuizRequest quizRequest) {
        if (!quizRepository.existsQuizByPostDate(quizRequest.getYear(), quizRequest.getMonth(), quizRequest.getDay())) {
            LocalDateTime postDate = LocalDateTime.of(quizRequest.getYear(), quizRequest.getMonth(), quizRequest.getDay(),
                    quizRequest.getHour(), quizRequest.getMinute());
            Quiz quiz = Quiz.builder()
                    .question(quizRequest.getQuestion())
                    .answer(quizRequest.getAnswer())
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
        Optional<Quiz> quizByPostDate = quizRepository.findQuizByPostDate(quizDate.getYear(), quizDate.getMonthValue(), quizDate.getDayOfMonth());

        return quizByPostDate.isEmpty() ?
                new TodayQuizResponse("nothing", "nothing") : new TodayQuizResponse(quizByPostDate.get().getQuestion(), quizByPostDate.get().getPostDate());
    }

    @Transactional(readOnly = true)
    @Override
    public List<AdminQuizResponse> getQuizList(int page, int size) {
        PageRequest pageInfo = PageRequest.of(page - 1, size);
        List<Quiz> quizzes = quizRepository.findAll(pageInfo).getContent();

        return quizzes.stream().map(AdminQuizResponse::new).collect(Collectors.toList());
    }

    @Override
    public void updateQuiz(QuizUpdateRequest quizUpdateRequest) {
        Quiz quiz = quizRepository.findById(quizUpdateRequest.getId()).get();
        quiz.updateQuiz(quizUpdateRequest.getQuestion(), quizUpdateRequest.getAnswer(),
                LocalDateTime.of(
                        quizUpdateRequest.getYear(),
                        quizUpdateRequest.getMonth(),
                        quizUpdateRequest.getDay(),
                        quizUpdateRequest.getHour(),
                        quizUpdateRequest.getMinute()
                ));
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
