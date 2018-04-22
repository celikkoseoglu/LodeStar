//
//  LogCell.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 13.01.2018.
//  Copyright © 2018 Berk Abbasoglu. All rights reserved.
//

import Foundation
import UIKit

class NearMeSmallCell: UICollectionViewCell {
    
    @IBOutlet weak var cellImage: UIImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var typeImage: UIImageView!
    @IBOutlet weak var typeLabel: UILabel!
    
    @IBOutlet weak var nearMeStarImage1: UIImageView!
    @IBOutlet weak var nearMeStarImage2: UIImageView!
    @IBOutlet weak var nearMeStarImage3: UIImageView!
    @IBOutlet weak var nearMeStarImage4: UIImageView!
    @IBOutlet weak var nearMeStarImage5: UIImageView!
    
    func displayContent(nearMeImage: UIImage, name: String, type: String, star: Int) {
        
        cellImage.image = nearMeImage
        nameLabel.text = name
        //typeImage.image = nearMeType
        typeLabel.text = type
        
        if star >= 5 {
            nearMeStarImage1.image = UIImage(named: "starFull.pdf")
            nearMeStarImage2.image = UIImage(named: "starFull.pdf")
            nearMeStarImage3.image = UIImage(named: "starFull.pdf")
            nearMeStarImage4.image = UIImage(named: "starFull.pdf")
            nearMeStarImage5.image = UIImage(named: "starFull.pdf")
            
        }
            
        else if star == 4 {
            nearMeStarImage1.image = UIImage(named: "starFull.pdf")
            nearMeStarImage2.image = UIImage(named: "starFull.pdf")
            nearMeStarImage3.image = UIImage(named: "starFull.pdf")
            nearMeStarImage4.image = UIImage(named: "starFull.pdf")
            nearMeStarImage5.image = UIImage(named: "star.pdf")
            
        }
            
        else if star == 3 {
            nearMeStarImage1.image = UIImage(named: "starFull.pdf")
            nearMeStarImage2.image = UIImage(named: "starFull.pdf")
            nearMeStarImage3.image = UIImage(named: "starFull.pdf")
            nearMeStarImage4.image = UIImage(named: "star.pdf")
            nearMeStarImage5.image = UIImage(named: "star.pdf")
            
        }
            
        else if star == 2 {
            nearMeStarImage1.image = UIImage(named: "starFull.pdf")
            nearMeStarImage2.image = UIImage(named: "starFull.pdf")
            nearMeStarImage3.image = UIImage(named: "star.pdf")
            nearMeStarImage4.image = UIImage(named: "star.pdf")
            nearMeStarImage5.image = UIImage(named: "star.pdf")
            
        }
            
        else {
            nearMeStarImage1.image = UIImage(named: "starFull.pdf")
            nearMeStarImage2.image = UIImage(named: "star.pdf")
            nearMeStarImage3.image = UIImage(named: "star.pdf")
            nearMeStarImage4.image = UIImage(named: "star.pdf")
            nearMeStarImage5.image = UIImage(named: "star.pdf")
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
