import usocket as socket
import machine
from machine import Pin, ADC, PWM
import uasyncio as aio
import network
import os
import sys
import time

# IP = "192.168.1.241"
# PORT = "19"
# SSID = "Da Snifs"
# PASSWORD = "11111111"
NAME = "Raspberry Pi Pico"

IP = "10.3.5.60"
PORT = "19"
SSID = "LVISD Student"
PASSWORD = "!V1k1ng$R0w1ng!"

# Use big encoding to get a unique ID for the Pico
UID = int.from_bytes(machine.unique_id(), "big")
print(NAME)
print(UID)

BUFFSIZE = 4096

RUN = True

output = None

processFunc = None

if IP == "" or PORT == "":
    selfNet = network.WLAN(network.AP_IF)
    selfNet.config(ssid=NAME, security=0, txpower=-60)
    selfNet.active(True)
    print(selfNet.ifconfig())
    site = usocket.socket(usocket.AF_INET, usocket.SOCK_STREAM)
    site.setsockopt(usocket.SOL_SOCKET, usocket.SO_REUSEADDR, 1)
    site.bind(('', 80))
    site.listen(5)

    html = """<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <title>Setup</title>
    <link rel="icon" href="data:,">
  </head>
  <body>
    <h1>Welcome to Javascape!</h1>
    <form>
        <label>IP Address of Server:</label> <br>
        <input type="text" name="ip" placeholder="192.168.1.254" /> </br>
        <label>Port of Server:</label> <br>
        <input type="number" name="port" placeholder="19" /> </br>
        <label>Wifi Network Name (SSID):</label> <br>
        <input type="text" name="ssid" placeholder="My Wifi Network" /> </br>
        <label>Wifi Network Password:</label> <br>
        <input type="password" name="password" placeholder="12345678" /> </br>
        <label>Household ID:</label> <br>
        <input type="number" name="ID" placeholder="1" /> </br>
        <button>Submit</button>
    </form>
  </body>
  <style>
        h1 {
            color: #277D50;
            font-size: 8em;
        }
        body {
            background-color: #E1DCC9;
            font-family: Arial, Helvetica, sans-serif;
            font-size: 14px;
            color: #345894;
            margin: 20px;
            padding: 20px;
            text-align: center;
        }
        label, input, button {
            margin: 5px;
            font-weight: bold;
            font-size: 400%;
        }
    </style>
</html>
"""
    while True:
        conn, addr = site.accept()

        conn.sendall(html)

        # print('Got a connection from %s' % str(addr))
        request = conn.recv(1024)
        request = str(request)
        # print('Content = %s' % request)

        argsIndex = request.find("/?") + 2
        argsEndIndex = request.find("HTTP") - 1
        request = request[argsIndex:argsEndIndex]
        requestargs = request.split("&")
        print(requestargs)
        if (len(requestargs) == 5) and requestargs[0][3:] != "" and requestargs[1][5:] != "" and requestargs[2][5:] != "" and requestargs[3][9:] != "" and requestargs[4][3:] != "":
            response = "Thank You!"
            conn.sendall(response)
            conn.close()
            site.close()
            break


wirelessNet = network.WLAN(network.STA_IF)
wirelessNet.active(True)
wirelessNet.connect(SSID, PASSWORD)

time.sleep(1)

if not wirelessNet.isconnected():
    print("No connection")

pins = []

# TODO: Check if socket is still active, if not, then close the socket and restart the connection
def process(data):
    print(data)
    args = data.split(" ")

    if args[0] == "getInfo":
        return "type 2 " + str(UID) + " Pico_W"
    
    elif args[0] == "set":
        if (args[1] == "LED"):
            Pin("LED", Pin.OUT, value=int(args[2]))
            return "Set LED to " + args[2]
        else:
            Pin(int(args[1]), Pin.OUT, value=int(args[2]))
            return "Set pin " + args[1] + " to " + args[2]
    elif args[0] == "setDigital":
        pins[int(args[1])] = ADC(Pin(int(args[1])))
        
    elif args[0] == "get":
        if (args[1] == "LED"):
            return Pin("LED").value
        else:
            return Pin(int(args[1])).value
        
    elif args[0] == "setAll":
        for i in range(int(args[1])):
            n = args[2+i].split(":")
            if n[0] == "0":
                pins.append(Pin(
                    int(i), Pin.OUT, value=int(n[1])))
            else:
                pins.append(Pin(int(i), Pin.IN))
        return "finished setAll"
    elif args[0] == "getAll":
        n = ""
        for i in range(len(pins)):
            n = n + str(pins[i].value())
        print(n)
    return "Unrecognized command"

# The main thread


async def run():
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # sock = socket.socket()

    def close():
        machine.Pin("LED", machine.Pin.OUT, value=0)
        print("Socket closed")

    # Attempt a connection to the server
    try:
        print("Connecting to " + IP + ":" + PORT)
        server = socket.getaddrinfo(IP, int(PORT))[0][-1]
        sock.connect(server)
        print("Connected")
    except OSError as e:
        print("Socket error: " + str(e))
        sock.close()
        return

    while True:
        sreader = aio.StreamReader(sock)
        swriter = aio.StreamWriter(sock, {})

        while True:
            try:
                input = (await sreader.readline())[:-2]
                input = input.decode("utf-8")
                output = process(input) + " \n"
                print("Sending: " + output[:-2])
                # sock.sendall(bytes(output, "utf-8"))
                swriter.write(output.encode("utf-8"))
                await swriter.drain()
                print("Sent")

            except OSError as e:
                close()
                return
            await aio.sleep(1)

try:
    aio.run(run())
except KeyboardInterrupt:
    print("Keyboard interrupt")
finally:
    _ = aio.new_event_loop()
    print("Done")


# n = machine.Pin("LED", machine.Pin.OUT, value=1)
