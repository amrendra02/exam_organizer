var x23=[];
function call(){
    list(x23);
}

function list(x23){
    const questionListContainer = document.getElementById("QuestionList");
    questionListContainer.innerHTML ="";

    x23.forEach((question, index) =>{
        questionListContainer.innerHTML += `
            <div class="Question1">${index}. ${question.questionText}</div>
            <div class="Line34"></div>
        `;
    });
}

function Qlist(examId){
    console.log('from q count...')
    var count=0;
    fetch("/admin/question-list/" + examId)
        .then(response => response.json())
        .then(data => {
        count=data.length;
        x23=data;
        document.getElementById("ex6").innerHTML = count+" Total question";
    }).catch(error => console.error('Error:'+ error));

}
