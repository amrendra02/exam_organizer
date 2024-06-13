
function getCsrfToken() {
    return document.querySelector('meta[name="csrf-token"]').getAttribute('content');
}


var questionList=null;
let size= 0;
let examNumber=null;

document.addEventListener("DOMContentLoaded", () => {
    // Extract examId from URL
    const urlPath = window.location.pathname;
    const pathParts = urlPath.split('/');
    const examId = pathParts[pathParts.length - 1]; // Assuming examId is at the end of the path
    examNumber = examId;
    //    console.log(examId);
    //    alert("Do not refresh the page")

    if (examId) {
        // Call the API with the extracted examId
        fetch(`/candidate/exam/${examId}/question-list`)
            .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
            .then(data => {
//                                                            console.log(data);
            questionList=data;
            size = Object.keys(questionList).length;

            const length = Object.keys(questionList).length;
            if(length>0){
                const questionAtIndex = getObjectByIndex(data, 0);
                console.log(questionAtIndex);
                previewQuestion(questionAtIndex);
            }

        })
            .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
    } else {
        console.error('examId not found in URL');
    }
});

const extractQuestionDetails = (questionStr) => {
    // Updated regex to handle question ID, question text, and image being either a value or null
    const regex = /questionId=(null|\d+),\s*questionText=(null|[^,]+),\s*image=(\[([^\]]+)\]|null),/;
    const matches = regex.exec(questionStr);
//    console.log(questionStr);
    if (matches) {
        const questionId = matches[1] !== 'null' ? parseInt(matches[1]) : null;
        const questionText = matches[2] !== 'null' ? matches[2].trim() : null;
        const imageData = matches[4] ? matches[4].trim() : null; // matches[4] will be undefined if image is null
        return {
            questionId: questionId,
            text: questionText,
            image: imageData
        };
    }
    return null;
};

// Function to get the object property by its index and return both key and value
function getObjectByIndex(obj, index) {
    const keys = Object.keys(obj);
    if (index >= 0 && index < keys.length) {
        const key = keys[index];
        //        console.log(extractQuestionDetails(key))
        return {
            key: extractQuestionDetails(key),
            value: obj[key]
        };
    } else {
        return null; // Return null if the index is out of bounds
    }
}

function arrayBufferToBase64(buffer) {
    let binary = '';
    const bytes = new Uint8Array(buffer);
    const len = bytes.byteLength;
    for (let i = 0; i < len; i++) {
        binary += String.fromCharCode(bytes[i]);
    }
    return window.btoa(binary);
}

var index=0;
let selectedOptions = {};

const nextQuestion=()=>{
    console.log("next page");
    console.log(index);
    const length = Object.keys(questionList).length;
    console.log(length);
    index<length-1?index+=1: "";

    console.log("current-indx: ",index);
    const data = getObjectByIndex(questionList, index);
    previewQuestion(data);
}
const previousQuestion=()=>{
    console.log("previous page");
    console.log(index)
    index>0?index-=1: "";

    console.log("current-indx: ",index);
    const data = getObjectByIndex(questionList, index);
    previewQuestion(data);
}

const previewQuestion=(data)=>{
//    console.log(data);
    const view =  document.getElementById("question-view");
    view.innerHTML="";
    view.innerHTML = `(<span style="font-size:16px; font-weight:600;">${index+1}/ ${size}</span>) ${data.key.text}`;

    let imageData = null;
    if (data.key.image && typeof data.key.image === 'string') {
        imageData = data.key.image.split(',').map(Number);
    }//    console.log(imageData);
    const image =document.getElementById('image-view')
    if (imageData) {
        const base64String = arrayBufferToBase64(imageData.map((value) => value < 0 ? value + 256 : value));
        image.src = `data:image/jpeg;base64,${base64String}`;
    } else {
        image.src = '';
        image.style.display='none';
    }


    const optionContainer = document.getElementById("option-view");
    // Clear the container before adding new options
    optionContainer.innerHTML = "";
    const options = data.value;
//    console.log(options);

    // Iterate over the options array and create radio button elements
    options.forEach(item => {

//        console.log(item)
        const label = document.createElement("label");
        label.className = "option";

        const input = document.createElement("input");
        input.type = "radio";
        input.name = "capital";
        input.value = item.number;

        input.addEventListener('change', (event) => {
            const questionKey = index.toString(); // Convert questionId to string to use it as the key
            selectedOptions[questionKey] = { // Store the selected option under the question key
                optionNumber: event.target.value,
                text: item.text,
                optionId: item.optionId,
                questionId: data.key.questionId
            };
//            console.log('Selected Options:', selectedOptions);
        });


        const text = document.createTextNode(`${item.number}. ${item.text}`);

        label.appendChild(input);
        label.appendChild(text);

        optionContainer.appendChild(label);
    });

}

const requestBody = {
    options: selectedOptions,
    username:null,
    email:null,
    examId:examNumber
};

const submitAnswer = () => {

    console.log("Submit Exam ID:", examNumber);
    console.log("Selected Options:", selectedOptions);

    const username = document.getElementById("user-name");
    const user = username.textContent;

    const email = document.getElementById("user-email");
    requestBody.username=user;
    requestBody.email= email.textContent;


    fetch(`/candidate/exam/${examNumber}/answer`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': getCsrfToken()

        },
        body: JSON.stringify(requestBody)
    }).then(function(response) {
        if (!response.ok) {
            throw new Error('Failed to submit answers');
        }
        // Handle success
        return response.json();
    }).then(function(data) {
        console.log('Response:', data);
        alert("Successfully Submitted")
        data.status?window.location.href = `/candidate/home`:alert("Some problem occur!!")
        // Redirect to another page or perform other actions
    }).catch(function(error) {
        console.error('Error:', error);
        // Handle error
    });
};
