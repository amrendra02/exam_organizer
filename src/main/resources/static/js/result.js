var csrfToken_ = window.csrfToken;
var csrfHeader_ = window.csrfHeader;


var block_b= document.getElementById("result_exam_search");
var warning = document.getElementById("warning_exam_search_result");

var examName = document.getElementById("res_exam_name_a");
var schedule = document.getElementById("res_exam_schedule");
var examStatus = document.getElementById("res_exam_status");
var marks = document.getElementById("res_total");

var eId="";


var page = 0;
var isLoading = false;
var loadMore = true;

function test(){
//    console.log("from Result.. Javascript")
    if(!eId){
        block_b.style.display="none";
    }
}
test();

function searchExam() {
    const form = document.getElementById('search_form');
    const formData = new FormData(form);
    fetch('/admin/exam-data', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
//        console.log('Received Exam id:', data);
        eId = data.examId;

        warning.style.display = "none";
        block_b.style.display="block";

        var x= data.duration;
        examName.innerHTML = `${data.examName} <br> id: ${data.examId}`;
        schedule.textContent =`${data.examDate} /${data.startTime} /${x}min`;
        examStatus.textContent = data.status;
        marks.textContent = data.totalMarks;

        fetchResult();

    }).catch(error => {
        warning.style.display = "block";
        block_b.style.display="none";
        q_save.style.display = "none";
        getQuestions(null);
        console.error('Error:'+ error)
    });
}

