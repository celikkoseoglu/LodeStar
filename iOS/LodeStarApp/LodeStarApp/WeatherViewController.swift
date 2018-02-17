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
fileprivate let sectionInsets = UIEdgeInsets(top: 0.0, left: 6.0, bottom: 3.0, right: 6.0)
fileprivate let cellCount = 6
fileprivate let reuseIdentifier = "weatherCell"

fileprivate var weatherPics = [UIImage(named: "maraudingFiendPanda"), UIImage(named: "maraudingFiendPanda"), UIImage(named: "maraudingFiendPanda"), UIImage(named: "maraudingFiendPanda"), UIImage(named: "maraudingFiendPanda"), UIImage(named: "maraudingFiendPanda")]
fileprivate var dayTexts = ["Today it is ", "Today it is ", "Today it is ", "Today it is ", "Today it is ", "Today it is "]
fileprivate var temperatures = [39, 38, 39, 39, 39, 34]
fileprivate var feelsLikeTemperatures = [41, 40, 41,41, 41, 36]
fileprivate var humidities = [88, 86, 85, 99, 42, 56]
fileprivate var humidityComments = ["Highly humid", "Highly humid", "Highly humid", "Highly humid", "Highly humid", "Highly humid"]
fileprivate var weatherTexts = ["Rainy", "Rainy", "Rainy", "Rainy", "Rainy", "Rainy"]

// MARK: - UICollectionViewDataSource
extension WeatherViewController {
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    //2
    func collectionView(_ collectionView: UICollectionView,
                        numberOfItemsInSection section: Int) -> Int {
        return 6
    }
    
    //3
    func collectionView(_ collectionView: UICollectionView,
                        cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier, for: indexPath) as! WeatherCell
        
        let index = indexPath as NSIndexPath
        
        //background shadow for collectionView elements
        cell.layer.shadowColor = UIColor.black.cgColor
        cell.layer.shadowOffset = CGSize(width: 5, height: 5)
        cell.layer.shadowRadius = 5;
        cell.layer.shadowOpacity = 0.25;
        cell.clipsToBounds = false
        cell.layer.masksToBounds = false
        
        cell.weatherPic.image = weatherPics[index.row]
        cell.dayText.text = dayTexts[index.row]
        cell.temperature.text = String(temperatures[index.row])
        cell.feelsLikeTemperature.text = String(feelsLikeTemperatures[index.row])
        cell.humidity.text = String(humidities[index.row])
        cell.humidityComment.text = humidityComments[index.row]
        cell.weatherText.text = weatherTexts[index.row]
        
        cell.displayContent(weatherPic: weatherPics[index.row]!, dayText: dayTexts[index.row], temperature: temperatures[index.row], feelsLikeTemperature: feelsLikeTemperatures[index.row], humidity: humidities[index.row], humidityComment: humidityComments[index.row], weatherText: weatherTexts[index.row])
        
        return cell
        
    }
}

extension WeatherViewController : UICollectionViewDelegateFlowLayout, UICollectionViewDelegate, UICollectionViewDataSource {
    //1
    func collectionView(_ collectionView: UICollectionView,
                        layout collectionViewLayout: UICollectionViewLayout,
                        sizeForItemAt indexPath: IndexPath) -> CGSize {
        //2
        let paddingSpace = sectionInsets.left * (itemsPerRow + 1)
        let availableWidth = view.frame.width - paddingSpace
        let widthPerItem = availableWidth / itemsPerRow
        let heightperItem = 230 as CGFloat
        
        return CGSize(width: widthPerItem, height: heightperItem )
    }
    
    //3
    func collectionView(_ collectionView: UICollectionView,
                        layout collectionViewLayout: UICollectionViewLayout,
                        insetForSectionAt section: Int) -> UIEdgeInsets {
        return sectionInsets
    }
    
    // 4
    func collectionView(_ collectionView: UICollectionView,
                        layout collectionViewLayout: UICollectionViewLayout,
                        minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return sectionInsets.left
    }
}

class WeatherViewController: UIViewController {
    
    @IBOutlet weak var collectionView: UICollectionView!
    override func viewDidLoad() {
        super.viewDidLoad()
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
    
    /*func jsonparse() {
       Alamofire.request("http://lodestarapp.com:3005/?city=Ankara&units=metric").responseJSON { response in
            //print("Request: \(String(describing: response.request))")   // original url request
            //print("Response: \(String(describing: response.response))") // http url response
            print("Result: \(response.result)")                         // response serialization result
                
                //print("JSON: \(json)") // serialized json response
                let JSON = response.result.value as? NSDictionary
                let id = JSON?["main"] as! NSDictionary
                var temp = id["temp"] as! Double
                
                temp = temp - 273
                let tempInt = Int(temp)
                
                temperatures[0] = tempInt
        }
    
            /*if let data = response.data, let utf8Text = String(data: data, encoding: .utf8) {
             print("Data: \(utf8Text)") // original server data as UTF8 string
             }*/
    }
 */
    
    // MARK: Actions
    @IBAction func backButtonTapAction(_ sender: UIButton) {
        
        dismiss(animated: true, completion: nil)
        
    }
    
}
