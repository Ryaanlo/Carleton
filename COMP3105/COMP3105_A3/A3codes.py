import numpy as np
from scipy.optimize import minimize
from scipy.special import logsumexp
from scipy.spatial.distance import cdist
import pandas as pd
from matplotlib import pyplot as plt
from A3helpers import augmentX, plotImgs, plotModel, generateData, plotPoints, synClsExperiments
from scipy.linalg import eigh

# Question 1: Linear Multi-Class classifier

# a)  Implement a Python function W = minMulDev(X, Y)

def multinomial_deviance_loss(W, X, Y):
    n, d = X.shape
    k = Y.shape[1]
    
    # Reshape W to a d x k matrix
    W = W.reshape((d, k))
    
    # Calculate predicted probabilities
    logits = np.dot(X, W)
    log_partition = logsumexp(logits, axis=1, keepdims=True)
    P = np.exp(logits - log_partition)
    
    # Calculate the multinomial deviance loss
    loss = -np.sum(Y * (logits - log_partition)) / n
    
    return loss

def minMulDev(X, Y):
    n, d = X.shape
    k = Y.shape[1]
    
    # Initialize weights matrix W with zeros
    initial_W = np.zeros((d, k))
    
    # Use scipy.optimize.minimize to find optimal weights
    result = minimize(multinomial_deviance_loss, initial_W.flatten(), args=(X, Y))
    
    # Reshape the optimal weights to a d x k matrix
    optimal_W = result.x.reshape((d, k))
    
    return optimal_W

# b) Implement a Python function Yhat = classify(Xtest, W)

def classify(Xtest, W):
    # Calculate the logits for each class
    logits = np.dot(Xtest, W)
    
    # Apply indmax to each row to get the index of the maximum value
    # This represents the predicted class for each data point
    predicted_classes = np.argmax(logits, axis=1)
    
    # Create a one-hot encoding for each predicted class
    Yhat = np.eye(W.shape[1])[predicted_classes]
    
    return Yhat

# c) Implement a Python function acc = calculateAcc(Yhat, Y)

def calculateAcc(Yhat, Y):
    # Compare the predicted classes with the ground-truth classes
    correct_predictions = np.sum(np.argmax(Yhat, axis=1) == np.argmax(Y, axis=1))
    
    # Calculate accuracy as the ratio of correct predictions to the total number of predictions
    acc = correct_predictions / Y.shape[0]
    
    return acc

# d) n this part, you will evaluate your implementation on the synthetic datasets from above.
# We have implemented a helper function
# train_acc, test_acc = synClsExperiments(minMulDev, classify, calculateAcc)

# train_acc, test_acc = synClsExperiments(minMulDev, classify, calculateAcc)
# print("Training Acc: ", train_acc)
# print("Test Acc: ", test_acc)

# Question 2: Principle Component Analysis (PCA)

# a) Implement a Python function U = PCA(X, k)

def PCA(X, k):
    # Step 1: Subtract the mean from the dataset
    mean_X = np.mean(X, axis=0)
    X_centered = X - mean_X

    # Step 2: Compute the covariance matrix
    covariance_matrix = np.dot(X_centered.T, X_centered) / (X.shape[0] - 1)

    # Step 3: Compute all eigenvalues and eigenvectors of the covariance matrix
    eigenvalues, eigenvectors = eigh(covariance_matrix)

    # Step 4: Select the top-k eigenvalues and corresponding eigenvectors
    top_indices = np.argsort(eigenvalues)[::-1][:k]
    U = eigenvectors[:, top_indices].T

    return U

# b) call the plotImgs helper function with the U to see the “eigen-shirts”

# Load the dataset
data = pd.read_csv('A3train.csv', header=None)
X = data.to_numpy()

# Set the number of top projecting directions (k)
k = 20

# Call PCA function
U = PCA(X, k)

# Display the eigen-shirts using the plotImgs helper function
plotImgs(U)
plt.show()

# c) Implement a Python function Xproj = projPCA(Xtest, mu, U)

