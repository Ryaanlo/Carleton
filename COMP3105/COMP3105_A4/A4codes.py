# COMP3105 A4

import time

import numpy as np

import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
from torch.utils.data import TensorDataset, DataLoader

# Prepare dataset
def prepareData(file_name):
    train_data = np.loadtxt(file_name, delimiter=',')
    y = train_data[:, 0]
    X = train_data[:, 1:] / 255.

    # Convert to PyTorch Tensors
    tensor_X = torch.Tensor(X)
    tensor_y = torch.LongTensor(y)

    dataset = TensorDataset(tensor_X, tensor_y) # create PyTorch TensorDataset
    dataloader = DataLoader(dataset, batch_size=32, shuffle=True) # create PyTorch DataLoader

    return dataset, dataloader

train_dataset, train_dataloader = prepareData('A4train.csv')
test_dataset, test_dataloader = prepareData('A4val.csv')

# Define neural network model
class Net(nn.Module):
    def __init__(self):
        super(Net, self).__init__()
        self.fc1 = nn.Linear(2352, 512)  # fully connected layers
        self.fc2 = nn.Linear(512, 256)
        self.fc3 = nn.Linear(256, 10)
        self.dropout = nn.Dropout(0.25)  # PyTorch: drop probability

    def forward(self, x):
        x = self.fc1(x)
        x = F.relu(x)  # ReLU activation
        x = self.dropout(x)
        x = self.fc2(x)
        x = F.relu(x)
        x = self.dropout(x)
        x = self.fc3(x)
        # softmax for multi-class classification
        # using log of softmax here due to how the loss is defined later
        output = F.log_softmax(x, dim=1)
        return output

device = "cuda" if torch.cuda.is_available() else "cpu"
print(device)
model = Net().to(device)  # use proper device

# Optimizer: Stochastic gradient descent
optimizer = optim.SGD(model.parameters(), lr=0.01)

# Training loop
def train(model, optimizer, train_dataloader, device):
    model.train()  # entering training mode (dropout behaves differently)
    for batch_idx, (data, target) in enumerate(train_dataloader):
        data, target = data.to(device), target.to(device)  # move data to the sPame device
        optimizer.zero_grad()  # clear existing gradients
        output = model(data)  # forward pass
        loss = F.nll_loss(output, target)  # compute the loss
        loss.backward()  # backward pass: calculate the gradients
        optimizer.step()  # take a gradient step

# Test loop
def test(model, test_dataloader, device):
    model.eval()  # entering evaluation mode (dropout behaves differently)
    m = len(test_dataloader.dataset)
    test_loss = 0
    correct = 0
    with torch.no_grad():  # do not compute gradient
        for data, target in test_dataloader:
            data, target = data.to(device), target.to(device)
            output = model(data)
            test_loss += F.nll_loss(output, target, reduction='sum').item()  # sum up batch loss
            pred = output.argmax(dim=1, keepdim=True)  # get the index of the max log-probability
            correct += pred.eq(target.view_as(pred)).sum().item()

    test_loss /= m
    accuracy = correct / m
    # print(f'Test average loss: {test_loss:.4f}, accuracy: {accuracy:.3f}')
    return test_loss, accuracy

n_epochs = 10

def learn(X, y):
    # Training
    start_time = time.time()
    for epoch in range(n_epochs):
        train(model, optimizer, train_dataloader, device)
        # test(model, test_dataloader, device)
    print(f"Time: {time.time() - start_time:.3f}")

    return model

def classify(Xtest, model):
    model.eval()
    predictions = []

    with torch.no_grad():
        for data, target in Xtest:  # Assuming Xtest is a DataLoader with the test dataset
            data, target = data.to(device), target.to(device)            
            data = data.to(device)
            output = model(data)
            pred = output.argmax(dim=1, keepdim=True)
            predictions.extend(pred.cpu().numpy())

    return np.array(predictions).flatten()


train_data = np.loadtxt('A4train.csv', delimiter=',')
y = train_data[:, 0]
X = train_data[:, 1:] / 255.
model = learn(X, y)

test_data = np.loadtxt('A4val.csv', delimiter=',')
y = test_data[:, 0]
X = test_data[:, 1:] / 255.
yhat = classify(test_dataloader, model)
print("yhat: ", yhat)
print("y: ", y)

def accuracy(y, yhat):
  y = y.flatten()
  yhat = yhat.flatten()
  return np.sum(y == yhat) / y.shape[0]

pre = accuracy(y, yhat)
print(pre)