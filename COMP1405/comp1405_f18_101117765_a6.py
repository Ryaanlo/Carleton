# Assignment 6
# Ryan Lo
# 101117765

'''
This is the board input function which is responsible for taking a user input
and having the user create the current state of a chess board

@params		none
@return		board

'''

def board_input():
	bpiece = ['K','Q','B','N','R','P'] # list of all black pieces
	wpiece = ['k','q','b','n','r','p'] # list of all white pieces
	bcounter1 = 0 # counter for number of black kings
	bcounter2 = 0 # counter for number of black queens
	bcounter3 = 0 # counter for number of black bishops
	bcounter4 = 0 # counter for number of black knights
	bcounter5 = 0 # counter for number of black rooks
	bcounter6 = 0 # counter for number of black pawns
	wcounter1 = 0 # counter for number of white kings
	wcounter2 = 0 # counter for number of white queens
	wcounter3 = 0 # counter for number of white bishops
	wcounter4 = 0 # counter for number of white knights
	wcounter5 = 0 # counter for number of white rooks
	wcounter6 = 0 # counter for number of white pawns
	board = [] # define the empty board
	number_of_rows = ['1','2','3','4','5','6','7','8'] # list of row number
	for i in range(8): # for loop for 8 rows
		inputflag = True # user input validation flag
		row = [] # defining empty row
		while inputflag:
			errorcounter = 0 # error counter
			row_input = input("Please insert row "+ number_of_rows[i] + ": ") # asking user to insert row of chessboard
			if (len(row_input)) == 8: # check to see of length is 8
				for test in row_input: # test each character in user input
					if (test not in bpiece) and (test not in wpiece) and (test != '-'): # if that character is not in either black or white pieces list or is not empty using '-'
						errorcounter += 1 # add an error count
				if errorcounter == 0: # if there are no errors
					for char in row_input: # check each character in the user input
						if char == 'K': # if charcter equals to this then add to the counter
							bcounter1 += 1
						elif char == 'Q': # if charcter equals to this then add to the counter
							bcounter2 += 1
						elif char == 'B': # if charcter equals to this then add to the counter
							bcounter3 += 1
						elif char == 'N': # if charcter equals to this then add to the counter
							bcounter4 += 1
						elif char == 'R': # if charcter equals to this then add to the counter
							bcounter5 += 1
						elif char == 'P': # if charcter equals to this then add to the counter
							bcounter6 += 1
						elif char == 'k': # if charcter equals to this then add to the counter
							wcounter1 += 1
						elif char == 'q': # if charcter equals to this then add to the counter
							wcounter2 += 1
						elif char == 'b': # if charcter equals to this then add to the counter
							wcounter3 += 1
						elif char == 'n': # if charcter equals to this then add to the counter
							wcounter4 += 1
						elif char == 'r': # if charcter equals to this then add to the counter
							wcounter5 += 1
						elif char == 'p': # if charcter equals to this then add to the counter
							wcounter6 += 1
					if bcounter1 > 1 or wcounter1 > 1 or bcounter2 > 1 or wcounter2 > 1 or bcounter3 > 2 or wcounter3 > 2 or bcounter4 > 2 or wcounter4 > 2 or bcounter5 > 2 or wcounter5 > 2 or bcounter6 > 8 or wcounter6 > 8: # if any of these conditions do not pass then
						print("You have too many pieces for a specific type of piece, please try again") # let user know
						for char in row_input: # check every character in user input
							if char == 'K': # revert any operations
								bcounter1 -= 1
							elif char == 'Q': # revert any operations
								bcounter2 -= 1
							elif char == 'B': # revert any operations
								bcounter3 -= 1
							elif char == 'N': # revert any operations
								bcounter4 -= 1
							elif char == 'R': # revert any operations
								bcounter5 -= 1
							elif char == 'P': # revert any operations
								bcounter6 -= 1
							elif char == 'k': # revert any operations
								wcounter1 -= 1
							elif char == 'q': # revert any operations
								wcounter2 -= 1
							elif char == 'b': # revert any operations
								wcounter3 -= 1
							elif char == 'n': # revert any operations
								wcounter4 -= 1
							elif char == 'r': # revert any operations
								wcounter5 -= 1
							elif char == 'p': # revert any operations
								wcounter6 -= 1
					else: # if it passes all the conditions then
						inputflag = False 
						for char in row_input: # for each character in the use input
							row.append(char) # append it to the row
				else: # if there are any errors
					print("You inputted an invalid piece or failed to input '-' as an empty spot, please try again")
			else: # if inputted not enough or too many characters
				print("You either have too many or not enough characters, please try again")
		board.append(row) # append the row to the main board
	print()
	for board_row in range(len(board)): # printing the board
		print(board[board_row])
	print()
	return board # returns back the board

