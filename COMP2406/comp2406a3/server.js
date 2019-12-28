const express = require('express');
const fs = require("fs");
const path = require('path');
let app = express();

app.use(express.static("public"));

const uuidv4 = require('uuid/v4');

// Route handlers
app.use(express.json());

app.get('/', getQuestions);

app.get('/questions', getQuestions);

app.post('/sessions', newSession);

app.get('/sessions', getSession);

app.delete('/sessions/:sessionid', deleteSession);

// Function for getting questions
function getQuestions(req, res, next){
    // Array to store all matching questions
    let array = [];
    // The respomse
    let response;
    // Boolean for if token is valid
    let validToken = false;
    
    // Store session data
    let sessionRequest;
    let id;

    // Limit Parameters
    // Defaults to 10 if undefined
    let limit = req.query.limit;
    if (limit == undefined){
        limit = 10;
    }

    // Saving all query to a variable
    let difficulty = req.query.difficulty;
    let category = req.query.category;
    let token = req.query.token;

    // Checking to see if there is a token is specified
    if (token != undefined){
        fs.readdir("./sessions", function(err, sessions){
            // Loop over all sessions and see if they match
            for (let session = 0; session < sessions.length; session ++){
                let file = fs.readFileSync(path.join("./sessions", sessions[session]), "utf8");
                let data = JSON.parse(file);

                // Check if matching token with id
                if (data.id == token){
                    // Set boolean to true and save the data to a variable
                    validToken = true;
                    sessionRequest = data.request;
                    id = data.id;
                    break;
                }
            }
        });
    }

    // Go through all the questions
    fs.readdir("./questions", function(err, questions){
        for (let question = 0; question < questions.length; question ++){
            let data = fs.readFileSync(path.join("./questions", questions[question]));
            let q = JSON.parse(data);

            // If token is not specified
            if (token == undefined){
                // Checking how many if the questions match the request difficulty and/or category
                // Add all questions that match to the array for testing
                if (difficulty != undefined && category != undefined){
                    if (q.difficulty_id == difficulty && q.category_id == category){
                        array.push(q);
                    }
                }else if(difficulty != undefined){
                    if (q.difficulty_id == difficulty){
                        array.push(q);        
                    }
                }else if(category != undefined){
                    if (q.category_id == category){
                        array.push(q);        
                    }
                }else{
                    array.push(q);
                }
            // If token is specified
            }else{
                // If token is valid
                if (validToken){
                    // Same thing as the above where it checks all questions to see if they match request
                    // But instead it also checks if the session has seen the question before
                    // If all conditions succeed then add to array for final testing
                    if (difficulty != undefined && category != undefined){
                        if (q.difficulty_id == difficulty && q.category_id == category){
                            if (!sessionRequest.includes(q.question_id)){
                                array.push(q);
                            }
                        }
                    }else if(difficulty != undefined){
                        if (q.difficulty_id == difficulty){
                            if (!sessionRequest.includes(q.question_id)){
                                array.push(q);
                            }
                        }
                    }else if(category != undefined){
                        if (q.category_id == category){
                            if (!sessionRequest.includes(q.question_id)){
                                array.push(q);
                            }  
                        }
                    }else{
                        if (!sessionRequest.includes(q.question_id)){
                            array.push(q);
                        }
                    }

                // If id doesn't exist
                }else{
                    // Send status 2 response
                    response = {status : 2};
                    res.status(200).send(response);
                    return;   
                }      
            }
        }

        // Array 
        let resArray = [];
        
        // Check to see if there's enough questions
        // If there isn't enough
        if (limit > array.length){
            // Set response to status 1
            response = {status : 1};
        // If there is enough questions
        }else{
            // Add the limit amount of questions
            for (let i = 0; i < limit; i ++){
                // Randomize questions
                let rand = Math.floor(Math.random() * array.length)
                resArray[i] = array[rand];
                // If there's a valid token
                if (validToken){
                    // Add the question id to the session requested seen
                    sessionRequest.push(array[rand].question_id);
                }
                array.splice(rand, 1);
            }

            // If there's valid token
            if (validToken){
                // Update session requested questions
                fs.writeFileSync('./sessions/' + id + ".txt", JSON.stringify({ "id": id , "request" : sessionRequest }));
            }

            response = {status : 0, results : resArray};
        }

        res.status(200).send(response);
    });
}

// Function for generating a new session
function newSession(req, res, next){
    // Generate id
    let id = uuidv4();
    // Write to file and send response
    fs.writeFileSync('./sessions/' + id + ".txt", JSON.stringify({ "id": id , "request" : [] }));
    res.status(201).send(id);
}

// Function for getting all sessions
function getSession(req, res, next){
    let array = [];
    // Go through all the sessions and add to array then send it
    fs.readdir("./sessions", function(err, sessions){
        for (let session = 0; session < sessions.length; session ++){
            let file = fs.readFileSync(path.join("./sessions", sessions[session]), "utf8");
            let data = JSON.parse(file);
            array.push(data.id);
        }
        res.status(200).send(array);
    });    
}


// Function for deleting a session
function deleteSession(req, res, next){
    // Go through all the sessions
    fs.readdir("./sessions", function(err, sessions){
        for (let session = 0; session < sessions.length; session ++){
            let token = req.params.sessionid;
            let file = fs.readFileSync(path.join("./sessions", sessions[session]), "utf8");
            let data = JSON.parse(file);
            // If it matches then delete
            if (data.id == token){
                fs.unlinkSync(path.join("./sessions", sessions[session]));
                res.status(200).send();
                return;
            }
        }
        res.status(404).send();
    });    
}

app.listen(3000);
console.log("Server listening at http://localhost:3000");