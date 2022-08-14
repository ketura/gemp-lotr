(function($) {
	
	var includes = $('[data-include]');
  $.each(includes, function () {
    var file = 'views/' + $(this).data('include') + '.html'
    $(this).load(file)
    //alert( "Loaded " + file );
  })
  
  var	$window = $(window);

  $window.on('load', function() {
  	window.setTimeout(function() {
  		$body.removeClass('is-preload');
  	}, 100);
  });

})(jQuery);