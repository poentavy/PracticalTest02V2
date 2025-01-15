import socket
from datetime import datetime

# Configurare server
HOST = "172.30.80.1"  # Ascultă pe toate interfețele
PORT = 5000      # Portul serverului

# Crearea unui socket de tip TCP
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((HOST, PORT))
server_socket.listen(1)  # Ascultă conexiuni

print(f"Serverul rulează pe {HOST}:{PORT}")

try:
    while True:
        client_socket, client_address = server_socket.accept()
        print(f"Conexiune acceptată de la {client_address}")

        # Trimite data și ora curente
        current_time = datetime.now().strftime("%H:%M:%S")
        client_socket.sendall(current_time.encode())

        # Închide conexiunea cu clientul
        client_socket.close()
except KeyboardInterrupt:
    print("Serverul s-a oprit.")
finally:
    server_socket.close()
