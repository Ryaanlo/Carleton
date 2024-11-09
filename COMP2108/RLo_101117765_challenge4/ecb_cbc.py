#!/usr/bin/env python3
from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes

BLOCK_SIZE = AES.block_size


def decrypt(ciphertext: bytes) -> bytes:
    key = bytes.fromhex("2c4b295fe9ca7c02208e22d25e2875a8")
    cipher = AES.new(key, AES.MODE_ECB)

    try:
        decrypted = cipher.decrypt(ciphertext)
    except ValueError as e:
        print(f"ERROR: {e}")
        return -1

    return decrypted


def encrypt(plaintext: bytes) -> bytes:
    key = bytes.fromhex("2c4b295fe9ca7c02208e22d25e2875a8")
    iv = get_random_bytes(BLOCK_SIZE)
    cipher = AES.new(key, AES.MODE_CBC, iv)

    encrypted = cipher.encrypt(plaintext)
    ciphertext = iv + encrypted

    return ciphertext

def xor(a: bytes, b: bytes) -> bytes:
    """
    The XOR of two array of bytes (cuts out the input that is longer)
    """
    return bytes([x ^ y for (x, y) in zip(a, b)])

def get_plaintext(ciphertext: bytes):
    """
    First, I used the decrypt function provided above which is in ECB mode
    Then I broke up both the ciphertext and the decrypted text into
    BLOCK_SIZE so that I can work with it in blocks. I then stored
    the blocks into an array to make it easier to compared them by
    indicies. In CBC mode, the plaintext is XOR'd with the previous
    encrypted block, so, I reconstructed the output msg by XOR-ing
    decrypt_arr[i + 1] with the previous block encrypt_arr[i]
    which then gets me the final message.  
    """
    # TODO: Replace this with your implementation
    decrypted_ciphertext = decrypt(ciphertext)
    decrypt_arr = []
    encrypt_arr = []

    for i in range(0, len(decrypted_ciphertext), BLOCK_SIZE):
        decrypt_arr.append(decrypted_ciphertext[i:i + BLOCK_SIZE])

    for i in range(0, len(ciphertext), BLOCK_SIZE):
        encrypt_arr.append(ciphertext[i:i + BLOCK_SIZE])

    output_msg = b''
    for i in range(0, len(encrypt_arr) - 1):
        output_msg += xor(decrypt_arr[i + 1], encrypt_arr[i])
    
    return output_msg


if __name__ == "__main__":
    plaintext = b"comp2108_3cb_5uck5_4v01d_17!!!!!"
    ciphertext = encrypt(plaintext)

    decrypted = get_plaintext(ciphertext)
    print(decrypted)
    assert decrypted == plaintext
