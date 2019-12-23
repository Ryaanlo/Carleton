# Assignment 5
# Ryan Lo
# 101117765

'''
This is the loading function which is responsible for taking a user input
for a file and returning the file contents

@params		text
@return		text (return the text as initial and current text)

'''

def loading(text):
	try:
		filetext = open(text, "r") # creates a link to the file
		text = filetext.read() # opens and read the contents inside the file
		return(text.upper()) # returning the contents in uppercase
		filetext.close() # closes the file
	except FileNotFoundError: # if file not found
		print("That file doesn't exist") 
		return('') # return empty current and initial text

'''
This is the saving function which is responsible for 
saving the name of the file from user input and 
write the current text into a text file

@params		name, text
@return		none

'''

def saving(name,text):
	savefile = open(name, "w") # creates a file named by user input
	print (text, file = savefile) # writes the content of current text into file
	savefile.close() # closes the file

'''
This is the enciphering function which is responsible for 
taking the initial text and the alphabet and create a new
text from it

@params		text, code
@return		encode (return the current text encoded)

'''

def enciphering(text, code):
	alpha = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"]
	encode = ""
	for j in range(len(text)): # check each letter in text
		if text[j] not in alpha: # if not in the list of alphabets
			encode += text[j] # add the character as is
		for i in range(len(alpha)): # check each alphabet
			if text[j] == alpha[i]: # if they match with text
				encode = encode + str(code[i]) # add the alphabet corresponding to position in code
	return (encode) # returns the encoded text

'''
This is the deciphering function which is responsible for 
taking the current text and the alphabet and create the original
text from it

@params		text, code
@return		decode (return the current text decoded)

'''

def deciphering(text, code):
	alpha = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"]
	decode= ""
	for j in range(len(text)): # check each letter in text
		if text[j] not in alpha: # if not in list of alphabet
			decode += text[j] # add the character as is
		for i in range(len(code)): # check each alphabet in code
			if text[j] == code[i]: # if they match with the text
				decode = decode + str(alpha[i]) # add the alphabet corresponding to position in alphabet
	return (decode) # return the decoded text

'''
This is the cryptogram function which is responsible for 
allowing the user to create a custom alphabet

@params		none
@return		code (returns the custom alphabet)

'''

def cryptogram_alphabet():
	letter = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"]
	unmodified = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	missingletter = 0
	code = input("Enter the code: ").upper() # ask user for custom alphabet
	filelength = (len(code)) # check the length of the custom alphabet
	if filelength == 26: # if it is 26 alphabets
		for i in range(len(letter)): # check each alphabet 
			if letter[i] not in code: # if that alphabet is not in the code
				print("You forgot", letter[i]) # let user know they forgot an alphabet
				missingletter += 1 # counter for missing alphabet
		if missingletter == 0: # if everything is fine
			return (code) # returns the custom alphabet
		else: # if they are missing an alphabet
			print("Returning to unmodified alphabet")
			code = unmodified # return code to unmodified alphabet
			return (code) # returns the alphabet
	elif filelength < 26: # if the custom alphabet is less than 26
		for i in range(len(letter)): # check for each alphabet
			if letter[i] not in code: # if the alphabet is not in code
				print("You forgot", letter[i]) # let them know
		print("Returning unmodified alphabet")
		code = unmodified # return to unmodified alphabet
		return (code) # returns the alphabet
	else: # if custom alphabet is greater than 26
		print("You have duplicate letters or non letter characters, returning unmodified alphabet.") # either have dup or non alphabetic characters
		code = unmodified # return to unmodified alphabet
		return (code) # returns the code

'''
This is the main menu function which is responsible for 
displaying instructions and displaying the initial text,
current text, and the custom alphabet

@params		load, current, code
@return		none

'''

def mainmenu(load, current, code):
	print("\nWelcome to the main menu. \nPlease begin by inputting the number corresponding to the option or the word in the corresponding option")
	print("This is the intial/loaded text:", load)
	print("This is the currrent text:", current)
	print("This is the current alphabet:", code)
	print("Here are a list of options that you could do:\n\n1.Saving a file\t(save or saving or s)\n2.Loading a file\t(load or loading or l)\n3.Defining an Alphabet\t(define or def or defining)\n4.Enciphering\t(encipher or enciphering or e)\n5.Deciphering\t(decipher or decipering or d)\n6.Quit\t(q or quit)")

'''
This is the main menu function which is responsible for 
the user interface and the whole program

@params			none
@return	none

'''

def main():
	mainflag = True # main loop for main menu
	optionflag = True # loop for correct user input for option
	loadtext = "" # loaded text
	currenttext = "" # current text
	code = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" # the alphabet
	while mainflag:
		mainmenu(loadtext, currenttext, code) # display main menu
		optionflag = True 
		while optionflag:
			option = input("What would you like to do? ").lower() # ask user what they want to do
			if option == 'load' or option == 'l' or option == 'loading' or option == '2':
				optionflag = False
				filename = input("Enter the filename: ") # filename used for loading file
				loadtext = loading(filename) # loading function with user input for file name
				currenttext = loadtext # change current text into the return of loading function
			elif option == 'save' or option == 's' or option == 'saving' or option == '1':
				optionflag = False
				if currenttext != '': # if current text is not empty
					filename = input("Enter the filename: ") # ask user for name of saving file
					textsave = currenttext
					saving(filename, textsave)
				else: # if current text empty then nothing to save
					print("Nothing to save. Please try loading a file, defining the alphabet, and either encode or decode first.")
			elif option == 'define' or option == 'def' or option == 'defining' or option == '3':
				optionflag = False
				code = cryptogram_alphabet() # run custom alphabet function
			elif option == 'enciphering' or option == 'e' or option == 'encipher' or option == '4':
				optionflag = False
				if loadtext == '':
					print("Text being enciphered not defined, please load the text first")
				else:
					encode = enciphering(loadtext, code) # run encode function
					currenttext = encode
			elif option == 'deciphering' or option == 'd' or option == 'decipher' or option == '5':
				optionflag = False
				if loadtext == '':
					print("Text being deciphered not defined, please load the text first")
				else:
					decode = deciphering(currenttext, code) # run decode function
					currenttext = decode 
			elif option == 'q' or option == 'quit' or option == '6':
				optionflag = False
				mainflag = False
			else:
				optionflag = False
				print("Not a valid option")


main()