'''
This is the current state function which is responsible for 
displaying the current state of the chess board

@params		board
@return		none

'''

def currentstate(board):
	for i in range(len(board)): # for every list in the list board
		print(board[i]) # print the list
	
'''
This is the score function which is responsible analyzing the board
to see which side is winning currently

@params		board
@return		none

'''
	
def score(board):
	white = 0 # assign white score
	black = 0 # assign black score
	bpiece = ['K','Q','B','N','R','P'] # list of all black pieces
	wpiece = ['k','q','b','n','r','p'] # list of all white pieces
	value = [0, 10, 5, 3.5, 3, 1] # list of values corresponding to piece at specific location in list
	for i in range(len(board)): # loop for y length of board
		for j in range(len(board[i])): # loop for x length of board 
			if board[i][j] in bpiece: # if the location on the board is one of the black pieces
				for k in range(len(bpiece)): # loop for length of all black pieces
					if board[i][j] == bpiece[k]: # if they match
						black += value[k] # add the value associated with piece to black
			elif board[i][j] in wpiece: # if the location on the board is one of the white pieces
				for k in range(len(wpiece)): # loop for length of all white pieces
					if board[i][j] == wpiece[k]: # if they match
						white += value[k] # add the value associated with piece to white
	print("White has", white, "points. Black has", black, "points.")
	if white > black: # if score of white is greater than score of black
		print("White is currently winning!") # let user know that white is winning
	elif black > white: # if score of black is greater than score of white
		print("Black is currently winning!") # let user know that black is winning
	else: # if scores are the same
		print("It's a tie :/") # let user know that it is a tie

'''
This is the move function which is responsible for taking user inputs
of coordinates for the piece to be moved and where it is being
moved to and moves the pieces. 

@params		board, x1, y1, x2, y2
@return		board

'''

def move(board, coordlist):
	invalidcounter = 0
	x1 = coordlist[0] # assigns x1 as the first number in list of coordinates
	y1 = coordlist[1] # assigns y1 as the second number in list of coordinates
	x2 = coordlist[2] # assigns x2 as the third number in list of coordinates
	y2 = coordlist[3] # assigns y2 as the forth number in list of coordinates
	bpiece = ['K','Q','B','N','R','P'] # list of all black pieces
	wpiece = ['k','q','b','n','r','p'] # list of all white pieces
	for invalid in coordlist: # for every coordinate in the list
		if invalid > 8 or invalid < 1: # if it's not in between 1 to 8 
			invalidcounter += 1 # add to the counter
	if invalidcounter == 0: # if coordinates are between 1 to 8
		if board[y1 - 1][x1 - 1] == '-': # if the spot is empty
			print("There is no piece at the provided coordinates to move") # tell user that there's nothing to move
		else: # if it's not empty
			piece = board[y1 - 1].pop(x1 - 1) # pop it and return the piece
			board[y1 - 1].insert((x1 - 1), '-') # replace the spot with empty
			board[y2 - 1].pop(x2 - 1) # pop the thing in the location to be moved to 
			board[y2 - 1].insert((x2 - 1), piece) # insert that piece into that location
	else: # if not in boundary or invalid
		print("The coordinates that you entered are out of bound or invalid") # let user know
	print()
	score(board) # runs the score function which computes the score to see who is winning
	print()
	return board

'''
This is the integer function which if the strig is a number,
it will convert to integer otherwise it will return a 0

@params		coord
@return		number

'''

