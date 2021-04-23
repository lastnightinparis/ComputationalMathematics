import numpy as np
import math
import matplotlib.pyplot as plt


def get_data(l, r, num):
    x_data = np.linspace(l, r, num)
    y_data = [f(i) for i in x_data]
    graphic(x_data, y_data)


def read_file(filename):
    x_data = []
    y_data = []
    with open(filename) as file:
        for line in file:
            x, y = [float(x) for x in line.split()]
            x_data.append(x)
            y_data.append(y)
    graphic(x_data, y_data)


def f(x):
    return math.sin(x)


def get_newton_coeffs(x, y):
    size = len(x)
    x = np.copy(x)
    a = np.copy(y)
    for k in range(1, size):
        a[k:size] = (a[k:size] - a[k - 1]) / (x[k:size] - x[k - 1])
    return a


def interpolate(x_data, y_data, i):
    a = get_newton_coeffs(x_data, y_data)
    n = len(x_data) - 1
    p = a[n]
    for j in range(1, n + 1):
        p = a[n - j] + (i - x_data[n - j]) * p
    return p


def graphic(x, y):
    step = (x[len(x) - 1] - x[0]) / 1000
    x_values = np.arange(x[0], x[len(x) - 1], step)
    y_values = [f(i) for i in x_values]
    interp_values = [interpolate(x, y, i) for i in x_values]
    plt.figure(figsize=(8, 8))
    plt.grid(True)
    plt.plot(x_values, interp_values, label='Вычисленная функция', linewidth=4, color='green')
    plt.plot(x_values, y_values, label='Текущая функция', linewidth=4, color='yellow')
    for i in range(len(x)):
        plt.scatter(x[i], y[i], s=100, marker='o', alpha=1)
    plt.legend(loc=0)
    plt.show()
    while True:
        xi = float(input('Введите точку:'))
        if xi < x[0] or xi > x[len(x) - 1]:
            print('Данное значение не входит в промежуток.')
        else:
            v = interpolate(x, y, xi)
            print('Значение в этой точке:')
            print(v)
