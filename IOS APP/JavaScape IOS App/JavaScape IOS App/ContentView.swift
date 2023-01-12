//
//  ContentView.swift
//  JavaScape IOS App
//
//  Created by Lincoln Smith on 11/14/22.
//

import SwiftUI
import socket

let white = Color(red: 1.0, green: 1.0, blue: 1.0, opacity: 1.0)
let lightGray = Color(red: 0.9, green: 0.9, blue: 0.9, opacity: 1.0)
let darkGray = Color(red: 0.4, green: 0.4, blue: 0.4, opacity: 1.0)


struct ContentView: View {
    @State var IP: String = ""
    var body: some View {
        VStack {
            TextField(text: $IP, prompt: Text("IP"))
                .disableAutocorrection(true)
                .foregroundColor(white)
                .padding()
                .background(darkGray)
                .cornerRadius(5.0)
                .padding(.bottom, 20)
        }
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
