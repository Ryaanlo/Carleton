# COMP 3105 Assignment 2
# Ryan Lo (101117765)

from cvxopt import matrix, solvers
import numpy as np
import pandas as pd
from scipy.optimize import minimize

# NOTE: We provide some helper functions here. 
#       See the A2 instructions for more information.
from scipy.spatial.distance import cdist
from matplotlib import pyplot as plt

solvers.options['show_progress'] = False

def linearKernel(X1, X2):
    return X1 @ X2.T


def polyKernel(X1, X2, degree=2):
    return (X1 @ X2.T + 1) ** degree


def gaussKernel(X1, X2, width=2):
    distances = cdist(X1, X2, 'sqeuclidean')
    return np.exp(- distances / (2*(width**2)))


def generateData(n, gen_model):

    # Controlling the random seed will give you the same 
    # random numbers every time you generate the data. 
    # The seed controls the internal random number generator (RNG).
    # Different seeds produce different random numbers. 
    # This can be handy if you want reproducible results for debugging.
    # For example, if your code *sometimes* gives you an error, try
    # to find a seed number (0 or others) that produces the error. Then you can
    # debug your code step-by-step because every time you get the same data.

    # np.random.seed(0)  # control randomness when debugging

    if gen_model == 1 or gen_model == 2:
        # Gen 1 & 2
        d = 2
        w_true = np.ones([d, 1])

        X = np.random.randn(n, d)

        if gen_model == 1:
            y = np.sign(X @ w_true)  # generative model 1
        else:
            y = np.sign((X ** 2) @ w_true - 1)  # generative model 2

    elif gen_model == 3:
        # Gen 3
        X, y = generateMoons(n)

    else:
        raise ValueError("Unknown generative model")

    return X, y


def generateMoons(n, noise=0.1):
    n_samples_out = n // 2
    n_samples_in = n - n_samples_out
    outer_circ_x = np.cos(np.linspace(0, np.pi, n_samples_out))
    outer_circ_y = np.sin(np.linspace(0, np.pi, n_samples_out))
    inner_circ_x = 1 - np.cos(np.linspace(0, np.pi, n_samples_in))
    inner_circ_y = 1 - np.sin(np.linspace(0, np.pi, n_samples_in)) - 0.5

    X = np.vstack(
        [np.append(outer_circ_x, inner_circ_x), 
         np.append(outer_circ_y, inner_circ_y)]
    ).T
    X += np.random.randn(*X.shape) * noise

    y = np.hstack(
        [-np.ones(n_samples_out, dtype=np.intp), 
         np.ones(n_samples_in, dtype=np.intp)]
    )[:, None]
    return X, y


def plotPoints(X, y):
    # plot the data points from two classes
    X0 = X[y.flatten() >= 0]
    X1 = X[y.flatten() < 0]

    plt.scatter(X0[:, 0], X0[:, 1], marker='x', label='class -1')
    plt.scatter(X1[:, 0], X1[:, 1], marker='o', label='class +1')
    return


def getRange(X):
    x_min = np.amin(X[:, 0]) - 0.1
    x_max = np.amax(X[:, 0]) + 0.1
    y_min = np.amin(X[:, 1]) - 0.1
    y_max = np.amax(X[:, 1]) + 0.1
    return x_min, x_max, y_min, y_max


def plotModel(X, y, w, w0, classify):

    plotPoints(X, y)

    # plot model
    x_min, x_max, y_min, y_max = getRange(X)
    grid_step = 0.01
    xx, yy = np.meshgrid(np.arange(x_min, x_max, grid_step),
                         np.arange(y_min, y_max, grid_step))
    z = classify(np.c_[xx.ravel(), yy.ravel()], w, w0)

    # Put the result into a color plot
    z = z.reshape(xx.shape)
    plt.contourf(xx, yy, z, cmap=plt.cm.RdBu, alpha=0.5)
    plt.legend()
    plt.show()
    return


def plotAdjModel(X, y, a, a0, kernel_func, adjClassify):

    plotPoints(X, y)

    # plot model
    x_min, x_max, y_min, y_max = getRange(X)
    grid_step = 0.01
    xx, yy = np.meshgrid(np.arange(x_min, x_max, grid_step),
                         np.arange(y_min, y_max, grid_step))
    z = adjClassify(np.c_[xx.ravel(), yy.ravel()], a, a0, X, kernel_func)

    # Put the result into a color plot
    z = z.reshape(xx.shape)
    plt.contourf(xx, yy, z, cmap=plt.cm.RdBu, alpha=0.5)
    plt.legend()
    plt.show()
    return


