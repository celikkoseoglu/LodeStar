import UIKit
import GVRKit
import AVKit

class VenueViewController: UIViewController/*, GVRRendererViewControllerDelegate*/ {
    /*func renderer(for displayMode: GVRDisplayMode) -> GVRRenderer! {
        
        let vodafoneImage = UIImage.init(named: "underwater.jpg")
        let renderer = GVRImageRenderer.init(image: vodafoneImage)
        renderer?.setSphericalMeshOfRadius(50, latitudes: 12, longitudes: 24, verticalFov: 180, horizontalFov: 360, meshType: GVRMeshType.stereoTopBottom)
        let sceneRenderer = GVRSceneRenderer.init()
        sceneRenderer.renderList.add(renderer)
        
        return sceneRenderer
    }*/ //commented parts will only be necessary if we decide to switch to GVRKit down the road. (because we might need OpenGL rendering functionality)
    
    @IBOutlet weak var imageLabel: UILabel!
    @IBOutlet weak var imageVRView: GVRPanoramaView!
    //@IBOutlet weak var scrollView: UIScrollView! //there has to be mock parent view to insert GVRKit GVRPanoramaView in. Add it to the storyboard when the inevitable end comes.
    
    enum Media {
        static var photoArray = ["sindhu_beach.jpg", "grand_canyon.jpg", "underwater.jpg"]
    }
    
    var currentView: UIView?

    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        /*var viewController = GVRRendererViewController.init()
        viewController.delegate = self
        var panoView = GVRRendererView.init(renderer: viewController.rendererView.renderer)
        scrollView.addSubview(panoView!)
        self.addChildViewController(viewController)
        panoView?.frame = CGRect(x: 16,y: 16,width: 200, height: 200)*/ //preserve this code as a reference for GVRKit and the difficulty involved...
        
        imageLabel.isHidden = true
        imageVRView.isHidden = true
        imageVRView.delegate = self
        
        imageVRView.load(UIImage(named: Media.photoArray.first!), of: GVRPanoramaImageType.mono)
        imageVRView.enableCardboardButton = true
        imageVRView.enableFullscreenButton = true
    }
}

extension VenueViewController: GVRWidgetViewDelegate {
    func widgetView(_ widgetView: GVRWidgetView!, didLoadContent content: Any!) {
        if content is UIImage {
            imageVRView.isHidden = false
            imageLabel.isHidden = false
        }
    }
    
    func widgetView(_ widgetView: GVRWidgetView!, didFailToLoadContent content: Any!,
                    withErrorMessage errorMessage: String!)  {
        print(errorMessage)
    }
    
    func widgetView(_ widgetView: GVRWidgetView!, didChange displayMode: GVRWidgetDisplayMode) {
    }
    
    func widgetViewDidTap(_ widgetView: GVRWidgetView!) {
        
        // 2
        if currentView == imageVRView {
            Media.photoArray.append(Media.photoArray.removeFirst())
            imageVRView?.load(UIImage(named: Media.photoArray.first!),
                              of: GVRPanoramaImageType.mono)
        }
    }
}
