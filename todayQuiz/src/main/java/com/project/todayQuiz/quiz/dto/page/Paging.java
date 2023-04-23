package com.project.todayQuiz.quiz.dto.page;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Paging {
    private int startPage;
    private int endPage;

    private int currentPage;
    private int realEndPage;

    private boolean isPreviousPage;
    private boolean isNextPage;

    private final int displayPageNum = 10; // pagingSize

    public static Paging of(int page, Long totalBoard) {
        Paging paging = new Paging();

        paging.endPage = (int) (Math.ceil(page / (double) paging.displayPageNum)) * paging.displayPageNum;
        paging.startPage = paging.endPage - 10;

        paging.currentPage = page;
        paging.realEndPage = (int) (Math.ceil(totalBoard / (double) paging.displayPageNum));

        if (paging.realEndPage < paging.endPage) {
            paging.endPage = paging.realEndPage;
        }

        paging.isPreviousPage = (paging.currentPage - 1) > 0;
        paging.isNextPage = (paging.realEndPage - paging.currentPage) > 0;

        return paging;
    }
}
