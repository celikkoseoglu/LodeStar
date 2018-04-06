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
fileprivate let sectionInsets = UIEdgeInsets(top: 0.0, left: 6.0, bottom: 0.0, right: 6.0)
fileprivate let cellCount = 5
fileprivate let reuseIdentifierSmall = "smallWeatherCell"
fileprivate let reuseIdentifier = "weatherCell"

fileprivate var weatherPics = [UIImage(named: "maraudingFiendPanda"), UIImage(named: "maraudingFiendPanda"), UIImage(named: "maraudingFiendPanda"), UIImage(named: "maraudingFiendPanda"), UIImage(named: "maraudingFiendPanda"), UIImage(named: "maraudingFiendPanda")]
fileprivate var weatherTexts = ["Chest & Back", "Saturday - Legs", "Sunday - Rest", "Monday - Chest & Back", "Tuesday - Legs", "Wednesday - Shoulders & Arms"]
fileprivate var temperatures = [39, 38, 39, 39, 39, 34]
fileprivate var feelsLikeTemperatures = [41, 40, 41,41, 41, 36]
fileprivate var humidities = [88, 86, 85, 99, 42, 56]
fileprivate var humidityComments = ["Highly humid", "Highly humid", "Highly humid", "Highly humid", "Highly humid", "Highly humid"]
fileprivate var weatherTexts2 = ["Today it is", "Today it is", "Today it is", "Today it is", "Today it is", "Today it is"]

var jsonDay1:NSDictionary = ["": [""]]
var jsonDay2:NSDictionary = ["": [""]]
var jsonDay3:NSDictionary = ["": [""]]
var jsonDay4:NSDictionary = ["": [""]]
var jsonDay5:NSDictionary = ["": [""]]
var jsonDay6:NSDictionary = ["": [""]]
//var jsonDay7:NSDictionary = ["": [""]]

