# COMP 3105 Fall 2023 Assignment 1

# import

import numpy as np
from cvxopt import matrix, solvers

solvers.options['show_progress'] = False

# Question 1: Linear Regression

# a) Implement w = minimizeL2(X, y)
# X is a n x d matrix, and y is a n x 1 vector, returns d x 1 weights

def minimizeL2(X, y):
    return (np.linalg.inv(X.T @ X)) @ (X.T @ y)

# b) w = minimizeL1(X, y)

def minimizeL1(X, y):
    w_shape = X.shape[1]        # d
    delta_shape = X.shape[0]    # n
    nI = -np.identity(delta_shape)  # negative identity of n x n
    # c
    c = matrix(np.concatenate((np.zeros(w_shape), np.ones(delta_shape))))
    # g
    G11 = np.zeros((delta_shape, w_shape))
    G1 = np.concatenate((G11, nI), 1)
    G2 = np.concatenate((X, nI), 1)
    G3 = np.concatenate((-X, nI), 1)
    G = matrix(np.concatenate((G1, G2, G3), 0))
    # h
    h1 = np.zeros((delta_shape,1))
    h = matrix(np.concatenate((h1,y,-y), 0))
    return solvers.lp(c, G, h)['x'][0:w_shape]

# c) w = minimizeLinf(X, y)

def minimizeLinf(X, y):
    w_shape = X.shape[1]        # d
    n = X.shape[0]              # n
    nV = -np.ones((n,1))        # negative vector of n x 1
    # c
    c = matrix(np.concatenate((np.zeros(w_shape), np.ones(1))))
    # g
    G11 = np.zeros((n, w_shape))
    G1 = np.concatenate((G11, nV), 1)
    G2 = np.concatenate((X, nV), 1)
    G3 = np.concatenate((-X, nV), 1)
    G = matrix(np.concatenate((G1, G2, G3), 0))
    # h
    h1 = np.zeros((n, 1))
    h = matrix(np.concatenate((h1, y, -y), 0))
    return solvers.lp(c, G, h)['x'][0:w_shape]

# d) Implement train_loss, test_loss = synRegExperiments()

