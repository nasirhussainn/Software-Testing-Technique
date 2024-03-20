import sqlite3
def read_queries_from_file(filename):
    with open(filename, 'r') as file:
        lines = file.readlines()
        queries = [line.strip().split(',') for line in lines]
    return queries

def connect_to_database():
    return sqlite3.connect('videorentals.db')

def create_table():
    conn = connect_to_database()
    cursor = conn.cursor()
    cursor.execute('''CREATE TABLE IF NOT EXISTS VideoRentals (
                        id INTEGER PRIMARY KEY,
                        title TEXT,
                        genre TEXT,
                        rental_price DECIMAL(10, 2),
                        rented BOOLEAN
                    )''')
    conn.commit()
    conn.close()

def insert_record(title, genre, rental_price, rented):
    try:
        rental_price = float(rental_price)
    except ValueError:
        return "Insertion failed."

    conn = connect_to_database()
    cursor = conn.cursor()

    sql = "INSERT INTO VideoRentals (title, genre, rental_price, rented) VALUES (?, ?, ?, ?)"
    val = (title, genre, rental_price, rented)

    cursor.execute(sql, val)
    conn.commit()
    conn.close()
    return "Record inserted."

def write_test_log(test_log_file, test_case_number, query, expected_output, observed_output, verdict):
    with open(test_log_file, 'a') as file:
        file.write(f"Test Case {test_case_number}:\n")
        file.write(f"Query: {query}\n")
        file.write(f"Expected Output: {expected_output}\n")
        file.write(f"Observed Output: {observed_output}\n")
        file.write(f"Verdict: {verdict}\n\n")

def print_test_case_results(test_case_number, query, expected_output, observed_output, verdict):
    print(f"Test Case {test_case_number}:")
    print(f"Query: {query}")
    print(f"Expected Output: {expected_output}")
    print(f"Observed Output: {observed_output}")
    print(f"Verdict: {verdict}\n")

def search_records(title):
    conn = connect_to_database()
    cursor = conn.cursor()

    sql = "SELECT * FROM VideoRentals WHERE title = ?"
    val = (title,)

    cursor.execute(sql, val)
    records = cursor.fetchall()

    conn.close()
    return records

if __name__ == "__main__":
    test_log_file = "E:\STT CS-474\Assignment 1\Qno_3\\test_log_file.txt"

    observed_queries = read_queries_from_file("observed_queries.txt")
    expected_output = read_queries_from_file("expected_output.txt")

    create_table()

    expected_index = 0
    for i, query in enumerate(observed_queries, 1):
        verdict = "Fail"
        if query[0] == "INSERT":
            observed_output = insert_record(*query[1:])
            expected_index += 1
            if observed_output == "Record inserted.":
                verdict = "Pass"
            else:
                verdict = "Pass" if "failed" in observed_output else "Fail"
        elif query[0] == "SEARCH":
            expected = expected_output[expected_index]
            expected_index += 1
            expected_output_str = str(expected)
            expected_output_str = expected_output_str.replace("[", "").replace("'", "").replace("]","")
            observed_records = search_records(query[1])

            if len(observed_records) == 0:
                observed_output = "Not found."
            else:
                observed_output = str(observed_records[0])


            expected_title = expected[0]
            expected_title = expected_title.replace("(", "").replace("'", "")
            observed_title = observed_records[0][1] if observed_records else None


            if expected_title == observed_title:
                verdict = "Pass"
            if expected_output_str == "Not found." and observed_output == "Not found.":
                verdict = "Pass"

        write_test_log(test_log_file, i, query, str(expected_output[expected_index - 1][0]), observed_output, verdict)

        print_test_case_results(i, query, str(expected_output[expected_index - 1][0]), observed_output, verdict)
