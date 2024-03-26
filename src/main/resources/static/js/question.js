var csrfHeader = document.getElementById("csrfHeader").getAttribute("data-th-value");
var csrfToken = document.getElementById("csrfToken").getAttribute("data-th-value");

var csrfToken_ = window.csrfToken;
var csrfHeader_ = window.csrfHeader;

var block_b= document.getElementById("question_b");
var warning = document.getElementById("warning_exam_search");
var q_save = document.getElementById("question_save");
var opt_save = document.getElementById("option_add_b");
var examName = document.getElementById("exam_name_a");
var schedule = document.getElementById("exam_schedule");
var examStatus = document.getElementById("exam_status");
var marks = document.getElementById("exam_marks");
var eId="";
var qId="";
var options = [];

if(!eId){
    block_b.style.display="none";
    q_save.style.display = "none";
    opt_save.style.display = "none";
}

function searchExam() {
    const form = document.getElementById('search_form');
    const formData = new FormData(form);
    fetch('/admin/exam-data', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
        console.log('Received Exam id:', data);
        eId = data.examId;

        warning.style.display = "none";
        q_save.style.display = "inline-block";
        block_b.style.display="flex";

        var x= data.duration;
        examName.innerHTML = `${data.examName} <br> id: ${data.examId}`;
        schedule.textContent =`${data.examDate} /${data.startTime} /${x}min`;
        examStatus.textContent = data.status;
        marks.textContent = data.totalMarks;

        getQuestions(eId);

//        console.log(data)

    }).catch(error => {
        warning.style.display = "block";
        block_b.style.display="none";
        q_save.style.display = "none";
        getQuestions(null);
        console.error('Error:'+ error)
    });
}
function submitForm() {
    console.log("from submit form...")
    const form = document.getElementById('question_form');
    const formData = new FormData(form);
    formData.append('examId', eId);
    if (eId == null) {
        console.log("Exam Id not found!...");
        return;
    }
    fetch('/admin/question', {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken,
        },
        body: formData,
    })
        .then(response => response.json())
        .then(data => {
//        console.log('Received Question ID:', data);
        qId = data;
        console.log("qId:"+qId);
        if(qId!=null){
            opt_save.style.display = "inline-block";
        }
        // Storing the ID in a JavaScript variable
        // Perform actions with the ID as needed
        //            console.log(data);
        alert("Question Saved successfully!");
    })
        .catch(error => {
        opt_save.style.display = "none";
        console.error('Error:', error)
    });
}

function previewFile() {
    const preview = document.getElementById('previewImage');
    const fileInput = document.getElementById('imageInput');
    const file = fileInput.files[0];
    const reader = new FileReader();

    const fileWarning = document.getElementById('fileWarning');

    const validExtensions = ['jpg', 'jpeg', 'png'];
    const fileExtension = file.name.split('.').pop().toLowerCase();

    if (!validExtensions.includes(fileExtension)) {
        preview.src = '';
        fileInput.value = null;
        fileWarning.style.display = 'block';
        return;
    }

    reader.onloadend = function () {
        preview.src = reader.result;
        fileWarning.style.display = 'none';
    };

    if (file) {
        reader.readAsDataURL(file);
    } else {
        preview.src = '';
    }
}

