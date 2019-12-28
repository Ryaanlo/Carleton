# Name: Ryan Lo
# Student Number: 101117765

# Defining Colours
WHITE = (255, 255, 255) # Defining WHITE as (255, 255, 255)
GRAY = (115, 111, 110) # Defining GRAY as (155, 111, 110)
BLACK = (0, 0, 0) # Defining BLACK as (0, 0, 0)
SKYBLUE = (102, 152, 255) # Defining SKY BLUE as (102, 152, 255)
MAHOGONY = (192, 64, 0) # Defining MAHOGONY as (192, 64, 0)
PLATINUM = (229, 228, 226) # Defining PLATINUM as (229, 228, 226)
CRANBERRY = (159, 0 , 15) # Defining CRANBERRY as (159, 0, 15)

# Importing PyGame
import pygame # this will import the functions from the pygame library

# Initializing PyGame
pygame.init() # this will initialize pygame

# Creating a Drawing Window
drawing_window = pygame.display.set_mode((500,500)) # this creates a window to draw on

# Filling in Drawing Window in WHITE
drawing_window.fill((WHITE)) # this will fill the surface with a near white(red = 255, green = 255, blue = 255) colour


# Top of the Car (Hood)
pygame.draw.ellipse(drawing_window, (BLACK), (150,200,200,100)) # this will draw the very top part of the car in BLACK(red = 0, green = 0, blue = 0)

pygame.draw.ellipse(drawing_window, (PLATINUM), (185,215,120,75)) # this will draw inner part of top also known as the window in PLATINUM(red = 299, green = 228, blue = 226)

pygame.draw.polygon(drawing_window, (BLACK), ((205,250),(200,250),(195,230),(200,225))) # this will draw the window divider in BLACK(red = 0, green = 0, blue = 0)


# Front of the Car
pygame.draw.ellipse(drawing_window, (CRANBERRY), (275,248,150,80)) # this will draw the upper part of front in CRANBERRY(red = 159, green = 0, blue = 15)

pygame.draw.ellipse(drawing_window, (CRANBERRY), (305,260,130,80)) # this will draw the lower part of front in CRANBERRY(red = 159, green = 0, blue = 15)

pygame.draw.polygon(drawing_window, (PLATINUM), ((395,280),(405,280),(420,300),(410,300))) # this will draw the headlight frame in the front of the car in PLATINUM(red = 299, green = 228, blue = 226)

pygame.draw.circle(drawing_window, (SKYBLUE), (410,294), (5)) # this will draw the headlight in SKY BLUE(red = 102, green = 152, blue = 255)

pygame.draw.polygon(drawing_window, (BLACK), ((412,310),(432,310),(412,335))) # this will draw the front mouth of the car in BLACK(red = 0, green = 0, blue = 0)


# Car Rear
pygame.draw.rect(drawing_window, (BLACK), (82, 315, 20, 10)) # this will draw the car's rear exhaust in BLACK(red = 0, green = 0, blue = 0)

pygame.draw.ellipse(drawing_window, (CRANBERRY), (80,260,150,80)) # this will draw the bottom part of the rear of car in CRANBERRY(red = 159, green = 0, blue = 15)

pygame.draw.ellipse(drawing_window, (CRANBERRY), (75,245,130,60)) # this will draw the top part of the rear of car in CRANBERRY(red = 159, green = 0, blue = 15)

pygame.draw.rect(drawing_window, (PLATINUM), (80, 270, 15, 10)) # this will draw the rear headlights in PLATINUM(red = 299, green = 228, blue = 226)

pygame.draw.circle(drawing_window, (WHITE), (61,280), (23)) # this will draw a white(red = 255, green = 255, blue = 255) circle in the rear of the car to overlap with a part of the rear to create a unique curve design

pygame.draw.rect(drawing_window, (WHITE), (50, 330, 400, 400)) # this will draw a white(red = 255, green = 255, blue = 255) box below the car to remove any unnecessary parts of shape bulging out


# Car Body
pygame.draw.rect(drawing_window, (CRANBERRY), (125, 250, 250, 80)) # this will draw the main body of the car in CRANBERRY(red = 159, green = 0, blue = 15)

pygame.draw.rect(drawing_window, (MAHOGONY), (200, 250, 100, 40)) # this will draw the door of the car in MAHOGONY(red = 192, green = 64, blue = 0)

pygame.draw.rect(drawing_window, (BLACK), (220, 260, 10, 5)) # this will draw the door handle of the car in BLACK(red = 0, green = 0, blue = 0)


# Left Tire
pygame.draw.circle(drawing_window, (BLACK), (170,330), (33)) # this will draw a black(red = 0, green = 0, blue = 0) circle as the outline of the tire

pygame.draw.circle(drawing_window, (GRAY), (170,330), (30)) # this will draw a gray(red = 115, green = 111, blue = 110) circle as the tire

pygame.draw.circle(drawing_window, (WHITE), (170,330), (20)) # this will draw a white(red = 255, green = 255, blue = 255) circle as the rim

pygame.draw.circle(drawing_window, (BLACK), (170,330), (10)) # this will draw a black(red = 0, green = 0, blue = 0) circle as the most inner part of the tire


# Right Tire
pygame.draw.circle(drawing_window, (BLACK), (340,330), (33)) # this will draw a black(red = 0, green = 0, blue = 0) circle as the outline of the tire

pygame.draw.circle(drawing_window, (GRAY), (340,330), (30)) # this will draw a gray(red = 115, green = 111, blue = 110) circle as the tire 

pygame.draw.circle(drawing_window, (WHITE), (340,330), (20)) # this will draw a white(red = 255, green = 255, blue = 255) circle as the rim 

pygame.draw.circle(drawing_window, (BLACK), (340,330), (10)) # this will draw a black(red = 0, green = 0, blue = 0) circle  as the most inner part of the tire


# Updating PyGame
pygame.display.update() # this updates what you drew onto the screen

# Delaying PyGame
pygame.time.delay(6000) # this will keep the window open for 6 secs

# Saving Image
pygame.image.save(drawing_window, "car_101117765.bmp") # this will save the image drawn

# Quitting PyGame
pygame.quit() # this will exit pygame



