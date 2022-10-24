import socket
import time
import asyncio
# remember to import gpio

IP = "localhost"
PORT = 19

bufferSize = 4096

run = True

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((IP, PORT))

printfunc = None


async def printData():
    data = sock.recv(bufferSize).decode('utf-8')
    data = data[:len(data)-1]
    #formattedData = convertData(data)
    print(data)
    out = None

    if data == "getType":
        out = "type 2"
    elif data[:6] == "setPin":
        pin = int(data[7:9])
        state = bool(data[10:14])
        print("Set pin # " + pin + " to " + state)

    if out != None:
        print("sending")
        sock.sendall(bytes(out + "\n", 'utf-8'))
        print("sent " + out)

    rintfunc = None

while run:
    if printfunc == None:
        printfunc = asyncio.run(printData())
