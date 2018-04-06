//
//  ProfileViewController.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 13.01.2018.
//  Copyright © 2018 Berk Abbasoglu. All rights reserved.
//

import Foundation
import UIKit

fileprivate let itemsPerRow: CGFloat = 1
fileprivate let sectionInsets = UIEdgeInsets(top: 0.0, left: 20.0, bottom: 10.0, right: 20.0)
fileprivate let cellCount = 3
fileprivate let reuseIdentifier = "logCell"

fileprivate let profilePic = UIImage(named: "maraudingFiendPanda")
fileprivate let name = "Yujuf Jan"
fileprivate let username = "@fiendishpanda"
fileprivate var date = ["14/11/19", "23/01/18", "12/12/17"]
fileprivate var logs = ["For the freedom and glory of Uganda!", "The fiercest beast of Malaysia? You're staring at him", "Ah, Chineese pandas... they are always the fluffiest"]
fileprivate var cityImages = [UIImage(named: "uganda"),UIImage(named: "malaysia"),UIImage(named: "china")]
fileprivate var cityNames = ["Uganda", "Malaysia", "China"]
fileprivate var taptoViewCities = ["Tap to view Uganda", "Tap to view Malaysia", "Tap to view China"]

// MARK: - UICollectionViewDataSource
extension ProfileViewController {
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    //2
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return date.count
    }
    
    //3
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifier, for: indexPath) as! LogCell
        
        let index = indexPath as NSIndexPath
        
        cell.profilePic.image = profilePic
        cell.name.text = name
        cell.username.text = username
        cell.date.text = date[index.row]
        cell.log.text = logs[index.row]
        cell.cityImage.image = cityImages[index.row]
        cell.cityName.text = cityNames[index.row]
        cell.tapToViewCity.text = taptoViewCities[index.row]
        
        cell.displayContent(profilePic: profilePic!, name: name, username: username, date: date[index.row], log: logs[index.row], cityImage: cityImages[index.row]!, cityName: cityNames[index.row], tapToViewCity: taptoViewCities[index.row])
        
        return cell
        
    }
}

extension ProfileViewController : UICollectionViewDelegateFlowLayout, UICollectionViewDelegate, UICollectionViewDataSource {
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
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return sectionInsets.left
    }
}

class ProfileViewController: UIViewController {
    
    @IBOutlet weak var collectionView: UICollectionView!
    
    @IBAction func logoutButton(_ sender: UIButton) {
        dismiss(animated: true, completion: nil)
        dismiss(animated: true, completion: nil) //why 2 times? ~celik
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        collectionView.dataSource = self
        collectionView.delegate = self
        
        self.collectionView.isScrollEnabled = true
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
}
