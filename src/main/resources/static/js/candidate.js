var csrfToken_ = window.csrfToken;
var csrfHeader_ = window.csrfHeader;


var block_b= document.getElementById("candidate_exam_search");
var warning = document.getElementById("warning_exam_search_candidate");

var examName = document.getElementById("can_exam_name_a");
var schedule = document.getElementById("can_exam_schedule");
var examStatus = document.getElementById("can_exam_status");
var marks = document.getElementById("can_total");

var eId="";


var page = 0;
var isLoading = false;
var loadMore = true;

function test(){
    console.log("from candidate.. Javascript")
    if(!eId){
        block_b.style.display="none";
    }
}
test();

function openPopup() {
    var popup = document.getElementById("popup");
    popup.style.display = "block";

}
function closePopup() {
    var popup = document.getElementById("popup");
    popup.style.display = "none";
}

function getBase64FromFile(file, callback) {
    const reader = new FileReader();

    // Event listener for when FileReader finishes reading the file
    reader.onload = function(event) {
        callback(event.target.result.split(',')[1]); // Invoke callback with base64 string
    };

    // Read the file as Data URL (base64 format)
    reader.readAsDataURL(file);
}

function createCandidate() {
    console.log("from create Candidate..");
    var csrfToken_ = window.csrfToken;
    var csrfHeader_ = window.csrfHeader;

    const fileInput = document.getElementById("imageInput");
    const imageFile = fileInput.files[0];

//    if (!imageFile) {
//        console.error("No image file selected.");
//        return;
//    }

    // Call getBase64FromFile to retrieve base64 encoded image string
    getBase64FromFile(imageFile, function(base64String) {
        // Create candidateData object inside the callback
        const candidateData = {
            candidateName: document.getElementById("candidateName").value,
            image: base64String,
            dateOfBirth: document.getElementById('dateOfBirth').value,
            username: document.getElementById('username').value,
            phoneNumber: document.getElementById('phoneNumber').value,
            password: document.getElementById('password').value,
            email: document.getElementById('email').value,
            role: "Candidate",
            status: document.getElementById('status').value,
            examId: document.getElementById('examNumber').value,
        };

        // Send candidateData to the API
        fetch('/admin/candidate', {
            method: 'POST',
            headers: {
                [csrfHeader_]: csrfToken_,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(candidateData)
        }).then(response => response.json())
            .then(data => {
            // set ExamId globally variable eId = data.examId
            console.log(data);
            eId = data.examId;
            console.log(eId);
            closePopup(0);
            document.getElementById("candidate_list_show").innerText = '';
            fetchCandidate();
        }).catch(error => {
            console.error('Error submitting Candidate data:', error);
        });
    });
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
        block_b.style.display="block";

        var x= data.duration;
        examName.innerHTML = `${data.examName} <br> id: ${data.examId}`;
        schedule.textContent =`${data.examDate} /${data.startTime} /${x}min`;
        examStatus.textContent = data.status;
        marks.textContent = data.totalMarks;


        fetchCandidate();
        //        console.log(data)

    }).catch(error => {
        warning.style.display = "block";
        block_b.style.display="none";
        q_save.style.display = "none";
        getQuestions(null);
        console.error('Error:'+ error)
    });
}

