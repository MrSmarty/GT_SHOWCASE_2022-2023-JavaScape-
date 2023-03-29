//
//  SensorCard.swift
//  Javascape IOS App
//
//  Created by Lincoln Smith on 3/7/23.
//

import Foundation

struct ModuleCard {
    // moduleKind defines sensor/something like a LED or Valve
    var moduleKind: String
    // moduleType is the technical spec
    var moduleType: String
    // The name given to the module by the user
    var moduleName: String
    // The device that the module is plugged into
    var moduleParent: String
}

extension ModuleCard {
    static let sampleData: [ModuleCard] = [
        ModuleCard(moduleKind: "sensor", moduleType: "Capacitive Moisture Sensor v2.0", moduleName: "Herb Garden Moisture Sensor", moduleParent: "My Pico W"),
        ModuleCard(moduleKind: "Other", moduleType: "LED", moduleName: "Herb Garden Moisture LED", moduleParent: "My Pico W")
    ]
}