def plotDualModel(X, y, a, b, lamb, kernel_func, dualClassify):

    plotPoints(X, y)

    # plot model
    x_min, x_max, y_min, y_max = getRange(X)
    grid_step = 0.01
    xx, yy = np.meshgrid(np.arange(x_min, x_max, grid_step),
                         np.arange(y_min, y_max, grid_step))
    z = dualClassify(np.c_[xx.ravel(), yy.ravel()], a, b, X, y, 
                     lamb, kernel_func)

    # Put the result into a color plot
    z = z.reshape(xx.shape)
    plt.contourf(xx, yy, z, cmap=plt.cm.RdBu, alpha=0.5)
    plt.legend()
    plt.show()

    return


def plotDigit(x):
    img = x.reshape((28, 28))
    plt.imshow(img, cmap='gray')
    plt.show()
    return

# Question 1: Binary Classifier (Primal Form)

# a) Implement a Python function 
# w, w0 = minBinDev(X, y, lamb)

def minBinDev(X, y, lamb):
    n, d = X.shape

    initial_weights = np.ones(d + 1)
    
    obj_func = lambda u: (0.5 * lamb * np.sum(u[:-1] * u[:-1]) +
                        np.sum(np.logaddexp(0, (1 + np.exp(-y * (X @ u[:-1][:, None] + u[-1]))))))

    # Minimize the regularized binomial deviance loss
    result = minimize(obj_func, initial_weights)
    
    # Get the solution
    w = result['x'][:-1][:, None]  # make it d-by-1
    w0 = result['x'][-1]
    return w, w0

# b) Implement a Python function
# w, w0 = minHinge(X, y, lamb, stablizer=1e-5)

def minHinge(X, y, lamb, stablizer=1e-5):
    n, d = X.shape

    diagY = np.diag(y.flatten())

    # Quadratic programming parameters
    P11 = lamb * np.identity(d)
    P12 = np.zeros((d, 1))
    P13 = np.zeros((d, n))
    P21 = np.zeros((1, d))
    P22 = np.zeros((1, 1))
    P23 = np.zeros((1, n))
    P31 = np.zeros((n, d))
    P32 = np.zeros((n, 1))
    P33 = np.zeros((n, n))
    P1 = np.concatenate((P11, P12, P13), axis=1)
    P2 = np.concatenate((P21, P22, P23), axis=1)
    P3 = np.concatenate((P31, P32, P33), axis=1)
    P = (np.concatenate((P1, P2, P3), axis=0))
    q = matrix(np.concatenate((np.zeros((d + 1, 1)), np.ones((n, 1)))))
    G11 = np.zeros((n, d))
    G12 = np.zeros((n, 1))
    G13 = -np.identity(n)
    G21 = -diagY @ X
    G22 = -diagY @ np.ones((n, 1))
    G23 = -np.identity(n)
    G1 = np.concatenate((G11, G12, G13), axis=1)
    G2 = np.concatenate((G21, G22, G23), axis=1)
    G = matrix(np.concatenate((G1, G2), axis=0))
    h1 = np.zeros((n, 1))
    h2 = -np.ones((n, 1))
    h = matrix(np.concatenate((h1, h2), axis=0))

    # Add small positive stabilizer
    P = matrix(P + stablizer * np.eye(n+d+1))

    # Solve the quadratic programming problem
    sol = solvers.qp(P, q, G, h)

    # Get the solution
    w = np.array(sol['x'][:d])
    w0 = sol['x'][d]
    return w, w0

# c) Implement a Python function
# yhat = classify(Xtest, w, w0)

def classify(Xtest, w, w0):
    return np.sign(Xtest @ w + w0)

# d) Implement a Python function
# train acc, test acc = synExperimentsRegularize()

