var csrfHeader_ = window.csrfHeader;
var page=0;

function getCsrfToken() {
    return document.querySelector('meta[name="csrf-token"]').getAttribute('content');
}

function getExamList() {
    const paragraph = document.getElementById("username");

    const username = paragraph.querySelector("span").innerText;
    console.log("Username:", username);

    fetch(`/candidate/${username}/exams/page/${page}`, {
    })
        .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
        .then(data => {
        console.log(data);
        data.forEach((item) => {
            //                console.log(item)

            const listContainer = document.getElementById("candidate-exam-container");
            const row = document.createElement("div");
            row.className = 'table-row';

            row.innerHTML= `
                    <div style="width:34%">${item.examName}</div>
                    <div style="width:22%">${item.examDate} / ${item.startTime}</div>
                    <div style="width:22%">${item.status}</div>
                    <div style="width:22%">
                    ${item.status=='ACTIVE'?`<button style=" width:80px; height:35px; font-size:16px; border-radius: 4px; border: 2px solid #636363;" class="start-btn" onclick="startTest(${item.examId})">Start</button>
                    `:`Close`}
                    </div>`;

            //                console.log(row);
            listContainer.append(row);
        });
        page++;
        // Process the data here
    })
        .catch(error => {
        console.error('There was a problem with the fetch operation:', error);
    });
}
getExamList();

document.getElementById("candidate-exam-container")
    .addEventListener("scroll", function () {
    if (this.scrollTop + this.clientHeight >= this.scrollHeight) {
        getExamList();
    }
});

function startTest(examId){
    console.log("Start exam clicked")
    console.log(examId);
    //    window.location.href = `/candidate/exam-test/${examId}`;

    fetch(`/candidate/exam-register-check/${examId}`, {
    })
        .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    }).then(data => {
        console.log(data);
        data.status?window.location.href = `/candidate/exam-test/${examId}`:alert(data.message);
        //        window.location.href = `/candidate/exam-test/${examId}`;
        // Process the data here
    }).catch(error => {
        console.error('There was a problem with the fetch operation:', error);
    });

}

document.getElementById("openFormBtn").addEventListener("click", function() {
    document.getElementById("popupForm").style.display = "block";
    document.getElementById("candidate-profile").classList.add("blur");
});

document.getElementById("closeFormBtn").addEventListener("click", function() {
    document.getElementById("popupForm").style.display = "none";
    document.getElementById("candidate-profile").classList.remove("blur");
});

window.onclick = function(event) {
    if (event.target == document.getElementById("popupForm")) {
        document.getElementById("popupForm").style.display = "none";
        document.getElementById("candidate-profile").classList.remove("blur");
    }
}

function participate() {
    console.log("clicked");

    const examId = document.getElementById('examId').value;
    const candidateId = document.getElementById('candidateId').value;
    const name = document.getElementById('name').value;
    const username = document.getElementById('user-name').value;
    const password = document.getElementById('password').value;

    // Clear previous warnings
    const warningMessage = document.getElementById('warning-message');
    warningMessage.style.display = 'none';
    warningMessage.textContent = '';


    if (!examId) {
        showError('Exam ID is required');
        return;
    }
    if (!candidateId) {
        showError('Candidate ID is required');
        return;
    }
    if (!name) {
        showError('Name is required');
        return;
    }
    if (!username) {
        showError('Username is required');
        return;
    }
    if (!password) {
        showError('Password is required');
        return;
    }


    const formData = {
        examId: examId,
        candidateId: candidateId,
        name: name,
        username: username,
        password: password
    };

    fetch('/candidate/exam/participate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': getCsrfToken()
        },
        body: JSON.stringify(formData)
    }).then(response => {
        if (!response.ok) {
            showError("Error: ",response.status);
        }
        return response.json();
    })
        .then(data => {
        console.log('Success:', data);
        if(data.status==true){
            document.getElementById("popupForm").style.display = "none";
            document.getElementById("candidate-profile").classList.remove("blur");
        }else{
            showError(data.message);
        }
    })
        .catch((error) => {
        console.error('Error:', error);
        const message = document.getElementById("warning-message");
        message.style.display = "block";
        message.innerHTML = "You are not authorized to perform this action.";
        return error;
    });
};

function showError(message) {
    const warningMessage = document.getElementById('warning-message');
    warningMessage.style.display = 'block';
    warningMessage.textContent = message;
}