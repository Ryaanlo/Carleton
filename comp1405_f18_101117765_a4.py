# Assignment 4
# Ryan Lo
# 101117765

# This will import the pygame library functions
import pygame

# This will initiate pygame
pygame.init()

# This will ask the user if they require instructions
instruction = input("Do you require instructions? ").lower()
if instruction == 'yes' or instruction == 'y':
	print ("Hello, welcome to the spooky ghost picture generator. First, you will be asked to input the desired background picture as well as the ghost picture. Next, a window with a background will appear. Then you will be asked either the option of inputting the coordinates of the ghost manually using the terminal or using your mouse to click on a location where you would like the ghost to be drawn. After about a second, the ghost will appear on the screen.")

# Ask the user to input background picture file name
backgroundpic = input("Please enter the background picture's file name: ")

# Ask the user to input ghost picture file name
ghostpic = input("Please enter the ghost picture's file name: ")

# This will load the background image
background = pygame.image.load(backgroundpic)

# This will load the ghost image
ghost = pygame.image.load(ghostpic)

# This will get the size of the background image
(bx,by) = background.get_rect().size

# This will create a window for the images
window = pygame.display.set_mode((bx,by))

# This will copy the background image onto the window
window.blit(background, (0,0))

# This will update pygame's display to show the background that was just copied
pygame.display.update()

# This will get the size of the ghost image
(gx,gy) = ghost.get_rect().size

# This will calculate half of the length of the ghost picture which will be used to check for ghost going off the screen
halfx = gx//2
halfy = gy//2

# This will calculate the maximum x and y values that will allow the ghost to fit onto the background
xvalue = bx - halfx
yvalue = by - halfy

# This will create a flag to loop for the correct values
coordinate = True # flag for correct terminal input coordinates
mouseflag = True # flag for mouse click
mouseorterm = True # flag for terminal or mouse option

# This will create the font and size of the coordinates when using mouse
mousecoord = pygame.font.SysFont('Comic Sans MS', 30)

# Main loop asking for terminal or mouse to determine ghost coordinates
while mouseorterm:
	option = input("Would you like to use the terminal or mouse? ") # Asking user what they would like to use
	if option == 'terminal' or option == 't':
		mouseorterm = False # stop loop for mouse or terminal
		# This will test for coordinate values that will allow the ghost to fit onto the screen
		while coordinate:
			ghostx = int(input("Please input the x-coordinate for the ghost ")) # Asking user for x-coordinate of ghost
			ghosty = int(input("Please input the y-coordinate for the ghost ")) # Asking user for y-coordinate of ghost
			if (ghostx < xvalue and ghostx >= halfx) and (ghosty < yvalue and ghosty >= halfy): # test to see if ghost will fit
				coordinate = False # End loop for correct coordinates
			else:
				print("The ghost won't fit")
				if (ghostx > xvalue): # test to see if ghost is too far to the right
					print("Try lowering the x-value")
				if (ghosty > yvalue): # test to see if ghost is too far down
					print("Try lowering the y-value")
				if (ghostx < halfx): # test to see if ghost is too far left
					print("Try raising the x-value")
				if (ghosty < halfy): # test to see if ghost is too far up
					print("Try raising the y-value")

	elif option == 'mouse' or option == 'm':
		print ("Please start by clicking anywhere on the screen")
		mouseorterm = False # stop loop for mouse or terminal
		while mouseflag:
			for event in pygame.event.get(): # looping for any mouse inputs
				if event.type == pygame.MOUSEMOTION: # if mouse moves
					x,y = event.pos # get coordinates of mouse and assign to x and y
					text = (x,y) # assigning x and y to variable text
					text = str(text) # changing variable text into a string
					textsurface = mousecoord.render(text, False, (255, 20, 147)) # renders the text
					window.blit(background, (0,0)) # replaces with new background everytime mouse moves to remove previous coordinates
					window.blit(textsurface,(x,y)) # copies coordinates onto screen
					pygame.display.update() # updates the display so user can see coordinates
				# This will test for any mouse clicks
				if event.type == pygame.MOUSEBUTTONDOWN: # if mouse clicks
					window.blit(background, (0,0)) # replaces background to remove the coordinates
					ghostx, ghosty = event.pos # gets coordinates of mouse and assigns to variable ghostx and ghosty
					if (ghostx < xvalue and ghostx >= halfx) and (ghosty < yvalue and ghosty >= halfy): # test to see if ghost will fit
						mouseflag = False
					else:
						print("The ghost won't fit")
						if (ghostx > xvalue): # test to see if ghost is too far to the right
							print("Try clicking more to the left")
						if (ghosty > yvalue): # test to see if ghost is too far down
							print("Try clicking higher")
						if (ghostx < halfx): # test to see if ghost is too far left
							print("Try clicking more to the right")
						if (ghosty < halfy): # test to see if ghost is too far up
							print("Try clicking lower")

	else: # if user doesn't pick terminal or mouse
		print("We don't have that option. We only have terminal or mouse. Please try any one of those two options.")

# This will generate the ghost
for y in range(gy): # for loop for y dimension
	for x in range(gx): # for loop for x dimension
		(r1, g1, b1, _) = ghost.get_at((x, y)) # checking and getting all colour values of ghost picture
		if g1 != 255: # checking for non full green pixel
			(r2, g2, b2, _) = window.get_at((ghostx + x - halfx, ghosty + y - halfy)) # checking and getting all colour values of the background
			r = (r1 + r2)/2 # Taking the average of the background and ghost's red colours
			g = (g1 + g2)/2 # Taking the average of the background and ghost's green colours
			b = (b1 + b2)/2 # Taking the average of the background and ghost's blue colours
			window.set_at((ghostx + x - halfx, ghosty + y - halfy), (r, g, b)) # copying the ghost onto the background with transparency

# Let the user know ghost has appeared
print ("The Ghost has appeared!")

# This will update pygame to display the ghost that has been generated
pygame.display.update()

# This will close pygame when the user tries to close the window
while True:
	for event in pygame.event.get():
		if event.type == pygame.QUIT:
			quit()
	pygame.display.update()
