from math import sin


def integrate(partition, type, l, r, f):
    neg = False
    if l > r:
        l, r = r, l
        neg = True
    int_sum = 0
    step = (r - l) / partition
    while l < r:
        if type == 'l':
            int_sum += f(l) * step
        if type == 'm':
            int_sum += f(l + step / 2) * step
        if type == 'r':
            int_sum += f(l + step) * step
        l += step
    if neg:
        return -int_sum
    else:
        return int_sum


def runge_rule(partition, acc, type, l, r, f):
    i0 = integrate(partition, type, l, r, f)
    partition *= 2
    i1 = integrate(partition, type, l, r, f)
    d = abs(i1 - i0) / 3
    global epsilon
    while d >= acc:
        i0 = i1
        partition *= 2
        i1 = integrate(partition, type, l, r, f)
        d = abs(i1 - i0) / 3
    epsilon = d
    return partition


def f1(x):
    return 1 / x


def f2(x):
    return x ** 3 - 10 * x ** 2 + 1


def f3(x):
    global delta
    global isFirstKindGap
    if x != 0:
        return sin(x) / x
    else:
        isFirstKindGap = True
        return ((sin(x + delta) / (x + delta)) + (sin(x - delta) / (x - delta))) / 2


def main():
    global curr_fun
    global delta
    global isFirstKindGap
    global isSecondKindGap
    delta = 0.1
    global epsilon
    epsilon = 0
    isFirstKindGap = False
    isSecondKindGap = False
    try:
        val = int(input('Выберите функцию для интегрирования: \n'
                        '1. 1 / x \n'
                        '2. x^3 - 10x^2 + 1 \n'
                        '3. sin(x) / x \n'))
        if val < 1 or val > 3:
            raise ValueError
        l = float(input('Выберите левую границу отрезка интегрирования: \nl = '))
        r = float(input('Выберите правую границу отрезка интегрирования: \nr = '))
        acc = float(input('Введите погрешность:\nacc = '))
        if val == 1:
            curr_fun = f1
            if l * r < 0:
                print('\nИнтервал содержит разрыв второго рода, невозможно вычислить интеграл.')
                isSecondKindGap = True
        elif val == 2:
            curr_fun = f2
        elif val == 3:
            curr_fun = f3
            if l * r < 0:
                isFirstKindGap = True
        if not isSecondKindGap:
            n = runge_rule(5, acc, 'l', l, r, curr_fun)
            result = integrate(n, 'l', l, r, curr_fun)
            print('Метод левых прямоугольников: ' + str(result))
            print('Число разбиений: ' + str(n))
            print('Полученная точность: ' + str(epsilon))
            n = runge_rule(5, acc, 'm', l, r, curr_fun)
            result = integrate(n, 'm', l, r, curr_fun)
            print('Метод средних прямоугольников: ' + str(result))
            print('Число разбиений: ' + str(n))
            print('Полученная точность: ' + str(epsilon))
            n = runge_rule(5, acc, 'r', l, r, curr_fun)
            result = integrate(n, 'r', l, r, curr_fun)
            print('Метод правых прямоугольников: ' + str(result))
            print('Число разбиений: ' + str(n))
            print('Полученная точность: ' + str(epsilon))
            if isFirstKindGap:
                print("Данный промежуток содержит точку разрыва первого рода, значение функции заменено на " + str(
                    (sin(delta) / delta + sin(-delta) / (-delta)) / 2))
    except ValueError:
        print("Такого значения не предусмотрено.")
        main()


if __name__ == '__main__':
    main()

