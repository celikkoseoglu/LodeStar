//
//  LoginViewController.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 17.01.2018.
//  Copyright © 2018 Berk Abbasoglu. All rights reserved.
//

import Foundation
import Alamofire
import MapKit

import UIKit

fileprivate let itemsPerRow: CGFloat = 1
fileprivate let sectionInsets = UIEdgeInsets(top: 6.0, left: 6.0, bottom: 6.0, right: 6.0)
fileprivate let cellCount = 2
fileprivate let reuseIdentifierMain = "nearMeMainCell"
fileprivate let reuseIdentifierSmall = "nearMeSmallCell"

fileprivate var types = ["Restaurants", "Parks"]

var nearMeNamesRestaurants = Array(repeating: "", count: 5)
var nearMeTypesRestaurants = Array(repeating: "", count: 5)
var nearMeDataRestaurants = Array(repeating: Data.init(), count: 5)
var nearMeRatingsRestaurants = Array(repeating: -1, count: 5)

var nearMeNamesParks = Array(repeating: "", count: 5)
var nearMeTypesParks = Array(repeating: "", count: 5)
var nearMeDataParks = Array(repeating: Data.init(), count: 5)
var nearMeRatingsParks = Array(repeating: -1, count: 5)

var nearMeArrElement:NSDictionary = ["": [""]]
var nearMeArrRestaurants = Array(repeating: nearMeArrElement, count: 10)
var nearMeArrParks = Array(repeating: nearMeArrElement, count: 10)

extension NearMeViewController {
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return cellCount
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifierMain, for: indexPath) as! NearMeMainCell
        
        let index = indexPath as NSIndexPath
        
        if types[index.row] != "" {
            
            if (index.row == 0) {
                cell.type = "restaurant"
            }
            
            else if (index.row == 1) {
                cell.type = "park"
            }
            
            cell.displayContent(type: types[index.row])
            cell.collectionView.reloadData()
        }
        
        return cell
        
    }
}

extension NearMeViewController {
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        let paddingSpace = sectionInsets.left * (itemsPerRow + 1)
        let availableWidth = view.frame.width - paddingSpace
        let widthPerItem = availableWidth / itemsPerRow
        let heightperItem = 247 as CGFloat
        
        return CGSize(width: widthPerItem, height: heightperItem )
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return sectionInsets
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return sectionInsets.top
    }
}

class NearMeViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    @IBOutlet weak var collectionViewMain: UICollectionView!
    //@IBOutlet weak var collectionViewSmall: UICollectionView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        jsonparseNearMe(location: "31.2339463%2C121.46388509999997", limit: 5, query: "restaurant")
        jsonparseNearMe(location: "31.2339463%2C121.46388509999997", limit: 5, query: "park")
        
        // Do any additional setup after loading the view,  from a nib.
        collectionViewMain.dataSource = self
        collectionViewMain.delegate = self
        
        //collectionViewSmall.dataSource = self
        //collectionViewSmall.delegate = self
    
        self.collectionViewMain.isScrollEnabled = true
        //self.collectionViewSmall.isScrollEnabled = true
        
        //self.navigationController?.navigationBar.isTranslucent = false
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func jsonparseNearMe(location: String, limit: Int, query: String) {
        
        var requestTemplate = "http://lodestarapp.com:3009/?location=#location&limit=#limit&query=#query"
        
        requestTemplate = requestTemplate.replacingOccurrences(of: "#location", with: location)
        requestTemplate = requestTemplate.replacingOccurrences(of: "#limit", with: String(limit))
        requestTemplate = requestTemplate.replacingOccurrences(of: "#query", with: query)
        
        Alamofire.request(requestTemplate).responseJSON { response in
            if let json = response.result.value {
                
                let jsonInfoNearMe = json as! NSDictionary
                let jsonGroupsNearMe = jsonInfoNearMe["groups"] as? NSArray
                let jsonItemsNearMe = jsonGroupsNearMe![0] as! NSDictionary
                let jsonItemsNearMeArr = jsonItemsNearMe["items"] as? NSArray
                
                if (query == "restaurant") {
                    
                    for i in 0..<5 {
                        nearMeArrRestaurants[i] = jsonItemsNearMeArr![i] as! NSDictionary
                    }
                    
                    for i in 0..<5 {
                        
                        let nearMeItem = nearMeArrRestaurants[i]
                        
                        let nearMeNameVenue = nearMeItem["venue"] as! NSDictionary
                        
                        let nearMeName = nearMeNameVenue["name"] as? String
                        nearMeNamesRestaurants[i] = nearMeName!
                        
                        let nearMeCategories = nearMeNameVenue["categories"] as? NSArray
                        let nearMeCategoriesDict = nearMeCategories![0] as! NSDictionary
                        let nearMeTypeName = nearMeCategoriesDict["name"] as? String
                        nearMeTypesRestaurants[i] = nearMeTypeName!
                        
                        let nearMeRating = nearMeNameVenue["rating"] as? Double
                        nearMeRatingsRestaurants[i] = Int(nearMeRating!)/2 // ratings come out of 10 here
                        
                        let nearMeNameVenueImage = nearMeItem["venueImage"] as? String
                        
                        let url = URL(string: nearMeNameVenueImage!)
                        let data = try? Data(contentsOf: url!)
                        
                        /* Alamofire.request(urlTemplate).responseJSON { response in
                         //print("Request: \(String(describing: response.request))")   // original url request
                         //print("Response: \(String(describing: response.response))") // http url response
                         //print("Result: \(response.result)")
                         
                         if let data = response.data, let utf8Text = String(data: data, encoding: .utf8) {
                         print("Data: \(utf8Text)") // original server data as UTF8 string
                         }
                         } */
                        
                        nearMeDataRestaurants[i] = data!
                        
                        self.collectionViewMain.reloadData()
                        
                    }
                }
                
                else if (query == "park") {
                    
                    for i in 0..<5 {
                        nearMeArrParks[i] = jsonItemsNearMeArr![i] as! NSDictionary
                    }
                    
                    for i in 0..<5 {
                        
                        let nearMeItem = nearMeArrParks[i]
                        
                        let nearMeNameVenue = nearMeItem["venue"] as! NSDictionary
                        
                        let nearMeName = nearMeNameVenue["name"] as? String
                        nearMeNamesParks[i] = nearMeName!
                        
                        let nearMeCategories = nearMeNameVenue["categories"] as? NSArray
                        let nearMeCategoriesDict = nearMeCategories![0] as! NSDictionary
                        let nearMeTypeName = nearMeCategoriesDict["name"] as? String
                        nearMeTypesParks[i] = nearMeTypeName!
                        
                        let nearMeRating = nearMeNameVenue["rating"] as? Double
                        nearMeRatingsParks[i] = Int(nearMeRating!)/2 // ratings come out of 10 here
                        
                        let nearMeNameVenueImage = nearMeItem["venueImage"] as? String
                        
                        let url = URL(string: nearMeNameVenueImage!)
                        let data = try? Data(contentsOf: url!)
                        
                        /* Alamofire.request(urlTemplate).responseJSON { response in
                         //print("Request: \(String(describing: response.request))")   // original url request
                         //print("Response: \(String(describing: response.response))") // http url response
                         //print("Result: \(response.result)")
                         
                         if let data = response.data, let utf8Text = String(data: data, encoding: .utf8) {
                         print("Data: \(utf8Text)") // original server data as UTF8 string
                         }
                         } */
                        
                        nearMeDataParks[i] = data!
                        
                        self.collectionViewMain.reloadData()
                    }
                    
                }
                //  self.collectionView.reloadData()
            }
        }
    }
    
    @IBAction func backButton(_ sender: UIButton) {
        dismiss(animated: true, completion: nil)
    }
}
