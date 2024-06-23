let memoryButton = document.getElementsByClassName("memoryButton");
const memoryArray = Array.from(memoryButton);

let playButton = document.getElementById("playButton");
let score = document.getElementById("scoreCount");
let scoreCount = 0;
let play = false;
let stage = 1;
let answerIndex = 0;
let click = true;

let life = document.getElementById("lifeCount");
let lifeCount = 3;

let limitTime = document.getElementById("timeCount");
let timeUnit = document.getElementById("timeUnit");

let gameMessage = document.getElementById("gameMessage");

let questionArray = [];
let repeatArray = [];
let answerArray = [];

let logoClick = document.getElementById("logo-img");
let explainList = document.getElementById("explainList");

playButton.addEventListener("click", memory_test);

logoClick.addEventListener("click", function () {
    explainList.classList.toggle("active");
});

async function memory_test() {
    if (play) {
        resetGame();
    }
    play = !play;
    playButton.innerText = "REPLAY";
    await blinkGameProcess(blinkAllButton);
    await stagePlay(1);
}

async function stagePlay(stage) {
    await typeMessage(stage);
    await new Promise((resolve) =>
        setTimeout(() => {
            resolve();
        }, 500)
    );
    await question(stage);
    await typeStartSignal("시작합니다!");
    countTime(stage);
    answer();
}

// 게임 진행 통지 관련 함수

async function blinkGameProcess(blinkFunction) {
    const delays = [300, 500, 500];
    click = false;
    limitTime.classList.remove("countStart");
    timeUnit.classList.remove("countStart");

    for (let i = 0; i < 3; i++) {
        await new Promise((resolve) => setTimeout(resolve, delays[i]));
        await blinkFunction();
    }
    await new Promise((resolve) =>
        setTimeout(() => {
            resolve();
        }, 1300)
    );
}

function blinkAllButton() {
    return new Promise((resolve) => {
        memoryArray.map((el) => el.classList.add(`message`));
        setTimeout(() => {
            memoryArray.map((el) => el.classList.remove(`message`));
            resolve();
        }, 500);
    });
}

function blinkSuccess() {
    return new Promise((resolve) => {
        memoryArray.map((el) =>
            (Number(el.id) > 1 && Number(el.id) < 5) ||
            (Number(el.id) > 21 && Number(el.id) < 25) ||
            (Number(el.id) % 5 === 1 &&
                Number(el.id) !== 1 &&
                Number(el.id) !== 21) ||
            (Number(el.id) % 5 === 0 && Number(el.id) !== 5 && Number(el.id) !== 25)
                ? el.classList.add(`success`)
                : el
        );
        setTimeout(() => {
            memoryArray.map((el) => el.classList.remove(`success`));
            resolve();
        }, 500);
    });
}

function blinkFail() {
    return new Promise((resolve) => {
        memoryArray.map((el) =>
            Number(el.id) % 6 === 1 || Number(el.id) % 4 === 1
                ? el.classList.add(`fail`)
                : el
        );
        setTimeout(() => {
            memoryArray.map((el) => el.classList.remove(`fail`));
            resolve();
        }, 500);
    });
}

// 진행 과정 관련 함수

async function resetGame() {
    let guide = confirm(
        `
    Game Over

    최종 클리어 : ${stage - 1} 단계
    총합 스코어 : ${scoreCount} 점

    기록을 개인 기록에 저장하시겠습니까?
    `
    );
    if (guide) {
        // 1. 기록을 먼저 저장하고
        await addScore(stage, scoreCount)

        // 2. 랭킹 등재여부 분기
        let ranking = confirm(`최종 클리어 : ${stage - 1} 단계\n총합 스코어 : ${scoreCount}\n\n기록을 랭킹에 등재하시겠습니까?`);

        if (ranking) {
            await addRanking(stage, scoreCount);
        }
    }

    alert("게임을 초기화합니다");

    location.reload();
}

// function currentDate() {
//     let currentDate = new Date();
//     let year = currentDate.getFullYear();
//     let month = currentDate.getMonth() + 1;
//     let day = currentDate.getDate();
//
//     month = month < 10 ? "0" + month : month;
//     day = day < 10 ? "0" + day : day;
//
//     return year + "." + month + "." + day;
// }

