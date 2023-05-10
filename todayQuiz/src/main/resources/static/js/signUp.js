function duplicateEmail() {
    const email = $("#email").val();
    checkDuplicate("/api/email?email=" + email, "email");
}

function duplicateNickname() {
    const nickname = $("#nickname").val();
    checkDuplicate("/api/nickname?nickname=" + nickname, "nickname");
}

function checkDuplicate(url, type) {
    console.log(url, type);
    $.ajax({
        url: url,
        type: 'GET',
        success: function (data) {
            if (type === 'email') {
                if (data.isDuplicated){
                    $('#email').removeClass('is-valid').addClass('is-invalid');
                    $('#error-email').show();
                }else{
                    $('#email').removeClass('is-invalid').addClass('is-valid');
                    $('#error-email').hide();
                }
            }

            if (type === 'nickname') {
                if (data.isDuplicated && type === "nickname") {
                    $('#nickname').removeClass('is-valid').addClass('is-invalid');
                    $('#error-nickname').show();
                }else{
                    $('#nickname').removeClass('is-invalid').addClass('is-valid');
                    $('#error-nickname').hide();
                }
            }
        },
        error: function (xhr, status, error) {
            console.error(error);
        }
    });
}

function signUp() {
    const email = $('#email').val();
    const password = $('#password').val();
    const nickname = $('#nickname').val();

    if (email === '' || password === '' || nickname === '') {
        alert("이메일, 비밀번호, 닉네임을 모두 등록해주세요");
    }else{
        $.ajax({
            url: '/signup',
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(
                {email: email,
                password: password,
                nickname: nickname}
            ),
            success: function (data) {
                if (data.isSignUp) {
                    alert(data.message);
                    window.location.href = "/login";
                } else {
                    alert(data.message);
                    window.location.href = "/signup";
                }
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });
    }
}