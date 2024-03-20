import numpy as np


def calculate_inverse(matrix):
    if np.linalg.det(matrix) == 0:
        return None
    else:
        inverse = np.linalg.inv(matrix)
        return inverse


def verify_inverse(matrix, inverse):
    if inverse is None:
        return "Fail (Singular Matrix)"
    else:
        identity = np.eye(matrix.shape[0])
        product = np.dot(matrix, inverse)
        if np.allclose(product, identity):
            return "Pass"
        else:
            return "Fail"


def print_matrix(matrix, name):
    print(f"{name}:")
    print(matrix)


def get_square_matrix(size):
    matrix = []
    print("Enter elements of the matrix row-wise:")
    for i in range(size):
        row = []
        for j in range(size):
            value = input(f"Enter value for [{i}][{j}]: ")
            row.append(int(value))
        matrix.append(row)
    return np.array(matrix)


if __name__ == "__main__":
    size = int(input("Enter the size of the square matrix: "))
    matrix = get_square_matrix(size)
    print_matrix(matrix, "Input Matrix")

    inverse = calculate_inverse(matrix)
    if inverse is not None:
        print_matrix(inverse, "Inverse Matrix")
        product = np.dot(matrix, inverse)
        print_matrix(product, "Product of A and A^-1 (A*A^-1)")

        verdict = verify_inverse(matrix, inverse)
        print("Verdict:", verdict)
    else:
        print("Singular Matrix: Inverse does not exist.")