function fetchResult() {
//    console.log("from fetching result.")
//    console.log("from fetching result list...")
    const url = `/admin/exam/${eId}/result/${page}`;
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
                const candidate = document.getElementById("result_list_show");

                data.forEach((result) => {
//                    console.log(result)

                    const listContainer = document.getElementById("result_list_show");
                    var row = document.createElement("div");
                    row.classList.add("r_flex_table_row");
                    row.style.display = "flex";
                    row.style.width = "100%";

                    let statusContent = '';

                    row.innerHTML = `
                    <div class="res_table_row" style="
                      width: 150px;
                      border: 1.5px solid black;
                      border-top: 0px;
                      height: 80px;

                      flex: auto;

                      color: #000;
                      font-family: sans-serif;
                      font-size: 13px;
                      font-weight: 500;
                      line-height: normal;
                      letter-spacing: 0.39px;
                      display: flex;
                      align-items: center;
                      justify-content: center;
                      background-color: #ffffff;
                    ">
                    Candidate id:${result.candidateModel.candidateId}<br>
                    Password: ${result.candidateModel.password}
                  </div>
                  <div class="res_table_row" style="
                      width: 260px;
                      border: 1.5px solid black;
                      border-top: 0px;
                      height: 80px;

                      flex: auto;

                      color: #000;
                      font-family: sans-serif;
                      font-size: 13px;
                      font-weight: 500;
                      line-height: normal;
                      letter-spacing: 0.39px;
                      display: flex;
                      align-items: center;
                      justify-content: center;
                      background-color: #ffffff;
                    ">
                    <img style="width: 80px; height: 60px; background-color: #000" src="" alt="">
                    <div style="
                        margin-left: 20px;
                        text-align: start;
                        overflow: hidden;
                        max-width: 200px;
                      ">
                      <div>${result.candidateModel.candidateName}</div>
                      <div>${result.candidateModel.email}</div>
                      <div>${result.candidateModel.phoneNumber}</div>
                    </div>
                  </div>

                  <!-- option block here------------- -->
                  <!-- correct here------------- -->
                  <div style="
                      width: 50px;
                      border: 1.5px solid black;
                      border-top: 0px;
                      height: 80px;

                      flex: auto;

                      color: #00f71d;
                      font-family: sans-serif;
                      font-size: 26px;
                      font-weight: 600;
                      line-height: normal;
                      letter-spacing: 0.39px;
                      display: flex;
                      align-items: center;
                      justify-content: center;
                      background-color: #ffffff;
                    ">
                    ${result.correct}
                  </div>
                  <!-- Wrong here------------- -->

                  <div style="
                      width: 50px;
                      border: 1.5px solid black;
                      border-top: 0px;
                      height: 80px;

                      flex: auto;

                      color: #ff0000;
                      font-family: sans-serif;
                      font-size: 26px;
                      font-weight: 600;
                      line-height: normal;
                      letter-spacing: 0.39px;
                      display: flex;
                      align-items: center;
                      justify-content: center;
                      background-color: #ffffff;
                    ">
                    ${result.wrong}
                  </div>
                  <!-- not attend here------------- -->
                  <div style="
                      width: 50px;
                      border: 1.5px solid black;
                      border-top: 0px;
                      height: 80px;

                      flex: auto;

                      color: #0047ff;
                      font-family: sans-serif;
                      font-size: 26px;
                      font-weight: 600;
                      line-height: normal;
                      letter-spacing: 0.39px;
                      display: flex;
                      align-items: center;
                      justify-content: center;
                      background-color: #ffffff;
                    ">
                     ${result.notDone}
                  </div>
                  <!-- status pass or not--------------- -->
                  <div style="
                      width: 50px;
                      border: 1.5px solid black;
                      border-top: 0px;
                      height: 80px;

                      flex: auto;

                      color: #00bd1e;
                      font-family: sans-serif;
                      font-size: 26px;
                      font-weight: 600;
                      line-height: normal;
                      letter-spacing: 0.39px;
                      display: flex;
                      align-items: center;
                      justify-content: center;
                      background-color: #ffffff;
                    ">
                     ${result.status}
                  </div>

                  <!-- option colum-------------- -->
                  <div style="
                      width: 94px;
                      display: flex;
                      justify-content: space-around;

                      border: 1.5px solid black;
                      border-top: 0px;
                      height: 80px;

                      flex: auto;

                      color: #000;
                      font-family: sans-serif;
                      font-size: 13px;
                      font-weight: 500;
                      line-height: normal;
                      letter-spacing: 0.39px;

                      align-items: center;

                      background-color: #ffffff;
                    ">
                    <!-- view option-- -->
                    <div style="
                        display: flex;
                        flex-direction: column;
                        justify-items: center;
                        align-items: center;
                      ">
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                        <path d="M10 12.5C11.3807 12.5 12.5 11.3807 12.5 10C12.5 8.61929 11.3807 7.5 10 7.5C8.61929 7.5 7.5 8.61929 7.5 10C7.5 11.3807 8.61929 12.5 10 12.5Z" fill="black"></path>
                        <path d="M19.3375 9.7875C18.6024 7.88603 17.3262 6.24164 15.6667 5.05755C14.0073 3.87347 12.0372 3.20161 9.99999 3.125C7.96282 3.20161 5.99274 3.87347 4.33325 5.05755C2.67375 6.24164 1.39759 7.88603 0.662494 9.7875C0.612848 9.92482 0.612848 10.0752 0.662494 10.2125C1.39759 12.114 2.67375 13.7584 4.33325 14.9424C5.99274 16.1265 7.96282 16.7984 9.99999 16.875C12.0372 16.7984 14.0073 16.1265 15.6667 14.9424C17.3262 13.7584 18.6024 12.114 19.3375 10.2125C19.3871 10.0752 19.3871 9.92482 19.3375 9.7875ZM9.99999 14.0625C9.19651 14.0625 8.41107 13.8242 7.74299 13.3778C7.07492 12.9315 6.55421 12.297 6.24673 11.5547C5.93925 10.8123 5.8588 9.99549 6.01555 9.20745C6.17231 8.4194 6.55922 7.69553 7.12737 7.12738C7.69552 6.55923 8.41939 6.17231 9.20744 6.01556C9.99549 5.85881 10.8123 5.93926 11.5546 6.24674C12.297 6.55422 12.9314 7.07492 13.3778 7.743C13.8242 8.41107 14.0625 9.19651 14.0625 10C14.0608 11.0769 13.6323 12.1093 12.8708 12.8708C12.1093 13.6323 11.0769 14.0608 9.99999 14.0625Z" fill="black"></path>
                        <rect x="0.5" y="0.5" width="19" height="19" stroke="black"></rect>
                      </svg>
                    </div>
                    <!-- download option -->
                    <div style="
                        display: flex;
                        flex-direction: column;
                        justify-items: center;
                        align-items: center;
                      ">
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                        <path d="M9.5575 14.8175C9.61555 14.8757 9.68452 14.9219 9.76045 14.9534C9.83639 14.9849 9.91779 15.0011 10 15.0011C10.0822 15.0011 10.1636 14.9849 10.2395 14.9534C10.3155 14.9219 10.3844 14.8757 10.4425 14.8175L14.1925 11.0675C14.3099 10.9501 14.3758 10.791 14.3758 10.625C14.3758 10.459 14.3099 10.2999 14.1925 10.1825C14.0751 10.0651 13.916 9.99921 13.75 9.99921C13.584 9.99921 13.4249 10.0651 13.3075 10.1825L10.625 12.8663V1.875C10.625 1.70924 10.5591 1.55027 10.4419 1.43306C10.3247 1.31585 10.1658 1.25 10 1.25C9.83424 1.25 9.67526 1.31585 9.55805 1.43306C9.44084 1.55027 9.375 1.70924 9.375 1.875V12.8663L6.6925 10.1825C6.57514 10.0651 6.41597 9.99921 6.25 9.99921C6.08403 9.99921 5.92485 10.0651 5.8075 10.1825C5.69014 10.2999 5.62421 10.459 5.62421 10.625C5.62421 10.791 5.69014 10.9501 5.8075 11.0675L9.5575 14.8175Z" fill="#0D0D0D"></path>
                        <rect x="0.5" y="0.5" width="19" height="19" stroke="black"></rect>
                      </svg>
                    </div>

                    <!-- end option--------------------- -->
                  </div>`;
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

document.getElementById("result_list_show")
    .addEventListener("scroll", function () {
    if (this.scrollTop + this.clientHeight >= this.scrollHeight) {
        fetchResult();
    }
});


