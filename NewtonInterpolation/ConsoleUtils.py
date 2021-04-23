def read_interval():
    try:
        print("Введите левую границу промежутка интерполирования:")
        l = float(input('l = '))
        print("Введите правую границу промежутка интерполирования:")
        r = float(input('r = '))
        if l >= r:
            raise ValueError
    except ValueError:
        print("Неверный формат")
        read_interval()
    return l, r


def read_nodes():
    print("Введите количество узлов интерполирования")
    try:
        num = int(input("num = "))
    except ValueError:
        print("Неверный формат")
        read_nodes()
    return num