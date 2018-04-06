//
//  Created by Berk Abbasoglu
//

import UIKit

class FlightDetailsCell: UICollectionViewCell {    
    //card 1
    @IBOutlet weak var originCityNameLabel: UILabel!
    @IBOutlet weak var originLeavingFromGateLabel: UILabel!
    @IBOutlet weak var originAirportNameLabel: UILabel!
    @IBOutlet weak var departureTimeOffsetLabel: UILabel!
    @IBOutlet weak var departureTimeLocalLabel: UILabel!
    
    @IBOutlet weak var arrivalCityNameLabel: UILabel!
    @IBOutlet weak var arrivalArrivingToGateLabel: UILabel!
    @IBOutlet weak var arrivalAirportNameLabel: UILabel!
    @IBOutlet weak var arrivalTimeOffSetLabel: UILabel!
    @IBOutlet weak var arrivalTimeLocalLabel: UILabel!
    
    @IBOutlet weak var averageDelayLabel: UILabel!
    @IBOutlet weak var directDistanceLabel: UILabel!
    @IBOutlet weak var plannedSpeedLabel: UILabel!
    
    // card 2
    @IBOutlet weak var weatherConditionLabel: UILabel!
    @IBOutlet weak var whenYouArriveInLabel: UILabel!
    @IBOutlet weak var temperatureLabel: UILabel!
    @IBOutlet weak var temperatureFeelsLikeLabel: UILabel!
    @IBOutlet weak var humidityLabel: UILabel!
    @IBOutlet weak var humidityFeelsLikeLabel: UILabel!
    
    // card 3
    @IBOutlet weak var altitudeLabel: UILabel!
    @IBOutlet weak var aircraftTypeLabel: UILabel!

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
