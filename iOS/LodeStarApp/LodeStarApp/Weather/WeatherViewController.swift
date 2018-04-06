//
//  WeatherViewController.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 11.01.2018.
//  Copyright © 2018 Berk Abbasoglu. All rights reserved.
//

import Foundation
import UIKit
import Alamofire

fileprivate let itemsPerRow: CGFloat = 1
fileprivate let sectionInsets = UIEdgeInsets(top: 6.0, left: 6.0, bottom: 0.0, right: 6.0)
fileprivate let cellCount = 5
fileprivate let reuseIdentifier = "weatherCell"
fileprivate let reuseIdentifierSmall = "smallWeatherCell"

fileprivate var weatherPics = [UIImage(named: "maraudingFiendPanda"), UIImage(named: "maraudingFiendPanda"), UIImage(named: "maraudingFiendPanda"), UIImage(named: "maraudingFiendPanda"), UIImage(named: "maraudingFiendPanda"), UIImage(named: "maraudingFiendPanda")]
fileprivate var weatherTexts = ["Chest & Back", "Saturday - Legs", "Sunday - Rest", "Monday - Chest & Back", "Tuesday - Legs", "Wednesday - Shoulders & Arms"]
fileprivate var temperatures = [39, 38, 39, 39, 39, 34]
fileprivate var feelsLikeTemperatures = [41, 40, 41,41, 41, 36]
fileprivate var humidities = [88, 86, 85, 99, 42, 56]
fileprivate var humidityComments = ["Highly humid", "Highly humid", "Highly humid", "Highly humid", "Highly humid", "Highly humid"]
fileprivate var weatherTexts2 = ["Today it is", "Today it is", "Today it is", "Today it is", "Today it is", "Today it is"]

var jsonDay0:NSDictionary = ["": [""]]
var jsonDay1:NSDictionary = ["": [""]]
var jsonDay2:NSDictionary = ["": [""]]
var jsonDay3:NSDictionary = ["": [""]]
var jsonDay4:NSDictionary = ["": [""]]
var jsonDay5:NSDictionary = ["": [""]]

extension WeatherViewController {
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 2
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        if (indexPath.row == 0) {
            
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier, for: indexPath) as! WeatherCell
            let index = indexPath as NSIndexPath
            
            if (jsonDay1["weather"] != nil) {
                
                let destinationCityName = "Ankara"// destinationInfo["city"] as? String
                
                let weatherArr = jsonDay0["weather"] as! NSArray
                let weatherDict = weatherArr[0] as! NSDictionary
                let weatherTempHumidity = jsonDay0["main"] as! NSDictionary
                
                let weatherCondition = weatherDict["description"] as? String
                let temperature = weatherTempHumidity["temp"] as? Double
                let humidity = weatherTempHumidity["humidity"] as? Double
                
                cell.locationText.text = "in " + destinationCityName
                cell.weatherText.text = weatherCondition
                cell.temperature.text = String(Int(temperature!)) + "°"
                cell.humidity.text = String(Int(humidity!))
            }
                
            else {
                
                cell.temperature.text = String(temperatures[index.row]) + "°"
                cell.feelsLikeTemperature.text = "feels like " + String(feelsLikeTemperatures[index.row])
                cell.humidity.text = String(humidities[index.row])
                cell.humidityComment.text = humidityComments[index.row]
                cell.weatherText.text = weatherTexts[index.row]
                
                cell.displayContent(weatherPic: weatherPics[index.row]!, dayText: weatherTexts2[index.row], temperature: temperatures[index.row], feelsLikeTemperature: feelsLikeTemperatures[index.row], humidity: humidities[index.row], humidityComment: humidityComments[index.row], weatherText: weatherTexts[index.row])
            }
            
