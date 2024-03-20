import numpy as np

def calculate_inverse(matrix):
    if np.linalg.det(matrix) == 0:
        return None
    else:
        inverse = np.linalg.inv(matrix)
        return inverse

def verify_inverse(matrix, expected_inverse):
    if matrix is None and expected_inverse is None:
        return "Pass (Singular Matrix)"
    elif matrix is None or expected_inverse is None:
        return "Pass"
    elif isinstance(matrix, str) and matrix == "Singular Matrix":
        return "Pass (Singular Matrix)"
    elif np.allclose(matrix, expected_inverse):
        identity = np.eye(matrix.shape[0])
        print(identity)
        product = np.dot(matrix, expected_inverse)
        if np.allclose(product, identity):
            return "Pass"
        else:
            return "Fail (A * A^-1 != I)"
    else:
        return "Fail"

def print_matrix(matrix, name, file):
    with open(file, 'a') as f:
        f.write(f"{name}:\n")
        for row in matrix:
            f.write(' '.join(map(str, row)) + '\n')
    print(f"{name}:")
    print(matrix)

def convert_to_numeric_or_str(value):
    try:
        return int(value)
    except ValueError:
        try:
            return float(value)
        except ValueError:
            return value

def read_matrix_from_file(filename):
    with open(filename, 'r') as file:
        lines = file.readlines()
        matrices = []
        matrix = []
        for line in lines:
            if line.strip():
                if line.strip() == "Singular Matrix":
                    matrices.append("Singular Matrix")
                else:
                    elements = line.strip().strip('[]').split(',')
                    if len(elements) == 1:
                        elements = line.strip().strip('[]').split()
                    row = [convert_to_numeric_or_str(element.strip()) for element in elements]
                    matrix.append(row)
            elif matrix:
                if matrix[0] == "Singular Matrix":
                    matrices.append("Singular Matrix")
                else:
                    matrices.append(np.array(matrix))
                matrix = []
        if matrix:
            if matrix[0] == "Singular Matrix":
                matrices.append("Singular Matrix")
            else:
                matrices.append(np.array(matrix))
        return matrices

if __name__ == "__main__":
    observed_matrix_file = "observed_matrix.txt"
    expected_matrix_file = "expected_matrix.txt"
    test_log_file = "test_log_file.txt"

    observed_matrices = read_matrix_from_file(observed_matrix_file)
    expected_matrices = read_matrix_from_file(expected_matrix_file)

    with open(test_log_file, 'w') as f:
        for i, (observed_matrix, expected_matrix) in enumerate(zip(observed_matrices, expected_matrices), 1):
            f.write(f"Test Case {i}:\n")
            print_matrix(observed_matrix, "Observed Matrix", test_log_file)
            print_matrix(expected_matrix, "Expected Matrix", test_log_file)

            if isinstance(observed_matrix, str) and observed_matrix == "Singular Matrix":
                if isinstance(expected_matrix, str) and expected_matrix == "Singular Matrix":
                    verdict = "Pass"
                else:
                    verdict = "Fail"
            else:
                observed_inverse = calculate_inverse(observed_matrix)
                verdict = verify_inverse(observed_inverse, expected_matrix)
            f.write(f"Verdict {i}: {verdict}\n\n")
            print(f"Verdict {i}: {verdict}\n")
