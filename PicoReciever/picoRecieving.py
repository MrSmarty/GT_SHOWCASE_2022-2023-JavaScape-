import usocket
import machine
import uasyncio as aio
import network
import os
import sys

IP = "107.217.165.178"
PORT = ""
SSID = "Da Snifs"
PASSWORD = "11111111"
NAME = "Raspberry Pi Pico"
# Use big encoding to get a unique ID for the Pico
UID = int.from_bytes(machine.unique_id(), "big")
print(UID)

BUFFSIZE = 4096

RUN = True

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
        
        #print('Got a connection from %s' % str(addr))
        request = conn.recv(1024)
        request = str(request)
        #print('Content = %s' % request)
        
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

if not wirelessNet.isconnected():
    print("No connection")

sock = usocket.socket(usocket.AF_INET, usocket.SOCK_STREAM)
sock.connect((int(IP), PORT))

processFunc = None

global output
output = None

async def processData():
    data = sock.recv(BUFFSIZE).decode('utf-8')
    data = data[:len(data)]
    print(data)

    args = data.split(" ")

    if args[0] == "getType":
        output = "type 2"
    # Set a GPIO Pin to a single 
    elif args[0] == "setPin":
        machine.Pin(int(args[1]), machine.Pin.OUT, value=int(args[2]))

    elif args[0] == "setupPin":
        machine.Pin(int(args[1]), machine.Pin.IN if int(args[2]) == 0 else machine.Pin.OUT)

    elif args[0] == "setPWM":
        machine.PWM(int(args[1]), args[2], args[3], args[4])
    # Returns the value of a regular Pin
    elif args[0] == "getPin":
        output = machine.Pin(int(args[1])).value()
    # Set the name of the Pico
    elif args[0] == "setName":
        NAME = args[1]
    # Get the name of the Pico
    elif args[0] == "getName":
        output = NAME
    elif args[0] == "getID":
        output = UID


def printData():
    sock.sendall(bytes(output + "\n", 'utf-8'))
    print("sent: " + output)
    output = None

while RUN:
    if processFunc == None:
        printFunc = aio.run(processData())
    if output != None:
        printData()

#n = machine.Pin("LED", machine.Pin.OUT, value=1)v