            return cell
        }
            
        else if (indexPath.row == 1) {
            
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifierSmall, for: indexPath) as! SmallWeatherCell
            
            let index = indexPath as NSIndexPath
            
            if (jsonDay2["weather"] != nil) {
                
                //let destinationInfo = jsonInfoFlight["destination"] as! NSDictionary
                
                var weatherArr = jsonDay1["weather"] as! NSArray
                var weatherDict = weatherArr[0] as! NSDictionary
                var weatherTempHumidity = jsonDay1["main"] as! NSDictionary
                
                var weatherCondition = weatherDict["description"] as? String
                var temperature = weatherTempHumidity["temp"] as? Double
                var humidity = weatherTempHumidity["humidity"] as? Double
                
                cell.weatherText1.text = weatherCondition
                cell.temperature1.text = String(Int(temperature!)) + "°"
                cell.humidity1.text = String(Int(humidity!)) + "%"
                
                /////////
                
                weatherArr = jsonDay2["weather"] as! NSArray
                weatherDict = weatherArr[0] as! NSDictionary
                weatherTempHumidity = jsonDay2["main"] as! NSDictionary
                
                weatherCondition = weatherDict["description"] as? String
                temperature = weatherTempHumidity["temp"] as? Double
                humidity = weatherTempHumidity["humidity"] as? Double
                
                cell.weatherText2.text = weatherCondition
                cell.temperature2.text = String(Int(temperature!)) + "°"
                cell.humidity2.text = String(Int(humidity!)) + "%"
                
                /////////
                
                weatherArr = jsonDay3["weather"] as! NSArray
                weatherDict = weatherArr[0] as! NSDictionary
                weatherTempHumidity = jsonDay3["main"] as! NSDictionary
                
                weatherCondition = weatherDict["description"] as? String
                temperature = weatherTempHumidity["temp"] as? Double
                humidity = weatherTempHumidity["humidity"] as? Double
                
                cell.weatherText3.text = weatherCondition
                cell.temperature3.text = String(Int(temperature!)) + "°"
                cell.humidity3.text = String(Int(humidity!)) + "%"
                
                ////////
                
                weatherArr = jsonDay4["weather"] as! NSArray
                weatherDict = weatherArr[0] as! NSDictionary
                weatherTempHumidity = jsonDay4["main"] as! NSDictionary
                
                weatherCondition = weatherDict["description"] as? String
                temperature = weatherTempHumidity["temp"] as? Double
                humidity = weatherTempHumidity["humidity"] as? Double
                
                cell.weatherText4.text = weatherCondition
                cell.temperature4.text = String(Int(temperature!)) + "°"
                cell.humidity4.text = String(Int(humidity!)) + "%"
                
                /////////
                
                weatherArr = jsonDay5["weather"] as! NSArray
                weatherDict = weatherArr[0] as! NSDictionary
                weatherTempHumidity = jsonDay5["main"] as! NSDictionary
                
                weatherCondition = weatherDict["description"] as? String
                temperature = weatherTempHumidity["temp"] as? Double
                humidity = weatherTempHumidity["humidity"] as? Double
                
                cell.weatherText4.text = weatherCondition
                cell.temperature4.text = String(Int(temperature!)) + "°"
                cell.humidity4.text = String(Int(humidity!)) + "%"
            }
                
            else {
                cell.temperature1.text = String(temperatures[index.row])
                cell.feelsLikeTemperature1.text = String(feelsLikeTemperatures[index.row])
                cell.humidity1.text = String(humidities[index.row])
                cell.humidityComment1.text = humidityComments[index.row]
                cell.weatherText1.text = weatherTexts[index.row]
                
                //cell.displayContent(temperature: temperatures[index.row], feelsLikeTemperature: feelsLikeTemperatures[index.row], humidity: humidities[index.row], humidityComment: humidityComments[index.row], weatherText: weatherTexts[index.row])
            }
            
            return cell
        }

            
        else {
            
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifierSmall, for: indexPath) as! SmallWeatherCell
            
            let index = indexPath as NSIndexPath
            
            cell.temperature1.text = String(temperatures[index.row])
            cell.feelsLikeTemperature1.text = String(feelsLikeTemperatures[index.row])
            cell.humidity1.text = String(humidities[index.row])
            cell.humidityComment1.text = humidityComments[index.row]
            cell.weatherText1.text = weatherTexts[index.row]
            
            //cell.displayContent(temperature: temperatures[index.row], feelsLikeTemperature: feelsLikeTemperatures[index.row], humidity: humidities[index.row], humidityComment: humidityComments[index.row], weatherText: weatherTexts[index.row])
            
            return cell
            
        }
    }
}

extension WeatherViewController {
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let paddingSpace = sectionInsets.left * (itemsPerRow + 1)
        let availableWidth = view.frame.width - paddingSpace
        let widthPerItem = availableWidth / itemsPerRow
        
        var heightPerItem = 400 as CGFloat
        
        if indexPath.row == 0 {
            heightPerItem = 240
        }
        else {
            heightPerItem = 650
        }
        
        return CGSize(width: widthPerItem, height: heightPerItem)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return sectionInsets
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return sectionInsets.top
    }
}

class WeatherViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {

    @IBOutlet weak var collectionView: UICollectionView!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        jsonparseWeather(city: "Ankara", units: "metric")
        
        // Do any additional setup after loading the view,  from a nib.
        collectionView.dataSource = self
        collectionView.delegate = self
        
        self.collectionView.isScrollEnabled = true
        //self.navigationController?.navigationBar.isTranslucent = false
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func backButtonTapAction(_ sender: UIButton) {
        dismiss(animated: true, completion: nil)
    }
    
    func jsonparseWeather(city: String, units: String) {
        
        var requestTemplate = "http://lodestarapp.com:3005/?city=#city&units=#units"
        
        requestTemplate = requestTemplate.replacingOccurrences(of: "#city", with: city)
        requestTemplate = requestTemplate.replacingOccurrences(of: "#units", with: units)
        
        Alamofire.request(requestTemplate).responseJSON { response in
            if let json = response.result.value {
            
                let jsonInfoWeather = (json as? NSArray)!
                jsonDay0 = jsonInfoWeather[0] as! NSDictionary
                jsonDay1 = jsonInfoWeather[1] as! NSDictionary
                jsonDay2 = jsonInfoWeather[2] as! NSDictionary
                jsonDay3 = jsonInfoWeather[3] as! NSDictionary
                jsonDay4 = jsonInfoWeather[4] as! NSDictionary
                jsonDay5 = jsonInfoWeather[5] as! NSDictionary
                
                self.collectionView.reloadData()
            }
        }
    }
    
}
