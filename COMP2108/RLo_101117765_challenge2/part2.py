# Challenge 2 Part 2:
# Write a Python program to determine the salt given the password and SHA256 hash value 
# The salt value is a sequence of 8 lowercase ascii characters

import string
import hashlib
from itertools import product, count

def bruteForce(hashed_Password, password, encoding):
    # Iterate on each possible salt value and check if it matches the sha256 hash provided
    # The function should return the salt value 
    for char in product(string.ascii_lowercase, repeat = 8):
        salt = ''.join(char)

        if hashlib.sha256((password + salt).encode(encoding)).hexdigest() == hashed_Password:
            return salt

    print("Function not defined")

def checkSalt(salt, password, hashed_password):
    # simple check to see if a salt and password match a known hash
    if hashlib.sha256((password + salt).encode(encoding)).hexdigest() == hashed_password:
        print("Salt value is correct for the given password")
    else:
        print("Salt value is incorrect for the given password")

if __name__ == "__main__":
    encoding = 'ascii'
    password = "comp2108"
    hashed_password = "64422e664eb16e0ee7c292b14953faeaf2f87fa5f162cce3f26e59a7584f99f5"
    saltValue = bruteForce(hashed_password, password, encoding)
    print(f"Salt value: {saltValue}")
    # sample values to check if you code works, checkSalt(salt, password, hashValue)
    checkSalt(saltValue,"comp2108","64422e664eb16e0ee7c292b14953faeaf2f87fa5f162cce3f26e59a7584f99f5")

    with open("part2.txt", "w") as file:
        file.write(saltValue)