def projPCA(Xtest, mu, U):
    # Center the test data
    X_centered = Xtest - mu

    # Project the centered test data onto the directions represented by U
    Xproj = np.dot(X_centered, U.T)

    return Xproj

# d) Implement a Python function train_acc, test_acc = synClsExperimentsPCA()

def synClsExperimentsPCA():
    n_runs = 100
    n_train = 128
    n_test = 1000
    dim_list = [1, 2]
    gen_model_list = [1, 2]
    train_acc = np.zeros([len(dim_list), len(gen_model_list), n_runs])
    test_acc = np.zeros([len(dim_list), len(gen_model_list), n_runs])
    for r in range(n_runs):
        for i, k in enumerate(dim_list):
            for j, gen_model in enumerate(gen_model_list):
                Xtrain, Ytrain = generateData(n=n_train, gen_model=gen_model)
                Xtest, Ytest = generateData(n=n_test, gen_model=gen_model)
                U = PCA(Xtrain, k)
                Xtrain_proj = projPCA(Xtrain, np.mean(Xtrain, axis=0), U)
                Xtest_proj = projPCA(Xtest, np.mean(Xtrain, axis=0), U)
                Xtrain_proj = augmentX(Xtrain_proj) # add augmentation
                Xtest_proj = augmentX(Xtest_proj)
                W = minMulDev(Xtrain_proj, Ytrain) # from Q1
                Yhat = classify(Xtrain_proj, W) # from Q1
                train_acc[i, j, r] = calculateAcc(Yhat, Ytrain) # from Q1
                Yhat = classify(Xtest_proj, W)
                test_acc[i, j, r] = calculateAcc(Yhat, Ytest)
    # Compute average accuracies over runs
    avg_train_acc = np.mean(train_acc, axis=2)
    avg_test_acc = np.mean(test_acc, axis=2)

    return avg_train_acc, avg_test_acc

# train_acc, test_acc = synClsExperimentsPCA()
# print("Training Acc: ", train_acc)
# print("Test Acc: ", test_acc)

# Question 3: k-means

# a) Implement a Python function Y, U, obj_val = kmeans(X, k, max iter=1000)

def kmeans(X, k, max_iter=1000):
    n, d = X.shape
    assert max_iter > 0
    # Step 1: Initialize cluster centers U
    idx = np.random.choice(n, k, replace=False)
    U = X[idx, :]
    for i in range(max_iter):
        D = cdist(X, U, 'euclidean')
        Y = np.zeros((n, k))
        Y[np.arange(n), np.argmin(D, axis=1)] = 1
        old_U = U
        U = np.linalg.solve(Y.T @ Y + 1e-8 * np.eye(k), Y.T @ X)
        if np.allclose(old_U, U):
            break
    obj_val = 0.5 * np.sum((X - Y @ U)**2)
    return Y, U, obj_val

# b) Implement a Python function Y, U, obj_val = repeatKmeans(X, k, n runs=100)

def repeatKmeans(X, k, n_runs=100):
    best_obj_val = float('inf')
    best_Y, best_U = None, None

    for r in range(n_runs):
        Y, U, obj_val = kmeans(X, k)
        # Compare obj_val with best_obj_val
        if obj_val < best_obj_val:
            best_obj_val = obj_val
            best_Y = Y
            best_U = U
    
    return best_Y, best_U, best_obj_val

# Y, U, obj_val = repeatKmeans(X, k, n runs=100)

# c) Implement a Python function

def chooseK(X, k_candidates=[2,3,4,5,6,7,8,9]):
    obj_val_list = []
    
    for k in k_candidates:
        _, _, obj_val = repeatKmeans(X, k)
        obj_val_list.append(obj_val)
    
    return obj_val_list

obj_val_list = chooseK(X, k_candidates=[2,3,4,5,6,7,8,9])

Xtrain, Ytrain = generateData(n=100, gen_model=2)
obj_val_list = chooseK(Xtrain)

# print(obj_val_list)
