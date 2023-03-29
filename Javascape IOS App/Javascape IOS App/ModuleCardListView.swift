//
//  ModuleCardListView.swift
//  Javascape IOS App
//
//  Created by Lincoln Smith on 3/7/23.
//

import SwiftUI

struct ModuleCardListView: View {
    let cards: [ModuleCard]
    var body: some View {
        List {
            ForEach(cards, id: \.moduleName) { card in
                            ModuleCardView(card: card)
                        }
        }
    }
}

struct ModuleCardListViewPreview_Previews: PreviewProvider {
    static var previews: some View {
        ModuleCardListView(cards:ModuleCard.sampleData)
    }
}
