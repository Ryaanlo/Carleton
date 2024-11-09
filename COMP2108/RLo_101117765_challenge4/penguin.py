#!/usr/bin/env python3
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad
from Crypto.Random import get_random_bytes

BLOCK_SIZE = AES.block_size


def ecb_penguin(key: bytes, img: bytes) -> bytes:
    """
    BMP files have a 54 bytes header, so slice the first 54 bytes of the img
    Next I take the body which starts after the header so after 54 bytes
    subtracted by 1 which is the trailing byte which is found using hexdump
    And finally took the trailing byte which was the last byte
    I took the body and did some padding to it to make sure the
    block size is correct and then encrypted it
    And finally I return the whole thing which is
    the header with the encrypted body and the trailing byte
    In this EBC mode, it just looks like the original penguin but colours are a bit off
    this is because it takes the plaintext block and encrypts it with 
    the same key which is not randomized so all of the white
    pixels will be encrypted to the same pixel
    """
    # TODO: Replace this with your implementation
    cipher = AES.new(key, AES.MODE_ECB)
    header = img[:54]
    body = img[54:len(img)-1]
    trailing_bytes = img[len(img)-1:]

    encrypted_body = cipher.encrypt(pad(body, AES.block_size))
    return header + encrypted_body + trailing_bytes



def cbc_penguin(key: bytes, iv: bytes, img: bytes) -> bytes:
    """
    Same as last time there's a 54 byte header which I sliced off
    Body starts from after the 54 byte header till minus 1 of the trialing byte
    Did some padding on the body to ensure block size then encrypted it
    Finally returned the whole thing which is
    the header with the encrypted body and the trailing byte
    In this CBC mode, it just creates an image with just a bunch of noise
    this is because the plaintext block is XOR'd with the prev 
    cipherblock. The IV that is randomly generated ensures that
    each message is different and unique every time.
    """
    assert iv is not None
    # TODO: Replace this with your implementation
    cipher = AES.new(key, AES.MODE_CBC, iv)
    header = img[:54]
    body = img[54:len(img)-1]
    trailing_bytes = img[len(img)-1:]

    encrypted_body = cipher.encrypt(pad(body, AES.block_size))
    return header + encrypted_body + trailing_bytes


if __name__ == "__main__":
    key = b"2108SaysAvoidECB"

    with open("tux.bmp", "rb") as f:
        img = f.read()

    with open("ecb_tux.bmp", "wb") as f:
        ciphertext = ecb_penguin(key, img)
        f.write(ciphertext)

    iv = get_random_bytes(AES.block_size)      
    with open("cbc_tux.bmp", "wb") as f:
        ciphertext = cbc_penguin(key, iv, img)
        f.write(ciphertext)
