jQuery(document).ready(function() {
      revapi = jQuery('.tp-banner').show().revolution({
        delay: 1000,
        startwidth: 1170,
        startheight: 500,
        hideThumbs: true,
        fullWidth: "on",
        fullScreen: "on",
        touchenabled:"on",                      // Enable Swipe Function : on/off
        onHoverStop:"on",                       // Stop Banner Timet at Hover on Slide on/off
        fullScreenOffsetContainer: ""
      });
    });