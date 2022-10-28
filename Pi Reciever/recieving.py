import socket
import time
import asyncio
import RPi.GPIO as GPIO
import json
from os.path import exists

# IP = ""
# PORT = 19

settings = None

if not exists("settings.json"):
    dictionary = {"IP": "", "PORT": 19, "NAME": ""}
    with open("settings.json", "w") as f:
        json.dump(dictionary, f)
    f.close()
    exit()
else:
    with open("settings.json", "r") as f:
        settings = json.load(f)
    f.close()

if settings[IP] == "" or settings[PORT] == "":
    print("Please set the IP and PORT variables in the settings file")
    exit()

GPIO.setmode(GPIO.BCM)

bufferSize = 4096

run = True

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((settings[IP], settings[PORT]))

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
        state = data[10:14]
        if state == "true":
            GPIO.output(pin, GPIO.HIGH)
            print("Set pin # " + str(pin) + " to true")
        if state == "fals":
            GPIO.output(pin, GPIO.LOW)
            print("Set pin # " + str(pin) + " to false")

    elif data[:8] == "setupPin":
        pin = int(data[9:11])
        state = data[12:14]
        if state == "in":
            GPIO.setup(pin, GPIO.IN)
            print("Set pin # " + str(pin) + " to in")
        if state == "ou":
            GPIO.setup(pin, GPIO.OUT)
            print("Set pin # " + str(pin) + " to out")

    if out != None:
        print("sending")
        sock.sendall(bytes(out + "\n", 'utf-8'))
        print("sent " + out)

    printfunc = None

while run:
    if printfunc == None:
        printfunc = asyncio.run(printData())
