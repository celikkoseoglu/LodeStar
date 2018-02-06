//
//  FlightInformationViewController.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 31.01.2018.
//  Copyright © 2018 Berk Abbasoglu. All rights reserved.
//

import Foundation

import UIKit

class FlightInformationViewController: UIViewController {

    @IBAction func backButton(_ sender: UIButton) {
        dismiss(animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
}
