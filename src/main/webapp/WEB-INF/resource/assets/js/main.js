jQuery(document).ready(function($){
	
	var parentListItem,topSelected,leftSelected,widthSelected ,
		heightSelected ,windowWidth ,windowHeight,finalLeft ,
		finalHeight,finalTop ,quickViewWidth ,quickViewLeft;
	//final width --> this is the quick view image slider width
	//maxQuickWidth --> this is the max-width of the quick-view panel
	var sliderFinalWidth = 400,
		maxQuickWidth = 900;

	//open the quick view panel

	$('#imge01').on('click', function(event)
			{	
				opendialog('01','监控管理','在增值税管理系统中，可对开票服务器，以及开票服务器下的开票点进行管理。具体功能包括添加、修改、删除开票服务器和开票点。并可监控掌握开票服务器的的状态、开票点数量以及库存等情况。同时可以控制开票点的开票状态。','images/cgddkj_icon.png');
			});
			
			$('#imge02').on('click', function(event)
			{	
				opendialog('02','开票管理','在增值税管理系统中，可进行手工开票申请，开票审核。也可对来自外部业务系统的销售单数据形成的待开数据进行开票。对于开具有误的发票可进行作废。还可对已作废发票提出重开票申请和审核。同时，还可对已通过红字发票信息表审核的蓝子发票开具对应的红字发票进行红冲。','images/cgdd_icon.png');
			});
			
			$('#imge03').on('click', function(event)
			{	
				opendialog('03','发票管理','在增值税管理系统中，可进行发票出入库操作，发票出入库统计以及发票库存查询。还可进行发票请领以及发票请领审核。请领通过后，可进行发票的分发即发货操作。对于破损或不使用的空白发票可进行缴销。','images/fpkj_icon.png');
			});
			
			$('#imge04').on('click', function(event)
			{	
				opendialog('04','单据管理','在增值税管理系统中，可通过业务系统传入待开票的业务数据。同时根据相应开票规则，对待开票的业务数据进行拆分合并形成待开数据。如需重新拆分合并还可对已拆分合并的业务数据进行还原。对于无需开票的业务数据可进行作废处理。','images/fpqs_icon.png');
			});
			
			$('#imge05').on('click', function(event)
			{	
				opendialog('05','发票认证','在增值税综合服务平台内采购商（购方）将发票电子信息上传至国税系统完成认证抵扣工作。','images/fprz_icon.png');
			});
			
	//剥离出的公共方法
	function opendialog(id,title,content,imgpath)
	{
		var selectedImage = $('#img'+id).children('img');
		animateQuickView(selectedImage, sliderFinalWidth, maxQuickWidth, 'open',title,content,imgpath);
	}

	//close the quick view panel
	$('body').on('click', function(event){
		if( $(event.target).is('.cd-close') || $(event.target).is('body.overlay-layer')) {
			closeQuickView( sliderFinalWidth, maxQuickWidth);
		}
	});
	$(document).keyup(function(event){
		//check if user has pressed 'Esc'
    	if(event.which=='27'){
			closeQuickView( sliderFinalWidth, maxQuickWidth);
		}
	});

	//quick view slider implementation
	$('.cd-quick-view').on('click', '.cd-slider-navigation a', function(){
		updateSlider($(this));
	});

	//center quick-view on window resize
	$(window).on('resize', function(){
		if($('.cd-quick-view').hasClass('is-visible')){
			window.requestAnimationFrame(resizeQuickView);
		}
	});

	function updateSlider(navigation) {
		var sliderConatiner = navigation.parents('.cd-slider-wrapper').find('.cd-slider'),
			activeSlider = sliderConatiner.children('.selected').removeClass('selected');
		if ( navigation.hasClass('cd-next') ) {
			( !activeSlider.is(':last-child') ) ? activeSlider.next().addClass('selected') : sliderConatiner.children('li').eq(0).addClass('selected'); 
		} else {
			( !activeSlider.is(':first-child') ) ? activeSlider.prev().addClass('selected') : sliderConatiner.children('li').last().addClass('selected');
		} 
	}

	function updateQuickView(url) {
		$('.cd-quick-view .cd-slider li').removeClass('selected').find('img[src="'+ url +'"]').parent('li').addClass('selected');
	}

	function resizeQuickView() {
		var quickViewLeft = ($(window).width() - $('.cd-quick-view').width())/2,
			quickViewTop = ($(window).height() - $('.cd-quick-view').height())/2;
		$('.cd-quick-view').css({
		    "top": quickViewTop,
		    "left": quickViewLeft
		});
	} 

	function closeQuickView(finalWidth, maxQuickWidth) 
	{
		var close = $('.cd-close'),
			activeSliderUrl = close.siblings('.cd-slider-wrapper').find('.selected img').attr('src'),
			selectedImage = $('.empty-box').find('img');
		//update the image in the gallery
		if( !$('.cd-quick-view').hasClass('velocity-animating') && $('.cd-quick-view').hasClass('add-content')) 
		{
			//selectedImage.attr('src', activeSliderUrl);
			animateQuickView(selectedImage, finalWidth, maxQuickWidth, 'close');
		} 
		else 
		{
			closeNoAnimation(selectedImage, finalWidth, maxQuickWidth);
		}
	}

	function animateQuickView(image, finalWidth, maxQuickWidth, animationType,title,content,srcpath) 
	{
		if( animationType == 'open') 
		{
		var pic = document.getElementById("imginfo");
		pic.src = srcpath;
		$('#title').text(title); 
		$("#content").text(content);
		
		//store some image data (width, top position, ...)
		//store window data to calculate quick view panel position
		  parentListItem = image.parent('.cd-item'),
			topSelected = image.offset().top - $(window).scrollTop(),
			leftSelected = image.offset().left,
			widthSelected = image.width(),
			heightSelected = image.height(),
			windowWidth = $(window).width(),
			windowHeight = $(window).height(),
			finalLeft = (windowWidth - finalWidth)/2,
			finalHeight = finalWidth * heightSelected/widthSelected,
			finalTop = (windowHeight - finalHeight)/2,
			quickViewWidth = ( windowWidth * .8 < maxQuickWidth ) ? windowWidth * .8 : maxQuickWidth ,
			quickViewLeft = (windowWidth - quickViewWidth)/2;

	
			//hide the image in the gallery
			parentListItem.addClass('empty-box');
			//place the quick view over the image gallery and give it the dimension of the gallery image
			$('.cd-quick-view').css({
			    "top": topSelected,
			    "left": leftSelected,
			    "width": widthSelected
			}).velocity({
				//animate the quick view: animate its width and center it in the viewport
				//during this animation, only the slider image is visible
			    'top': finalTop+ 'px',
			    'left': finalLeft+'px',
			    'width': finalWidth+'px'
			}, 1000, [ 400, 20 ], function(){
				//animate the quick view: animate its width to the final value
				$('.cd-quick-view').addClass('animate-width').velocity({
					'left': quickViewLeft+'px',
			    	'width': quickViewWidth+'px'
				}, 300, 'ease' ,function(){
					//show quick view content
					$('.cd-quick-view').addClass('add-content');
				});
			}).addClass('is-visible');
		} 
		else 
		{
			//close the quick view reverting the animation
			$('.cd-quick-view').removeClass('add-content').velocity({
			    'top': finalTop+ 'px',
			    'left': finalLeft+'px',
			    'width': finalWidth+'px'
			}, 300, 'ease', function()
			{
				$('body').removeClass('overlay-layer');
				$('.cd-quick-view').removeClass('animate-width').velocity({
					"top": topSelected,
				    "left": leftSelected,
				    "width": widthSelected
				}, 500, 'ease', function(){
					$('.cd-quick-view').removeClass('is-visible');
					parentListItem.removeClass('empty-box');
				});
			});
		}
	}
	
	function closeNoAnimation(image, finalWidth, maxQuickWidth) {
		var parentListItem = image.parent('.cd-item'),
			topSelected = image.offset().top - $(window).scrollTop(),
			leftSelected = image.offset().left,
			widthSelected = image.width();

		//close the quick view reverting the animation
		$('body').removeClass('overlay-layer');
		parentListItem.removeClass('empty-box');
		$('.cd-quick-view').velocity("stop").removeClass('add-content animate-width is-visible').css({
			"top": topSelected,
		    "left": leftSelected,
		    "width": widthSelected
		});
	}
});