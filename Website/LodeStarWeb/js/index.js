/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
var vrView;

var timer;
var delay = 500;

//the VR view will take 80% of the browser display height on a page refresh.
jQuery(document).ready(function() {
    var height = $(window).height();
    var width = $(window).width();
    
    clearTimeout(timer);
    timer = setTimeout(function() {
        $('iframe').css('height', height * 0.8);
        $('iframe').css('width', width);

        $('.hero-padding').css('height', height * 0.8);
        $('.hero-padding').css('width', width);
    }, delay );


});

//Could change this to run on window resize.
$( window ).resize(function() {
    var height = $(window).height();
    var width = $(window).width();

    $('iframe').css('height', height * 0.8 | 0);
    $('iframe').css('width', width | 0);

    $('.hero-padding').css('height', height * 0.8 | 0);
    $('.hero-padding').css('width', width | 0);
});

// All the scenes for the experience
var scenes = {
    thy_lounge: {
        image: 'images/thy_lounge_stereo.jpg',
        preview: 'images/thy_lounge_stereo.jpg'
    },
    christTheRedeemer: {
        image: 'christ-redeemer.jpg',
        preview: 'christ-redeemer-preview.jpg'
    },
    machuPicchu: {
        image: 'machu-picchu.jpg',
        preview: 'machu-picchu-preview.jpg'
    },
    chichenItza: {
        image: 'chichen-itza.jpg',
        preview: 'chichen-itza-preview.jpg'
    },
    tajMahal: {
        image: 'taj-mahal.jpg',
        preview: 'taj-mahal-preview.jpg'
    }
};

function onLoad() {
    vrView = new VRView.Player('#vrview', {
        image: 'blank.png',
        is_stereo: true,
        is_autopan_off: true,
        is_vr_off: false
    });

    vrView.on('ready', onVRViewReady);
    vrView.on('modechange', onModeChange);
    vrView.on('getposition', onGetPosition);
    vrView.on('error', onVRViewError);
}

function loadScene(id) {
    console.log('loadScene', id);

    // Set the image
    vrView.setContent({
        image: scenes[id].image,
        preview: scenes[id].preview,
        is_autopan_off: false,
        is_stereo: true
    });
}

function onVRViewReady(e) {
    console.log('onVRViewReady');

    loadScene('thy_lounge');
}

function onModeChange(e) {
    console.log('onModeChange', e.mode);
}

function onVRViewError(e) {
    console.log('Error! %s', e.message);
}

function onGetPosition(e) {
    console.log(e)
}

window.addEventListener('load', onLoad);