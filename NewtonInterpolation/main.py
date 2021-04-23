import ConsoleUtils
import NewtonInterpolation


def main():
    print("Будем интерполировать sin(x)")
    # l, r = ConsoleUtils.read_interval()
    # num = ConsoleUtils.read_nodes()
    # NewtonInterpolation.get_data(l, r, num)
    NewtonInterpolation.read_file('data.txt')

if __name__ == '__main__':
    main()
