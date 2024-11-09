import hashlib

# Challenge 2 Part 4:

# Write a python program to find the preimage of a given BLAKE2 hash value
def find_preimage(hash, student_number):
    # Iterate through the range of initial values
    for i in range(5000001):
        initial_value = str(student_number + i).encode()  # Convert integer to bytes

        # Compute a hash chain of length N=1000
        current_hash = initial_value
        for j in range(1000):
            preimage = current_hash
            current_hash = hashlib.blake2b(current_hash).digest()

            # Check if the current hash matches the target hash
            if current_hash.hex() == hash:
                print(f"Found preimage for hash {hash}: {initial_value.decode()}")
                return preimage.hex()
    
    return

if __name__ == "__main__":
    student_number = 101117765 #replace this with your student number
    hash = "781780ddb6750c6f88a9dda585b738a8a7e2297da08974ec106532ac7cb4583abb9749466465be2bff38ae575374435f3d361eb06759bad9b01eb98ab0e4c975"

    preimage = find_preimage(hash, student_number)

    with open("part4.txt", "w") as file:
        file.write(preimage)