async function countTime(stage) {
    limitTime.innerText = stage + 5;
    limitTime.classList.add("countStart");
    timeUnit.classList.add("countStart");

    await new Promise((resolve) => {
        let intervalId = setInterval(function () {
            if (
                parseInt(limitTime.innerText) === 0 ||
                compareArrays() ||
                JSON.stringify(questionArray) === JSON.stringify(answerArray)
            ) {
                clearInterval(intervalId);
                resolve();
            } else {
                limitTime.innerText--;
            }
        }, 1000);
    });
}

function typeMessage(stage) {
    return new Promise((resolve) => {
        let stageMessage = ` Stage ${stage} `;
        let index = 0;
        const typeNextCharacter = () => {
            if (index < stageMessage.length) {
                gameMessage.innerText += stageMessage[index];
                index++;
                setTimeout(typeNextCharacter, 100);
            } else {
                resolve();
            }
        };
        typeNextCharacter();
    });
}

function typeStartSignal(message) {
    return new Promise((resolve) => {
        let stageMessage = message;
        let index = 0;
        const typeNextCharacter = () => {
            if (index < stageMessage.length) {
                gameMessage.innerText += stageMessage[index];
                index++;
                setTimeout(typeNextCharacter, 100);
            } else {
                resolve();
            }
        };
        typeNextCharacter();
    });
}

function deleteMessage(standardIndex) {
    return new Promise((resolve) => {
        const messageLength = gameMessage.innerText.length;
        let index = messageLength - 1;
        const deleteNextCharacter = () => {
            if (index >= standardIndex) {
                gameMessage.innerText = gameMessage.innerText.slice(0, index);
                index--;
                setTimeout(deleteNextCharacter, 100);
            } else {
                resolve();
            }
        };
        deleteNextCharacter();
    });
}

// 스테이지 문제 제출 함수

async function question(stage) {
    click = false;
    for (let i = 0; i < stage; i++) {
        await blinkQuestion();
        await new Promise((resolve) => setTimeout(resolve, 250));
    }
    await new Promise((resolve) =>
        setTimeout(() => {
            click = true;
            resolve();
        }, stage * 150)
    );
}

async function repeatQuestion() {
    click = false;
    for (let i = 0; i < repeatArray.length; i++) {
        repeatArray[i].classList.add("message");
        await new Promise((resolve) => {
            setTimeout(() => {
                repeatArray[i].classList.remove("message");
                resolve();
            }, 500);
        });
        await new Promise((resolve) => setTimeout(resolve, 150));
    }
    await new Promise((resolve) =>
        setTimeout(() => {
            click = true;
            resolve();
        }, repeatArray.length * 150)
    );
}

function blinkQuestion() {
    return new Promise((resolve) => {
        let randomIndex = Math.floor(Math.random() * memoryButton.length);
        const randomButton = memoryArray[randomIndex];

        questionArray.push(Number(randomButton.id));
        repeatArray.push(randomButton);

        randomButton.classList.add("message");
        setTimeout(() => {
            randomButton.classList.remove("message");
            resolve(randomButton);
        }, 650);
    });
}

// 스테이지 정답 제출 함수

async function answer() {
    for (let i = 0; i < memoryArray.length; i++) {
        memoryArray[i].addEventListener("click", handleButtonClick);
        memoryArray[i].addEventListener("click", compareArrays);
    }
    setTimeout(async () => {
        if (
            limitTime.innerText === "0" &&
            answerArray.length !== questionArray.length
        ) {
            lifeCount--;
            click = false;
            if (lifeCount === 0) {
                life.innerText = " ";
            }
            deleteMessage(9);
            life.innerText = "❤️".repeat(lifeCount);
            await blinkGameProcess(blinkFail);

            if (lifeCount === 0) {
                resetGame();
            }

            await new Promise((resolve) => setTimeout(resolve, 150));
            answerArray = [];
            await repeatQuestion();
            await typeStartSignal("다시 도전!");
            limitTime.innerText = `${questionArray.length + 5}`;
            rightAnswer = false;
            correctAnswer = false;
            answerIndex = 0;
            countTime(stage);
            answer();
            return;
        }
    }, stage * 1000 + 5000);
}

function handleButtonClick(event) {
    if (click) {
        const clickedButtonId = Number(event.target.id);
        answerArray.push(clickedButtonId);

        checkAnswer();
    }
}

