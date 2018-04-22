//
//  LogCell.swift
//  LodeStarApp
//
//  Created by Berk Abbasoglu on 13.01.2018.
//  Copyright © 2018 Berk Abbasoglu. All rights reserved.
//

import Foundation
import UIKit

fileprivate let itemsPerRow: CGFloat = 5
fileprivate let sectionInsets = UIEdgeInsets(top: 0.0, left: 6.0, bottom: 0.0, right: 6.0)
fileprivate let cellCount = 5
fileprivate let reuseIdentifierSmall = "nearMeSmallCell"

extension NearMeMainCell {
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return cellCount
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: reuseIdentifierSmall, for: indexPath) as! NearMeSmallCell
        
        //background shadow for collectionView elements
        cell.layer.shadowColor = UIColor.black.cgColor
        cell.layer.shadowOffset = CGSize(width: 5, height: 5)
        cell.layer.shadowRadius = 5;
        cell.layer.shadowOpacity = 0.25;
        cell.clipsToBounds = false
        cell.layer.masksToBounds = false
        
        let index = indexPath as NSIndexPath
        
        if nearMeNamesRestaurants[0] != "" && nearMeNamesParks[0] != "" {
            
            if (type == "restaurant") {
            
                cell.displayContent(nearMeImage: UIImage(data: nearMeDataRestaurants[index.row])!, name: nearMeNamesRestaurants[index.row], type: nearMeTypesRestaurants[index.row], star: nearMeRatingsRestaurants[index.row] + 1)
            }
            
            else if (type == "park") {
                
                cell.displayContent(nearMeImage: UIImage(data: nearMeDataParks[index.row])!, name: nearMeNamesParks[index.row], type: nearMeTypesParks[index.row], star: nearMeRatingsParks[index.row] + 1)
                
            }
        }
        
        return cell
        
    }
}

extension NearMeMainCell {
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        let paddingSpace = sectionInsets.left * (itemsPerRow + 1)
        let availableWidth = 130 - paddingSpace
        let widthPerItem = availableWidth / itemsPerRow
        let heightperItem = 192 as CGFloat
        
        return CGSize(width: 130, height: heightperItem )
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return sectionInsets
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return sectionInsets.top
    }
}

class NearMeMainCell: UICollectionViewCell, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    @IBOutlet weak var typeLabel: UILabel!
    @IBOutlet weak var collectionView: UICollectionView!
    
    var type = ""
    
    func displayContent(type: String) {//}, collection: UICollectionView) {
        
        self.typeLabel.text = type
        //self.collectionView = collection
        
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
        
        self.collectionView.delegate = self as UICollectionViewDelegate
        self.collectionView.dataSource = self as UICollectionViewDataSource
        self.collectionView.isScrollEnabled = true
    }
}
