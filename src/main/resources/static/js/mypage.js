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