// MARK: - UICollectionViewDataSource
extension WeatherViewController {
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    //2
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 6
    }
    
    //3
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        if indexPath.row == 0 {
            
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier, for: indexPath) as! WeatherCell
            
            let index = indexPath as NSIndexPath
            
            if (jsonDay1["weather"] != nil) {
                
                //let destinationInfo = jsonInfoFlight["destination"] as! NSDictionary
                let destinationCityName = "Shangai"// destinationInfo["city"] as? String
                
                let weatherArr = jsonDay1["weather"] as! NSArray
                let weatherDict = weatherArr[0] as! NSDictionary
                let weatherTempHumidity = jsonDay1["main"] as! NSDictionary
                
                let weatherCondition = weatherDict["description"] as? String
                let temperature = weatherTempHumidity["temp"] as? Double
                let humidity = weatherTempHumidity["humidity"] as? Double
                
                cell.weatherText.text = weatherCondition
                cell.locationText.text = "in " + destinationCityName
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
            
        else if indexPath.row == 1 {
            
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifierSmall, for: indexPath) as! SmallWeatherCell
            
            let index = indexPath as NSIndexPath
            
            if (jsonDay2["weather"] != nil) {
                
                //let destinationInfo = jsonInfoFlight["destination"] as! NSDictionary
                let destinationCityName = "Shangai"//destinationInfo["city"] as? String
                
                let weatherArr = jsonDay2["weather"] as! NSArray
                let weatherDict = weatherArr[0] as! NSDictionary
                let weatherTempHumidity = jsonDay2["main"] as! NSDictionary
                
                let weatherCondition = weatherDict["description"] as? String
                let temperature = weatherTempHumidity["temp"] as? Double
                let humidity = weatherTempHumidity["humidity"] as? Double
                
                cell.weatherText.text = weatherCondition
                cell.temperature.text = String(Int(temperature!)) + "°"
                cell.humidity.text = String(Int(humidity!))
            }
                
            else {
                
                /*//background shadow for collectionView elements
                 cell.layer.shadowColor = UIColor.black.cgColor
                 cell.layer.shadowOffset = CGSize(width: 5, height: 5)
                 cell.layer.shadowRadius = 5;
                 cell.layer.shadowOpacity = 0.25;
                 cell.clipsToBounds = false
                 cell.layer.masksToBounds = false
                 */
                
                cell.temperature.text = String(temperatures[index.row])
                cell.feelsLikeTemperature.text = String(feelsLikeTemperatures[index.row])
                cell.humidity.text = String(humidities[index.row])
                cell.humidityComment.text = humidityComments[index.row]
                cell.weatherText.text = weatherTexts[index.row]
                
                cell.displayContent(temperature: temperatures[index.row], feelsLikeTemperature: feelsLikeTemperatures[index.row], humidity: humidities[index.row], humidityComment: humidityComments[index.row], weatherText: weatherTexts[index.row])
            }
            
            return cell
        }
            
        else if indexPath.row == 2 {
            
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifierSmall, for: indexPath) as! SmallWeatherCell
            
            let index = indexPath as NSIndexPath
            
            if (jsonDay3["weather"] != nil) {
                
                //let destinationInfo = jsonInfoFlight["destination"] as! NSDictionary
                let destinationCityName = "Shangai"//destinationInfo["city"] as? String
                
                let weatherArr = jsonDay3["weather"] as! NSArray
                let weatherDict = weatherArr[0] as! NSDictionary
                let weatherTempHumidity = jsonDay3["main"] as! NSDictionary
                
                let weatherCondition = weatherDict["description"] as? String
                let temperature = weatherTempHumidity["temp"] as? Double
                let humidity = weatherTempHumidity["humidity"] as? Double
                
                cell.weatherText.text = weatherCondition
                cell.temperature.text = String(Int(temperature!)) + "°"
                cell.humidity.text = String(Int(humidity!))
            }
                
            else {
                
                /*//background shadow for collectionView elements
                 cell.layer.shadowColor = UIColor.black.cgColor
                 cell.layer.shadowOffset = CGSize(width: 5, height: 5)
                 cell.layer.shadowRadius = 5;
                 cell.layer.shadowOpacity = 0.25;
                 cell.clipsToBounds = false
                 cell.layer.masksToBounds = false
                 */
                
                cell.temperature.text = String(temperatures[index.row])
                cell.feelsLikeTemperature.text = String(feelsLikeTemperatures[index.row])
                cell.humidity.text = String(humidities[index.row])
                cell.humidityComment.text = humidityComments[index.row]
                cell.weatherText.text = weatherTexts[index.row]
                
                cell.displayContent(temperature: temperatures[index.row], feelsLikeTemperature: feelsLikeTemperatures[index.row], humidity: humidities[index.row], humidityComment: humidityComments[index.row], weatherText: weatherTexts[index.row])
            }
            
            return cell
        }
            
        else if indexPath.row == 3 {
            
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifierSmall, for: indexPath) as! SmallWeatherCell
            
            let index = indexPath as NSIndexPath
            
            if (jsonDay4["weather"] != nil) {
                
                //let destinationInfo = jsonInfoFlight["destination"] as! NSDictionary
                let destinationCityName = "Shangai"//destinationInfo["city"] as? String
                
                let weatherArr = jsonDay4["weather"] as! NSArray
                let weatherDict = weatherArr[0] as! NSDictionary
                let weatherTempHumidity = jsonDay4["main"] as! NSDictionary
                
                let weatherCondition = weatherDict["description"] as? String
                let temperature = weatherTempHumidity["temp"] as? Double
                let humidity = weatherTempHumidity["humidity"] as? Double
                
                cell.weatherText.text = weatherCondition
                cell.temperature.text = String(Int(temperature!)) + "°"
                cell.humidity.text = String(Int(humidity!))
            }
                
            else {
                
                /*//background shadow for collectionView elements
                 cell.layer.shadowColor = UIColor.black.cgColor
                 cell.layer.shadowOffset = CGSize(width: 5, height: 5)
                 cell.layer.shadowRadius = 5;
                 cell.layer.shadowOpacity = 0.25;
                 cell.clipsToBounds = false
                 cell.layer.masksToBounds = false
                 */
                
                cell.temperature.text = String(temperatures[index.row])
                cell.feelsLikeTemperature.text = String(feelsLikeTemperatures[index.row])
                cell.humidity.text = String(humidities[index.row])
                cell.humidityComment.text = humidityComments[index.row]
                cell.weatherText.text = weatherTexts[index.row]
                
                cell.displayContent(temperature: temperatures[index.row], feelsLikeTemperature: feelsLikeTemperatures[index.row], humidity: humidities[index.row], humidityComment: humidityComments[index.row], weatherText: weatherTexts[index.row])
            }
            
            return cell
        }
            
        else if indexPath.row == 4 {
            
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifierSmall, for: indexPath) as! SmallWeatherCell
            
            let index = indexPath as NSIndexPath
            
            if (jsonDay2["weather"] != nil) {
                
                //let destinationInfo = jsonInfoFlight["destination"] as! NSDictionary
                let destinationCityName = "Shangai"//destinationInfo["city"] as? String
                
                let weatherArr = jsonDay5["weather"] as! NSArray
                let weatherDict = weatherArr[0] as! NSDictionary
                let weatherTempHumidity = jsonDay5["main"] as! NSDictionary
                
                let weatherCondition = weatherDict["description"] as? String
                let temperature = weatherTempHumidity["temp"] as? Double
                let humidity = weatherTempHumidity["humidity"] as? Double
                
                cell.weatherText.text = weatherCondition
                cell.temperature.text = String(Int(temperature!)) + "°"
                cell.humidity.text = String(Int(humidity!))
            }
                
            else {
                
                /*//background shadow for collectionView elements
                 cell.layer.shadowColor = UIColor.black.cgColor
                 cell.layer.shadowOffset = CGSize(width: 5, height: 5)
                 cell.layer.shadowRadius = 5;
                 cell.layer.shadowOpacity = 0.25;
                 cell.clipsToBounds = false
                 cell.layer.masksToBounds = false
                 */
                
                cell.temperature.text = String(temperatures[index.row])
                cell.feelsLikeTemperature.text = String(feelsLikeTemperatures[index.row])
                cell.humidity.text = String(humidities[index.row])
                cell.humidityComment.text = humidityComments[index.row]
                cell.weatherText.text = weatherTexts[index.row]
                
                cell.displayContent(temperature: temperatures[index.row], feelsLikeTemperature: feelsLikeTemperatures[index.row], humidity: humidities[index.row], humidityComment: humidityComments[index.row], weatherText: weatherTexts[index.row])
            }
            
            return cell
        }
            
        else if indexPath.row == 5 {
            
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifierSmall, for: indexPath) as! SmallWeatherCell
            
            let index = indexPath as NSIndexPath
            
            if (jsonDay2["weather"] != nil) {
                
                //let destinationInfo = jsonInfoFlight["destination"] as! NSDictionary
                let destinationCityName = "Shangai"//destinationInfo["city"] as? String
                
                let weatherArr = jsonDay6["weather"] as! NSArray
                let weatherDict = weatherArr[0] as! NSDictionary
                let weatherTempHumidity = jsonDay6["main"] as! NSDictionary
                
                let weatherCondition = weatherDict["description"] as? String
                let temperature = weatherTempHumidity["temp"] as? Double
                let humidity = weatherTempHumidity["humidity"] as? Double
                
                cell.weatherText.text = weatherCondition
                cell.temperature.text = String(Int(temperature!)) + "°"
                cell.humidity.text = String(Int(humidity!))
            }
                
            else {
                
                /*//background shadow for collectionView elements
                 cell.layer.shadowColor = UIColor.black.cgColor
                 cell.layer.shadowOffset = CGSize(width: 5, height: 5)
                 cell.layer.shadowRadius = 5;
                 cell.layer.shadowOpacity = 0.25;
                 cell.clipsToBounds = false
                 cell.layer.masksToBounds = false
                 */
                
                cell.temperature.text = String(temperatures[index.row])
                cell.feelsLikeTemperature.text = String(feelsLikeTemperatures[index.row])
                cell.humidity.text = String(humidities[index.row])
                cell.humidityComment.text = humidityComments[index.row]
                cell.weatherText.text = weatherTexts[index.row]
                
                cell.displayContent(temperature: temperatures[index.row], feelsLikeTemperature: feelsLikeTemperatures[index.row], humidity: humidities[index.row], humidityComment: humidityComments[index.row], weatherText: weatherTexts[index.row])
            }
            
            return cell
        }
            
            /* else if indexPath.row == 6 {
             
             let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifierSmall, for: indexPath) as! SmallWeatherCell
             
             let index = indexPath as NSIndexPath
             
             if (jsonDay2["weather"] != nil) {
             
             //let destinationInfo = jsonInfoFlight["destination"] as! NSDictionary
             let destinationCityName = "Shangai"//destinationInfo["city"] as? String
             
             let weatherArr = jsonDay7["weather"] as! NSArray
             let weatherDict = weatherArr[0] as! NSDictionary
             let weatherTempHumidity = jsonDay7["main"] as! NSDictionary
             
             let weatherCondition = weatherDict["description"] as? String
             let temperature = weatherTempHumidity["temp"] as? Double
             let humidity = weatherTempHumidity["humidity"] as? Double
             
             cell.weatherText.text = weatherCondition
             cell.temperature.text = String(Int(temperature!)) + "°"
             cell.humidity.text = String(Int(humidity!))
             }
             
             else {
             
             /*//background shadow for collectionView elements
             cell.layer.shadowColor = UIColor.black.cgColor
             cell.layer.shadowOffset = CGSize(width: 5, height: 5)
             cell.layer.shadowRadius = 5;
             cell.layer.shadowOpacity = 0.25;
             cell.clipsToBounds = false
             cell.layer.masksToBounds = false
             */
             
             cell.temperature.text = String(temperatures[index.row])
             cell.feelsLikeTemperature.text = String(feelsLikeTemperatures[index.row])
             cell.humidity.text = String(humidities[index.row])
             cell.humidityComment.text = humidityComments[index.row]
             cell.weatherText.text = weatherTexts[index.row]
             
             cell.displayContent(temperature: temperatures[index.row], feelsLikeTemperature: feelsLikeTemperatures[index.row], humidity: humidities[index.row], humidityComment: humidityComments[index.row], weatherText: weatherTexts[index.row])
             }
             
             return cell
             }
             */
        else {
            
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifierSmall, for: indexPath) as! SmallWeatherCell
            
            let index = indexPath as NSIndexPath
            
            cell.temperature.text = String(temperatures[index.row])
            cell.feelsLikeTemperature.text = String(feelsLikeTemperatures[index.row])
            cell.humidity.text = String(humidities[index.row])
            cell.humidityComment.text = humidityComments[index.row]
            cell.weatherText.text = weatherTexts[index.row]
            
            cell.displayContent(temperature: temperatures[index.row], feelsLikeTemperature: feelsLikeTemperatures[index.row], humidity: humidities[index.row], humidityComment: humidityComments[index.row], weatherText: weatherTexts[index.row])
            //}
            
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
            heightPerItem = 115
        }
        
        return CGSize(width: widthPerItem, height: heightPerItem)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return sectionInsets
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return sectionInsets.left
    }
}

class WeatherViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    
    @IBOutlet weak var collectionView: UICollectionView!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        jsonparseWeather(city: "Shangai", units: "metric")
        
        // Do any additional setup after loading the view,  from a nib.
        collectionView.dataSource = self
        collectionView.delegate = self
        
        self.collectionView.isScrollEnabled = true
        //self.navigationController?.navigationBar.isTranslucent = false
        
        jsonparse()
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func jsonparse() {
        Alamofire.request("http://lodestarapp.com:3005/?city=Ankara&units=metric").responseJSON { response in
            //print("Request: \(String(describing: response.request))")   // original url request
            //print("Response: \(String(describing: response.response))") // http url response
            print("Result: \(response.result)")                         // response serialization result
            
            //print("JSON: \(json)") // serialized json response
            let JSON = response.result.value as? NSDictionary
            //let id = JSON?["main"] as! NSDictionary
            //var temp = id["temp"] as! Double
            
            //temp = temp - 273
            //let tempInt = Int(temp)
            
            //temperatures[0] = tempInt
        }
        
        /*if let data = response.data, let utf8Text = String(data: data, encoding: .utf8) {
         print("Data: \(utf8Text)") // original server data as UTF8 string
         }*/
    }
    
    // MARK: Actions
    @IBAction func backButtonTapAction(_ sender: UIButton) {
        
        dismiss(animated: true, completion: nil)
        
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
                jsonDay1 = jsonInfoWeather[0] as! NSDictionary
                jsonDay2 = jsonInfoWeather[1] as! NSDictionary
                jsonDay3 = jsonInfoWeather[2] as! NSDictionary
                jsonDay4 = jsonInfoWeather[3] as! NSDictionary
                jsonDay5 = jsonInfoWeather[4] as! NSDictionary
                jsonDay6 = jsonInfoWeather[5] as! NSDictionary
                //jsonDay7 = jsonInfoWeather[6] as! NSDictionary
                
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
