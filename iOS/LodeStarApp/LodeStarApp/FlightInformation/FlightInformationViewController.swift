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

var jsonInfoFlight:NSDictionary = ["": [""]]
var jsonInfoWeatherDay1:NSDictionary = ["": [""]]

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
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifiers[index.row], for: indexPath) as! FlightDetailsCell
        
        //remember, remember the 6 pixel margin between consecutive cards
        let cardMargin = 6
        
        if (index.row == 0) {
            if(jsonInfoFlight["destination"] != nil) {
                
                //dynamically adjust cell height. On a second thought we don't need this. Just specify the correct size before drawing them.
                //cell.frame = CGRect(x: cell.frame.origin.x, y: cell.frame.origin.y, width: cell.frame.size.width, height: CGFloat(firstCardHeight))
                
                let destinationInfo = jsonInfoFlight["destination"] as! NSDictionary
                let originInfo = jsonInfoFlight["origin"] as! NSDictionary
                let filedDepartureTime = jsonInfoFlight["filed_departure_time"] as! NSDictionary
                let filedArrivalTime = jsonInfoFlight["filed_arrival_time"] as! NSDictionary
                
                let destinationAirportName = destinationInfo["airport_name"]
                let destinationCityName = destinationInfo["city"]
                //let destinationCountryName = destinationInfo["country"]
                //let destinationGate = destinationInfo
                
                let originAirportName = originInfo["airport_name"]
                let originCityName = originInfo["city"]
                //let originCountryName = originInfo["country"]
                
                let arrivalHourLocal = filedArrivalTime["time"] as! String
                let departureHourLocal = filedDepartureTime["time"] as! String
                let arrivalDelay = jsonInfoFlight["arrival_delay"] as! Int
                let directDistance = jsonInfoFlight["distance_filed"] as! Int
                let speed = jsonInfoFlight["filed_airspeed_kts"] as! Int
                
                cell.originAirportNameLabel.text = destinationAirportName as? String
                cell.originCityNameLabel.text = originCityName as? String
                
                cell.arrivalAirportNameLabel.text = originAirportName as? String
                cell.arrivalCityNameLabel.text = destinationCityName as? String
                
                cell.departureTimeLocalLabel.text = departureHourLocal
                cell.arrivalTimeLocalLabel.text = arrivalHourLocal
                cell.averageDelayLabel.text = String((-1)*(arrivalDelay/60)) + " MIN"
                cell.plannedSpeedLabel.text = String(Int(Double(speed)*1.852)) + " KM/HR"
                cell.directDistanceLabel.text = String(directDistance) + " miles"
                
            }
        }
        
        if (index.row == 1) {
            if((jsonInfoWeatherDay1["weather"] != nil) && (jsonInfoFlight["destination"] != nil)) {
                
                let destinationInfo = jsonInfoFlight["destination"] as! NSDictionary
                let destinationCityName = destinationInfo["city"] as? String
                
                let weatherArr = jsonInfoWeatherDay1["weather"] as! NSArray
                let weatherDict = weatherArr[0] as! NSDictionary
                let weatherTempHumidity = jsonInfoWeatherDay1["main"] as! NSDictionary
                
                let weatherCondition = weatherDict["description"] as? String
                let temperature = weatherTempHumidity["temp"] as? Double
                let humidity = weatherTempHumidity["humidity"] as? Double
                
                cell.weatherConditionLabel.text = weatherCondition
                cell.whenYouArriveInLabel.text = "when you arrive in " + destinationCityName!
                cell.temperatureLabel.text = String(Int(temperature!)) + "°"
                cell.humidityLabel.text = String(Int(humidity!))
                
            }
        }
        
        if (index.row == 2) {
            // no altitude
            let aircraftType = jsonInfoFlight["aircrafttype"] as? String
            cell.aircraftTypeLabel.text = aircraftType
        }
        
        return cell
        
    }
}

extension FlightViewController : UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        let index = indexPath as NSIndexPath
        
        let paddingSpace = sectionInsets.left * (itemsPerRow + 1)
        let availableWidth = view.frame.width - paddingSpace
        let widthPerItem = availableWidth / itemsPerRow
        
        if (index.row == 0) {
            return CGSize(width: widthPerItem, height: 240)
        }
        else if (index.row == 1) {
            return CGSize(width: widthPerItem, height: 170)
        }
        else if (index.row == 2) {
            return CGSize(width: widthPerItem, height: 170)
        }
        //else, return the default calue
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
        
        jsonparse(originAirportCode: "IST", destinationAirportCode: "PVG", flightNumber: "THY26")
        jsonparseWeather(city: "Shangai", units: "metric")
        collectionView.reloadData()
        collectionView.dataSource = self
        collectionView.delegate = self
        
        self.collectionView.isScrollEnabled = true
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
            
            if let json = response.result.value {
                jsonInfoFlight = (json as? NSDictionary)!
                self.collectionView.reloadData()
            }
        }
    }
    
    func jsonparseWeather(city: String, units: String) {
        
        var requestTemplate = "http://lodestarapp.com:3005/?city=#city&units=#units"
        
        requestTemplate = requestTemplate.replacingOccurrences(of: "#city", with: city)
        requestTemplate = requestTemplate.replacingOccurrences(of: "#units", with: units)
        
        Alamofire.request(requestTemplate).responseJSON { response in
            //print("Request: \(String(describing: response.request))")   // original url request
            //print("Response: \(String(describing: response.response))") // http url response
            //print("Result: \(response.result)")
            
            if let json = response.result.value {
                //print("JSON: \(json)") // serialized json response
                
                let jsonInfoWeather = (json as? NSArray)!
                jsonInfoWeatherDay1 = jsonInfoWeather[0] as! NSDictionary
                
                //print("JSON: \(jsonInfoWeatherDay1)") // serialized json response
                
                self.collectionView.reloadData()
                
                //let id = JSON?["aircrafttype"] as! String
                //print("Aircraft Type: \(id)")
            }
            
            /*if let data = response.data, let utf8Text = String(data: data, encoding: .utf8) {
             print("Data: \(utf8Text)") // original server data as UTF8 string
             }*/
        }
    }
}
