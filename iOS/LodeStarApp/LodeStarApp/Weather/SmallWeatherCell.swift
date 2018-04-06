//
//  LogCell.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 13.01.2018.
//  Copyright © 2018 Berk Abbasoglu. All rights reserved.
//

import Foundation
import UIKit

class SmallWeatherCell: UICollectionViewCell {
    
    @IBOutlet weak var weatherText1: UILabel!
    @IBOutlet weak var temperature1: UILabel!
    @IBOutlet weak var feelsLikeTemperature1: UILabel!
    @IBOutlet weak var humidity1: UILabel!
    @IBOutlet weak var humidityComment1: UILabel!
    
    @IBOutlet weak var weatherText2: UILabel!
    @IBOutlet weak var temperature2: UILabel!
    @IBOutlet weak var feelsLikeTemperature2: UILabel!
    @IBOutlet weak var humidity2: UILabel!
    @IBOutlet weak var humidityComment2: UILabel!
    
    @IBOutlet weak var weatherText3: UILabel!
    @IBOutlet weak var temperature3: UILabel!
    @IBOutlet weak var feelsLikeTemperature3: UILabel!
    @IBOutlet weak var humidity3: UILabel!
    @IBOutlet weak var humidityComment3: UILabel!
    
    @IBOutlet weak var weatherText4: UILabel!
    @IBOutlet weak var temperature4: UILabel!
    @IBOutlet weak var feelsLikeTemperature4: UILabel!
    @IBOutlet weak var humidity4: UILabel!
    @IBOutlet weak var humidityComment4: UILabel!
    
    @IBOutlet weak var weatherText5: UILabel!
    @IBOutlet weak var temperature5: UILabel!
    @IBOutlet weak var feelsLikeTemperature5: UILabel!
    @IBOutlet weak var humidity5: UILabel!
    @IBOutlet weak var humidityComment5: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        //background shadow for collectionView elements
        self.layer.shadowColor = UIColor.black.cgColor
        self.layer.shadowOffset = CGSize(width: 5, height: 5)
        self.layer.shadowRadius = 5;
        self.layer.shadowOpacity = 0.25;
        self.clipsToBounds = false
        self.layer.masksToBounds = false
    }
    
    func displayContent(weatherText1: String, temperature1: Int, feelsLikeTemperature1: Int, humidity1: Int, humidityComment1: String, weatherText2: String, temperature2: Int, feelsLikeTemperature2: Int, humidity2: Int, humidityComment2: String, weatherText3: String, temperature3: Int, feelsLikeTemperature3: Int, humidity3: Int, humidityComment3: String, weatherText4: String, temperature4: Int, feelsLikeTemperature4: Int, humidity4: Int, humidityComment4: String, weatherText5: String, temperature5: Int, feelsLikeTemperature5: Int, humidity5: Int, humidityComment5: String) {
        
        self.weatherText1.text = weatherText1
        self.temperature1.text = String(temperature1)  + "°"
        self.feelsLikeTemperature1.text = "feels like " + String(feelsLikeTemperature1)  + "°"
        self.humidity1.text = String(humidity1)
        self.humidityComment1.text = humidityComment1
        
        self.weatherText2.text = weatherText2
        self.temperature2.text = String(temperature2)  + "°"
        self.feelsLikeTemperature2.text = "feels like " + String(feelsLikeTemperature2)  + "°"
        self.humidity2.text = String(humidity2)
        self.humidityComment2.text = humidityComment2
        
        self.weatherText3.text = weatherText3
        self.temperature3.text = String(temperature3)  + "°"
        self.feelsLikeTemperature3.text = "feels like " + String(feelsLikeTemperature3)  + "°"
        self.humidity3.text = String(humidity3)
        self.humidityComment3.text = humidityComment3
        
        self.weatherText4.text = weatherText4
        self.temperature4.text = String(temperature4)  + "°"
        self.feelsLikeTemperature4.text = "feels like " + String(feelsLikeTemperature4)  + "°"
        self.humidity4.text = String(humidity4)
        self.humidityComment4.text = humidityComment4
        
        self.weatherText5.text = weatherText5
        self.temperature5.text = String(temperature5)  + "°"
        self.feelsLikeTemperature5.text = "feels like " + String(feelsLikeTemperature5)  + "°"
        self.humidity5.text = String(humidity5)
        self.humidityComment5.text = humidityComment5
    }
}