function fetchCandidate() {
    //console.log("from fetching candidate.")
    //    console.log(page+" "+isLoading+" "+loadMore);
    console.log(eId);
    console.log("from fetching candidate list...")
    const url = `/admin/exam/${eId}/candidates/${page}`;
    if (!isLoading && loadMore) {
        isLoading = true;

        fetch(url)
            .then((response) => response.json())
            .then((data) => {
            console.log(data);
            isLoading = false;
            if (data.length === 0) {
                loadMore = false;
            } else {
                const candidate = document.getElementById("candidate_list_show");

                data.forEach((candidate) => {
                    console.log(candidate)


                    const listContainer = document.getElementById("candidate_list_show");
                    const row = document.createElement("div");
                    row.className = 'flex_table_row';

                    let statusContent = '';
                    if (candidate.status === 'Verify') {
                        statusContent = `<div style="
                        display: flex;
                        flex-direction: column;
                        justify-items: center;
                        align-items: center;
                      ">
                      <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 30 30" fill="none">
                        <path d="M13.6875 19.4375L20.75 12.375L18.9688 10.5938L13.6875 15.875L11.0625 13.25L9.28125 15.0312L13.6875 19.4375ZM15 27.5C12.1042 26.7708 9.71375 25.1092 7.82875 22.515C5.94375 19.9208 5.00083 17.0408 5 13.875V6.25L15 2.5L25 6.25V13.875C25 17.0417 24.0571 19.9221 22.1712 22.5162C20.2854 25.1104 17.895 26.7717 15 27.5ZM15 24.875C17.1667 24.1875 18.9583 22.8125 20.375 20.75C21.7917 18.6875 22.5 16.3958 22.5 13.875V7.96875L15 5.15625L7.5 7.96875V13.875C7.5 16.3958 8.20833 18.6875 9.625 20.75C11.0417 22.8125 12.8333 24.1875 15 24.875Z" fill="#00D215"></path>
                      </svg>
                      <p style="
                          margin: 0;
                          color: #000;
                          font-family: sans-serif;
                          font-size: 16px;
                          font-style: normal;
                          font-weight: 400;
                          line-height: normal;
                          letter-spacing: 0.48px;
                        ">
                        Verify
                      </p>
                    </div>`;
                    }
                    else if (candidate.status === 'Block') {
                        statusContent = `<div style="
                        display: flex;
                        flex-direction: column;
                        justify-items: center;
                        align-items: center;
                      ">
                      <svg xmlns="http://www.w3.org/2000/svg" width="27" height="27" viewBox="0 0 27 27" fill="none">
                        <g clip-path="url(#clip0_44_1031)">
                          <path d="M13.5 24.75C19.7132 24.75 24.75 19.7132 24.75 13.5C24.75 7.2868 19.7132 2.25 13.5 2.25C7.2868 2.25 2.25 7.2868 2.25 13.5C2.25 19.7132 7.2868 24.75 13.5 24.75Z" stroke="#FF0000" stroke-width="3"></path>
                          <path d="M5.625 21.375L21.375 5.625" stroke="#FF0000" stroke-width="3"></path>
                        </g>
                        <defs>
                          <clipPath id="clip0_44_1031">
                            <rect width="27" height="27" fill="white"></rect>
                          </clipPath>
                        </defs>
                      </svg>
                      <p style="
                          margin: 0;
                          color: #000;
                          font-family: sans-serif;
                          font-size: 16px;
                          font-style: normal;
                          font-weight: 400;
                          line-height: normal;
                          letter-spacing: 0.48px;
                        ">
                        Block
                      </p>
                    </div>`;
                    }
                    else{
                        statusContent = `<div style="
                        display: flex;
                        flex-direction: column;
                        justify-items: center;
                        align-items: center;
                      ">
                      <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" viewBox="0 0 25 25" fill="none">
                        <path d="M7.29171 14.0624C7.72574 14.0624 8.09483 13.9103 8.399 13.6062C8.70317 13.302 8.8549 12.9333 8.85421 12.4999C8.85421 12.0659 8.70212 11.6968 8.39796 11.3926C8.09379 11.0885 7.72504 10.9367 7.29171 10.9374C6.85768 10.9374 6.48858 11.0895 6.18442 11.3937C5.88025 11.6978 5.72851 12.0666 5.72921 12.4999C5.72921 12.9339 5.88129 13.303 6.18546 13.6072C6.48962 13.9114 6.85837 14.0631 7.29171 14.0624ZM12.5 14.0624C12.9341 14.0624 13.3032 13.9103 13.6073 13.6062C13.9115 13.302 14.0632 12.9333 14.0625 12.4999C14.0625 12.0659 13.9105 11.6968 13.6063 11.3926C13.3021 11.0885 12.9334 10.9367 12.5 10.9374C12.066 10.9374 11.6969 11.0895 11.3927 11.3937C11.0886 11.6978 10.9368 12.0666 10.9375 12.4999C10.9375 12.9339 11.0896 13.303 11.3938 13.6072C11.698 13.9114 12.0667 14.0631 12.5 14.0624ZM17.7084 14.0624C18.1424 14.0624 18.5115 13.9103 18.8157 13.6062C19.1198 13.302 19.2716 12.9333 19.2709 12.4999C19.2709 12.0659 19.1188 11.6968 18.8146 11.3926C18.5105 11.0885 18.1417 10.9367 17.7084 10.9374C17.2743 10.9374 16.9053 11.0895 16.6011 11.3937C16.2969 11.6978 16.1452 12.0666 16.1459 12.4999C16.1459 12.9339 16.298 13.303 16.6021 13.6072C16.9063 13.9114 17.275 14.0631 17.7084 14.0624ZM12.5 22.9166C11.0591 22.9166 9.7049 22.643 8.43754 22.0958C7.17018 21.5485 6.06775 20.8065 5.13025 19.8697C4.19275 18.9322 3.45074 17.8298 2.90421 16.5624C2.35768 15.2951 2.08407 13.9409 2.08337 12.4999C2.08337 11.0589 2.35699 9.70478 2.90421 8.43742C3.45143 7.17006 4.19344 6.06763 5.13025 5.13013C6.06775 4.19263 7.17018 3.45061 8.43754 2.90409C9.7049 2.35756 11.0591 2.08395 12.5 2.08325C13.941 2.08325 15.2952 2.35686 16.5625 2.90409C17.8299 3.45131 18.9323 4.19332 19.8698 5.13013C20.8073 6.06763 21.5497 7.17006 22.0969 8.43742C22.6441 9.70478 22.9174 11.0589 22.9167 12.4999C22.9167 13.9409 22.6431 15.2951 22.0959 16.5624C21.5487 17.8298 20.8066 18.9322 19.8698 19.8697C18.9323 20.8072 17.8299 21.5496 16.5625 22.0968C15.2952 22.644 13.941 22.9173 12.5 22.9166ZM12.5 20.8333C14.8264 20.8333 16.7969 20.026 18.4115 18.4114C20.0261 16.7968 20.8334 14.8263 20.8334 12.4999C20.8334 10.1735 20.0261 8.20304 18.4115 6.58846C16.7969 4.97388 14.8264 4.16659 12.5 4.16659C10.1737 4.16659 8.20317 4.97388 6.58858 6.58846C4.974 8.20304 4.16671 10.1735 4.16671 12.4999C4.16671 14.8263 4.974 16.7968 6.58858 18.4114C8.20317 20.026 10.1737 20.8333 12.5 20.8333Z" fill="#0085FF"></path>
                      </svg>
                      <p style="
                          margin: 0;
                          color: #000;
                          font-family: sans-serif;
                          font-size: 16px;
                          font-style: normal;
                          font-weight: 400;
                          line-height: normal;
                          letter-spacing: 0.48px;
                        ">
                        Pendding
                      </p>
                    </div>`;
                    }

                    row.innerHTML = `
                 <div class="flex_table_row">
                  <div class="candidate_table_row" style="width: 150px; text-align: center; flex-direction:column;">
                    Candidate id: ${candidate.candidateId} <br>
                    Password:
                    <p style=" width:150px;margin:0px; overflow:auto;">${candidate.password}</p>
                  </div>
                  <div class="candidate_table_row" style="width: 260px">
                    <img id="candidatePhoto" style="width: 80px; height: 60px; background-color: #000" src="data:image/png;base64,${candidate.image}" alt="">
                    <div style="
                        margin-left: 20px;
                        text-align: start;
                        overflow: hidden;
                        max-width: 200px;
                      ">
                      <div>${candidate.candidateName}</div>
                      <div>${candidate.email}</div>
                      <div>${candidate.phoneNumber}</div>
                    </div>
                  </div>
                  <div class="candidate_table_row" style="width: 80px">
                   ${statusContent}
                  </div>

                  <!-- option colum-------------- -->
                  <div class="candidate_table_row" style="
                      width: 250px;
                      display: flex;
                      justify-content: space-around;
                    ">
                    <div style="
                        display: flex;
                        flex-direction: column;
                        justify-items: center;
                        align-items: center;
                      ">
                      <svg xmlns="http://www.w3.org/2000/svg" width="27" height="27" viewBox="0 0 27 27" fill="none">
                        <g clip-path="url(#clip0_44_1031)">
                          <path d="M13.5 24.75C19.7132 24.75 24.75 19.7132 24.75 13.5C24.75 7.2868 19.7132 2.25 13.5 2.25C7.2868 2.25 2.25 7.2868 2.25 13.5C2.25 19.7132 7.2868 24.75 13.5 24.75Z" stroke="#FF0000" stroke-width="3"></path>
                          <path d="M5.625 21.375L21.375 5.625" stroke="#FF0000" stroke-width="3"></path>
                        </g>
                        <defs>
                          <clipPath id="clip0_44_1031">
                            <rect width="27" height="27" fill="white"></rect>
                          </clipPath>
                        </defs>
                      </svg>
                      <p style="
                          margin: 0;
                          color: #000;
                          font-family: sans-serif;
                          font-size: 16px;
                          font-style: normal;
                          font-weight: 400;
                          line-height: normal;
                          letter-spacing: 0.48px;
                        ">
                        Block
                      </p>
                    </div>
                    <div style="
                        display: flex;
                        flex-direction: column;
                        justify-items: center;
                        align-items: center;
                      ">
                      <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 30 30" fill="none">
                        <path d="M13.6875 19.4375L20.75 12.375L18.9688 10.5938L13.6875 15.875L11.0625 13.25L9.28125 15.0312L13.6875 19.4375ZM15 27.5C12.1042 26.7708 9.71375 25.1092 7.82875 22.515C5.94375 19.9208 5.00083 17.0408 5 13.875V6.25L15 2.5L25 6.25V13.875C25 17.0417 24.0571 19.9221 22.1712 22.5162C20.2854 25.1104 17.895 26.7717 15 27.5ZM15 24.875C17.1667 24.1875 18.9583 22.8125 20.375 20.75C21.7917 18.6875 22.5 16.3958 22.5 13.875V7.96875L15 5.15625L7.5 7.96875V13.875C7.5 16.3958 8.20833 18.6875 9.625 20.75C11.0417 22.8125 12.8333 24.1875 15 24.875Z" fill="#00D215"></path>
                      </svg>
                      <p style="
                          margin: 0;
                          color: #000;
                          font-family: sans-serif;
                          font-size: 16px;
                          font-style: normal;
                          font-weight: 400;
                          line-height: normal;
                          letter-spacing: 0.48px;
                        ">
                        Verify
                      </p>
                    </div>
                    <div style="
                        display: flex;
                        flex-direction: column;
                        justify-items: center;
                        align-items: center;
                      ">
                      <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" viewBox="0 0 25 25" fill="none">
                        <path d="M7.29171 14.0624C7.72574 14.0624 8.09483 13.9103 8.399 13.6062C8.70317 13.302 8.8549 12.9333 8.85421 12.4999C8.85421 12.0659 8.70212 11.6968 8.39796 11.3926C8.09379 11.0885 7.72504 10.9367 7.29171 10.9374C6.85768 10.9374 6.48858 11.0895 6.18442 11.3937C5.88025 11.6978 5.72851 12.0666 5.72921 12.4999C5.72921 12.9339 5.88129 13.303 6.18546 13.6072C6.48962 13.9114 6.85837 14.0631 7.29171 14.0624ZM12.5 14.0624C12.9341 14.0624 13.3032 13.9103 13.6073 13.6062C13.9115 13.302 14.0632 12.9333 14.0625 12.4999C14.0625 12.0659 13.9105 11.6968 13.6063 11.3926C13.3021 11.0885 12.9334 10.9367 12.5 10.9374C12.066 10.9374 11.6969 11.0895 11.3927 11.3937C11.0886 11.6978 10.9368 12.0666 10.9375 12.4999C10.9375 12.9339 11.0896 13.303 11.3938 13.6072C11.698 13.9114 12.0667 14.0631 12.5 14.0624ZM17.7084 14.0624C18.1424 14.0624 18.5115 13.9103 18.8157 13.6062C19.1198 13.302 19.2716 12.9333 19.2709 12.4999C19.2709 12.0659 19.1188 11.6968 18.8146 11.3926C18.5105 11.0885 18.1417 10.9367 17.7084 10.9374C17.2743 10.9374 16.9053 11.0895 16.6011 11.3937C16.2969 11.6978 16.1452 12.0666 16.1459 12.4999C16.1459 12.9339 16.298 13.303 16.6021 13.6072C16.9063 13.9114 17.275 14.0631 17.7084 14.0624ZM12.5 22.9166C11.0591 22.9166 9.7049 22.643 8.43754 22.0958C7.17018 21.5485 6.06775 20.8065 5.13025 19.8697C4.19275 18.9322 3.45074 17.8298 2.90421 16.5624C2.35768 15.2951 2.08407 13.9409 2.08337 12.4999C2.08337 11.0589 2.35699 9.70478 2.90421 8.43742C3.45143 7.17006 4.19344 6.06763 5.13025 5.13013C6.06775 4.19263 7.17018 3.45061 8.43754 2.90409C9.7049 2.35756 11.0591 2.08395 12.5 2.08325C13.941 2.08325 15.2952 2.35686 16.5625 2.90409C17.8299 3.45131 18.9323 4.19332 19.8698 5.13013C20.8073 6.06763 21.5497 7.17006 22.0969 8.43742C22.6441 9.70478 22.9174 11.0589 22.9167 12.4999C22.9167 13.9409 22.6431 15.2951 22.0959 16.5624C21.5487 17.8298 20.8066 18.9322 19.8698 19.8697C18.9323 20.8072 17.8299 21.5496 16.5625 22.0968C15.2952 22.644 13.941 22.9173 12.5 22.9166ZM12.5 20.8333C14.8264 20.8333 16.7969 20.026 18.4115 18.4114C20.0261 16.7968 20.8334 14.8263 20.8334 12.4999C20.8334 10.1735 20.0261 8.20304 18.4115 6.58846C16.7969 4.97388 14.8264 4.16659 12.5 4.16659C10.1737 4.16659 8.20317 4.97388 6.58858 6.58846C4.974 8.20304 4.16671 10.1735 4.16671 12.4999C4.16671 14.8263 4.974 16.7968 6.58858 18.4114C8.20317 20.026 10.1737 20.8333 12.5 20.8333Z" fill="#0085FF"></path>
                      </svg>
                      <p style="
                          margin: 0;
                          color: #000;
                          font-family: sans-serif;
                          font-size: 16px;
                          font-style: normal;
                          font-weight: 400;
                          line-height: normal;
                          letter-spacing: 0.48px;
                        ">
                        Pendding
                      </p>
                    </div>
                  </div>
                </div>
                            `;

//                    const imgElement = document.getElementById("candidatePhoto");
//                    imgElement.src = `data:image/png;base64, ${candidate.image}`;

                    listContainer.append(row);

                });

                page++;
            }
        })
            .catch((error) => {
            isLoading = false;
            console.error("Error fetching data:", error);
        });
    }
}

document.getElementById("candidate_list_show")
    .addEventListener("scroll", function () {
    if (this.scrollTop + this.clientHeight >= this.scrollHeight) {
        fetchCandidate();
    }
});


