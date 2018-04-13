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
    
    func displayContent(venueImage: UIImage, venueName: String, venueReviewCount: String, venueType: String, venueAddress: String, venuePhoneNumber: String) {
        //venueImage.image
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
