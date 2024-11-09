import pandas as pd

def read_and_print_csv(file_path):
    try:
        # Read the CSV file into a DataFrame without headers
        df = pd.read_csv(file_path, header=None)
        # Print the first few rows of the DataFrame
        print(df.head())
    except Exception as e:
        print(f"An error occurred: {e}")

# Replace 'path_to_csv' with the actual paths of your CSV files
read_and_print_csv('./Examples/Example0/Trials/trial0.csv')
read_and_print_csv('./Examples/Example0/policy_tests.csv')
read_and_print_csv('./Examples/Example0/qvalue_tests.csv')
