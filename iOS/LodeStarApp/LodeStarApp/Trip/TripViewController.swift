//
//  ViewController.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 17.10.2017.
//  Copyright © 2017 Berk Abbasoglu. All rights reserved.
//

import UIKit
import Alamofire

fileprivate let itemsPerRow: CGFloat = 3
fileprivate let sectionInsets = UIEdgeInsets(top: 0.0, left: 6.0, bottom: 3.0, right: 6.0)
fileprivate let reuseIdentifier = "tripCell"
fileprivate var availableServices = ["Transport Options", "Weather", "Flight Information", "Shopping", "Lounge", "Restaurants", "Living Expenses", "Near Me", "Accomodation", "Landmarks", "Get a SIM Card", "N/A"]
fileprivate var availableServiceImages = ["transport", "weather", "flight", "shopping", "lounge", "restaurants", "livingExpenses", "placesToSee", "accomodation", "landmarks", "getASIMCard", "na"]

// MARK: - UICollectionViewDataSource
extension TravelViewController {
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    // TODO this should return the number of available services in the airport
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return availableServices.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier, for: indexPath) as! TripCell
        
        //background shadow for collectionView elements
        cell.layer.shadowColor = UIColor.black.cgColor
        cell.layer.shadowOffset = CGSize(width: 5, height: 5)
        cell.layer.shadowRadius = 5;
        cell.layer.shadowOpacity = 0.25;
        cell.clipsToBounds = false
        cell.layer.masksToBounds = false
        
        let index = indexPath as NSIndexPath
        
        let image = UIImage(named: availableServiceImages[index.row])
        let text = availableServices[index.row]
        cell.cellImage.image = image
        cell.title.text = text
        
        if text == "Transport Options" {
            
            let transportTap = UITapGestureRecognizer(target: self, action: #selector(TravelViewController.transportTapAction(_:)))
            cell.cellImage.isUserInteractionEnabled = true
            cell.cellImage.addGestureRecognizer(transportTap)
            
        }
            
        else if text == "Weather" {
            
            let weatherTap = UITapGestureRecognizer(target: self, action: #selector(TravelViewController.weatherTapAction(_:)))
            cell.cellImage.isUserInteractionEnabled = true
            cell.cellImage.addGestureRecognizer(weatherTap)
            
        }
            
        else if text == "Flight Information" {
            
            let flightInformationTap = UITapGestureRecognizer(target: self, action: #selector(TravelViewController.flightInformationTapAction(_:)))
            cell.cellImage.isUserInteractionEnabled = true
            cell.cellImage.addGestureRecognizer(flightInformationTap)
            
        }
        
        else if text == "Landmarks" {
            
            let landmarksTap = UITapGestureRecognizer(target: self, action: #selector(TravelViewController.landmarksTapAction(_:)))
            cell.cellImage.isUserInteractionEnabled = true
            cell.cellImage.addGestureRecognizer(landmarksTap)
            
        }
        
        else if text == "Near Me" {
            
            let nearMeTap = UITapGestureRecognizer(target: self, action: #selector(TravelViewController.nearMeTapAction(_:)))
            cell.cellImage.isUserInteractionEnabled = true
            cell.cellImage.addGestureRecognizer(nearMeTap)
            
        }
        
        cell.displayContent(title: text, cellImage: image!)
        
        return cell
        
    }
}

extension TravelViewController : UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let paddingSpace = sectionInsets.left * (itemsPerRow + 1)
        let availableWidth = view.frame.width - paddingSpace
        let widthPerItem = availableWidth / itemsPerRow
        
        return CGSize(width: widthPerItem, height: widthPerItem)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return sectionInsets
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return sectionInsets.left
    }
}

class TravelViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {
    
    // variables
    @IBOutlet weak var tripLabel: UILabel!
    @IBOutlet weak var boardingInfoLabel: UILabel!
    @IBOutlet weak var destinationCityNameLabel: UILabel!
    @IBOutlet weak var destinationCityLabel: UILabel!
    @IBOutlet weak var departureCityLabel: UILabel!
    
    @IBOutlet weak var collectionView: UICollectionView!
    
    @IBOutlet weak var slideShow: ImageSlideshow!
    //var imageString = [AlamofireSource(urlString: "https://lonelyplanetwp.imgix.net/2017/03/Shanghai_for_free-abe6e2eb510b.jpg?crop=entropy&fit=crop&h=421&sharp=10&vib=20&w=748")!, AlamofireSource(urlString: "https://lonelyplanetwp.imgix.net/2017/09/GettyRF_508077687-0bda14a9c9a7-2.jpg?crop=entropy&fit=crop&h=421&sharp=10&vib=20&w=748")!]
    
    var destination = "Shanghai"
    var departure = "Istanbul"
    
    var direction = true
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        collectionView.dataSource = self
        collectionView.delegate = self
        
        self.collectionView.isScrollEnabled = true
        
        // init
        boardingInfoLabel.text = "You will be boarding PJ15396 to"
        destinationCityNameLabel.text = destination
        destinationCityLabel.text = "Showing information about " + departure
        departureCityLabel.text = "Swipe left to see information about " + destination
        
        slideShow.backgroundColor = UIColor.white
        //slideShow.slideshowInterval = 5.0
        slideShow.pageControlPosition = PageControlPosition.insideScrollView
        slideShow.pageControl.currentPageIndicatorTintColor = UIColor.lightGray
        slideShow.pageControl.pageIndicatorTintColor = UIColor.black
        slideShow.contentScaleMode = UIViewContentMode.scaleAspectFill
        //slideShow.setImageInputs(imageString)
        
        slideShow.pauseTimer()
        
        // additions
        slideShow.travelView = self
        slideShow.travelViewSet = true
        slideShow.circular = false
        
        var cityImages = [ImageSource]()
        
        let image1URL = "https://lonelyplanetwp.imgix.net/2017/03/Shanghai_for_free-abe6e2eb510b.jpg?crop=entropy&fit=crop&h=421&sharp=10&vib=20&w=748"
        let image2URL = "https://lonelyplanetwp.imgix.net/2017/09/GettyRF_508077687-0bda14a9c9a7-2.jpg?crop=entropy&fit=crop&h=421&sharp=10&vib=20&w=748"
        
        //huge bug: image order might get mixed yp
        
        Alamofire.request(image1URL).responseImage { response in
            if let image = response.result.value {
                var modifiedImage = self.applyDarkenFilter(image: image)
                modifiedImage = self.applyBlurFilter(image: modifiedImage)
                cityImages.append(ImageSource(image: modifiedImage)) //image is of type UIImage
                self.slideShow.setImageInputs(cityImages)
            }
        }
        
        Alamofire.request(image2URL).responseImage { response in
            if let image = response.result.value {
                var modifiedImage = self.applyDarkenFilter(image: image)
                modifiedImage = self.applyBlurFilter(image: modifiedImage)
                cityImages.append(ImageSource(image: modifiedImage)) //image is of type UIImage
                self.slideShow.setImageInputs(cityImages)
            }
        }
    }
    
    func applyDarkenFilter(image: UIImage) -> UIImage {
        let context = CIContext(options: nil)
        
        if let currentFilter = CIFilter(name: "CIExposureAdjust") {
            let beginImage = CIImage(image: image)
            currentFilter.setValue(beginImage, forKey: kCIInputImageKey)
            currentFilter.setValue(-3.0, forKey: kCIInputEVKey)
            //currentFilter.setValue(10.0, forKey: kCIInputRadiusKey)
            
            if let output = currentFilter.outputImage {
                if let cgimg = context.createCGImage(output, from: output.extent) {
                    return UIImage(cgImage: cgimg)
                }
            }
        }
        return image //return the image untouched
    }
    
    func applyBlurFilter(image: UIImage) -> UIImage {
        let context = CIContext(options: nil)
        
        if let currentFilter = CIFilter(name: "CIBoxBlur") {
            let beginImage = CIImage(image: image)
            currentFilter.setValue(beginImage, forKey: kCIInputImageKey)
            currentFilter.setValue(2.0, forKey: kCIInputRadiusKey)
            
            if let output = currentFilter.outputImage {
                if let cgimg = context.createCGImage(output, from: output.extent) {
                    return UIImage(cgImage: cgimg)
                }
            }
        }
        return image //return the image untouched
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    open func handleSwipe() {
        
        if (direction == true) {
        
            destinationCityNameLabel.text = departure
            destinationCityLabel.text = "Showing information about " + departure
            departureCityLabel.text = "Swipe to see information about " + destination
            boardingInfoLabel.text = "You will be boarding PJ15396 from"
            
            direction = false
        }
        
        else {
            
            destinationCityNameLabel.text = destination
            destinationCityLabel.text = "Showing information about " + destination
            departureCityLabel.text = "Swipe to see information about " + departure
            boardingInfoLabel.text = "You will be boarding PJ15396 to"
            
            direction = true
            
        }
        
    }
    
    // MARK: Tap Functions
    @IBAction func transportTapAction(_ sender: UITapGestureRecognizer) {
        
        let storyboard = self.storyboard
        let controller = storyboard?.instantiateViewController(withIdentifier: "transportNAV")
        self.present(controller!, animated: true, completion: nil)
        
    }
    
    @IBAction func logoutButtonAction(_ sender: UIButton) {
        dismiss(animated: true, completion: nil)
    }
    
    
    @IBAction func weatherTapAction(_ sender: UITapGestureRecognizer) {
        
        let storyboard = self.storyboard
        let controller = storyboard?.instantiateViewController(withIdentifier: "weatherNAV")
        self.present(controller!, animated: true, completion: nil)
        
    }
    
    @IBAction func flightInformationTapAction(_ sender: UITapGestureRecognizer) {
        
        let storyboard = self.storyboard
        let controller = storyboard?.instantiateViewController(withIdentifier: "flightInformationNAV")
        self.present(controller!, animated: true, completion: nil)
        
    }
    
    @IBAction func landmarksTapAction(_ sender: UITapGestureRecognizer) {
        
        let storyboard = self.storyboard
        let controller = storyboard?.instantiateViewController(withIdentifier: "landmarksNAV")
        self.present(controller!, animated: true, completion: nil)
        
    }
    
    @IBAction func nearMeTapAction(_ sender: UITapGestureRecognizer) {
        
        let storyboard = self.storyboard
        let controller = storyboard?.instantiateViewController(withIdentifier: "nearMeNAV")
        self.present(controller!, animated: true, completion: nil)
        
    }
}