// 실시간 정답 검증을 위한 배열 비교
function compareArrays() {
    for (let i = 0; i < answerArray.length; i++) {
        if (answerArray[i] !== questionArray[i]) {
            return true;
        }
    }
    if (parseInt(limitTime.innerText) === 0) {
        return true;
    }
}

async function checkAnswer() {
    if (answerIndex < questionArray.length) {
        if (compareArrays()) {
            lifeCount--;
            click = false;
            if (lifeCount === 0) {
                life.innerText = " ";
            }
            deleteMessage(9);
            life.innerText = "❤️".repeat(lifeCount);
            await blinkGameProcess(blinkFail);

            if (lifeCount === 0) {
                resetGame();
            }

            await new Promise((resolve) => setTimeout(resolve, 150));
            answerArray = [];
            await repeatQuestion();
            await typeStartSignal("다시 도전!");
            limitTime.innerText = `${questionArray.length + 5}`;
            rightAnswer = false;
            correctAnswer = false;
            answerIndex = 0;
            countTime(stage);
            answer();
            return;
        }

        answerIndex++;
    }

    if (answerIndex === questionArray.length) {
        if (JSON.stringify(questionArray) === JSON.stringify(answerArray)) {
            questionArray = [];
            repeatArray = [];
            answerArray = [];
            let timeGap = parseInt(limitTime.innerText);
            scoreCount += 15 * timeGap;
            score.innerText = String(scoreCount).padStart(5, "0");

            deleteMessage(0);
            stage++;
            await blinkGameProcess(blinkSuccess);

            rightAnswer = false;
            correctAnswer = false;
            answerIndex = 0;
            stagePlay(stage);
        }
    }
}

// 점수 기록 관련 함수

async function addScore(stage, scoreCount) {
    const formData = {
        level: stage - 1,
        gameScore: scoreCount
    }

    try {
        const response = await fetch("/mini/game/record", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        console.log("서버에서 받은 데이터:", data);
        alert("기록이 저장됐습니다.");
    } catch (error) {
        console.error("기록 저장 실패:", error);
        alert("기록 저장에 실패했습니다. 게임을 초기화합니다.");
        window.location.href = "/memory_test"; // 홈페이지로 이동
    }
}


async function addRanking(stage, scoreCount) {
    const formData = {
        level: stage - 1,
        gameScore: scoreCount
    };

    try {
        const response = await fetch("/mini/game/ranking", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        console.log("서버에서 받은 데이터:", data);
        alert("랭킹에 등재됐습니다.");
    } catch (error) {
        console.error("랭킹 등재 실패:", error);
        alert("랭킹 등재에 실패했습니다. 게임을 초기화합니다.");
        window.location.href = "/memory_test"; // 홈페이지로 이동
    }
}

// window.onload = function () {
//     let savedScores = JSON.parse(localStorage.getItem("scores")) || [];
//     let scoreList = document.getElementById("scoreList");
//
//     savedScores.forEach((score) => {
//         let scoreItem = document.createElement("li");
//         scoreItem.id = "scoreItem";
//
//         let scoreName = document.createElement("div");
//         scoreName.innerText = `${score.name}`;
//         scoreName.className = "scoreProperty";
//
//         let scoreDate = document.createElement("div");
//         scoreDate.innerText = `${score.date}`;
//         scoreDate.className = "scoreDate";
//
//         let scoreStage = document.createElement("div");
//         scoreStage.innerText = `${
//             score.stage === 1 ? `없음` : `${score.stage - 1} 단계`
//         }`;
//         scoreStage.className = "scoreProperty";
//
//         let scoreAmount = document.createElement("div");
//         scoreAmount.innerText = `${score.scoreCount} 점`;
//         scoreAmount.className = "scoreProperty";
//
//         let deleteButton = document.createElement("button");
//         deleteButton.innerText = "x";
//         deleteButton.className = "deleteButton";
//         deleteButton.addEventListener("click", function () {
//             scoreList.removeChild(scoreItem);
//             let updatedScores = savedScores.filter((s) => s.name !== score.name);
//             savedScores = updatedScores;
//             localStorage.setItem("scores", JSON.stringify(savedScores));
//         });
//
//         scoreItem.appendChild(scoreName);
//         scoreItem.appendChild(scoreDate);
//         scoreItem.appendChild(scoreStage);
//         scoreItem.appendChild(scoreAmount);
//         scoreItem.appendChild(deleteButton);
//
//         scoreList.appendChild(scoreItem);
//     });
// };