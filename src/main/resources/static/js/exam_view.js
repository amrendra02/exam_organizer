console.log("........test3 .. ")
function list(){
    const questionListContainer = document.getElementById("QuestionList");
    questionListContainer.innerHTML ="";

    for (let i = 1; i <= 15; i++) {
        questionListContainer.innerHTML += `
            <div class="Question1">Question ${i}.....</div>
            <div class="Line34"></div>
        `;
    }
    console.log("from exam view .. ..")
}
list();