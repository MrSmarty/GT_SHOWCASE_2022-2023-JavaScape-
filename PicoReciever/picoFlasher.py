import os
from tkinter import *

pico_flash_cmd = "dfu-util -a 0 -s 0x08000000:leave -D"


def flash_file(filename):
  os.system(pico_flash_cmd + " " + filename)


def select_file():
  filename = filedialog.askopenfilename(
      title="Select file to flash onto Raspberry Pi Pico")
  flash_file(filename)


root = Tk()
root.title("Raspberry Pi Pico Flasher")

btn_select = Button(root, text="Select file", command=select_file)
btn_select.pack()

root.mainloop()
