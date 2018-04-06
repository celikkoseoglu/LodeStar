//
//  Created by Berk Abbasoglu
//

import UIKit

class TripCell: UICollectionViewCell {
    
    @IBOutlet weak var cellImage: UIImageView!
    @IBOutlet weak var title: UILabel!
    
    func displayContent(title: String, cellImage: UIImage) {
        self.title.text = title
        self.cellImage.image = cellImage
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
