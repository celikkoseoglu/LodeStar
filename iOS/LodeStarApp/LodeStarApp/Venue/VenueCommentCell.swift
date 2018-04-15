//
//  Created by Celik Koseoglu
//

import UIKit

class VenueCommentCell: UICollectionViewCell {
    
    @IBOutlet weak var nameDateLabel: UILabel!
    @IBOutlet weak var commentTextField: UITextView!
    
    
    func displayContent(nameAndDate: String, comment: String) {
        
        nameDateLabel.text = nameAndDate
        commentTextField.text = comment
        
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