function addOptionToList() {
    var optionText = document.getElementById("optionInput").value;
    var optionNumber = document.getElementById("optionNumber").value;
    if (optionText.trim() === "" || optionNumber.trim() === "") {
        alert("Please enter both option text and number");
        return;
    }
    var optionsContainer = document.getElementById("optionsContainer");
    var newOption = document.createElement("div");
    newOption.className = "q_option";
    newOption.innerHTML = `
                 <p
            class="q_o_a"
            style="display: flex; justify-content: center; align-items: center"
          >
          ${optionNumber}
          </p>
          <p
            class="q_o_b"
            style="display: flex; align-items: center; padding-left: 5px"
          >
          ${optionText}
          </p>
          <div class="q_o_c" onclick="deleteOption(this)">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="20"
              height="20"
              viewBox="0 0 20 20"
              fill="none"
              style="cursor: pointer"
            >
              <path
                d="M3.125 5.00003H1.875C1.70924 5.00003 1.55027 4.93418 1.43306 4.81697C1.31585 4.69976 1.25 4.54079 1.25 4.37503C1.25 4.20927 1.31585 4.0503 1.43306 3.93309C1.55027 3.81588 1.70924 3.75003 1.875 3.75003H6.875V1.87378C6.875 1.70802 6.94085 1.54905 7.05806 1.43184C7.17527 1.31463 7.33424 1.24878 7.5 1.24878H12.5C12.6658 1.24878 12.8247 1.31463 12.9419 1.43184C13.0592 1.54905 13.125 1.70802 13.125 1.87378V3.75003H18.125C18.2908 3.75003 18.4497 3.81588 18.5669 3.93309C18.6842 4.0503 18.75 4.20927 18.75 4.37503C18.75 4.54079 18.6842 4.69976 18.5669 4.81697C18.4497 4.93418 18.2908 5.00003 18.125 5.00003H16.875V18.125C16.875 18.2908 16.8092 18.4498 16.6919 18.567C16.5747 18.6842 16.4158 18.75 16.25 18.75H3.75C3.58424 18.75 3.42527 18.6842 3.30806 18.567C3.19085 18.4498 3.125 18.2908 3.125 18.125V5.00003ZM11.875 3.75003V2.50003H8.125V3.75003H11.875ZM4.375 17.5H15.625V5.00003H4.375V17.5ZM8.125 15C7.95924 15 7.80027 14.9342 7.68306 14.817C7.56585 14.6998 7.5 14.5408 7.5 14.375V8.12503C7.5 7.95927 7.56585 7.8003 7.68306 7.68309C7.80027 7.56588 7.95924 7.50003 8.125 7.50003C8.29076 7.50003 8.44973 7.56588 8.56694 7.68309C8.68415 7.8003 8.75 7.95927 8.75 8.12503V14.375C8.75 14.5408 8.68415 14.6998 8.56694 14.817C8.44973 14.9342 8.29076 15 8.125 15ZM11.875 15C11.7092 15 11.5503 14.9342 11.4331 14.817C11.3158 14.6998 11.25 14.5408 11.25 14.375V8.12503C11.25 7.95927 11.3158 7.8003 11.4331 7.68309C11.5503 7.56588 11.7092 7.50003 11.875 7.50003C12.0408 7.50003 12.1997 7.56588 12.3169 7.68309C12.4342 7.8003 12.5 7.95927 12.5 8.12503V14.375C12.5 14.5408 12.4342 14.6998 12.3169 14.817C12.1997 14.9342 12.0408 15 11.875 15Z"
                fill="black"
              />
            </svg>
          </div>
            `;
    optionsContainer.appendChild(newOption);
    options.push({ number: optionNumber, text: optionText });
    document.getElementById("optionInput").value = "";
    document.getElementById("optionNumber").value = "";
}

function deleteOption(element) {
    var optionContainer = element.parentNode;
    var optionIndex = Array.prototype.indexOf.call(
        optionContainer.parentNode.children,
        optionContainer
    );
    optionContainer.parentNode.removeChild(optionContainer);
    options.splice(optionIndex, 1);
}

function sendOptionsToAPI() {
    console.log('from option send on api...')
    console.log(options);
    console.log("qID:" + qId);

    var formData = new FormData();
    formData.append("options", JSON.stringify(options)); //append  json as string
    formData.append("qId", qId);
    formData.append("eId", eId);
    fetch("/admin/option-add", {
        method: "POST",
        headers: {
            [csrfHeader]: csrfToken,
        },
        body: formData,
    }).then((response) => {
                    console.log("----", response);
        return response.json();
    }).then((data) => {
        alert("Option Saved successfully!");
        console.log("Option save"+data);
    }).catch((error) => console.error("Error:", error));

    getQuestions(eId);

    var optionsContainer = document.getElementById("optionsContainer");
    optionsContainer.innerHTML="";
    options=[];
}

