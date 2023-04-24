function postQuiz() {
    let dateInput = document.querySelector("#datetime-local").value;
    let questionInput = document.getElementById("question");
    let answerInput = document.getElementById("answer");

    let year = dateInput.substring(0, 4);
    let month = dateInput.substring(5, 7);
    let day = dateInput.substring(8, 10);
    let hour = dateInput.substring(11, 13);
    let minute = dateInput.substring(14, 16);

    const question = questionInput.value;
    const answer = answerInput.value;

    $.ajax({
        url: '/quiz', // 요청 보낼 URL
        type: 'POST', // 요청 메소드
        dataType: 'json', // 응답 데이터 타입 (JSON 형식으로 응답을 기대하는 경우)
        data: {
            question: question, // 보낼 데이터 (key-value 형태로 전달)
            answer: answer,
            year: year,
            month: month,
            day: day,
            hour: hour,
            minute: minute
        },
        success: function(response) {
            alert("퀴즈가 등록되었습니다.");
            window.location.href = "/admin/1";
        },
        error: function(xhr, status, error) {
            // 요청이 실패했을 때 실행되는 콜백 함수
            alert('요청이 실패하였습니다.');
            console.log('에러:', error);
        }
    });
}