# Assignment 2
# Ryan Lo
# 101117765
import math # This will import the functions from the math library

# Printing out a set of instructions for the user
print ("When this program begins, you will be prompted with 3 sets of questions for 6 different ingredients. This includes: what your ingredient is, what do you want for your unit of measurement, and how many times will you add the ingredient. You will be asked about these questions 6 times. After that, you will be required  to input the name of your product, how many servings this recipe will produce, and how many servings do you want to make. Once finished, a list of all your information for this recipe will be printed out. Next, you will input the amount of calories that is in a serving. Finally, information about number of calories in a full batch, total calories in serving that is requested by you, and how many serving(s) are required to consume at least 2000 calories. And that is all! Now let's begin!")

# Asking the user to input 3 sets of questions 6 times
ingredient1 = input("What is the first ingredient? ")
unit1 = input("What is the unit of measurement for this ingredient? ")
quantity1 = float(input("How many of this ingredient will you add? "))

ingredient2 = input("What is the second ingredient? ")
unit2 = input("What is the unit of measurement for this ingredient? ")
quantity2 = float(input("How many of this ingredient will you add? "))

ingredient3 = input("What is the third ingredient? ")
unit3 = input("What is the unit of measurement for this ingredient? ") 
quantity3 = float(input("How many of this ingredient will you add? "))

ingredient4 = input("What is the forth ingredient? ")
unit4 = input("What is the unit of measurement for this ingredient? ")
quantity4 = float(input("How many of this ingredient will you add? "))

ingredient5 = input("What is the fifth ingredient? ")
unit5 = input("What is the unit of measurement for this ingredient? ")
quantity5 = float(input("How many of this ingredient will you add? "))

ingredient6 = input("What is the sixth and last ingredient? ")
unit6 = input("What is the unit of measurement for this ingredient? ")
quantity6 = float(input("How many of this ingredient will you add? "))

# Asking the user to input name of product
name = input("What is the name of the product? ")

# Asking the user to input number of individual servings for one set of ingredients
batch = float(input("How many individual servings will adding all these ingredients make? "))

# Asking the user to input the amount of servings that they want to make
servings = float(input("How many servings do you want to make? "))

# Calculating the ratio between number of servings to batches
ratio = float(servings / batch)

# Calculating the amount of ingredients required for user requested batch
ingred1 = float(ratio * quantity1)
ingred2 = float(ratio * quantity2)
ingred3 = float(ratio * quantity3)
ingred4 = float(ratio * quantity4)
ingred5 = float(ratio * quantity5)
ingred6 = float(ratio * quantity6)

# Printing out the user's recipe list
print ("\nFor your recipe of", name, "you will need: \n{:.2f}".format(ingred1), unit1, "of", ingredient1, "\n{:.2f}".format(ingred2), unit2, "of", ingredient2, "\n{:.2f}".format(ingred3), unit3, "of", ingredient3, "\n{:.2f}".format(ingred4), unit4, "of", ingredient4, "\n{:.2f}".format(ingred5), unit5, "of", ingredient5, "\nand {:.2f}".format(ingred6), unit6, "of", ingredient6)

# Asking the user to input amount of calories per serving
calories = int(input("\nWhat is the amount of calories per serving? "))

# Calculating the amount of calories per batch
batchcal = calories * batch
# Calculating the amount of calories in batch requested by user
totalcal = calories * servings
# Calculating the amount of servings needed to reach 2000 calories
twothousand = 2000 / calories

# Printing out a list of calories information for user
print ("\nThe amount of calories for a full batch of", batch, "servings is {:.0f}".format(batchcal), "calories.\nThe amount of calories for the batch requested is {:.0f}".format(totalcal), "calories.\nFor you to have consumed 2000 calories, you will need", math.ceil(twothousand), "serving(s)") 
