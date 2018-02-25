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
    
    @IBOutlet weak var weatherText: UILabel!
    @IBOutlet weak var temperature: UILabel!
    @IBOutlet weak var feelsLikeTemperature: UILabel!
    @IBOutlet weak var humidity: UILabel!
    @IBOutlet weak var humidityComment: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    func displayContent(temperature: Int, feelsLikeTemperature: Int, humidity: Int, humidityComment: String, weatherText: String) {
        
        self.temperature.text = String(temperature)  + "°"
        self.feelsLikeTemperature.text = "feels like " + String(feelsLikeTemperature)  + "°"
        self.humidity.text = String(humidity)
        self.humidityComment.text = humidityComment
        self.weatherText.text = weatherText
        
        self.backgroundColor = UIColor(red: 209/255, green: 209/255, blue: 209/255, alpha: 0.8)
        
    }
    
    
}