def integer(coord):
	number = 0 # assign number to 0
	numberlist = [0,1,2,3,4,5,6,7,8,9] # list of integers for all single digit integers
	numberslist = ['0','1','2','3','4','5','6','7','8','9'] # list of integers in string of all single digit numbers
	for j in range(len(numberlist)): # loop for length of number of single digit integers
		if coord[0] in numberslist[j]: # if that coordinate is in string numberlist at specific location
			number += numberlist[j] # add that integer from the integer list
	return number # returns the number

'''
This is the main function which is responsible
for the whole program

@params		none
@return		none

'''

def main():
	board = [['-','-','-','-','-','-','-','-'], # defining initial empty board
		['-','-','-','-','-','-','-','-'],
		['-','-','-','-','-','-','-','-'],
		['-','-','-','-','-','-','-','-'],
		['-','-','-','-','-','-','-','-'],
		['-','-','-','-','-','-','-','-'],
		['-','-','-','-','-','-','-','-'],
		['-','-','-','-','-','-','-','-']]
	instruction = input("Do you require any instructions? (y or yes) ") # asking user for instructions
	if instruction == 'yes' or instruction == 'y': # if user input for instructions is yes
		print("Hello, welcome to the chess game in progress simulation. You will be provided with 4 options: Input the current state of the chessboard, calculate the score to see which side is winning, move a chess piece, or print the current state of the chess board. When you are inputting the current state of the chessboard, you will be required to input q(queen), k(king), n(knight), b(bishop), r(rook), p(pawn). Lowercase will be associated with the white team and uppercase for black. Any empty spots should use '-'. When moving a chess piece you will be required to enter the coordinates for the piece you want to move and coordinates for the location that you want to move the piece to.") # printing instructions for user
	optionflag = True # flag validation for option
	options = ["1. Input current state of board (i)","2. Calculate the score (c)","3. Move pieces (m)","4. Print current state of chessboard (p)","5. Quit (or q to quit)"] # list of options
	while optionflag:
		print("Here are the options, please input the number associated with the option or the key letter in brackets:")
		for option in options: # for every option in option list
			print(option) # print each option
		option = input("What would you like to do? ") # asking user what to do
		if option == '1' or option == 'i': # if user input 1 for inputting current state of board
			board = board_input() # runs the input board function and assigns to variable board
		elif option == '2' or option == 'c': # if user input 2 for calculating the score
			print() # prints an empty line to make it more readable
			score(board) # runs calculating score function
			print() # prints another empty line to allow for easier readablity
		elif option == '3' or option == 'm': # if user input 3 for moving a piece
			coordflag = True # flag for validating number of characters inputted
			while coordflag:
				x1 = input("What is x coord of piece to be moved? ") # asking for x coordinate of piece to be moved
				y1 = input("What is y coord of piece to be moved? ") # asking for y coordinate of piece to be moved
				x2 = input("What is x coord of piece to be moved to? ") # asking for x coordinate of piece to be moved to
				y2 = input("What is y coord of piece to be moved to? ") # asking for y coordinate of piece to be moved to
				if len(x1) == 1 and len(y1) == 1 and len(x2) == 1 and len(y2) == 1: # if length of all inputs equal one
					coordflag = False # end loop
				else: # if length not equal to one
					print("Please enter a single integer for the coordinate") # let user know
			x1 = integer(x1) # runs function integer of x1 to return interger value or 0
			y1 = integer(y1) # runs function integer of y1 to return interger value or 0
			x2 = integer(x2) # runs function integer of x2 to return interger value or 0
			y2 = integer(y2) # runs function integer of y2 to return interger value or 0
			coordlist = [x1,y1,x2,y2] # put all the coordinates into a list
			board = move(board,coordlist) # runs move function and assigns it to board
		elif option == '4' or option == 'p': # user input 4 prints the board
			print()
			currentstate(board) # runs the function that prints the board
			print()
		elif option == '5' or option == 'q': # if user input 5 exit loop and end program
			optionflag = False
		else: # if not a valid input
			print("That is not an option, please try again") # let user know

main()

