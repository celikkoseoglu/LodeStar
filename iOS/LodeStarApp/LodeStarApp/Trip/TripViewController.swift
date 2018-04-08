//
//  ViewController.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 17.10.2017.
//  Copyright © 2017 Berk Abbasoglu. All rights reserved.
//

import UIKit

fileprivate let itemsPerRow: CGFloat = 3
fileprivate let sectionInsets = UIEdgeInsets(top: 0.0, left: 6.0, bottom: 3.0, right: 6.0)
fileprivate let reuseIdentifier = "tripCell"
fileprivate var availableServices = ["Transport Options", "Weather", "Flight Information", "Shopping", "Lounge", "Restaurants", "Living Expenses", "Places to See", "Accomodation", "Landmarks", "Get a SIM Card", "N/A"]
fileprivate var availableServiceImages = ["transport", "weather", "flight", "shopping", "lounge", "restaurants", "livingExpenses", "placesToSee", "accomodation", "landmarks", "getASIMCard", "na"]

protocol TravelViewControllerDelegate {
    func setDestinationInfo(_ name:String)
}

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

class TravelViewController: UIViewController, TravelViewControllerDelegate, UICollectionViewDelegate, UICollectionViewDataSource {
    
    // variables
    @IBOutlet weak var tripLabel: UILabel!
    @IBOutlet weak var boardingInfoLabel: UILabel!
    @IBOutlet weak var destinationCityNameLabel: UILabel!
    @IBOutlet weak var destinationCityLabel: UILabel!
    @IBOutlet weak var departureCityLabel: UILabel!
    
    @IBOutlet weak var destinationCityImage: UIImageView!
    @IBOutlet var tapImage: UITapGestureRecognizer!
    
    @IBOutlet weak var collectionView: UICollectionView!
    
    @IBOutlet weak var cityImage: UIImageView!
    
    var destination = "Tap to Set"
    var departure = "Tap to Set"
    
    var swipeRight = UISwipeGestureRecognizer()
    var swipeLeft = UISwipeGestureRecognizer()
    
    var direction = true
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        collectionView.dataSource = self
        collectionView.delegate = self
        
        // add gesture recognizer to image
        let imageTap = UITapGestureRecognizer(target: self, action: #selector(self.handleTap(_:)))
        destinationCityImage.addGestureRecognizer(imageTap)
        destinationCityImage.isUserInteractionEnabled = true
        
        // swipe gestures
        swipeRight = UISwipeGestureRecognizer(target: self, action: #selector(handleRightSwipe(_:)))
        swipeRight.direction = .right
        destinationCityImage.addGestureRecognizer(swipeRight)
        
        swipeLeft = UISwipeGestureRecognizer(target: self, action: #selector(handleLeftSwipe(_:)))
        swipeLeft.direction = .left
        
        self.collectionView.isScrollEnabled = true
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // function which is triggered when handleTap is called
    func handleTap(_ sender: UITapGestureRecognizer) {
        
        let popupSelectNation = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "selectNation") as! SelectNationViewController
        popupSelectNation.travelViewControllerDelegate = self
        self.addChildViewController(popupSelectNation)
        popupSelectNation.view.frame = self.view.frame
        self.view.addSubview(popupSelectNation.view)
        popupSelectNation.didMove(toParentViewController: self)
        
    }
    
    func handleRightSwipe(_ sender: UISwipeGestureRecognizer) {
        
        destinationCityNameLabel.text = departure
        destinationCityLabel.text = "Showing information about " + departure
        departureCityLabel.text = "Swipe left to see information about " + destination
        
        destinationCityImage.removeGestureRecognizer(swipeRight)
        destinationCityImage.addGestureRecognizer(swipeLeft)
        
        //destinationCityImage.image = UIImage(named: "istanbul.png")
        
        boardingInfoLabel.text = "You will be boarding PJ15396 from"
        
        cityImage.image = UIImage(named: "shanghai")
        
        var fadeAnim:CABasicAnimation = CABasicAnimation(keyPath: "contents")
        fadeAnim.fromValue = UIImage(named: "shanghai")
        fadeAnim.toValue   = UIImage(named: "uganda")
        fadeAnim.duration  = 0.8;
        
        direction = false
        
    }
    
    func handleLeftSwipe(_ sender: UISwipeGestureRecognizer) {
        
        destinationCityNameLabel.text = destination
        destinationCityLabel.text = "Showing information about " + destination
        departureCityLabel.text = "Swipe right to see information about " + departure
        
        destinationCityImage.removeGestureRecognizer(swipeLeft)
        destinationCityImage.addGestureRecognizer(swipeRight)
        
        boardingInfoLabel.text = "You will be boarding PJ15396 to"
        
        cityImage.image = UIImage(named: "uganda")
        
        var fadeAnim:CABasicAnimation = CABasicAnimation(keyPath: "contents")
        fadeAnim.fromValue = UIImage(named: "uganda")
        fadeAnim.toValue   = UIImage(named: "shanghai")
        fadeAnim.duration  = 0.8;
        
        cityImage.layer.add(fadeAnim, forKey: "contents")
        
        direction = true
        
    }
    
    func setDestinationInfo(_ name: String) {
        
        // don't allow further changes
        //destinationCityImage.isUserInteractionEnabled = false
        
        if direction == true {
            // update content of the labels etc for destination
            destination = name
            destinationCityNameLabel.text = destination
            destinationCityLabel.text = "Showing information about " + destination
            boardingInfoLabel.text = "You will be boarding PJ15396 to"
        }
            
        else {
            // update content of the labels etc for departure
            departure = name
            destinationCityNameLabel.text = departure
            destinationCityLabel.text = "Showing information about " + departure
            boardingInfoLabel.text = "You will be boarding PJ15396 from"
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
    
}

