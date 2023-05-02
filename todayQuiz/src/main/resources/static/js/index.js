function showCurrentTime() {
    const quizDate = document.getElementById("quizTime").getAttribute("data-mydata");
    let remainDiv = document.getElementById("remainingTime");
    if (quizDate === 'nothing') {
        remainDiv.innerHTML = "오늘은 퀴즈가 없습니다!!!";
    }else{
        const targetTime = new Date(quizDate); // 특정 시간 설정
        const now = new Date(); // 현재 시간 가져오기

        const text_input = document.getElementById('text-input');
        const send_button = document.getElementById('send-button');

        const timeDifference = targetTime - now; // 남은 시간 계산 (밀리초 단위)
        if (timeDifference <= 0) {
            remainDiv.innerHTML = "정답을 입력해주세요!!";
            text_input.disabled = false;
            text_input.placeholder = "정답을 입력하고 엔터를 누르거나 아래의 버튼을 눌러주세요"
            send_button.disabled = false;
        }else{
            const hours = Math.floor(timeDifference / (1000 * 60 * 60)); // 시간 계산
            const minutes = Math.floor((timeDifference % (1000 * 60 * 60)) / (1000 * 60)); // 분 계산
            const seconds = Math.floor((timeDifference % (1000 * 60)) / 1000); // 초 계산

            document.getElementById("remainingTime").innerHTML = "남은 시간: " + hours + "시간 " + minutes + "분 " + seconds + "초";
            text_input.disabled = true;
            text_input.placeholder = "아직은 정답 입력시간이 아닙니다."
            send_button.disabled = true;
        }
    }

}

window.onload = function () {
    setInterval(showCurrentTime, 1000);
}

function handleKeyPress(event) {
    if (event.keyCode === 13) {
        // input 태그의 값이 빈 값인 경우 오류 메시지 표시
        if (event.target.value.trim() === '') {
            document.getElementById('error-message').style.display = 'block';
        } else {
            document.getElementById('error-message').style.display = 'none';
            sendData();
        }
    }
}

function sendData() {
    const text_input = document.getElementById('text-input');
    const inputText = text_input.value;

    text_input.value = ""
    if(inputText === ""){
        document.getElementById('error-message').style.display = 'block';
    }else{
        // 서버로 데이터 전송하는 로직 작성
        // 예시로 콘솔에 입력된 데이터 출력
        document.getElementById('error-message').style.display = 'none';

        $.ajax({
            url: '/answer', // 요청 보낼 URL
            type: 'POST', // 요청 메소드
            contentType: 'application/json', // 응답 데이터 타입 (JSON 형식으로 응답을 기대하는 경우)
            data: JSON.stringify({answer: inputText}),
            dataType : 'json',
            success: function(data) {
                let resultStr = data.message
                if (data.state === "CORRECT") {
                    resultStr += data.todayRank + "순위로 정답을 맞추셨습니다!!";
                }
                // $('.alert').remove(); // Alert 컴포넌트가 이미 존재한다면 제거
                // $('body').append('<div class="alert alert-success" role="alert">Ajax 통신이 성공적으로 완료되었습니다!</div>');
                alert(resultStr);
            },
            error: function(xhr, status, error) {
                // 요청이 실패했을 때 실행되는 콜백 함수
                alert("에러에러");
                console.log('에러:', xhr.response);
            }
        });
    }
}

function logout() {
    $.ajax({
        url: "/api/logout",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({}),
        success: function (data) {
            window.location.href = '/';
        },
        error: function (xhr, textStatus, errorThrown) {
            console.error('Logout failed : ', xhr);
        }
    });
}