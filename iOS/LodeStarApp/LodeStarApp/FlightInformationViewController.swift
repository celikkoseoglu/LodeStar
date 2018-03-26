//
//  FlightInformationViewController.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 31.01.2018.
//  Copyright © 2018 Berk Abbasoglu. All rights reserved.
//

import Foundation
import Alamofire

import UIKit

fileprivate let itemsPerRow: CGFloat = 1
fileprivate let sectionInsets = UIEdgeInsets(top: 0.0, left: 6.0, bottom: 3.0, right: 6.0)
fileprivate var reuseIdentifiers = ["FlightInfoCell", "WeatherInfoCell", "TechnicalDetailsCell"]

// MARK: - UICollectionViewDataSource
extension FlightViewController {
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    // TODO this should return the number of available services in the airport
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 3
    }
    
    //3
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let index = indexPath as NSIndexPath
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifiers[index.row], for: indexPath) as! CollectionCell
        
        //background shadow for collectionView elements
        cell.layer.shadowColor = UIColor.black.cgColor
        cell.layer.shadowOffset = CGSize(width: 5, height: 5)
        cell.layer.shadowRadius = 5;
        cell.layer.shadowOpacity = 0.25;
        cell.clipsToBounds = false
        cell.layer.masksToBounds = false
        
        let text = reuseIdentifiers[index.row]
        
        return cell
        
    }
}

extension FlightViewController : UICollectionViewDelegateFlowLayout {
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


class FlightViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {
    
    @IBOutlet weak var collectionView: UICollectionView!
    //@IBOutlet weak var tripLabel: UILabel!

    @IBAction func backButton(_ sender: UIButton) {
        dismiss(animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        collectionView.dataSource = self
        collectionView.delegate = self
        
        self.collectionView.isScrollEnabled = true
        
        jsonparse(originAirportCode: "IST", destinationAirportCode: "PVG", flightNumber: "THY26")
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func flightInformationTapAction(_ sender: UITapGestureRecognizer) {
        
        let storyboard = self.storyboard
        let controller = storyboard?.instantiateViewController(withIdentifier: "flightInformationNAV")
        self.present(controller!, animated: true, completion: nil)
        
    }
    
    
    func jsonparse(originAirportCode: String, destinationAirportCode: String, flightNumber: String) {
        
        var requestTemplate = "http://lodestarapp.com:3006/?originAirportCode=#originAirportCode&destinationAirportCode=#destinationAirportCode&flightNumber=#flightNumber"
        
        requestTemplate = requestTemplate.replacingOccurrences(of: "#originAirportCode", with: originAirportCode)
        requestTemplate = requestTemplate.replacingOccurrences(of: "#destinationAirportCode", with: destinationAirportCode)
        requestTemplate = requestTemplate.replacingOccurrences(of: "#flightNumber", with: flightNumber)
        
        Alamofire.request(requestTemplate).responseJSON { response in
            //print("Request: \(String(describing: response.request))")   // original url request
            //print("Response: \(String(describing: response.response))") // http url response
            print("Result: \(response.result)")                         // response serialization result
            
            if let json = response.result.value {
                print("JSON: \(json)") // serialized json response
                //let JSON = response.result.value as? NSDictionary
                //let id = JSON?["aircrafttype"] as! String
                //print("Aircraft Type: \(id)")
            }
            
            /*if let data = response.data, let utf8Text = String(data: data, encoding: .utf8) {
             print("Data: \(utf8Text)") // original server data as UTF8 string
             }*/
        }
    }
    
}