def synExperimentsRegularize():
    n_runs = 100
    n_train = 100
    n_test = 1000
    lamb_list = [0.001, 0.01, 0.1, 1.]
    gen_model_list = [1, 2, 3]
    train_acc_bindev = np.zeros([len(lamb_list), len(gen_model_list), n_runs])
    test_acc_bindev = np.zeros([len(lamb_list), len(gen_model_list), n_runs])
    train_acc_hinge = np.zeros([len(lamb_list), len(gen_model_list), n_runs])
    test_acc_hinge = np.zeros([len(lamb_list), len(gen_model_list), n_runs])
    for r in range(n_runs):
        for i, lamb in enumerate(lamb_list):
            for j, gen_model in enumerate(gen_model_list):
                Xtrain, ytrain = generateData(n=n_train, gen_model=gen_model)
                Xtest, ytest = generateData(n=n_test, gen_model=gen_model)
                w, w0 = minBinDev(Xtrain, ytrain, lamb)
                train_acc_bindev[i, j, r] = np.mean((classify(Xtrain, w, w0) == ytrain).astype(int))
                test_acc_bindev[i, j, r] = np.mean((classify(Xtest, w, w0) == ytest).astype(int))
                w, w0 = minHinge(Xtrain, ytrain, lamb)
                train_acc_hinge[i, j, r] = np.mean((classify(Xtrain, w, w0) == ytrain).astype(int))
                test_acc_hinge[i, j, r] = np.mean((classify(Xtest, w, w0) == ytest).astype(int))

    # compute the average accuracies over runs
    avg_train_acc_bindev = np.mean(train_acc_bindev, axis=2)
    avg_test_acc_bindev = np.mean(test_acc_bindev, axis=2)
    avg_train_acc_hinge = np.mean(train_acc_hinge, axis=2)
    avg_test_acc_hinge = np.mean(test_acc_hinge, axis=2)
    # combine accuracies (bindev and hinge)
    train_acc = np.stack((avg_train_acc_bindev, avg_train_acc_hinge), axis=2)
    test_acc = np.stack((avg_test_acc_bindev, avg_test_acc_hinge), axis=2)
    # Reshape to a 4x6 matrix
    train_acc = train_acc.reshape((4, 6))
    test_acc = test_acc.reshape((4, 6))
    # return 4-by-6 train accuracy and 4-by-6 test accuracy
    return train_acc, test_acc

# e)Looking at your tables from above, analyze the results and discuss any
# findings you may have and the possible reason behind them.

# Question 2:  Binary Classification (Adjoint Form)

# a)Implement a Python function
# a, a0 = adjBinDev(X, y, lamb, kernel func)

def adjBinDev(X, y, lamb, kernel_func):
    n, d = X.shape

    initial_weights = np.ones(n + 1)

    K = kernel_func(X, X)
    
    obj_func = lambda u: (0.5 * lamb * np.sum(u[:-1].T @ K @ u[:-1]) +
                    np.sum(np.log(1 + np.logaddexp(0, -y * (K.T @ u[:-1][:, None] + u[-1])))))

    # Minimize the regularized binomial deviance loss
    result = minimize(obj_func, initial_weights)
    
    # Get the solution
    a = result['x'][:-1][:, None]  # make it d-by-1
    a0 = result['x'][-1]
    return a, a0

# b) Implement a Python function
# a, a0 = adjHinge(X, y, lamb, kernel func, stability=1e-5)

def adjHinge(X, y, lamb, kernel_func, stabilizer=1e-5):
    n, d = X.shape

    # Compute the kernel matrix K
    K = kernel_func(X, X)

    diagY = np.diag(y.flatten())

    # Quadratic programming parameters
    P11 = lamb * K
    P12 = np.zeros((n, 1))
    P13 = -np.eye(n)
    P21 = np.zeros((1, n))
    P22 = np.zeros((1, 1))
    P23 = np.zeros((1, n))
    P31 = np.zeros((n, n))
    P32 = np.zeros((n, 1))
    P33 = np.zeros((n, n))

    P1 = np.concatenate((P11, P12, P13), axis=1)
    P2 = np.concatenate((P21, P22, P23), axis=1)
    P3 = np.concatenate((P31, P32, P33), axis=1)
    P = np.concatenate((P1, P2, P3), axis=0)
    P = matrix(P)

    q = matrix(np.concatenate((np.zeros((n + 1, 1)), np.ones((n, 1)))))
    
    G11 = np.zeros((n, n))
    G12 = np.zeros((n, 1))
    G13 = -np.eye(n)
    G21 = -diagY @ K
    G22 = -diagY @ np.ones((n, 1))
    G23 = -np.eye(n)
    
    G1 = np.concatenate((G11, G12, G13), axis=1)
    G2 = np.concatenate((G21, G22, G23), axis=1)
    G = matrix(np.concatenate((G1, G2), axis=0))
    
    h1 = matrix(np.zeros((n, 1)))
    h2 = -np.ones((n, 1))
    h = matrix(np.concatenate((h1, h2), axis=0))

    # Apply the stabilizer
    P = matrix(P + stabilizer * np.eye(2 * n + 1))

    # Solve the QP problem
    sol = solvers.qp(P, q, G, h)

    # Extract the solution
    a = np.array(sol['x'][:n])
    a0 = sol['x'][n]

    return a, a0

