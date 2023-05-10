const signUpBtn = document.getElementById("signUp");
const signInBtn = document.getElementById("signIn");
const container = document.querySelector(".container");

signUpBtn.addEventListener("click", () => {
    container.classList.add("right-panel-active");
});
signInBtn.addEventListener("click", () => {
    container.classList.remove("right-panel-active");
});

function login() {
    const email = $('#email').val();
    const password = $('#password').val();

    if (email === '' || password === '') {
        alert("아이디와 비밀번호를 모두 입력해주세요");
    }else{
        $.ajax({
            url: '/login',
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(
                {email: email,
                    password: password}
            ),
            success: function (data) {
                console.log(data);
                if (data.isLogin) {
                    window.location.href = "/auth?token=" + data.securityToken;
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