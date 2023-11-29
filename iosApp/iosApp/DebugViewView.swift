//
//  DebugViewView.swift
//  iosApp
//
//  Created by Виктор Савчик on 13/10/2023.
//

import SwiftUI
import DebugView

struct DebugViewView: UIViewControllerRepresentable {
    typealias UIViewControllerType = UIViewController

    func makeUIViewController(context: Context) -> UIViewController {
        return DebugViewManager.shared.getDebugViewViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        print("Update ui View Controller \(uiViewController.view.bounds.size)")
    }
}
