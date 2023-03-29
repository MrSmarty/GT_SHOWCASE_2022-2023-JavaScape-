//
//  ModuleCardView.swift
//  Javascape IOS App
//
//  Created by Lincoln Smith on 3/7/23.
//

import SwiftUI

struct ModuleCardView: View {
    let card: ModuleCard
    var body: some View {
        VStack {
            Text(card.moduleName)
                .font(.headline)
            Spacer()
            Text(card.moduleType)
            HStack {
                Text(card.moduleKind)
                Spacer()
                Text(card.moduleParent)
            }
        }
        .padding()
    }
}

struct ModuleCardView_Previews: PreviewProvider {
    static var card = ModuleCard.sampleData[0]
    static var previews: some View {
        ModuleCardView(card:card)
            .previewLayout(.fixed(width: 400, height: 80))
    }
}
