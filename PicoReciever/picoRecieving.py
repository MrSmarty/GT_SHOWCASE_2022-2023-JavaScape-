import socket
import machine
import asyncio as aio


IP = "107.217.165.178"
PORT = 19

BUFFSIZE = 4096

RUN = True

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((IP, PORT))

processFunc = None

output = None

async def processData():
    data = sock.recv(BUFFSIZE).decode('utf-8')
    data = data[:len(data)]
    print(data)

    args = data.split(" ")

    if args[0] == "getType":
        output = "type 2"
    elif args[0] == "setPin":
        machine.Pin(int(args[1]), machine.Pin.OUT, value=int(args[2]))
    elif args[0] == "setupPin":
        machine.Pin(int(args[1]), machine.Pin.IN if int(args[2]) == 0 else machine.Pin.OUT)
    elif args[0] == "getPin":
        output = machine.Pin(int(args[1])).value()


async def printData():
    sock.sendall(bytes(output + "\n", 'utf-8'))

#n = machine.Pin("LED", machine.Pin.OUT, value=1)