function getQuestions(i) {
     console.log("get question list...")
    var examId = i;
    var count=0;

    if(i==null){
        console.log()
        var questionContainer = document.getElementById("questionContainer");
        questionContainer.innerHTML = '';
        return;
    }

    fetch("/admin/question-list/" + examId)
        .then(response => response.json())
        .then(data => {
        console.log(data);
        var questionContainer = document.getElementById("questionContainer");
        questionContainer.innerHTML="";
        data.forEach((data)=>{
            count+=1;
            const div = document.createElement("div");
            div.setAttribute("class","q_one");
            div.innerHTML = `
                                <p class="q_one_a">${count}</p>
                                <p class="q_one_b" >${data.questionText}</p>
                                <div class="q_one_c">
                                    <!-- view----------- -->
                                    <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            width="20"
                                            height="20"
                                            viewBox="0 0 20 20"
                                            fill="none"
                                            style="cursor: pointer"
                                    >
                                        <path
                                                d="M10 12.5C11.3807 12.5 12.5 11.3807 12.5 10C12.5 8.61929 11.3807 7.5 10 7.5C8.61929 7.5 7.5 8.61929 7.5 10C7.5 11.3807 8.61929 12.5 10 12.5Z"
                                                fill="black"
                                        />
                                        <path
                                                d="M19.3375 9.7875C18.6024 7.88603 17.3262 6.24164 15.6667 5.05755C14.0072 3.87347 12.0372 3.20161 9.99998 3.125C7.9628 3.20161 5.99272 3.87347 4.33323 5.05755C2.67374 6.24164 1.39758 7.88603 0.662478 9.7875C0.612833 9.92482 0.612833 10.0752 0.662478 10.2125C1.39758 12.114 2.67374 13.7584 4.33323 14.9424C5.99272 16.1265 7.9628 16.7984 9.99998 16.875C12.0372 16.7984 14.0072 16.1265 15.6667 14.9424C17.3262 13.7584 18.6024 12.114 19.3375 10.2125C19.3871 10.0752 19.3871 9.92482 19.3375 9.7875ZM9.99998 14.0625C9.19649 14.0625 8.41105 13.8242 7.74298 13.3778C7.0749 12.9315 6.5542 12.297 6.24672 11.5547C5.93924 10.8123 5.85879 9.99549 6.01554 9.20745C6.17229 8.4194 6.55921 7.69553 7.12736 7.12738C7.69551 6.55923 8.41938 6.17231 9.20742 6.01556C9.99547 5.85881 10.8123 5.93926 11.5546 6.24674C12.297 6.55422 12.9314 7.07492 13.3778 7.743C13.8242 8.41107 14.0625 9.19651 14.0625 10C14.0608 11.0769 13.6323 12.1093 12.8708 12.8708C12.1093 13.6323 11.0769 14.0608 9.99998 14.0625Z"
                                                fill="black"
                                        />
                                    </svg>
                                    <!-- edit------------- -->
                                    <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            width="20"
                                            height="20"
                                            viewBox="0 0 20 20"
                                            fill="none"
                                            style="cursor: pointer"
                                    >
                                        <path
                                                d="M13.7282 4.50677L15.4932 6.27093M14.8632 2.9526L10.0907 7.7251C9.84408 7.97135 9.6759 8.28509 9.60734 8.62677L9.1665 10.8334L11.3732 10.3918C11.7148 10.3234 12.0282 10.1559 12.2748 9.90927L17.0473 5.13677C17.1908 4.99335 17.3045 4.82309 17.3821 4.63571C17.4597 4.44833 17.4997 4.2475 17.4997 4.04468C17.4997 3.84186 17.4597 3.64103 17.3821 3.45365C17.3045 3.26627 17.1908 3.09601 17.0473 2.9526C16.9039 2.80919 16.7337 2.69542 16.5463 2.61781C16.3589 2.54019 16.1581 2.50024 15.9553 2.50024C15.7524 2.50024 15.5516 2.54019 15.3642 2.61781C15.1768 2.69542 15.0066 2.80919 14.8632 2.9526Z"
                                                stroke="black"
                                                stroke-linecap="round"
                                                stroke-linejoin="round"
                                        />
                                        <path
                                                d="M15.8333 12.5001V15.0001C15.8333 15.4421 15.6577 15.866 15.3451 16.1786C15.0325 16.4912 14.6086 16.6667 14.1666 16.6667H4.99992C4.55789 16.6667 4.13397 16.4912 3.82141 16.1786C3.50885 15.866 3.33325 15.4421 3.33325 15.0001V5.83341C3.33325 5.39139 3.50885 4.96746 3.82141 4.6549C4.13397 4.34234 4.55789 4.16675 4.99992 4.16675H7.49992"
                                                stroke="black"
                                                stroke-linecap="round"
                                                stroke-linejoin="round"
                                        />
                                    </svg>

                                    <!-- delete----------------------- -->
                                    <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            width="18"
                                            height="18"
                                            viewBox="0 0 18 18"
                                            fill="none"
                                            style="cursor: pointer"
                                    >
                                        <path
                                                d="M2.8125 4.4999H1.6875C1.53832 4.4999 1.39524 4.44064 1.28975 4.33515C1.18426 4.22966 1.125 4.08659 1.125 3.9374C1.125 3.78822 1.18426 3.64515 1.28975 3.53966C1.39524 3.43417 1.53832 3.3749 1.6875 3.3749H6.1875V1.68628C6.1875 1.53709 6.24676 1.39402 6.35225 1.28853C6.45774 1.18304 6.60082 1.12378 6.75 1.12378H11.25C11.3992 1.12378 11.5423 1.18304 11.6477 1.28853C11.7532 1.39402 11.8125 1.53709 11.8125 1.68628V3.3749H16.3125C16.4617 3.3749 16.6048 3.43417 16.7102 3.53966C16.8157 3.64515 16.875 3.78822 16.875 3.9374C16.875 4.08659 16.8157 4.22966 16.7102 4.33515C16.6048 4.44064 16.4617 4.4999 16.3125 4.4999H15.1875V16.3124C15.1875 16.4616 15.1282 16.6047 15.0227 16.7102C14.9173 16.8156 14.7742 16.8749 14.625 16.8749H3.375C3.22582 16.8749 3.08274 16.8156 2.97725 16.7102C2.87176 16.6047 2.8125 16.4616 2.8125 16.3124V4.4999ZM10.6875 3.3749V2.2499H7.3125V3.3749H10.6875ZM3.9375 15.7499H14.0625V4.4999H3.9375V15.7499ZM7.3125 13.4999C7.16332 13.4999 7.02024 13.4406 6.91475 13.3352C6.80926 13.2297 6.75 13.0866 6.75 12.9374V7.3124C6.75 7.16322 6.80926 7.02015 6.91475 6.91466C7.02024 6.80917 7.16332 6.7499 7.3125 6.7499C7.46168 6.7499 7.60476 6.80917 7.71025 6.91466C7.81574 7.02015 7.875 7.16322 7.875 7.3124V12.9374C7.875 13.0866 7.81574 13.2297 7.71025 13.3352C7.60476 13.4406 7.46168 13.4999 7.3125 13.4999ZM10.6875 13.4999C10.5383 13.4999 10.3952 13.4406 10.2898 13.3352C10.1843 13.2297 10.125 13.0866 10.125 12.9374V7.3124C10.125 7.16322 10.1843 7.02015 10.2898 6.91466C10.3952 6.80917 10.5383 6.7499 10.6875 6.7499C10.8367 6.7499 10.9798 6.80917 11.0852 6.91466C11.1907 7.02015 11.25 7.16322 11.25 7.3124V12.9374C11.25 13.0866 11.1907 13.2297 11.0852 13.3352C10.9798 13.4406 10.8367 13.4999 10.6875 13.4999Z"
                                                fill="black"
                                        />
                                    </svg>
                                </div>

            `; // Clear existing content
            questionContainer.appendChild(div);
        });
    }).catch(error => console.error('Error:'+ error));
}
