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

class WeatherViewController: UIViewController {
    
    
    
    @IBOutlet weak var todayItIsText: UILabel!
    @IBOutlet weak var weatherImage: UIImageView!
    @IBOutlet weak var todayHumidityText: UILabel!
    @IBOutlet weak var todayHumidityNumber: UILabel!
    @IBOutlet weak var todayFeelsTemperature: UILabel!
    @IBOutlet weak var todayTemperature: UILabel!
    @IBOutlet weak var locationText: UILabel!
    @IBOutlet weak var todayWeatherConditionText: UILabel!
    
    @IBOutlet weak var theRestOfTheWeekText: UILabel!
    @IBOutlet weak var nextDayHumidityText: UILabel!
    @IBOutlet weak var nextDayHumidityNumber: UILabel!
    @IBOutlet weak var nextDayFeelsTemperature: UILabel!
    @IBOutlet weak var nextDayTemperature: UILabel!
    @IBOutlet weak var nextDayText: UILabel!

    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view,  from a nib.
        defaultValues()
        
        jsonparse()
        
        //self.navigationController?.navigationBar.isTranslucent = false
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func defaultValues() {
        
        todayItIsText.text = "Today it is"
        todayTemperature.text = "11°"
        todayFeelsTemperature.text = "feels like 12°"
        locationText.text = "in Bull Town"
        todayWeatherConditionText.text = "Raining Shit"
        todayHumidityNumber.text = "89%"
        todayHumidityText.text = "Highly humid"
        
        theRestOfTheWeekText.text = "The rest of the week"
        nextDayText.text = "Friday - Not as Shitty"
        nextDayTemperature.text = "13°"
        nextDayFeelsTemperature.text = "12°"
        nextDayHumidityText.text = "not as humid"
        nextDayHumidityNumber.text = "88%"
        
    }
    
    func jsonparse() {
        Alamofire.request("http://localhost:3001/?dataType=weather&city=Ankara").responseJSON { response in
            //print("Request: \(String(describing: response.request))")   // original url request
            //print("Response: \(String(describing: response.response))") // http url response
            print("Result: \(response.result)")                         // response serialization result
            
            if let json = response.result.value {
                //print("JSON: \(json)") // serialized json response
                let JSON = response.result.value as? NSDictionary
                let id = JSON?["main"] as! NSDictionary
                var temp = id["temp"] as! Double
                
                temp = temp - 273
                let tempInt = Int(temp)
                
                self.todayTemperature.text = String(tempInt)
            }
            
            /*if let data = response.data, let utf8Text = String(data: data, encoding: .utf8) {
             print("Data: \(utf8Text)") // original server data as UTF8 string
             }*/
        }
    }
    
    // MARK: Actions
    @IBAction func backButtonTapAction(_ sender: UIButton) {
        
        dismiss(animated: true, completion: nil)
        
    }
    
}
