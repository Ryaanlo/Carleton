let socket = null;

/* Init on mainmenu load */
function initMain(){
    let join = document.getElementById("join");
    join.onclick = enterGame;
    let stats = document.getElementById("statistics");
    stats.onclick = loadStats;
}

/* Function that goes to the stats page */
function loadStats(){
    window.location = "stats.html";
}

/* Function that gets the stats and puts it on the screen */
function getStats(){
    let stats = document.getElementById("stats");

    // Clear Previous Stats
	while(stats.firstChild){
		stats.removeChild(stats.firstChild);
    }

    /* Request for the stats */
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        /* If everything is working */
        if (this.readyState == 4 && this.status == 200){
            /* Parse the recieved stats */
            let statistics = JSON.parse(this.response);

            /* Display the stats */
            for (let i = 0; i < statistics.length; i ++){
                let playerInfo = "Name: " + statistics[i].name + ", Score: " + statistics[i].score + ", Games Played: " + statistics[i].gamesplayed + ", Average Score: " + statistics[i].averagescore + ", Wins: " + statistics[i].wins;
                let text = document.createTextNode(playerInfo);
                let div = document.createElement("strong");
                div.appendChild(text);
                stats.appendChild(div);
                stats.appendChild(createBr());
            }
        }
    };
    xhttp.open("POST", "/getStats");
    xhttp.send();
}

/* Called when player wants to join game */
function initGame(){
    let loadGame = document.getElementById("load");
    loadGame.onclick = newPlayer;
}

/* Function that goes to the game page */
function enterGame(){
    window.location = "game.html";
}

/* Function for creating new player */
function newPlayer(){
    /* Get the inputted name */
    let textbox = document.getElementById("name");
    let name = textbox.value;
    /* Used for checking duplicate names */
    let dup = false;

    // Checking if name is not blank
    if (name.length > 0){
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            /* If everything is working */
            if (this.readyState == 4 && this.status == 200){
                /* Get the response and parse it */
                let players = JSON.parse(this.response);

                // Checking for duplicate names
                for (let i = 0; i < players.length; i ++){
                    console.log(players[0].name);
                    if (players[i].name == name){
                        dup = true;
                    }
                }
                /* If no duplicates */
                if (dup == false){
                    /* Remove textbox and button for entering name */
                    let enterName = document.getElementById("enterName");
                    enterName.remove();

                    /* Give them a socket */
                    if (socket == null){
                        socket = io();
                        socket.on("loadQuestion", loadQuestion);
                        socket.on("leaderboard", buildLeaderboard);
                        socket.on("displayWinner", displayWinner);
                        socket.on("newQuestions", test)
                        socket.on("buildChat", buildChat);
                        socket.on("newUser", newUser);
                        socket.on("newMsg", newMessage)
                        socket.emit("register", name);
                    }
                /* If there are duplicates */
                }else{
                    alert("Someone already has that name!");
                }
            }
        };
        xhttp.open("POST", "/getPlayers");
        xhttp.send();
    }else{
        alert("Please enter a name!");
    }
}

/* Function for building the chat box */
function buildChat(messages){
    let msg = JSON.parse(messages).messages;
	msg.forEach(elem => {
		newMessage(elem);
	})
}

/* Function for displaying new user joining */
function newUser(name){
	let newLI = document.createElement("li");
	let text = document.createTextNode(name + " joined the chat.");
	newLI.appendChild(text);
	document.getElementById("messages").appendChild(newLI);
}

/* Function for appending a message to the chat */
function newMessage(message){
	let newLI = document.createElement("li");
	let text = document.createTextNode(message);
	newLI.appendChild(text);
	document.getElementById("messages").appendChild(newLI);
}

/* Function for sending a message */
function sendMessage(){
	let msg = document.getElementById("message");
	if(msg.value.length > 0){
		socket.emit("newMsg", msg.value);
    }
    msg.value = "";
}