# c) Implement a Python function
# yhat = adjClassify(Xtest, a, a0, X, kernel_func)

def adjClassify(Xtest, a, a0, X, kernel_func):
    return np.sign(kernel_func(Xtest, X) @ a + a0)

# d) Implement a Python function
# train acc, test acc = synExperimentsKernel()

def synExperimentsKernel():
    n_runs = 10
    n_train = 100
    n_test = 1000
    lamb = 0.001
    kernel_list = [linearKernel,
                    lambda X1, X2: polyKernel(X1, X2, 2),
                    lambda X1, X2: polyKernel(X1, X2, 3),
                    lambda X1, X2: gaussKernel(X1, X2, 1.0),
                    lambda X1, X2: gaussKernel(X1, X2, 0.5)]
    gen_model_list = [1, 2, 3]
    train_acc_bindev = np.zeros([len(kernel_list), len(gen_model_list), n_runs])
    test_acc_bindev = np.zeros([len(kernel_list), len(gen_model_list), n_runs])
    train_acc_hinge = np.zeros([len(kernel_list), len(gen_model_list), n_runs])
    test_acc_hinge = np.zeros([len(kernel_list), len(gen_model_list), n_runs])
    for r in range(n_runs):
        for i, kernel in enumerate(kernel_list):
            for j, gen_model in enumerate(gen_model_list):
                Xtrain, ytrain = generateData(n=n_train, gen_model=gen_model)
                Xtest, ytest = generateData(n=n_test, gen_model=gen_model)
                a, a0 = adjBinDev(Xtrain, ytrain, lamb, kernel)
                train_acc_bindev[i, j, r] = np.mean((adjClassify(Xtrain, a, a0, Xtrain, kernel) == ytrain).astype(int))
                test_acc_bindev[i, j, r] = np.mean((adjClassify(Xtest, a, a0, Xtrain, kernel) == ytest).astype(int))
                a, a0 = adjHinge(Xtrain, ytrain, lamb, kernel)
                train_acc_hinge[i, j, r] = np.mean((adjClassify(Xtrain, a, a0, Xtrain, kernel) == ytrain).astype(int))
                test_acc_hinge[i, j, r] = np.mean((adjClassify(Xtest, a, a0, Xtrain, kernel) == ytest).astype(int))

    # compute the average accuracies over runs
    avg_train_acc_bindev = np.mean(train_acc_bindev, axis=2)
    avg_test_acc_bindev = np.mean(test_acc_bindev, axis=2)
    avg_train_acc_hinge = np.mean(train_acc_hinge, axis=2)
    avg_test_acc_hinge = np.mean(test_acc_hinge, axis=2)    
    # combine accuracies (bindev and hinge)
    train_acc = np.stack((avg_train_acc_bindev, avg_train_acc_hinge), axis=2)
    test_acc = np.stack((avg_test_acc_bindev, avg_test_acc_hinge), axis=2)
    # Reshape to a 5x6 matrix
    train_acc = train_acc.reshape((5, 6))
    test_acc = test_acc.reshape((5, 6))
    # return 5-by-6 train accuracy and 5-by-6 test accuracy
    return train_acc, test_acc

# e) Looking at your tables from above, analyze the results and discuss any
# findings you may have and the possible reason behind them.

# Question 3: Binary Classification (SVM Dual Form)

# a) Implement a Python function 
# a, b = dualHinge(X, y, lamb, kernel func, stabilizer=1e-5)

