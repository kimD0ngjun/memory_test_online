// 기록 삭제
document.addEventListener('DOMContentLoaded', (event) => {
    document.querySelectorAll('.deleteButton').forEach(button => {
        button.addEventListener('click', () => {
            const parentLi = button.closest('li');
            const hiddenElement = parentLi.querySelector('.hidden');
            const date = parentLi.querySelector('.date').textContent
            const id = hiddenElement.textContent;
            console.log('삭제 아이디:', id);
            console.log('삭제하려는 기록 날짜:', date)

            const check = confirm(`삭제하려는 기록 날짜: ${date}\n\n기록을 삭제하시겠습니까?\n(이미 랭킹에 등재됐으면 기록만 삭제되고 랭킹은 삭제되지 않습니다.)`);

            if (check) {
                fetch(`/mini/game/record?id=${id}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok ' + response.statusText);
                        }
                        return response.json();
                    })
                    .then(data => {
                        console.log('삭제 성공:', data);
                        parentLi.remove();
                        alert(data.message); // Show success message
                    })
                    .catch(error => {
                        console.error('삭제 에러:', error);
                        alert('삭제가 되지 않았습니다. 관리자에게 문의하세요.');
                    });
            }
        });
    });
});

// 닉네임 변경
document.querySelector('#name-button').addEventListener('click', () => {
    // 사용자로부터 새로운 닉네임을 입력받음
    const newUsername = prompt('새로운 닉네임을 입력하세요.\n(이미 랭킹에 등재된 기록의 닉네임은 변하지 않습니다)');

    // 닉네임이 입력되지 않았을 경우 처리
    if (!newUsername || newUsername.trim().length === 0) {
        alert('닉네임 길이는 최소 1자 이상이며, 공백만으로는 작성할 수 없습니다.');
        return;
    }

    // 서버에 PUT 요청 보내기
    fetch('/mini/user/update', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username: newUsername })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            alert(data.message);  // 서버로부터의 응답 메시지 출력
            window.location.reload();  // 페이지 새로고침
        })
        .catch(error => {
            console.error('Error:', error);
            alert('닉네임 변경에 실패했습니다. 다시 시도해주세요.');
        });
});