/* Function that loads the questions onto the screen */
function loadQuestion(questions){
    // Parse the questions
    questions = JSON.parse(questions);

    /* Get the test */
    let test = document.getElementById("test");
    
    /* Clear Previous Test */
	while(test.firstChild){
		test.removeChild(test.firstChild);
    }

	/* Empty out/Clear everything in the correctAnswer array */
	correctAnswer = [];

	/* Array to store possible selections */
	let options = [];
	/* Array to store randomized possible selections */
	let randOptions = [];
	/* Add the first option */
	options.push(questions.correct_answer);
	/* Add the correct answers to the Array */
	correctAnswer.push(questions.correct_answer);
	/* Add the rest of the possible options */
	for (let j = 0; j < (questions.incorrect_answers.length); j ++){
		options.push(questions.incorrect_answers[j]);
	}

	/* Variable for amount of questions */
	let questionAmount = options.length;
	let rand = options.length;
	/* Randomize choices */
	for (let j = 0; j < questionAmount; j ++){
	let randNum = Math.floor(Math.random() * Math.floor(rand));
	randOptions.push(options[randNum]);
		options.splice(randNum, 1);
		rand --;
	}

	/* Question Text */
	let text = document.createTextNode(questions.question);
	let questionText = document.createElement("strong"); 
	questionText.setAttribute("name", "questionText");
	questionText.appendChild(text);
	/* Create a form to store all radio input of a question */
    let form = document.createElement("form");
    
	/* Loop over the number of options in a question */
	for (let j = 0; j < randOptions.length; j ++){
		/* Create radio input and label for the option text */
		let radio = document.createElement("input");
		let optionText = document.createElement("label");
		/* Set attributes for radio input */
		radio.setAttribute("type", "radio");
		radio.setAttribute("name", "option");
		radio.setAttribute("value", randOptions[j]);
		/* Set the optionText */
		optionText.innerHTML = randOptions[j];
		/* Create div to store a selection */
		let selection = document.createElement("div");
		selection.appendChild(radio);
		selection.appendChild(optionText);
		selection.appendChild(createBr());
		/* Store all the radio buttons for each question into the form */
		/* Set name of form */
		form.appendChild(selection);
		form.setAttribute("name", "form");
	}
	/* Create  a div to store every element of a question */
	let group = document.createElement("div");
	group.setAttribute("name", "group");
	group.appendChild(questionText);
	group.appendChild(form);
	group.appendChild(createBr());
	/* Append the whole question to the test of question */
	test.appendChild(group);

	// Create Clear and Check Answer Buttons
	let submit = document.createElement("button");
	submit.setAttribute("type", "button");
	submit.innerHTML = "Submit Answer";

	/* Set the onclick to activate a function */
    submit.onclick = submitAnswer;
    submit.setAttribute("id", "submit");

	/* Append those buttons to the test */
	test.appendChild(submit);

}

/* Function for when player submits their answer */
function submitAnswer(){
	/* Boolean for checking if at least one selection is selected */
	let checked = false;
    /* Boolean for corrent answer */
    let correct = false;

	/* Get all radio buttons in a question, get the test, get the questionText, get all question */
	let form = document.getElementsByName("form");
	let submit = document.getElementById("submit");

    /* Checking for a selected answer */
    for (let i = 0; i < form[0].length; i ++){
        if (form[0][i].checked){
            checked = true;
            break;
        }
    }

    /* If there is a selected answer */
    if (checked){
        /* Disable all the buttons */
        for (let i = 0; i < form[0].length; i ++){
            form[0][i].disabled = true;
        }
        submit.disabled = true;
    /* If no answer is selected */
    }else{
        alert("Please select an answer!");
    }

	/* Loop over every selection */
	for (let i = 0; i < form[0].length; i ++){
		/* If that selection is checked */
		if (form[0][i].checked) {
			/* Loop over all correct answers */
			for (let j = 0; j < correctAnswer.length; j ++){
				/* If the selected answer match one of the correct answers */
				if (form[0][i].value == correctAnswer[j]){
                    correct = true;
					break;
				}
            }
            /* Update the score */
            socket.emit("updateScore", correct);
		}
	}
}

/* Function for displaying the winner(s) */
function displayWinner(winners){
    let test = document.getElementById("test");
    let winner = "";

    /* Clear Test */
	while(test.firstChild){
		test.removeChild(test.firstChild);
    }

    /* Create the text that is to be displayed */
    if (winners.length > 1){
        winner += "Winners are ";
        for (let i = 0; i < winners.length - 1; i ++){
            winner += winners[i].name + ", "; 
        }
        winner += winners[winners.length - 1].name; 
        winner += "! Score: " + winners[0].score;
    }else{
        winner += "Winner is " + winners[0].name + "! Score: " + winners[0].score;
    }

    /* Create and append the text of the winner(s) */
    let text = document.createTextNode(winner);
	let display = document.createElement("h1"); 
	display.setAttribute("name", "questionText");
    display.appendChild(text);
    test.appendChild(display);

    /* Create a button to return to main menu */
    let button = document.createElement("button");
    button.type = "button";
    button.onclick = returnToMainMenu;
    button.innerText = "Return to Main Menu";
    test.appendChild(button);

    /* Disconnect the socket */
    socket.disconnect();
}

/* Function that returns to the main menu */
function returnToMainMenu(){
    window.location = "mainmenu.html";
}

/* Function that builds the leaderboard */
function buildLeaderboard(players){
    let leaderboard = document.getElementById("leaderboard");

    /* Clear Leaderboard */
	while(leaderboard.firstChild){
		leaderboard.removeChild(leaderboard.firstChild);
    }
    
    /* Loop over all players */
    for (let i = 0; i < players.length; i ++){
        /* Create a text for them */
        let player = "Player: " + players[i].name + " | Score: " + players[i].score;
        let div = document.createElement("div");
        let text = document.createTextNode(player);
 
        /* If they haven't answered then change their text to red */
        if (players[i].answered == false){
            div.style.color = "red";
        }

        div.appendChild(text);

        leaderboard.appendChild(div);
    }
}

/* Function that returns a br */
function createBr(){
	let br = document.createElement("br");
	return br;
}