//
//  Created by Celik Koseoglu
//

import UIKit

class VenueVRCell: UICollectionViewCell {
    
    @IBOutlet weak var imageVRView: GVRPanoramaView!
    enum Media {
        static var photoArray = ["vodafone_ataturk_airport.jpg", "sindhu_beach.jpg", "grand_canyon.jpg", "underwater.jpg"]
    }
    
    func displayContent(landmarkPic: UIImage) {
        
        //imageVRView.isHidden = true
        //imageVRView.delegate = self
        
        imageVRView.load(landmarkPic, of: GVRPanoramaImageType.mono)
        imageVRView.enableCardboardButton = true
        imageVRView.enableFullscreenButton = true
        imageVRView.enableTouchTracking = true
        imageVRView.enableInfoButton = false
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

extension VenueVRCell: GVRWidgetViewDelegate {
    func widgetView(_ widgetView: GVRWidgetView!, didLoadContent content: Any!) {
        if content is UIImage {
            imageVRView.isHidden = false
        }
    }
    
    func widgetView(_ widgetView: GVRWidgetView!, didFailToLoadContent content: Any!, withErrorMessage errorMessage: String!)  {
        print(errorMessage)
    }
    
    func widgetView(_ widgetView: GVRWidgetView!, didChange displayMode: GVRWidgetDisplayMode) {
    }
    
    func widgetViewDidTap(_ widgetView: GVRWidgetView!) {
        Media.photoArray.append(Media.photoArray.removeFirst())
        imageVRView?.load(UIImage(named: Media.photoArray.first!), of: GVRPanoramaImageType.mono)
    }
}
