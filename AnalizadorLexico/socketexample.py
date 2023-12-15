import socket
import sys

# Configura el cliente de sockets en Python
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(('localhost', 12345))

# Lee la instrucción desde el servidor Java
instruction = input().strip()

if instruction == "IN":
    # Muestra un JOptionPane para recibir datos
    user_input = input("Ingresa un valor: ")
    # Envía los datos al servidor Java
    client_socket.sendall(user_input.encode('utf-8'))

# Otros pasos de tu script de Python
# ...

# Cierra el socket
client_socket.close()

sys.exit(0)
