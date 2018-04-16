//
//  Created by Celik Koseoglu
//

import UIKit

class PhotoDescriptionCell: UICollectionViewCell {
    
    @IBOutlet weak var venueImage: UIImageView!
    @IBOutlet weak var venueName: UILabel!
    @IBOutlet weak var venueReviewCount: UILabel!
    @IBOutlet weak var venueType: UILabel!
    @IBOutlet weak var venueAddress: UILabel!
    @IBOutlet weak var venuePhoneNumber: UILabel!
    
    @IBOutlet weak var landmarkStarImage1: UIImageView!
    @IBOutlet weak var landmarkStarImage2: UIImageView!
    @IBOutlet weak var landmarkStarImage3: UIImageView!
    @IBOutlet weak var landmarkStarImage4: UIImageView!
    @IBOutlet weak var landmarkStarImage5: UIImageView!
    
    @IBOutlet weak var slideShow: ImageSlideshow!
    
    func displayContent(venueImage: UIImage, venueName: String, venueReviewCount: String, venueType: String, venueAddress: String, venuePhoneNumber: String, star: Int) {
        //venueImage.image
        
        self.venueName.text = venueName
        self.venueReviewCount.text = venueReviewCount
        self.venueType.text = venueType
        self.venueAddress.text = venueAddress
        self.venuePhoneNumber.text = venuePhoneNumber
        
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
