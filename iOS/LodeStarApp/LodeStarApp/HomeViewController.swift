//
//  LoginViewController.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 17.01.2018.
//  Copyright © 2018 Berk Abbasoglu. All rights reserved.
//

import Foundation

import UIKit

class HomeViewController: UIViewController {
    
    @IBOutlet weak var boardingPassBackView: UIView!
    @IBOutlet weak var virtualRealityBackView: UIView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // shadows
        boardingPassBackView.layer.shadowColor = UIColor.black.cgColor
        boardingPassBackView.layer.shadowOffset = CGSize(width: 5, height: 5)
        boardingPassBackView.layer.shadowRadius = 5;
        boardingPassBackView.layer.shadowOpacity = 0.25;
        boardingPassBackView.clipsToBounds = false
        boardingPassBackView.layer.masksToBounds = false
        
        virtualRealityBackView.layer.shadowColor = UIColor.black.cgColor
        virtualRealityBackView.layer.shadowOffset = CGSize(width: 5, height: 5)
        virtualRealityBackView.layer.shadowRadius = 5;
        virtualRealityBackView.layer.shadowOpacity = 0.25;
        virtualRealityBackView.clipsToBounds = false
        virtualRealityBackView.layer.masksToBounds = false
        
        //blackLayer.layer.cornerRadius = 8
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func logoutButtonAction(_ sender: UIButton) {
        dismiss(animated: true, completion: nil)
    }
    
}
