#!/usr/bin/env python3
import argparse, decimal
parser = argparse.ArgumentParser()
parser.add_argument('fileName')
args = parser.parse_args()
file = open(args.fileName,"r")
decimal.getcontext().prec = 500
xi = [decimal.Decimal(i) for i in file.readline().split(" ")]
yi = [decimal.Decimal(i) for i in file.readline().split(" ")]
k = len(xi)
a = yi
for i in range(1, k):
    for j in range(k-1,i-1,-1):
        a[j] = (a[j] - a[j-1]) / (xi[j] - xi[j-i])

def newton(x):
    total = decimal.Decimal(0)
    for j in range(k):
        total = total + (a[j] * n(j,x))
    return total

def n(j,x):
    prod = decimal.Decimal(1)
    for i in range(j):
        prod = prod * (x - xi[i])
    return prod

while True:
    inp = input("Enter value: ")
    if inp == "q":
        exit()
    print(float(newton(decimal.Decimal(inp))))
