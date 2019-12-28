const fs = require('fs');
const express = require('express');

let app = express();
let server = require('http').Server(app);

app.use(express.static(__dirname));

app.get('/', function(req, res){
	res.sendFile(__dirname + "/mainmenu.html");
})

app.post('/getPlayers', function(req, res){
	res.send(JSON.stringify(players));
})

app.post('/getStats', function(req, res){
	res.send(JSON.stringify(statistics));
})

//Server listens on port 3000
server.listen(3000);
console.log('Server running at http://localhost:3000/');

/* Arrays to store players, stats, and questions */
let players = [];
let statistics = [];
let questions = [];

let msgs = [];

/* Indicators for which question and number of players answered */
let currentQuestion = 0;
let answered = 0;

const io = require("socket.io")(server);
const request = require('request');

/* Function to get 5 questions from opentdb */
function newQuestions(){
	request("https://opentdb.com/api.php?amount=5", function (error, response, body) {
		fs.writeFile("test.txt", body, (err) => {
			if (err) console.log(err);
			console.log("Successfully Written Questions to File.");
			fs.readFile("test.txt", function(err, buf) {
				questions.push(JSON.parse(buf).results);
		  	});	
		});	
	})	
}

/* Get first set of questions */
newQuestions();

/* Socket */
io.on('connection', socket => {
	console.log("A connection was made");
	
	socket.on('disconnect', () => {
		console.log(socket.username + " has disconnected");

		/* Find the player who disconnected */
		for (let i = 0; i < players.length; i ++){
			if(players[i].name == socket.username){
				/* Calls a function to add to the statistics */
				addStatisics(players[i]);
				/* Store the statistics */
				storeStats();
				/* Remove the player */		
				players.splice(i, 1);
			}
		}

		/* If all players left */
		if (players.length == 0){
			/* End the game and reset everything */
			console.log("End game");
			currentQuestion = 0;
			answered = 0;
			questions = [];
			newQuestions();
		}else{
			/* Update the leaderboard */
			io.emit("leaderboard", players);
		}
	})

	socket.on("register", name => {
		console.log(name + " has joined!");
		/* Add them to list */
		players.push({name: name, score: 0, answered : false, wins : 0})
		socket.username = name;

		// Give them the questions
		socket.emit("loadQuestion", JSON.stringify(questions[0][currentQuestion]));
		// Add to leaderboard
		io.emit("leaderboard", players);

		/* Give them all messages */
		socket.emit("buildChat", JSON.stringify({messages : msgs}));
		/* Let everyone know of the player that joined */
		io.emit("newUser", name);
	})

	socket.on("updateScore", correct => {
		/* Find the player */
		for (let i = 0; i < players.length; i ++){
			/* Add or subtract score depending on if they got it right */
			if (players[i].name == socket.username){
				if (correct){
					players[i].score += 100;
				}else{
					players[i].score -= 100;
				}
				// Player has answered
				players[i].answered = true;
			}
		}
		answered = 0;
		/* Check how many has answered */
		for (let i = 0; i < players.length; i ++){
			if (players[i].answered == true){
				answered ++;
			}
		}
		/* Update leaderboard */
		io.emit("leaderboard", players);

		/* Check to see if everyone has answered */
		if (answered == players.length){
			/* if still have questions left */
			if (currentQuestion != 4){
				/* Change answered to false for next question */
				for (let i = 0; i < players.length; i ++){
					players[i].answered = false;
				}
				/* Update leaderboard */
				io.emit("leaderboard", players);
				/* Index to next question */
				currentQuestion ++;
				/* Send the question */
				io.emit("loadQuestion", JSON.stringify(questions[0][currentQuestion]))
			/* If finished all questions */
			}else{
				currentQuestion = 0;
				io.emit("displayWinner", calculateWinner());
			}
		}
		
	})

	/* Dealing with new messages */
	socket.on('newMsg', message => {
		message = socket.username + ": " + message;
		msgs.push(message);
		io.emit("newMsg", message);
	})
})

/* Function for adding a players statistics */
function addStatisics(player){
	// Boolean to see if player has played before
	let dup = false;
	for (i = 0; i < statistics.length; i ++){
		if(statistics[i].name == player.name){
			dup = true;
			break;
		}
	}

	/* If they have played before */
	if (dup){
		/* Add total score, wins and gamesplayed */
		for (i = 0; i < statistics.length; i ++){
			if(statistics[i].name == player.name){
				statistics[i].score += player.score;
				statistics[i].wins += player.wins;
				statistics[i].gamesplayed ++;
				statistics[i].averagescore = statistics[i].score / statistics[i].gamesplayed;
				break;
			}
		}
	/* If they are new */
	}else{
		/* Set their stats */
		statistics.push(player);
		let newPlayer = statistics[statistics.length - 1];
		delete newPlayer.answered;
		newPlayer.gamesplayed = 1;
		newPlayer.averagescore = newPlayer.score / newPlayer.gamesplayed;
	}
}

/* Function that returns an array of winner(s) */
function calculateWinner(){
	let winner = players[0];
	let winners = [];

	/* If there are more than 1 player */
	if(players.length > 1){
		/* Compare all player for highest score */
		for (let i = 1; i < players.length; i ++){
			if (players[i].score > winner.score){
				winner = players[i];
			}
		}

		/* Append the players with highest score */
		for (let i = 0; i < players.length; i ++){
			if (players[i].score == winner.score){
				winners.push(players[i]);
			}
		}
	/* If there is only one player */
	}else{
		winners.push(winner);
	}

	/* Set all winner's win to 1 */
	for (let i = 0; i < players.length; i ++){
		for (let j = 0; j < winners.length; j ++){
			if (players[i].name == winners[j].name){
				players[i].wins = 1;
			}
		}
	}

	return winners;
}

/* Function that stores all statistics in a file */
function storeStats(){
	fs.writeFile("stats.txt", JSON.stringify(statistics), (err) => {
		if (err) console.log(err);
		console.log("Successfully Written Statistics to File.");
	});
}