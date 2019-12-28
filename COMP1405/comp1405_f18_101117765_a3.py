# Assignment 3
# Ryan Lo
# 101117765

# Anime - Science Fiction
# Dystopia - Science Fiction

# Anime
# Your Name, A Silent Voice, Spirited Away, Garden of the Sinners, Kuroko's Basketball: Last Game
# The Girl Who Leapt Through Time, Fate/Stay Night: Heaven's Feel, Maquia, Steins Gate: Deju Vu, No Game No Life: Zero

# Dystopia
# The Hunger Games, Blade Runner 2049, The Matrix, WALL-E, RoboCop
# The Terminator, A Clockwork Orange, Planet of the Apes, A.I Artificial Intelligence, Children of Men

genre = True
restartflag = True

while restartflag:
	while genre:
		instruction = input("Do you need instructions? ").lower()
		if instruction == "yes":
			print("Hello there, welcome to the expert system for films but only for 20 movies total. First, you will be prompted to pick one of the two sub-genres, Anime or Dystopia. Next, you will be asked a set of questions which will ultimately lead to the movie that you were thinking about. Here is the list of all the movies in which subgenre:\nAnime:\t\t\t\t\tDystopia:\nYour Name\t\t\t\tThe Hunger Games\nA slient Voice\t\t\t\tBlade Runner 2049\nSpirited Away\t\t\t\tThe Matrix\nGarden of the Sinners\t\t\tWALL-E\nKuroko's Basketball: Last Game\t\tRoboCop\nThe Girl Who Leapt Through Time\t\tThe Terminator\nFate/Stay Night: Heaven's Feel\t\tA Clockwork Orange\nMaquia\t\t\t\t\tPlanet of the Apes\nSteins Gate: Deja Vu\t\t\tA.I Artificial Intelligence\nNo Game No Life: Zero\t\t\tChildren of Men")
		subgenre = input("What subgenre do you want to be asked about? Anime or Dystopia? ").lower()
		if not (subgenre == "anime" or subgenre == "dystopia"):
			print("We do not have that genre, try anime or dystopia")
		if subgenre == "anime" or subgenre == "dystopia":
			genre = False
		if subgenre == 'anime':
			anime1 = input("Is the film linked to a TV episode series? ").lower()
			if anime1 == 'yes':
				anime2 = input("Is the film a sequel to the series? ").lower()
				if anime2 == 'yes':
					anime3 = input("Is the film related to basketball? ").lower()
					if anime3 == 'yes':
						movie = ("The movie is Kuroko's Basketball: Last Game.")
					else:
						movie = ("The movie is Steins Gate: Deja Vu.")
				else:
					anime4 = input("Is the film a prequel to the series? ").lower()
					if anime4 == 'yes':
						movie = ("The movie is No Game No Life: Zero.")
					else:
						movie = ("The movie is Fate/Stay Night: Heaven's Feel")
			else:
				anime5 = input("Is the film related to time travel? ").lower()
				if anime5 == 'yes':
					movie = ("The movie is The Girl Who Leapt Through Time.")
				else:
					anime6 = input("Does the film involve murder? ").lower()
					if anime6 == 'yes':
						movie = ("The movie is Garden of the Sinners.")
					else:
						anime7 = input("Does the film involve romance between the main characters? ").lower()
						if anime7 == 'yes':
							anime8 = input("Did the film win anime film of the year in 2018? ").lower()
							if anime8 == 'yes':
								movie = ("The movie is Your Name.")
							else:
								movie = ("The movie is A Silent Voice.")
						else:
							anime9 = input("Does the film involve supernatural events? ").lower()
							if anime9 == 'yes':
								movie = ("The movie is Spirited Away.")
							else:
								movie = ("The movie is Maquia.")
		if subgenre == 'dystopia':
			dys1 = input("Does the film involve robots? ").lower()
			if dys1 == 'yes':
				dys2 = input("Are the robots in the film a threat to humans? ").lower()
				if dys2 == 'yes':
					movie = ("The movie is The Terminator.")
				else:
					dys3 = input("Is Earth is danger in the film? ").lower()
					if dys3 == 'yes':
						movie = ("The movie is WALL-E.")
					else:
						dys4 = input("Does the film involve crimes? ").lower()
						if dys4 == 'yes':
							movie = ("The movie is RoboCop.")
						else:
							movie = ("The movie is A.I Artificial Intelligence.")
			else:
				dys5 = ("Does the film involve survival? ")
				if dys5 == 'yes':
					dys6 = input("Does the film involve near extinction of human race? ").lower()
					if dys6 == 'yes':
						dys7 = input("Does the film involve intelligent animals? ").lower()
						if dys7 == 'yes':
							movie = ("The movie is Planet of the Apes.")
						else:
							movie = ("The movie is Children of Men.")
					else:
						dys8 = input("Does the film involve a fight to the death? ").lower()
						if dys8 == 'yes':
							movie = ("The movie is The Hunger Games.")
						else:
							movie = ("The movie is The Matrix.")
				else:
					dys9 = input("Does the film involve brainwashing? ").lower()
					if dys9 == 'yes':
						movie = ("The movie is Clockwork Orange.")
					else:
						movie = ("The movie is Blade Runner 2049.")


		if subgenre == "anime" or subgenre == "dystopia":
			print(movie)
	
	restart = input("Do you wish to restart? ").lower()
	if restart == "no":
		restartflag = False
	else:
		genre = True
	
			
				
