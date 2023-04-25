// 시간은 퀴즈 날짜를 파라미터로 넘겨 사용하자.
function showCurrentTime(quizDate) {
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
    const quizDate = document.getElementById("quizTime").getAttribute("data-mydata");
    console.log(quizDate);
    setInterval(showCurrentTime(quizDate), 1000);
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
        console.log('전송할 데이터:', inputText);
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