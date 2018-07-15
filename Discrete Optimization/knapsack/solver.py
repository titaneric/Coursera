#!/usr/bin/python
# -*- coding: utf-8 -*-

from collections import namedtuple
Item = namedtuple("Item", ['index', 'value', 'weight'])

def solve_it(input_data):
    # Modify this code to run your optimization algorithm

    # parse the input
    lines = input_data.split('\n')

    firstLine = lines[0].split()
    item_count = int(firstLine[0])
    capacity = int(firstLine[1])

    items = []



    for i in range(1, item_count+1):
        line = lines[i]
        parts = line.split()
        items.append(Item(i-1, int(parts[0]), int(parts[1])))

    if item_count <= 200:
        obj, taken = dynimic_programming(items, capacity)
        output_data = "{} {}\n".format(obj, 1)
    else:
        obj, taken = greedy(items, capacity)
        output_data = "{} {}\n".format(obj, 0)

    # prepare the solution in the specified output format
    
    output_data += ' '.join(map(str, taken))
    return output_data


def greedy(items, capacity):
    # revise from the original in-order version
    # sort the item by their value/weight in descending order
    items = sorted(items, key=lambda item: item.value/item.weight, reverse=True)
    # a trivial greedy algorithm for filling the knapsack
    # it takes items which has the largest value/weight density until the knapsack is full
    obj = 0
    weight = 0
    taken = [0 for _ in range(len(items))]

    for item in items:
        if weight + item.weight <= capacity:
            taken[item.index] = 1
            obj += item.value
            weight += item.weight

    return obj, taken


def dynimic_programming(items, capacity):
    table = [[0 for _ in range(capacity+1)] for _ in range(len(items)+1)]
    for i, item in zip(range(1, len(items)+1), items):
        for k in range(1, capacity+1):
            table[i][k] = max(table[i-1][k], item.value + table[i-1][k-item.weight]) \
                            if item.weight <= k else table[i-1][k]
    obj = table[len(items)][capacity]
    taken = _find_DP_taken(items, table, capacity, obj)
    return obj, taken

def _find_DP_taken(items: list, table, capacity, obj):
    taken = [0 for _ in range(len(items))]
    val = obj
    k = capacity
    for i, item in zip(reversed(range(1, len(items)+1)), reversed(items)):
        if table[i-1][k] == val:
            continue
        else:
            taken[item.index] = 1
            k -= item.weight
            val = table[i-1][k]
    return taken

if __name__ == '__main__':
    import sys
    if len(sys.argv) > 1:
        file_location = sys.argv[1].strip()
        with open(file_location, 'r') as input_data_file:
            input_data = input_data_file.read()
        print(solve_it(input_data))
    else:
        print('This test requires an input file.  Please select one from the data directory. (i.e. python solver.py ./data/ks_4_0)')

