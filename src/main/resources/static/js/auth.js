const container = document.getElementById('container');
const registerBtn = document.getElementById('register');
const loginBtn = document.getElementById('login');

registerBtn.addEventListener('click', () => {
    container.classList.add("active");
    console.log("test")
});

loginBtn.addEventListener('click', () => {
    container.classList.remove("active");
});

// 회원가입 관련 로직

document.getElementById("form-container-signup-box").addEventListener("submit", function (event) {
    event.preventDefault(); // 기본 동작 중지

    const password = document.getElementById("signup-password").value;
    const checkPassword = document.getElementById("signup-checkPassword").value;

    // 비밀번호와 비밀번호 확인이 일치하는지 확인
    if (password !== checkPassword) {
        alert("비밀번호가 일치하지 않습니다. 다시 확인해주세요.");
        return; // 함수 종료
    }

    const formData = {
        username: document.getElementById("signup-nickname").value,
        email: document.getElementById("signup-email").value,
        password: password
    };

    console.log(formData)

    fetch("/mini/user/signup", { // 서버에 POST 요청 보내기
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); // JSON 형식으로 변환된 응답을 반환
        })
        .then(data => {
            console.log("서버에서 받은 데이터:", data); // "회원가입 성공!"과 같은 메시지 출력
            alert("회원가입에 성공했습니다.");
            window.location.href = "/"; // 홈페이지로 이동
        })
        .catch(error => {
            alert("회원가입에 실패했습니다. 입력 정보를 확인해주세요.");
            console.error("회원가입 실패:", error);
        });
});

// 로그인 관련 로직

document.getElementById("form-container-box").addEventListener("submit", function (event) {
    event.preventDefault(); // 기본 동작 중지
    const formData = {
        email: document.getElementById("login-email").value,
        password: document.getElementById("login-password").value,
    };

    console.log(formData)

    fetch("/mini/user/login", { // 서버에 POST 요청 보내기
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            console.log(response)
            if (response.status === 200) {
                // 로그인 성공 시 알림창 표시
                alert("로그인에 성공했습니다.");
                window.location.href = "/game"; // 홈페이지로 이동
            } else {
                // 회원가입 실패 시 알림창 표시
                alert("로그인 실패. 입력 정보를 확인해주세요.");
                console.error("로그인 실패:", response.message);
            }
        })
        .catch(error => {
            // 오류 발생 시 알림창 표시
            alert("오류 발생. 다시 시도해주세요.");
            console.error("오류 발생:", error);
        });
});