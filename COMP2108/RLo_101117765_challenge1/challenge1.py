#!/usr/bin/env python3
import base64
from Crypto.Random import get_random_bytes

STUDENT_NUMBER = b"101117765"
RANDOM_BYTES = get_random_bytes(9)


def bytexor(a: bytes, b: bytes) -> bytes:
    """
    XOR two byte arrays (trims the longer input)
    """
    return bytes([x ^ y for (x, y) in zip(a, b)])


def main(a: bytes, b: bytes) -> bytes:
    """
    This is a docstring, you may get asked in future challenges to explain
    your code (for marks) in these sections.
    """
    ab_xored = bytexor(a, b)

    return base64.b64encode(ab_xored)


if __name__ == "__main__":
    output = main(STUDENT_NUMBER, RANDOM_BYTES)
    print(output)
    decoded = base64.b64decode(output)
    print(decoded)
    student_number = bytexor(decoded, RANDOM_BYTES)
    print(student_number)

    assert student_number != b""
    assert student_number == STUDENT_NUMBER
