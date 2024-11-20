#!/usr/bin/env python3
import argparse, random, time
parser = argparse.ArgumentParser()
parser.add_argument('n')
args = parser.parse_args()
n = int(args.n)
MIN,MAX = -1000,1000
xi,yi = [],[]
size = (MAX - MIN) // n
for i in range(n):
    mini = size * i + MIN
    maxi = size * i + size + MIN - 1
    xi.append(random.randrange(mini, maxi) + random.random())
    yi.append(random.randrange(MIN, MAX) + random.random())

timestr = time.strftime("%H%M%S")
file = open(timestr+".txt", "w")
file.write(' '.join(map(str,xi)))
file.write("\n")
file.write(' '.join(map(str,yi)))
file.write("\n")
file.close()
