import csv
import numpy as np
import os
import glob
import skimage as sk

# Define the prior probabilities for each class
prior_probabilities = {
    "power": 0.57,
    "wifi": 0.29,
    "bluetooth": 0.14,
}

# Define the probabilities for the Naive Bayes classifier
probabilities = {
    "power": {
        "NumBlack=1": 0.17,
        "NumBlack=2": 0.81,
        "NumBlack=3": 0.02,
        "NumWhite=1": 0.79,
        "NumWhite=2": 0.18,
        "NumWhite=3": 0.03,
    },
    "wifi": {
        "NumBlack=2": 0.03,
        "NumBlack=3": 0.41,
        "NumBlack=4": 0.52,
        "NumBlack=5": 0.04,
        "NumWhite=1": 0.96,
        "NumWhite=2": 0.04,
    },
    "bluetooth": {
        "NumBlack=1": 0.91,
        "NumBlack=2": 0.09,
        "NumWhite=2": 0.01,
        "NumWhite=3": 0.88,
        "NumWhite=4": 0.11,
    },
}

# Format results for comparison to expected output
def format_output(result):
    class_name, probabilities = result
    probabilities_str = ", ".join(
        f"{float('{:.16g}'.format(prob))}" for prob in probabilities
    )
    return f'"{class_name}", [{probabilities_str}]'

# Helper function to read the expected results
def read_expected_results(file_path):
    with open(file_path, "r") as file:
        return file.read().strip()
    
def calculate_components(image_data):
    image_array = np.array(image_data, dtype=bool)
    labeled = sk.measure.label(image_array, connectivity=1)
    num_black = np.max(labeled)
    num_white = np.max(sk.measure.label(1 - np.array(image_data), connectivity=1))
    return num_black, num_white

################
# Naive Bayes

def naive_bayes_classifier(input_filepath):
    num_black = 0
    num_white = 0

    with open(input_filepath, "r") as csv_file:
        csv_reader = csv.reader(csv_file)
        image_data = [list(map(int, row)) for row in csv_reader]

    num_black, num_white = calculate_components(image_data)

    # calculate the class probabilities based on the number of components
    class_probabilities = {}
    for class_name, probs in probabilities.items():
        # initialize probability with prior
        class_prob = prior_probabilities[class_name]
        # multiply with the conditional probabilities
        class_prob *= probs.get(f"NumBlack={num_black}", 0) * probs.get(f"NumWhite={num_white}", 0)
        class_probabilities[class_name] = class_prob

    # normalize probabilities
    total_prob = sum(class_probabilities.values())
    if total_prob == 0:
        uniform_prob = 1 / len(class_probabilities)
        for class_name in class_probabilities:
            class_probabilities[class_name] = uniform_prob
        # Handle the case where total_prob is zero
        # This might involve setting a minimum non-zero value or adjusting the probabilities in a meaningful way
    else:
        for class_name in class_probabilities:
            class_probabilities[class_name] /= total_prob

    most_likely_class = max(class_probabilities, key=class_probabilities.get)
    return most_likely_class, [class_probabilities["power"], class_probabilities["wifi"], class_probabilities["bluetooth"]]

################
# Fuzzy Classifier

# Define the membership functions
def fuzzy_membership_low(x):
    if x == 0:
        return 1
    elif x == 1:
        return 0.75
    else:
        return 0

def fuzzy_membership_medium(x):
    if x == 1 or x == 3:
        return 0.25
    elif x == 2:
        return 1
    else:
        return 0

def fuzzy_membership_high(x):
    if x == 3:
        return 0.75
    elif x >= 4:
        return 1
    else:
        return 0
    
# Helper function to calculate Goguen t-norm and s-norm
def goguen_t_norm(a, b):
    return (a * b)

def goguen_s_norm(a, b):
    return (a + b) - (a * b)

# Define the fuzzy rules
def fuzzy_rules(num_black, num_white):
    # Membership functions values
    num_black_low = fuzzy_membership_low(num_black)
    num_black_medium = fuzzy_membership_medium(num_black)
    num_black_high = fuzzy_membership_high(num_black)
    
    num_white_low = fuzzy_membership_low(num_white)
    num_white_medium = fuzzy_membership_medium(num_white)
    num_white_high = fuzzy_membership_high(num_white)
    
    # Apply the fuzzy rules using Goguen t-norm and s-norm
    power = goguen_s_norm(num_black_medium, num_white_medium)
    wifi = goguen_t_norm(num_black_high, num_white_low)
    bluetooth = goguen_s_norm(num_black_low, num_white_high)
    
    return power, wifi, bluetooth


def fuzzy_classifier(input_filepath):
    with open(input_filepath, "r") as csv_file:
        csv_reader = csv.reader(csv_file)
        image_data = [list(map(int, row)) for row in csv_reader]

    num_black, num_white = calculate_components(image_data)
    power, wifi, bluetooth = fuzzy_rules(num_black, num_white)
    # Determine the highest membership class
    memberships = {"power": power, "wifi": wifi, "bluetooth": bluetooth}
    highest_membership_class = max(memberships, key=memberships.get)

    return highest_membership_class, [power, wifi, bluetooth]

def main():
    examples_path = "./Examples"
    example_dirs = [dir_name for dir_name in os.listdir(examples_path) if os.path.isdir(os.path.join(examples_path, dir_name))]
    # Sort directories to ensure correct order
    example_dirs.sort()

    # Loop over each example directory
    for example_dir in example_dirs:
        print(f"Processing {example_dir}...")
        example_path = os.path.join(examples_path, example_dir)

        csv_files = glob.glob(os.path.join(example_path, "*.csv"))
        if csv_files:
            csv_path = csv_files[0]

            # Run both classifiers on the image CSV
            naive_bayes_result = naive_bayes_classifier(csv_path)
            fuzzy_result = fuzzy_classifier(csv_path)

            naive_bayes_result_formatted = format_output(naive_bayes_result)
            fuzzy_result_formatted = format_output(fuzzy_result)

            # Read expected results
            expected_naive_bayes = read_expected_results(os.path.join(example_path, "naive_bayes_classifier.txt"))
            expected_fuzzy = read_expected_results(os.path.join(example_path, "fuzzy_classifier.txt"))

            # Compare expected results with actual results and print the outcome
            print(f"Naive Bayes Classifier expected: {expected_naive_bayes}, got: {naive_bayes_result_formatted}")
            print(f"Fuzzy Classifier expected: {expected_fuzzy}, got: {fuzzy_result_formatted}")
            print(f"Naive Bayes match: {expected_naive_bayes == str(naive_bayes_result_formatted)}")
            print(f"Fuzzy Classifier match: {expected_fuzzy == str(fuzzy_result_formatted)}")
            print()  # Print a new line for readability
        else:
            print(f"No CSV file found in {example_path}")


if __name__ == "__main__":
    main()