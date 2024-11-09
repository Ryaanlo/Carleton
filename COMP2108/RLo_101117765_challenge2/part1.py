import string
import hashlib
import random

# Define your student number
student_number = "101117765"

# Function to generate a random string of a given length
def generate_random_string(length):
    characters = string.ascii_letters + string.digits
    return ''.join(random.choice(characters) for _ in range(length))

while True:
    # Generate a random password
    password = generate_random_string(15)  # Adjust the length as needed

    # Concatenate the password with the student number for salting
    password_with_salt = password + student_number

    # Calculate the SHA256 hash
    password_hash = hashlib.sha256(password_with_salt.encode()).hexdigest()

    # Check if the hash meets the criteria
    if password_hash.startswith("c0ffee") and password_hash[6:8] == student_number[-2:]:
        # Save the password to a file
        with open("part1.txt", "w") as file:
            file.write(password)
        break