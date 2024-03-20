def sort_sequence(sequence, order):
    if order == 'A':
        return sorted(sequence)
    elif order == 'D':
        return sorted(sequence, reverse=True)
    elif order == '':
        return "Invalid format due to blank space"
    else:
        return "Invalid request character. Please use 'A' for ascending or 'D' for descending."


def read_input_sequences(filename):
    sequences = []
    with open(filename, 'r') as file:
        for line in file:
            if not line.strip():
                sequences.append(("Invalid format", []))
                continue
            request_char, *sequence = line.strip().split()
            if not request_char:
                sequences.append(("Invalid request character", []))
            elif not sequence:
                sequences.append(("Invalid format", []))
            else:
                sequences.append((request_char, list(map(int, sequence))))
    return sequences


def read_expected_output(filename):
    with open(filename, 'r') as file:
        test_cases = []
        for line in file:
            test_cases.append(line.strip())
        return test_cases


def compare_output(expected, observed):
    if isinstance(expected, list) and isinstance(observed, list):
        if len(expected) != len(observed):
            return False
        return expected == observed
    elif isinstance(expected, str):
        if "Invalid request character" in expected:
            return "Invalid request character" in observed
        else:
            observed_str = ' '.join(map(str, observed))
            return observed_str == expected
    return False

def write_test_log(filename, test_cases, results):
    with open(filename, 'w') as file:
        file.write("Test Case\tExpected Output\tObserved Output\tVerdict\n")
        for i, (expected, observed) in enumerate(zip(test_cases, results), 1):
            verdict = "Pass" if compare_output(expected, observed) else "Fail"
            file.write(f"{i}\t{expected}\t{observed}\t{verdict}\n")
            print(f"Test Case {i}:")
            print(f"  Expected Output: {expected}")
            print(f"  Observed Output: {observed}")
            print(f"  Verdict: {verdict}\n")


if __name__ == "__main__":

    input_sequences = read_input_sequences("input_sequences.txt")
    expected_output = read_expected_output("expected_output.txt")

    observed_output = []
    for request_char, sequence in input_sequences:
        sorted_sequence = sort_sequence(sequence, request_char)
        observed_output.append(sorted_sequence)

    write_test_log("test_log.txt", expected_output, observed_output)
