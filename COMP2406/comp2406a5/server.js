
const mongoose = require("mongoose");
const express = require('express');
const Question = require("./QuestionModel");
const User = require("./UserModel");
const bodyParser = require("body-parser");

const app = express();

const session = require('express-session');
const MongoDBStore = require('connect-mongodb-session')(session);
const store = new MongoDBStore({
	uri: 'mongodb://localhost:27017/quiztracker',
	collection: 'sessions'
});

app.use(session({secret: 'some secret', cookie: {maxAge: 120000}, store: store}))	// Expires after 2 mins

app.use(bodyParser.urlencoded());

app.set("view engine", "pug");
app.use(express.static("public"));
app.use(express.json());

// Get homepage
app.get('/', function(req, res, next) {
	res.render("pages/index", {session : req.session});
	return;
});

// Login POST
app.post("/login", function(req, res, next){
	// Check if user is loggedin
	if(req.session.loggedin){
		res.status(200).send("Already logged in.");
		return;
	}
	// Set the username and password to a variable
	let username = req.body.username;
	let password = req.body.password;

	// Find the username in the database
	db.collection("users").findOne({username: username}, function(err, result){
		if(err)throw err;
		// If result is found
		if(result){
			// Check if the password matches
			if(result.password === password){
				// Setup their session
				req.session.loggedin = true;
				req.session.username = username;
				req.session.userid = result._id;
				// Send them to their profile
				res.redirect(`/users/${result._id}`);
			// If password doesn't match
			}else{
				// Go back to homepage
				res.redirect(`/`);
			}
		}else{
			res.redirect(`/`);
		}
		
	});
})

// Getting all users
app.get("/users", function(req, res, next){
	// Find all users with privacy = false
	db.collection("users").find({privacy : false}).toArray(function(err, results){
		// Render the page to show all users with privacy = false
		res.render("pages/users", {users : results, session : req.session});
	})
})

// Get a specific user by ID
app.get("/users/:userID", function(req, res, next){
	let ID;
	// Try to create ID
    try {
        ID = new mongoose.Types.ObjectId(req.params.userID);
    }catch (err){
        // Invalid ID
        if (err){ 
            res.status(404).send("Invalid ID");
            return;
        }
    }
    
    // Find the user with that specific ID
    db.collection("users").findOne({'_id' : ID}, function(err, result){
        if (err) throw err;
        // If there is no user with that ID
        if (result.length == 0){
            res.status(404).send("User does not exist");
        // If ID is valid
        }else{
			// Check if privacy is true
			if (result.privacy == true){
				// Has to be loggedin and username is correct to render page
				if (req.session.loggedin == true && req.session.username == result.username){
					res.render('pages/single', { user : result, session : req.session});
				// Otherwise deny them
				}else{
					res.status(403).send("Access Denied");
				}
			// If privacy is false
			}else{
				res.render('pages/single', { user : result, session : req.session});     
			}
                  
        }
	})
})

app.post("/savePrivacy", function(req, res, next){
	// Create mongoose ID
	let ID = new mongoose.Types.ObjectId(req.session.userid);

	// If privacy is checked as true
	if (req.body.privacy == 'true'){
		// Find the user and set their privacy to true
		db.collection("users").updateOne({'_id' : ID}, {"$set" : {privacy : true}}, function(err, result){
			if (err) throw err;
			// If there is no user with that ID
			if (result.length == 0){
				res.status(404).send("User does not exist");
			// If ID is valid
			}else{
				res.status(200);
				res.redirect(`/users/${req.session.userid}`);
			}
		})
	}else{
		// Find the user and set their privacy to false
		db.collection("users").updateOne({'_id' : ID}, {"$set" : {privacy : false}}, function(err, result){
			if (err) throw err;
			// If there is no user with that ID
			if (result.length == 0){
				res.status(404).send("User does not exist");
			// If ID is valid
			}else{
				res.status(200);
				res.redirect(`/users/${req.session.userid}`);
			}
		})
	}
})

// Logs out user and redirects to homepage
app.post("/logout", function(req, res, next){
	if(req.session.loggedin){
		req.session.loggedin = false;
	}
	res.redirect("/");
})

//Returns a page with a new quiz of 10 random questions
app.get("/quiz", function(req, res, next){
	Question.getRandomQuestions(function(err, results){
		if(err) throw err;
		res.status(200).render("pages/quiz", {questions: results, session : req.session});
		return;
	});
})

//The quiz page posts the results here
//Extracts the JSON containing quiz IDs/answers
//Calculates the correct answers and replies
app.post("/quiz", function(req, res, next){
	let ids = [];
	try{
		//Try to build an array of ObjectIds
		for(id in req.body){
			ids.push(new mongoose.Types.ObjectId(id));
		}
		
		//Find all questions with Ids in the array
		Question.findIDArray(ids, function(err, results){
			if(err)throw err; //will be caught by catch below
			
			//Count up the correct answers
			let correct = 0;
			for(let i = 0; i < results.length; i++){
				if(req.body[results[i]._id] === results[i].correct_answer){
					correct++;
				}
			}
			
			// Check if logged in
			if (req.session.loggedin){
				// Create mongoose ID
				let ID = new mongoose.Types.ObjectId(req.session.userid);
				// Search for ID and increment total quizzes and total score
				db.collection("users").updateOne({'_id' : ID}, {"$inc" : {total_quizzes : 1}}, function(err, result){
					if (err) throw err;
					db.collection("users").updateOne({'_id' : ID}, {"$inc" : {total_score : correct}}, function(err, result){
						if (err) throw err;
						res.json({url: `/users/${req.session.userid}`, correct: correct});
					})
				})
			}else{
				//Send response
				res.json({url: "/", correct: correct});
			}
			return;
		});
	}catch(err){
		//If any error is thrown (casting Ids or reading database), send 500 status
		console.log(err);
		res.status(500).send("Error processing quiz data.");
		return;
	}
	
});

//Connect to database
mongoose.connect('mongodb://localhost/quiztracker', {useNewUrlParser: true});
let db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function() {
	app.listen(3000);
	console.log("Server listening on port 3000");
});