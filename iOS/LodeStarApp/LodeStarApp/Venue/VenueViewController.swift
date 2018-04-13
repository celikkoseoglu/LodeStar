import UIKit
import GVRKit
//import AVKit

fileprivate var cellCount = 1
fileprivate let itemsPerRow: CGFloat = 1
fileprivate var reuseIdentifier = "venueVRCell"
fileprivate var reuseIdentifiers = ["photoDescriptionCell", "venueVRCell"]
fileprivate let sectionInsets = UIEdgeInsets(top: 0.0, left: 6.0, bottom: 3.0, right: 6.0)

extension VenueViewController {
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 2
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let index = indexPath as NSIndexPath
        
        if (index.item == 0) {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifiers[index.item], for: indexPath) as! PhotoDescriptionCell
            let vrImage = UIImage(named: "sindhu_beach.jpg")
            cell.displayContent(venueImage: vrImage!, venueName: "asd", venueReviewCount: "dsa", venueType: "asd", venueAddress: "asd", venuePhoneNumber: "asd")
            return cell
        }
        else if (index.item == 1) {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifiers[index.item], for: indexPath) as! VenueVRCell
            let vrImage = UIImage(named: "sindhu_beach.jpg")
            cell.displayContent(landmarkPic: vrImage!)
            return cell
        }
        else {
            return UICollectionViewCell.init() //return empty cell if no index is found
        }
    }
}

extension VenueViewController : UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        let index = indexPath as NSIndexPath
        
        let paddingSpace = sectionInsets.left * (itemsPerRow + 1)
        let availableWidth = view.frame.width - paddingSpace
        let widthPerItem = availableWidth / itemsPerRow
        
        if (index.row == 0) {
            return CGSize(width: widthPerItem, height: 490)
        }
        else if (index.row == 1) {
            return CGSize(width: widthPerItem, height: 230)
        }
        else if (index.row == 2) {
            return CGSize(width: widthPerItem, height: 170)
        }
        
        return CGSize(width: widthPerItem, height: 300)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return sectionInsets
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return sectionInsets.left
    }
}

class VenueViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource/*, GVRRendererViewControllerDelegate*/ {
    /*func renderer(for displayMode: GVRDisplayMode) -> GVRRenderer! {
        
        let vodafoneImage = UIImage.init(named: "underwater.jpg")
        let renderer = GVRImageRenderer.init(image: vodafoneImage)
        renderer?.setSphericalMeshOfRadius(50, latitudes: 12, longitudes: 24, verticalFov: 180, horizontalFov: 360, meshType: GVRMeshType.stereoTopBottom)
        let sceneRenderer = GVRSceneRenderer.init()
        sceneRenderer.renderList.add(renderer)
        
        return sceneRenderer
    }*/ //commented parts will only be necessary if we decide to switch to GVRKit down the road. (because we might need OpenGL rendering functionality)
    
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var imageLabel: UILabel!
    //@IBOutlet weak var scrollView: UIScrollView! //there has to be mock parent view to insert GVRKit GVRPanoramaView in. Add it to the storyboard when the inevitable end comes.
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.isScrollEnabled = true
        
        /*var viewController = GVRRendererViewController.init()
        viewController.delegate = self
        var panoView = GVRRendererView.init(renderer: viewController.rendererView.renderer)
        scrollView.addSubview(panoView!)
        self.addChildViewController(viewController)
        panoView?.frame = CGRect(x: 16,y: 16,width: 200, height: 200)*/ //preserve this code as a reference for GVRKit and the difficulty involved...
        
        
    }
}