def synRegExperiments():
    def genData(n_points):
        '''
        This function generate synthetic data
        '''
        X = np.random.randn(n_points, d) # input matrix
        X = np.concatenate((np.ones((n_points, 1)), X), axis=1) # augment input
        y = X @ w_true + np.random.randn(n_points, 1) * noise # ground truth label
        return X, y
    n_runs = 100
    n_train = 30
    n_test = 1000
    d = 5
    noise = 0.2
    train_loss = np.zeros([n_runs, 3, 3]) # n_runs * n_models * n_metrics
    test_loss = np.zeros([n_runs, 3, 3]) # n_runs * n_models * n_metrics
    for r in range(n_runs):
        w_true = np.random.randn(d + 1, 1)
        Xtrain, ytrain = genData(n_train)
        Xtest, ytest = genData(n_test)
        
        # Learn different models from the training data
        w_L2 = minimizeL2(Xtrain, ytrain)
        w_L1 = minimizeL1(Xtrain, ytrain)
        w_Linf = minimizeLinf(Xtrain, ytrain)
        
        # TODO: Evaluate the three models' performance (for each model,
        # calculate the L2, L1 and L infinity losses on the training
        # data). Save them to `train_loss`
        
        # L2 model
        L2_loss = Xtrain@w_L2 - ytrain
        train_loss[r][0][0] = (1 / (2 * n_train) * (L2_loss.T @ L2_loss))[0][0]     # L2 Loss
        train_loss[r][0][1] = np.average(np.absolute(L2_loss))      # L1 loss
        train_loss[r][0][2] = np.max(np.absolute(L2_loss))          # Linf loss
        
        # L1 model
        L1_loss = Xtrain@w_L1 - ytrain
        train_loss[r][1][0] = (1 / (2 * n_train) * (L1_loss.T @ L1_loss))[0][0]     # L2 Loss
        train_loss[r][1][1] = np.average(np.absolute(L1_loss))      # L1 loss
        train_loss[r][1][2] = np.max(np.absolute(L1_loss))          # Linf loss
        
        # Linf model
        Linf_loss = Xtrain@w_Linf - ytrain
        train_loss[r][2][0] = (1 / (2 * n_train) * (Linf_loss.T @ Linf_loss))[0][0]     # L2 Loss
        train_loss[r][2][1] = np.average(np.absolute(Linf_loss))    # L1 loss
        train_loss[r][2][2] = np.max(np.absolute(Linf_loss))        # Linf loss
        
        # TODO: Evaluate the three models' performance (for each model,
        # calculate the L2, L1 and L infinity losses on the test
        # data). Save them to `test_loss`
        
        # L2 model
        L2_loss = Xtest@w_L2 - ytest
        test_loss[r][0][0] = (1 / (2 * n_train) * (L2_loss.T @ L2_loss))[0][0]     # L2 Loss
        test_loss[r][0][1] = np.average(np.absolute(L2_loss))      # L1 loss
        test_loss[r][0][2] = np.max(np.absolute(L2_loss))          # Linf loss
        
        # L1 model
        L1_loss = Xtest@w_L1 - ytest
        test_loss[r][1][0] = (1 / (2 * n_train) * (L1_loss.T @ L1_loss))[0][0]     # L2 Loss
        test_loss[r][1][1] = np.average(np.absolute(L1_loss))      # L1 loss
        test_loss[r][1][2] = np.max(np.absolute(L1_loss))          # Linf loss
        
        # Linf model
        Linf_loss = Xtest@w_Linf - ytest
        test_loss[r][2][0] = (1 / (2 * n_train) * (Linf_loss.T @ Linf_loss))[0][0]     # L2 Loss
        test_loss[r][2][1] = np.average(np.absolute(Linf_loss))    # L1 loss
        test_loss[r][2][2] = np.max(np.absolute(Linf_loss))        # Linf loss
    
    # TODO: compute the average losses over runs
    
    # training loss
    train_avg_loss = np.zeros((3,3))
    for i in train_loss:
        train_avg_loss += i
    train_avg_loss /= n_runs
    
    # test loss
    test_avg_loss = np.zeros((3,3))
    for i in test_loss:
        test_avg_loss += i
    test_avg_loss /= n_runs
    
    # TODO: return a 3-by-3 training loss variable and a 3-by-3 test loss variable

    return train_avg_loss, test_avg_loss

# e)    Looking at your tables from above, analyze the results and discuss any
#       findings you may have and the possible reason behind them.

# f) Implement X, y = preprocessAutoMPG(dataset_folder)

def preprocessAutoMPG(dataset_folder):
    # to store data
    X = []
    y = []
    
    # read file line by line
    file = open(dataset_folder, "r")
    for line in file:
        # get the first 7 columns of data
        data = line.strip().split()[0:7]
        # skip the ones with missing values
        if data[3] == '?':
            continue
        # convert every data into float
        mpg = float(data[0])
        cylinders = float(data[1])
        displacement = float(data[2])
        horsepower = float(data[3])
        weight = float(data[4])
        acceleration = float(data[5])
        model_year = float(data[6])
        # X
        X.append([cylinders, displacement, horsepower, weight, acceleration, model_year])
        # y
        y.append([mpg])
    # convert results into numpy matrix
    X = np.array(X)
    y = np.array(y)
    return X, y

# g) Implement train_loss, test_loss = runAutoMPG(dataset_folder)