def dualHinge(X, y, lamb, kernel_func, stabilizer=1e-5):
    n, d = X.shape

    # Define the kernel matrix K based on the kernel function
    K = kernel_func(X, X)
    
    # Define the variables for the quadratic programming problem
    P = matrix(np.outer(y, y) * K)
    q = matrix(-np.ones(n))
    G = matrix(np.vstack((-np.eye(n), np.eye(n))))
    h = matrix(np.hstack((np.zeros(n), np.ones(n))))
    A = matrix(y.reshape(1, -1), (1, n), 'd')
    b = matrix(0.0)

    # Add stabilizer to the P matrix to ensure numerical stability
    P = P + matrix(np.eye(n) * stabilizer)

    # Solve the quadratic programming problem using cvxopt
    sol = solvers.qp(P, q, G, h, A, b)

    # Extract the solution, i.e., the dual variables (a)
    a = np.array(sol['x']).flatten()

    # Compute the offset (b) directly
    delta_y = np.array([1 if yi == 1 else -1 for yi in y])
    b = y[0] - (1 / (2 * lamb)) * sum(a[j] * delta_y[j] for j in range(n))    
    return a, b

# b) Implement a Python function
# yhat = dualClassify(Xtest, a, b, X, y, lamb, kernel func)

def dualClassify(Xtest, a, b, X, y, lamb, kernel_func):
    m, n = Xtest.shape[0], X.shape[0]
    yhat = np.zeros((m, 1))

    for i in range(m):
        prediction = 0
        for j in range(n):
            prediction += a[j] * y[j] * kernel_func(Xtest[i:i+1], X[j:j+1])
        prediction = prediction / lamb + b   

        yhat[i] = np.sign(prediction)

    return yhat

# c) Implement a Python function
# cv_acc, best_lamb, best_kernel = cvMnist(dataset_folder, lamb_list, kernel_list, k=5)

def kFoldSplit(X, y, k):
    n = len(X)
    fold_size = n // k
    indices = np.arange(n)
    np.random.shuffle(indices)
    
    X_folds = []
    y_folds = []
    
    for i in range(k):
        start = i * fold_size
        end = (i + 1) * fold_size if i < k - 1 else n
        fold_indices = indices[start:end]
        X_folds.append(X[fold_indices])
        y_folds.append(y[fold_indices])
    
    return X_folds, y_folds

def cvMnist(dataset_folder, lamb_list, kernel_list, k=5):
    train_data = pd.read_csv(f"{dataset_folder}/A2train.csv", header=None).to_numpy()
    X = train_data[:, 1:] / 255.
    y = train_data[:, 0][:, None]
    y[y == 4] = -1
    y[y == 9] = 1

    cv_acc = np.zeros([k, len(lamb_list), len(kernel_list)])

    # TODO: perform any necessary setup
    X_folds, y_folds = kFoldSplit(X, y, k)

    for i, lamb in enumerate(lamb_list):
        for j, kernel_func in enumerate(kernel_list):
            for l in range(k):
                # Use all folds except the current fold for training
                Xtrain = np.concatenate([X_folds[x] for x in range(k) if x != l], axis=0)
                ytrain = np.concatenate([y_folds[x] for x in range(k) if x != l], axis=0)
                Xval = X_folds[l]
                yval = y_folds[l]

                a, b = dualHinge(Xtrain, ytrain, lamb, kernel_func)
                yhat = dualClassify(Xval, a, b, Xtrain, ytrain, lamb, kernel_func)

                correct = np.sum(yval == yhat)
                accuracy = correct / len(yval)
                cv_acc[l, i, j] = accuracy

    # compute the average accuracies over k folds
    avg_acc = np.mean(cv_acc, axis=0)
    # identify the best lamb and kernel function
    best_lamb_idx, best_kernel_idx = np.unravel_index(np.argmax(avg_acc), avg_acc.shape)
    best_lamb = lamb_list[best_lamb_idx]
    best_kernel = kernel_list[best_kernel_idx]
    # Reshape to a "len(lamb_list)-by-len(kernel_list)" matrix
    avg_acc = avg_acc.reshape((len(lamb_list), len(kernel_list)))
    # return a "len(lamb_list)-by-len(kernel_list)" accuracy variable,
    # the best lamb and the best kernel
    return avg_acc, best_lamb, best_kernel

# d) We will evaluate your choices from (c) on a new test dataset (that you
# don’t have access to until the A2 grades are released). You will get full mark here if
# your chosen hyper-parameters, kernel function, dualHinge and dualClassify can
# achieve acceptable performance on the test dataset.