from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes

def byte_xor(ba1, ba2):
    return bytes([_a ^ _b for _a, _b in zip(ba1, ba2)])

def generate_attack_iv(original_iv, encrypted_message):
    # Define the target account number
    target_account = b'101117765'  # Your student number

    # Perform XOR between the original IV and the XOR of the original and target account numbers
    modified_iv = byte_xor(original_iv, byte_xor(encrypted_message, target_account))

    return modified_iv
    
if __name__ == '__main__':

    # Given encrypted message and IV
    iv = b'\x99\xf4\x9f\xa7\x03\xcc\x81+$\xd5\\\x13n\x1f\xbfj'
    encrypted_message = b'\xdfW\xdb\xc1[\te\x8b-\xa2\xfcB\xf88\xb4F'

    # Generate a new IV for the attack
    new_iv = generate_attack_iv(iv, encrypted_message)
    print('Modified IV:', new_iv)

