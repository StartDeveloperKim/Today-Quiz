function getRequestData(quizId) {
    let dateInput = getElement("postDatetime").value;
    let questionInput = getElement("question");
    let answerInput = getElement("answer");

    let year = dateInput.substring(0, 4);
    let month = dateInput.substring(5, 7);
    let day = dateInput.substring(8, 10);
    let hour = dateInput.substring(11, 13);
    let minute = dateInput.substring(14, 16);

    const question = questionInput.value;
    const answer = answerInput.value;

    if (question === '' || answer ==='' || dateInput === ''){
        alert("모든 칸을 입력해주세요");
        return '';
    }

    const requestData = {
        question: question, // 보낼 데이터 (key-value 형태로 전달)
        answer: answer,
        year: year,
        month: month,
        day: day,
        hour: hour,
        minute: minute
    }

    if (quizId !== '') {
        requestData.id = quizId;
    }
    return requestData;
}

function postQuiz() {
    const requestData = getRequestData('');
    if (requestData !== '') {
        $.ajax({
            url: '/quiz', // 요청 보낼 URL
            type: 'POST', // 요청 메소드
            contentType: 'application/json', // 응답 데이터 타입 (JSON 형식으로 응답을 기대하는 경우)
            data: JSON.stringify(requestData),
            dataType : 'json',
            success: function(response) {
                alert("퀴즈가 등록되었습니다.");
                window.location.href = "/admin/1";
            },
            error: function(xhr, status, error) {
                // 요청이 실패했을 때 실행되는 콜백 함수
                alert("중복된 날짜의 퀴즈는 등록하지 못합니다.");
                console.log('에러:', xhr.response);
            }
        });
    }
}

function getElement(id) {
    return document.getElementById(id);
}

function addButton(quizId) {
    if (confirm("수정하시겠습니까?")) {
        let dateInput = getElement("postDatetime");
        let questionInput = getElement("question");
        let answerInput = getElement("answer");

        const question = getElement("question" + quizId).textContent;
        const answer = getElement("answer" + quizId).textContent;
        const postDate = getElement("postDate" + quizId).textContent;

        questionInput.value = question;
        answerInput.value = answer;
        dateInput.value = postDate;

        let postBtn = getElement('postBtn');
        postBtn.innerText = '수정';
        postBtn.classList.replace("btn-primary", "btn-success");
        postBtn.onclick = ()=>{
            updateQuiz(quizId);
        }

        const formDiv = getElement("inputForm");
        let removeButton = document.createElement("button");
        removeButton.innerText = "삭제"
        removeButton.classList.add('btn');
        removeButton.classList.add('btn-danger');
        removeButton.onclick = () => {
            remove(quizId);
        }
        formDiv.appendChild(removeButton);
    }
}

function updateQuiz(id) {
    const requestData = getRequestData(id);

    if (requestData !== '') {
        $.ajax({
            url: '/quiz', // 요청 보낼 URL
            type: 'PATCH', // 요청 메소드
            contentType: 'application/json', // 응답 데이터 타입 (JSON 형식으로 응답을 기대하는 경우)
            data: JSON.stringify(requestData),
            dataType : 'json',
            success: function(response) {
                alert("수정이 완료되었습니다.");
                window.location.href = "/admin/1";
            },
            error: function(xhr, status, error) {
                // 요청이 실패했을 때 실행되는 콜백 함수
                alert('요청이 실패하였습니다.');
                console.log('에러:', error);
            }
        });
    }
}

function remove(id) {
    if (confirm("삭제하시겠습니까?")) {
        $.ajax({
            url: '/quiz/'+id, // DELETE 요청을 보낼 URL
            type: 'DELETE', // HTTP 요청의 메소드를 DELETE로 지정
            success: function(data) {
                alert("삭제가 완료되었습니다.");
                window.location.href = "/admin/1";
            },
            error: function(xhr, textStatus, errorThrown) {
                // 요청이 실패한 경우의 처리
            }
        });
    }
}
