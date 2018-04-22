import UIKit
import GVRKit
import Alamofire
//import AVKit

fileprivate var cellCount = 1
fileprivate let itemsPerRow: CGFloat = 1
fileprivate var reuseIdentifier = "venueVRCell"
fileprivate var reuseIdentifiers = ["photoDescriptionCell", "venueVRCell", "venueCommentCell", "venueCommentCell", "venueCommentCell", "venueCommentCell", "venueCommentCell"]
fileprivate let sectionInsets = UIEdgeInsets(top: 0.0, left: 6.0, bottom: 3.0, right: 6.0)

fileprivate var venueData = Array(repeating: Data.init(), count: 5)
fileprivate var venueString = Array(repeating: AlamofireSource(urlString: "sindhu_beach.jpg")!, count: 5)

fileprivate var allcommentNamesAndDates = Array(repeating: "", count: 5)
fileprivate var allcomments = Array(repeating: "", count: 5)

// var for filling information passed from landmarks VC
var venuePlaceID = ""
var venueName = ""
var venueRating = -1.0
var venueType = ""
var venueAddress = ""
var venuePhone = ""

extension VenueViewController {
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 6
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let index = indexPath as NSIndexPath
        
        if (index.row == 0) {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifiers[index.row], for: indexPath) as! PhotoDescriptionCell
            
            cell.slideShow.backgroundColor = UIColor.white
            cell.slideShow.slideshowInterval = 5.0
            cell.slideShow.pageControlPosition = PageControlPosition.underScrollView
            cell.slideShow.pageControl.currentPageIndicatorTintColor = UIColor.lightGray
            cell.slideShow.pageControl.pageIndicatorTintColor = UIColor.black
            cell.slideShow.contentScaleMode = UIViewContentMode.scaleAspectFill
            
            // related to trip view controller
            cell.slideShow.travelViewSet = false
            
            if venueString[index.row] != nil {
            
                // can be used with other sample sources as `afNetworkingSource`, `alamofireSource` or `sdWebImageSource` or `kingfisherSource`
                cell.slideShow.setImageInputs(venueString)
                
                let recognizer = UITapGestureRecognizer(target: self, action: #selector(VenueViewController.didTap))
                cell.slideShow.addGestureRecognizer(recognizer)

                
                let vrImage = UIImage(named: "sindhu_beach.jpg")
                cell.displayContent(venueImage: vrImage!, venueName: venueName, venueReviewCount: "", venueType: venueType, venueAddress: venueAddress, venuePhoneNumber: venuePhone, star: Int(venueRating) + 1)
            }
            return cell
        }
            else if (index.row == 1){
            
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifiers[index.row], for: indexPath) as! VenueVRCell
            
            let vrImage = UIImage(named: "sindhu_beach.jpg")
            cell.displayContent(landmarkPic: vrImage!)
            
            return cell
            
        }
            else if (index.row > 1 && index.row < 7) {
                let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifiers[index.row], for: indexPath) as! VenueCommentCell
            
                //background shadow for collectionView elements
                cell.layer.shadowColor = UIColor.black.cgColor
                cell.layer.shadowOffset = CGSize(width: 5, height: 5)
                cell.layer.shadowRadius = 5;
                cell.layer.shadowOpacity = 0.25;
                cell.clipsToBounds = false
                cell.layer.masksToBounds = false
            
                cell.displayContent(nameAndDate: allcommentNamesAndDates[index.row - 1], comment: allcomments[index.row - 1])
            
                return cell
        }
        else {
            return UICollectionViewCell.init() //return empty cell if no index is found
        }
        
        return UICollectionViewCell.init() //return empty cell if no index is found
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
        else if (index.row > 0 && index.row < 6) {
            return CGSize(width: widthPerItem, height: 269)
        }
        else if (index.row > 5) {
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
        
        
        jsonparseVenue(placeID: venuePlaceID)
        
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
    
    func jsonparseVenue(placeID: String) {
        
        var requestTemplate = "http://lodestarapp.com:3010/placeDetails?placeid=#placeID"
        
        requestTemplate = requestTemplate.replacingOccurrences(of: "#placeID", with: placeID)
        
        Alamofire.request(requestTemplate).responseJSON { response in
            if let json = response.result.value {
                
                // first get the photos
                let jsonInfoVenues = json as! NSDictionary
                
                let jsonPhotoArr = jsonInfoVenues["photos"] as? NSArray
                let photoRequestTemplate = "https://maps.googleapis.com/maps/api/place/photo?photoreference=#ref&key=#key&maxheight=#maxheight"
                let maxheight = 200
                
                for i in 0..<5 {
                
                    let photorefDict = jsonPhotoArr![i] as! NSDictionary
                    let photoref = (photorefDict["photo_reference"] as? String)!
                    var photorefURL = photoRequestTemplate.replacingOccurrences(of: "#ref", with: photoref)
                    photorefURL = photorefURL.replacingOccurrences(of: "#key", with: key)
                    photorefURL = photorefURL.replacingOccurrences(of: "#maxheight", with: String(maxheight))
                    print("photo ref: " + photorefURL)
                    
                    //let photorefImageString = ImageSource(imageString: photorefURL)
                    
                    let photoURL = URL(string: photorefURL)
                    let data = try? Data(contentsOf: photoURL!)
                    
                    venueData[i] = data!
                    
                    let photorefImageString = AlamofireSource(urlString: photorefURL)
                    venueString[i] = photorefImageString!
                
                }
                
                // photos done
                // now get comments
                
                let jsonCommentArr = jsonInfoVenues["reviews"] as? NSArray
                
                for i in 0..<5 {
                    
                    let commentDict = jsonCommentArr![i] as! NSDictionary
                    let name = commentDict["author_name"] as? String
                    let dateRelative = commentDict["relative_time_description"] as? String
                    let nameAndDate = name! + " - " + dateRelative!
                    let comment = commentDict["text"] as? String
                    
                    allcommentNamesAndDates[i] = nameAndDate
                    allcomments[i] = comment!
                    
                }
                
                
                self.collectionView.reloadData()
                
                //  self.collectionView.reloadData()
            }
        }
    }
    
    @IBAction func backButtonTap(_ sender: UIButton) {
        dismiss(animated: true, completion: nil)
    }
    
    
    @objc func didTap() {
       // let fullScreenController = slideShow.presentFullScreenController(from: self)
        // set the activity indicator for full screen controller (skipping the line will show no activity indicator)
        //fullScreenController.slideshow.activityIndicator = DefaultActivityIndicator(style: .white, color: nil)
    }
}

