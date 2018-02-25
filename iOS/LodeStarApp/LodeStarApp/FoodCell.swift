//
//  LogCell.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 13.01.2018.
//  Copyright © 2018 Berk Abbasoglu. All rights reserved.
//

import Foundation
import UIKit

class FoodCell: UICollectionViewCell {
    
    @IBOutlet weak var foodName: UILabel!
    @IBOutlet weak var priceTarget: UILabel!
    @IBOutlet weak var priceHome: UILabel!
    @IBOutlet weak var priceInTargetCountry: UILabel!
    @IBOutlet weak var priceInYourCountry: UILabel!
    @IBOutlet weak var foodPic: UIImageView!
    
    override func awakeFromNib() {
        
        super.awakeFromNib()
        // Initialization code
    }
    
    func displayContent(foodPic: UIImage, foodName: String, priceTarget: Int, priceHome: Int, priceInYourCountry: String, priceInTargetCountry: String) {
        
        self.foodPic.image = foodPic
        self.foodName.text = foodName
        self.priceTarget.text = String(priceTarget)
        self.priceHome.text = String(priceHome)
        self.priceInYourCountry.text = String(priceInYourCountry)
        self.priceInTargetCountry.text = String(priceInTargetCountry)
        
        self.backgroundColor = UIColor(red: 209/255, green: 209/255, blue: 209/255, alpha: 0.8)
        
    }
    
    
}
