import socket
import time
import asyncio
import RPi.GPIO as GPIO
# remember to import gpio

IP = "107.217.165.178"
PORT = 19

GPIO.setmode(GPIO.BCM)

bufferSize = 4096

run = True

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((IP, PORT))

printfunc = None


async def printData():
    data = sock.recv(bufferSize).decode('utf-8')
    data = data[:len(data)]
    #formattedData = convertData(data)
    print(data)
    out = None

    if data == "getType":
        out = "type 2"
    elif data[:6] == "setPin":
        pin = int(data[7:9])
        state = bool(data[10:14])
        if state == True:
            GPIO.output(pin, GPIO.HIGH)
        if state == False:
            GPIO.output(pin, GPIO.LOW)
        print("Set pin # " + pin + " to " + state)
    elif data[:8] == "setupPin":
        pin = int(data[9:11])
        state = data[12:14]
        if state == "in":
            GPIO.setup(pin, GPIO.IN)
            print("Set pin # " + pin + " to in")
        if state == "ou":
            GPIO.setup(pin, GPIO.OUT)
            print("Set pin # " + pin + " to out")

    if out != None:
        print("sending")
        sock.sendall(bytes(out + "\n", 'utf-8'))
        print("sent " + out)

    rintfunc = None

while run:
    if printfunc == None:
        printfunc = asyncio.run(printData())
