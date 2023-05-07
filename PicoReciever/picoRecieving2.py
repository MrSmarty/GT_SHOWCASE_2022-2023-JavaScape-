import usocket as socket
import machine
import uasyncio as aio
import network
import os
import sys

IP = "107.217.165.178"
PORT = "19"
SSID = "Da Snifs"
PASSWORD = "11111111"
NAME = "Raspberry Pi Pico"
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

if not wirelessNet.isconnected():
    print("No connection")

pins = [machine.Pin] * 28


def process(data):
    print(data)
    args = data.split(" ")

    if args[0] == "getInfo":
        return "type 2 " + str(UID)
    if args[0] == "set":
        if (args[1] == "LED"):
            machine.Pin("LED", machine.Pin.OUT, value=int(args[2]))
            return "Set LED to " + args[2]
        else:
            machine.Pin(int(args[1]), machine.Pin.OUT, value=int(args[2]))
            return "Set pin " + args[1] + " to " + args[2]

    return "Unrecognized command"

# The main thread


async def run():
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def close():
        sock.close()
        print("Socket closed")

    # Attempt a connection to the server
    try:
        server = socket.getaddrinfo(IP, int(PORT))[0][-1]
        sock.connect(server)
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
                output = process(input) + "\n"
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
