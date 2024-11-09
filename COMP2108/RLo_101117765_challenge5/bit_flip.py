from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes

def byte_xor(ba1, ba2):
    return bytes([_a ^ _b for _a, _b in zip(ba1, ba2)])

def generate_attack_iv(original_iv, original_msg):
    # Define the modified message
    modified_msg = b"$99 to 101117765"

    # Perform XOR between the original IV and the XOR of the original and modified messages
    modified_iv = byte_xor(original_iv, byte_xor(original_msg, modified_msg))

    return modified_iv
    
if __name__ == '__main__':

    key = b"very secure key!" # note this is not the key we used to encrypt the message in challenge 5!
    msg = b'$10 to 999123456'
    print('Original Message:', msg)
    iv = b'm\xd9\xf3\x94\xea\x8e)pF\xc3\x16cm\xb59\xc0'
    cipher = AES.new(key,AES.MODE_CBC,iv)
    encrypted = cipher.encrypt(msg)
    print('Encrypted: ', encrypted)
    cipher = AES.new(key,AES.MODE_CBC,iv)
    decrypted = cipher.decrypt(encrypted)
    print('IV        ', iv)
    print('Decrypted:', decrypted )

    newiv = generate_attack_iv(iv, msg)
    
    print('New IV:   ', newiv )
    cipher = AES.new(key,AES.MODE_CBC,newiv)
    decrypted = cipher.decrypt(encrypted)
    print('Decrypted:', decrypted )

