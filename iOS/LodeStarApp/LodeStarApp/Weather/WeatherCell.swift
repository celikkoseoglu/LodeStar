//
//  LogCell.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 13.01.2018.
//  Copyright © 2018 Berk Abbasoglu. All rights reserved.
//

import Foundation
import UIKit

class WeatherCell: UICollectionViewCell {
    
    @IBOutlet weak var locationText: UILabel!
    @IBOutlet weak var weatherText: UILabel!
    @IBOutlet weak var humidityComment: UILabel!
    @IBOutlet weak var humidity: UILabel!
    @IBOutlet weak var feelsLikeTemperature: UILabel!
    @IBOutlet weak var temperature: UILabel!
    @IBOutlet weak var dayText: UILabel!
    @IBOutlet weak var weatherPic: UIImageView!
    
    func displayContent(weatherPic: UIImage, dayText: String, temperature: Int, feelsLikeTemperature: Int, humidity: Int, humidityComment: String, weatherText: String) {
        
        self.weatherPic.image = weatherPic
        self.dayText.text = dayText
        self.temperature.text = String(temperature)  + "°"
        self.feelsLikeTemperature.text = "feels like " + String(feelsLikeTemperature)  + "°"
        self.humidity.text = String(humidity)
        self.humidityComment.text = humidityComment
        self.weatherText.text = weatherText
    }
    
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
    
}