def runAutoMPG(dataset_folder):
    X, y = preprocessAutoMPG(dataset_folder)
    n, d = X.shape
    X = np.concatenate((np.ones((n, 1)), X), axis=1) # augment
    
    n_runs = 100
    train_loss = np.zeros([n_runs, 3, 3]) # n_runs * n_models * n_metrics
    test_loss = np.zeros([n_runs, 3, 3]) # n_runs * n_models * n_metrics
    
    for r in range(n_runs):
        
        # TODO: Randomly partition the dataset into two parts (50%
        # training and 50% test)
        
        train_cutoff = int(X.shape[0] // 2)
        test_cutoff = int(X.shape[0] - train_cutoff)
        
        Xtrain = X[:train_cutoff]
        ytrain = y[:train_cutoff]
        Xtest = X[test_cutoff:]
        ytest = y[test_cutoff:]
        
        # TODO: Learn three different models from the training data
        # using L1, L2 and L infinity losses
        
        w_L2 = minimizeL2(Xtrain, ytrain)
        w_L1 = minimizeL1(Xtrain, ytrain)
        w_Linf = minimizeLinf(Xtrain, ytrain)
        
        # TODO: Evaluate the three models' performance (for each model,
        # calculate the L2, L1 and L infinity losses on the training
        # data). Save them to `train_loss`
        
        # L2 model
        L2_loss = Xtrain@w_L2 - ytrain
        train_loss[r][0][0] = (1 / (2 * Xtrain.shape[0]) * (L2_loss.T @ L2_loss))[0][0]     # L2 Loss
        train_loss[r][0][1] = np.average(np.absolute(L2_loss))      # L1 loss
        train_loss[r][0][2] = np.max(np.absolute(L2_loss))          # Linf loss
        
        # L1 model
        L1_loss = Xtrain@w_L1 - ytrain
        train_loss[r][1][0] = (1 / (2 * Xtrain.shape[0]) * (L1_loss.T @ L1_loss))[0][0]     # L2 Loss
        train_loss[r][1][1] = np.average(np.absolute(L1_loss))      # L1 loss
        train_loss[r][1][2] = np.max(np.absolute(L1_loss))          # Linf loss
        
        # Linf model
        Linf_loss = Xtrain@w_Linf - ytrain
        train_loss[r][2][0] = (1 / (2 * Xtrain.shape[0]) * (Linf_loss.T @ Linf_loss))[0][0]     # L2 Loss
        train_loss[r][2][1] = np.average(np.absolute(Linf_loss))    # L1 loss
        train_loss[r][2][2] = np.max(np.absolute(Linf_loss))        # Linf loss
        
        # TODO: Evaluate the three models' performance (for each model,
        # calculate the L2, L1 and L infinity losses on the test
        # data). Save them to `test_loss`
        
        # L2 model
        L2_loss = Xtest@w_L2 - ytest
        test_loss[r][0][0] = (1 / (2 * Xtest.shape[0]) * (L2_loss.T @ L2_loss))[0][0]     # L2 Loss
        test_loss[r][0][1] = np.average(np.absolute(L2_loss))      # L1 loss
        test_loss[r][0][2] = np.max(np.absolute(L2_loss))          # Linf loss
        
        # L1 model
        L1_loss = Xtest@w_L1 - ytest
        test_loss[r][1][0] = (1 / (2 * Xtest.shape[0]) * (L1_loss.T @ L1_loss))[0][0]     # L2 Loss
        test_loss[r][1][1] = np.average(np.absolute(L1_loss))      # L1 loss
        test_loss[r][1][2] = np.max(np.absolute(L1_loss))          # Linf loss
        
        # Linf model
        Linf_loss = Xtest@w_Linf - ytest
        test_loss[r][2][0] = (1 / (2 * Xtest.shape[0]) * (Linf_loss.T @ Linf_loss))[0][0]     # L2 Loss
        test_loss[r][2][1] = np.average(np.absolute(Linf_loss))    # L1 loss
        test_loss[r][2][2] = np.max(np.absolute(Linf_loss))        # Linf loss
        
    # TODO: compute the average losses over runs
    
    # training loss
    train_avg_loss = np.zeros((3,3))
    for i in train_loss:
        train_avg_loss += i
    train_avg_loss /= n_runs
    
    # test loss
    test_avg_loss = np.zeros((3,3))
    for i in test_loss:
        test_avg_loss += i
    test_avg_loss /= n_runs
    
    # TODO: return a 3-by-3 training loss variable and a 3-by-3 test loss variable
    
    return train_avg_loss, test_avg_loss

# Question 2 - Gradient Descent & Logistic Regression

# a)
# takes a d × 1 vector of parameters w, an n × d input matrix X and an n × 1 label vector y
def linearRegL2Obj(w, X, y):
    n = X.shape[0]

    obj_val = (0.5 / n) * (X @ w - y).T @ (X @ w - y)
    grad = (1/n) * np.dot(X.T, (X @ w - y))
    return obj_val[0][0], grad

# b)

def gd(obj_func, w_init, X, y, eta, max_iter, tol):
    # Initialize w
    w = w_init
    
    for _ in range(max_iter):
        # Compute the gradient of the obj_func at current w
        _, grad = obj_func(w, X, y)
        
        # Break if the L2 norm of the gradient is smaller than tol
        if np.linalg.norm(grad) < tol:
            break
        
        # Perform gradient descent update to w
        w -= eta * grad
    
    return w

# c) 

def logisticRegObj(w, X, y):
    n = X.shape[0]

    Xw = np.dot(X, w)
    sigmoid_Xw = 1 /(1 + np.exp(-Xw))

    # Calculate the cross-entropy loss
    obj_val = (-y.T @ -np.logaddexp(0, -Xw) - ((1 - y).T @ -np.logaddexp(0, Xw)))/n

    # Calculate the gradient
    grad = X.T.dot(sigmoid_Xw - y) / n

    return obj_val, grad

# d)

def synClsExperiments():
    
    def genData(n_points, d):
        '''
        This function generate synthetic data
        '''
        c0 = np.ones([1, d]) # class 0 center
        c1 = -np.ones([1, d]) # class 1 center
        X0 = np.random.randn(n_points, d) + c0 # class 0 input
        X1 = np.random.randn(n_points, d) + c1 # class 1 input
        X = np.concatenate((X0, X1), axis=0)
        X = np.concatenate((np.ones((2 * n_points, 1)), X), axis=1) # augmentation
        y = np.concatenate([np.zeros([n_points, 1]), np.ones([n_points, 1])], axis=0)
        return X, y
    
    def runClsExp(m=100, d=2, eta=0.1, max_iter=1000, tol=1e-10):
        '''
        Run classification experiment with the specified arguments
        '''
        Xtrain, ytrain = genData(m, d)
        n_test = 10
        Xtest, ytest = genData(n_test, d)
        
        w_init = np.zeros([d + 1, 1])
        w_logit = gd(logisticRegObj, w_init, Xtrain, ytrain, eta, max_iter, tol)
        
        # TODO: Compute predicted labels of the training points
        ytrain_hat = ((Xtrain @ w_logit) >= 0).astype(int)
        
        # TODO: Compute the accuarcy of the training set
        train_acc = np.mean((ytrain_hat == ytrain).astype(int))
        
        # TODO: Compute predicted labels of the test points
        ytest_hat = ((Xtest @ w_logit) >= 0).astype(int)
        
        # TODO: Compute the accuarcy of the training set
        test_acc = np.mean((ytest_hat == ytest).astype(int))
        
        return train_acc, test_acc
    
    n_runs = 100
    train_acc = np.zeros([n_runs, 4, 3])
    test_acc = np.zeros([n_runs, 4, 3])
    
    for r in range(n_runs):
        for i, m in enumerate((10, 50, 100, 200)):
            train_acc[r, i, 0], test_acc[r, i, 0] = runClsExp(m=m)
        for i, d in enumerate((1, 2, 4, 8)):
            train_acc[r, i, 1], test_acc[r, i, 1] = runClsExp(d=d)
        for i, eta in enumerate((0.1, 1.0, 10., 100.)):
            train_acc[r, i, 2], test_acc[r, i, 2] = runClsExp(eta=eta)
    
    # TODO: compute the average accuracies over runs
    
    # training accuracy
    train_avg_acc = np.zeros((4,3))
    for i in train_acc:
        train_avg_acc += i
    train_avg_acc /= n_runs
    
    # test accuracy
    test_avg_acc = np.zeros((4,3))
    for i in test_acc:
        test_avg_acc += i
    test_avg_acc /= n_runs
    
    # TODO: return a 4-by-3 training accuracy variable and a 4-by-3 test accuracy variable
    
    return train_avg_acc, test_avg_acc

# e)Looking at your tables from above, analyze the results and discuss any findings you may have and the possible reason behind them.

# f) 

def preprocessSonar(dataset_folder):
    # to store data
    X = []
    y = []
    
    # read file line by line
    file = open(dataset_folder, "r")
    for line in file:
        # store data in list
        data = line.strip().split(",")
        
        # get first 60 values in data and store in X
        X.append([float(value) for value in data[:-1]])
        
        # convert last element to integer and store in Y
        if data[60] == 'R':
            y.append([0])
        else:
            y.append([1])
            
    # convert X and y into matrix
    X = np.array(X)
    y = np.array(y)

    return X, y

# g) 

def custom_train_test_split(X, y, train_fraction=0.4, val_fraction=0.4):
    n = X.shape[0]
    train_size = int(round(train_fraction * n))
    val_size = int(round(val_fraction * n))

    # Shuffle the dataset
    indices = np.arange(n)
    np.random.shuffle(indices)

    # Split the dataset into training, validation, and test sets
    X_train = X[indices[:train_size]]
    y_train = y[indices[:train_size]]
    X_val = X[indices[train_size:train_size + val_size]]
    y_val = y[indices[train_size:train_size + val_size]]
    X_test = X[indices[train_size + val_size:]]
    y_test = y[indices[train_size + val_size:]]

    return X_train, y_train, X_val, y_val, X_test, y_test

def evaluate_accuracy(w, X, y):
    # Calculate the predicted probabilities using the logistic regression model
    y_pred_probs = 1 / (1 + np.exp(-np.dot(X, w)))
    
    # Convert predicted probabilities to binary predictions (0 or 1)
    y_pred = (y_pred_probs >= 0.5).astype(int)
    
    # Calculate accuracy by comparing predicted and true labels
    accuracy = np.mean(y_pred == y)
    
    return accuracy

def runSonar(dataset_folder):
    X, y = preprocessSonar(dataset_folder)
    n, d = X.shape
    X = np.concatenate((np.ones((n, 1)), X), axis=1) # augment
    eta_list = [0.1, 1, 10, 100]
    train_acc = np.zeros([len(eta_list)])
    val_acc = np.zeros([len(eta_list)])
    test_acc = np.zeros([len(eta_list)])

    # TODO: Randomly partition the dataset into three parts (40%
    # training (use the round function), 40% validation and
    # the remaining ~20% as test)
    # Custom random partitioning without scikit-learn
    # Randomly partition the dataset into three parts (40% training, 40% validation, and 20% test)
    X_train, y_train, X_val, y_val, X_test, y_test = custom_train_test_split(X, y)

    for i, eta in enumerate(eta_list):
        w_init = np.zeros([d + 1, 1])
        w = gd(logisticRegObj, w_init, X_train, y_train, eta, max_iter=1000, tol=1e-8)

        # Evaluate the model's accuracy on the training data
        train_acc[i] = evaluate_accuracy(w, X_train, y_train)

        # Evaluate the model's accuracy on the validation data
        val_acc[i] = evaluate_accuracy(w, X_val, y_val)

        # Evaluate the model's accuracy on the test data
        test_acc[i] = evaluate_accuracy(w, X_test, y_test)

    return train_acc, val_acc, test_acc
