//
//  LogCell.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 13.01.2018.
//  Copyright © 2018 Berk Abbasoglu. All rights reserved.
//

import Foundation
import UIKit

class LandmarkCell: UICollectionViewCell {
    
    @IBOutlet weak var landmarkImage: UIImageView!
    @IBOutlet weak var landmarkNameLabel: UILabel!
    @IBOutlet weak var landmarkTypeTextLabel: UILabel!
    @IBOutlet weak var landmarkLocationTextLabel: UILabel!
    
    @IBOutlet weak var landmarkTypeImage: UIImageView!
    @IBOutlet weak var landmarkLocationImage: UIImageView!
    @IBOutlet weak var landmarkStarImage1: UIImageView!
    @IBOutlet weak var landmarkStarImage2: UIImageView!
    @IBOutlet weak var landmarkStarImage3: UIImageView!
    @IBOutlet weak var landmarkStarImage4: UIImageView!
    @IBOutlet weak var landmarkStarImage5: UIImageView!
    
    var landmarkPlaceID = ""
    
    func displayContent(landmarkPic: UIImage, name: String, landmarkType: UIImage, type: String, landmarkLocation: UIImage, location: String, star: Int, ID: String) {
        
        landmarkImage.image = landmarkPic
        landmarkNameLabel.text = name
        landmarkTypeImage.image = landmarkType
        landmarkTypeTextLabel.text = type
        landmarkLocationImage.image = landmarkLocation
        landmarkLocationTextLabel.text = location
        
        landmarkPlaceID = ID
        
        if star >= 5 {
            landmarkStarImage1.image = UIImage(named: "starFull.pdf")
            landmarkStarImage2.image = UIImage(named: "starFull.pdf")
            landmarkStarImage3.image = UIImage(named: "starFull.pdf")
            landmarkStarImage4.image = UIImage(named: "starFull.pdf")
            landmarkStarImage5.image = UIImage(named: "starFull.pdf")
            
        }
        
        else if star == 4 {
            landmarkStarImage1.image = UIImage(named: "starFull.pdf")
            landmarkStarImage2.image = UIImage(named: "starFull.pdf")
            landmarkStarImage3.image = UIImage(named: "starFull.pdf")
            landmarkStarImage4.image = UIImage(named: "starFull.pdf")
            landmarkStarImage5.image = UIImage(named: "star.pdf")
            
        }
        
        else if star == 3 {
            landmarkStarImage1.image = UIImage(named: "starFull.pdf")
            landmarkStarImage2.image = UIImage(named: "starFull.pdf")
            landmarkStarImage3.image = UIImage(named: "starFull.pdf")
            landmarkStarImage4.image = UIImage(named: "star.pdf")
            landmarkStarImage5.image = UIImage(named: "star.pdf")
            
        }
        
        else if star == 2 {
            landmarkStarImage1.image = UIImage(named: "starFull.pdf")
            landmarkStarImage2.image = UIImage(named: "starFull.pdf")
            landmarkStarImage3.image = UIImage(named: "star.pdf")
            landmarkStarImage4.image = UIImage(named: "star.pdf")
            landmarkStarImage5.image = UIImage(named: "star.pdf")
            
        }
        
        else {
            landmarkStarImage1.image = UIImage(named: "starFull.pdf")
            landmarkStarImage2.image = UIImage(named: "star.pdf")
            landmarkStarImage3.image = UIImage(named: "star.pdf")
            landmarkStarImage4.image = UIImage(named: "star.pdf")
            landmarkStarImage5.image = UIImage(named: "star.pdf")
        }
        
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        //background shadow for collectionView elements
        self.layer.shadowColor = UIColor.black.cgColor
        self.layer.shadowOffset = CGSize(width: 5, height: 5)
        self.layer.shadowRadius = 5;
        self.layer.shadowOpacity = 0.25;
        self.clipsToBounds = false
        self.layer.masksToBounds = false
    }
    
    
    
}
