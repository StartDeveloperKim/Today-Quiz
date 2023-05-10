let emailTimeoutId;
let nicknameTimeoutId;

$("#signUp_email").on("input", () => {
    clearTimeout(emailTimeoutId);
    emailTimeoutId = setTimeout(duplicateEmail, 2000);
})

$("#signUp_nickname").on("input", () => {
    clearTimeout(nicknameTimeoutId);
    nicknameTimeoutId = setTimeout(duplicateNickname, 2000);
})

function duplicateEmail() {
    const email = document.getElementById("signUp_email").value;
    checkDuplicate("/api/email?email=" + email, "email");
}

function duplicateNickname() {
    const nickname = document.getElementById("signUp_nickname").value;
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
                    $('#error-email').show();
                    $('#good-email').hide();
                }else{
                    $('#error-email').hide();
                    $('#good-email').show();
                }
            }

            if (type === 'nickname') {
                if (data.isDuplicated && type === "nickname") {
                    $('#nickname').removeClass('is-valid').addClass('is-invalid');
                    $('#error-nickname').show();
                    $('#good-nickname').hide();
                }else{
                    $('#nickname').removeClass('is-invalid').addClass('is-valid');
                    $('#error-nickname').hide();
                    $('#good-nickname').show();
                }
            }
        },
        error: function (xhr, status, error) {
            console.error(error);
        }
    });
}

function signUp() {
    const email = $('#signUp_email').val();
    const password = $('#signUp_password').val();
    const nickname = $('#signUp_nickname').val();

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
                }
            },
            error: function (xhr, status, error) {
                console.error(error);
            }
        });
    }
}