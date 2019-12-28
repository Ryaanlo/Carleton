// Get elements and set them to a variable
let results = document.getElementById("results");
let questions = document.getElementById("questions");
let creator = document.getElementById("creator");
let tags = document.getElementById("tags");

let save = document.getElementById("save");
save.onclick = saveQuiz;

let category = document.getElementById("category");
let difficulty = document.getElementById("difficulty");
category.onchange = getQuestions;
difficulty.onchange = getQuestions;

// Function for save button on click
function saveQuiz(){
	// Get all href elements from questions added to the quiz
	let a = questions.getElementsByTagName('a');

	// Validation for creator and tags input and at least one question
    if (creator.value.length == 0){
        alert("Please fill in the creator's name");
    }else if(tags.value.length == 0){
        alert("Please fill in the tags");
    }else if(a.length == 0){
		alert("Please add some questions");
	// If everything is filled in
	}else{
		// Disable the buttons and dropdown list
		let buttons = document.getElementsByTagName("button");
		for (let i = 0; i < buttons.length; i ++){
			buttons[i].disabled = true;
		}
		let dropdown = document.getElementsByTagName("select");
		for (let i = 0; i < dropdown.length; i ++){
			dropdown[i].disabled = true;
		}
		
		// Separate tags by spaces
		let tagsArray = tags.value.split(" ");
		// Where all quiz questions go
		let quizQuestions = [];

		// Looping over all questions added to quiz
		for (let i = 0; i < a.length; i ++){
			// Send GET request for each question
			let xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if(this.readyState==4 && this.status==200){
					// Parse the response
					let question = JSON.parse(this.response).question;
					// Add the question to the array
					quizQuestions.push(question[0]);
					// Once all questions are added
					if (i == a.length - 1){
						// Call function for sending response
						sendResponse(tagsArray, quizQuestions);
					}
				}
			}
			xhttp.open("GET", `/questions/${a[i].className}`);
			xhttp.setRequestHeader("Content-Type", "application/json");
			xhttp.send();
		}
    }
}

// Function for sending a POST request to add quiz to database
function sendResponse(tagsArray, quizQuestions){
	let response = {creator : creator.value, tags : tagsArray, questions : quizQuestions};

	// XML request to send the response
	let xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(this.readyState==4 && this.status==200){
			console.log("Creating quiz");
			// Get the ID of the quiz
			let res = this.response;
			let id = res.replace(/['"]+/g, '');
			// redirect to the quiz page
			window.location.replace(`/quiz/${id}`);
		}
	}
	xhttp.open("POST", `/quizzes`);
	xhttp.setRequestHeader("Content-Type", "application/json");
	xhttp.send(JSON.stringify(response));
}

// Call function on first load
getQuestions();

// Function for getting questions that match the category and difficulty drop down list
function getQuestions(){
	let categoryQuery = encodeURIComponent(category.value);

	// Request for all sets of questions
    let xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if(this.readyState==4 && this.status==200){
			// Clear all previous questions
			while(results.firstChild){
				results.removeChild(results.firstChild);
			}
			// Parse response
			let questions = JSON.parse(this.response).questions;
			// Loop over every questions
			for (let i = 0; i < questions.length; i++){
				// Include add button
				let add = document.createElement('button');
				add.innerHTML = "Add";
				
				// Include href
				let a = document.createElement('a');
                let text = document.createTextNode(`${questions[i].question}`);
                let br = document.createElement("br");
                a.href = `/questions/${questions[i]._id}`;
				a.className = questions[i]._id;
				add.addEventListener('click', function(){
					addQuestion(a);
				});

				// Append to result list
                a.appendChild(text);
				a.appendChild(br);
				results.appendChild(add);
                results.appendChild(a);
			}
			
		}
	}
	xhttp.open("GET", `/questions?category=${categoryQuery}&difficulty=${difficulty.value}`);
	xhttp.setRequestHeader("Content-Type", "application/json");
	xhttp.send();
}

// Function for adding question to quiz
function addQuestion(href){
	// Make a clone
	let a = href.cloneNode(true);

	// Checking if question already exist in the quiz
	let allQuestions = questions.getElementsByTagName("a");
	for (let i = 0; i < allQuestions.length; i ++){
		if (href.className == allQuestions[i].className){
			return;
		}
	}

	// Create remove button
	let remove = document.createElement('button');
	remove.innerHTML = "Remove";
	remove.className = href.className;
	remove.addEventListener('click', function(){
		removeQuestion(href.className);
	});

	// Append to quiz questions
	questions.appendChild(remove);
	questions.appendChild(a);
}

// Function for removing the question from the quiz
function removeQuestion(id){
	let remove = questions.getElementsByClassName(id);
	while(remove.length > 0){
		remove[0].parentNode.removeChild(remove[0]);
	}
}