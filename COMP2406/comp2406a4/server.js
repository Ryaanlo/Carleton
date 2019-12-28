// Set up express
const express = require('express');
let app = express();

// Set up mongodb
const mongo = require('mongodb');
let MongoClient = mongo.MongoClient;
let db;

// Set up pug view engine
app.set('view engine', 'pug');

// Set up routes
app.use(express.static("public"));
app.use(express.json());
app.get('/', mainMenu);
app.get('/questions', getQuestions);
app.get('/questions/:qID', getQuestionByID);
app.get('/createquiz', createQuiz);
app.get('/quizzes', getQuizzes);
app.get('/quiz/:quizID', getQuiz);
app.post('/quizzes', addQuiz);

// Function for rendering mainmenu/homepage
function mainMenu(req, res, next){
    res.render('home');
}

// Function for getting questions
function getQuestions(req, res, next){
    let difficulty = req.query.difficulty;      // Difficulty query in string: easy, medium, hard
    let category = req.query.category;          // Category query in string: many..

    // If query contains both difficulty and category
    if (difficulty != undefined && category != undefined){
        // Find questions matching those difficulty and category with limit as 25
        db.collection("questions").find({difficulty : difficulty, category : category}).limit(25).toArray(function(err, result){
            if (err) throw err;
            // Format to send json or html on request
            res.format(
                {'application/json' : function () {
                    res.send({ questions : result });
                },
                'text/html' : function () {
                    res.render('list/questions', { questions : result});
                }
            })
        })
    // If query only contains difficulty
    }else if(difficulty != undefined){
        // Find questions containing difficulty
        db.collection("questions").find({difficulty : difficulty}).limit(25).toArray(function(err, result){
            if (err) throw err;
            // Format response
            res.format(
                {'application/json' : function () {
                    res.send({ questions : result });
                },
                'text/html' : function () {
                    res.render('list/questions', { questions : result});
                }
            })
        })
    // If query contains only category
    }else if(category != undefined){
        // Find questions with category
        db.collection("questions").find({category : category}).limit(25).toArray(function(err, result){
            if (err) throw err;
            // Format response
            res.format(
                {'application/json' : function () {
                    res.send({ questions : result });
                },
                'text/html' : function () {
                    res.render('list/questions', { questions : result});
                }
            })
        })
    // If no query specified
    }else{
        // Find first 25 questions
        db.collection("questions").find({}).limit(25).toArray(function(err, result){
            if (err) throw err;
            // Format it
            res.format(
                {'application/json' : function () {
                    res.send({ questions : result });
                },
                'text/html' : function () {
                    res.render('list/questions', { questions : result});
                }
            })
        })
    }
}

// Function for getting specific question with ID
function getQuestionByID(req, res, next){
    let ID;
    // Try to create ID
    try {
        ID = new mongo.ObjectId(req.params.qID);
    }catch (err){
        // Invalid ID
        if (err){ 
            res.status(404).send("Invalid question ID");
            return;
        }
    }
    
    // Find the question with that specific ID
    db.collection("questions").find({'_id' : ID}).toArray(function(err, result){
        if (err) throw err;
        // If there is no questions with that ID
        if (result.length == 0){
            res.status(404).send("Question ID does not exist");
        // If ID is valid
        }else{
            // Format the response
            res.format(
                {'application/json' : function () {
                    res.send({ question : result });
                },
                'text/html' : function () {
                    res.render('single/question', { question : result });
                }
            })            
        }
    })
}

// Function for displaying the create quiz page
function createQuiz(req, res, next){
    // Get all distinct categories
    db.collection("questions").distinct("category", function (err, category){
        if (err) throw err;
        // Get all distinct difficulty
        db.collection("questions").distinct("difficulty", function (err, difficulty){
            if (err) throw err;
            // Render the create quiz page with all the categories and difficulties
            res.render('create/createquiz', {category : category, difficulty : difficulty});
        })
    })
}

// Function for getting and displaying all quizzes
function getQuizzes(req, res, next){
    let creator = req.query.creator;    // Creator query
    let tag = req.query.tag;            // Tag query

    // If creator and tag are both specified
    if (creator != undefined && tag != undefined){
        // Search for those in the database
        db.collection("quizzes").find({creator : {$regex : `${creator}`, $options : 'i'}, tags : {$regex : `^${tag}$`, $options : 'i'}}).toArray(function(err, result){
            if (err) throw err;
            // Format response and send
            res.format(
                {'application/json' : function () {
                    res.send({ quizzes : result });
                },
                'text/html' : function () {
                    res.render('list/quizzes', { quizzes : result});
                }
            })
        })
    // If creator is specified
    }else if(creator != undefined){
        // Search for matching
        db.collection("quizzes").find({creator : {$regex : `${creator}`, $options : 'i'}}).toArray(function(err, result){
            if (err) throw err;
            // Format and send
            res.format(
                {'application/json' : function () {
                    res.send({ quizzes : result });
                },
                'text/html' : function () {
                    res.render('list/quizzes', { quizzes : result});
                }
            })
        })
    // If tag specified
    }else if(tag != undefined){
        db.collection("quizzes").find({tags : {$regex : `^${tag}$`, $options : 'i'}}).toArray(function(err, result){
            if (err) throw err;
            // Format, send
            res.format(
                {'application/json' : function () {
                    res.send({ quizzes : result });
                },
                'text/html' : function () {
                    res.render('list/quizzes', { quizzes : result});
                }
            })
        })
    // If nothing specified
    }else{
        // Search
        db.collection("quizzes").find().toArray(function(err, result){
            if (err) throw err;
            // Format send
            res.format(
                {'application/json' : function () {
                    res.send({ quizzes : result });
                },
                'text/html' : function () {
                    res.render('list/quizzes', { quizzes : result});
                }
            })
        })
    }
}

// Function for getting one quiz
function getQuiz(req, res, next){
    let quizID;
    // Check if ID is valid
    try {
        quizID = new mongo.ObjectId(req.params.quizID);
    }catch (err){
        if (err){
            res.status(404).send("Invalid quiz ID");
            return;
        }
    }
    // Search database for matching ID
    db.collection("quizzes").find({'_id' : quizID}).toArray(function(err, result){
        if (err) throw err;
        // If invalid
        if (result.length == 0){
            res.status(404).send("Invalid quiz ID");
        // If it exists
        }else{
            // Format and send
            res.format(
                {'application/json' : function () {
                    res.send({ quiz : result });
                },
                'text/html' : function () {
                    res.render('single/quiz', { quiz : result });
                }
            })            
        }
    })
}

// Function for adding a new quiz to the database
function addQuiz(req, res, next){
    let quiz = req.body;
    // Validate
    if (quiz.creator != undefined && quiz.tags != undefined && quiz.questions != undefined){
        // Insert into the database
        db.collection("quizzes").insertOne(quiz, function(err, result){
            if(err) throw err;
            console.log("Inserted quiz.");
            // Send back the ID of the quiz
            res.send(result.insertedId);
        });
    }else{
        res.status(400).send("Creator, Quiz or Tags undefined");
    }
}

// Initialize database connection
MongoClient.connect("mongodb://localhost:27017/", function(err, client) {
  if(err) throw err;

  //Get the a4 database
  db = client.db('a4');
  
  // Start server once Mongo is initialized
  app.listen(3000);
  console.log("Listening on port 3000");
});