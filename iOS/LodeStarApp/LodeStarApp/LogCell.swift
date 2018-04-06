//
//  LogCell.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 13.01.2018.
//  Copyright © 2018 Berk Abbasoglu. All rights reserved.
//

import Foundation
import UIKit

class LogCell: UICollectionViewCell {
    
    @IBOutlet weak var profilePic: UIImageView!
    @IBOutlet weak var name: UILabel!
    @IBOutlet weak var username: UILabel!
    @IBOutlet weak var date: UILabel!
    @IBOutlet weak var log: UILabel!
    @IBOutlet weak var cityImage: UIImageView!
    @IBOutlet weak var cityName: UILabel!
    @IBOutlet weak var tapToViewCity: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    func displayContent(profilePic: UIImage, name: String, username: String, date: String, log: String, cityImage: UIImage, cityName: String, tapToViewCity: String) {
        
        self.profilePic.image = profilePic
        self.name.text = name
        self.username.text = username
        self.date.text = date
        self.log.text = log
        self.cityImage.image = cityImage
        self.cityName.text = cityName
        self.tapToViewCity.text = tapToViewCity
        
        self.backgroundColor = UIColor(red: 209/255, green: 209/255, blue: 209/255, alpha: 0.8)
        
    }
    
    
}
