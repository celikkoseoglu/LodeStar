//
//  ViewController.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 17.10.2017.
//  Copyright © 2017 Berk Abbasoglu. All rights reserved.
//

import UIKit

protocol DestinationViewControllerDelegate {
    
    func setDestinationName(_ name:String)

}

class DestinationViewController: UIViewController, DestinationViewControllerDelegate {

    @IBOutlet weak var destinationNameLabel: UILabel!
    @IBOutlet weak var destinationTableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func setDestinationName(_ name: String) {
        
        destinationNameLabel.text = name;
        
    }


}

