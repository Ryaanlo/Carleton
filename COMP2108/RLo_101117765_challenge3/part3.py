import requests
import string
import random

usernames = ['michael', 'john', 'david', 'chris', 'mike', 'james', 'mark', 'jason', 'robert', 'jessica', 'sarah', 'jennifer', 'paul', 'brian', 'kevin', 'daniel', 'ryan', 'matt', 'andrew', 'michelle', 'steve', 'lisa', 'alex', 'joe', 'amanda', 'ashley', 'scott', 'richard', 'eric', 'jeff', 'justin', 'karen', 'linda', 'mary', 'adam', 'melissa', 'matthew', 'nick', 'stephanie', 'anthony', 'tom', 'josh', 'laura', 'tim', 'jim', 'amy', 'peter', 'dan', 'nicole', 'tony', 'steven', 'susan', 'kelly', 'dave', 'brandon', 'maria', 'ben', 'kim', 'julie', 'sam', 'jonathan', 'rachel', 'joseph', 'christopher', 'william', 'heather', 'bill', 'katie', 'thomas', 'kyle', 'patrick', 'stephen', 'aaron', 'angela', 'elizabeth', 'sean', 'gary', 'emily', 'bob', 'samantha', 'greg', 'sara', 'jamie', 'sharon', 'george', 'joshua', 'anna', 'andy', 'nancy', 'donna', 'jeremy', 'debbie', 'christine', 'rebecca', 'kathy', 'jay', 'sandra', 'andrea', 'megan', 'lauren']
url = 'http://127.0.0.1:8282/login'

# password policies
upper = string.ascii_uppercase
lower = string.ascii_lowercase
symbols = ['#','$','%','@','*','!']

def generatePassword():
    # Include at least 1 upper, 1 lower, 1 symbol
    password = random.choice(upper) + random.choice(lower) + random.choice(symbols)
    password += ''.join(random.choice(string.ascii_letters + string.digits) for _ in range(7))
    
    # Shuffle the password
    output = ''.join(random.sample(password,len(password)))
    return output

# Trying passwords 2 times for every user
for _ in range(2):
    for user in usernames:
        r = requests.post(url, 
                        data={'username': user, 
                                'password': generatePassword()
                                })