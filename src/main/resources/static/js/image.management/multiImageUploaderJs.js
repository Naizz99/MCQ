jQuery(document).ready(function ($) {
	jQuery("#crop").cropzee();
});

var defaultWidth = 210;
var selectedCanvas = null;
var selectedCropzee = null;
var imageDataInput = null;
var extention = null;

function selectQuestion(id){
	selectedCanvas = "cropzee-hidden-canvas" + id;
	selectedCropzee = "crop" + id;
	imageDataInput = "image-data" + id;
	extention = "extension" + id;
		
	//alert(document.getElementById(selectedCanvas).value);
	//alert(document.getElementById(selectedCanvas).toDataURL('image/jpeg'));
	
	document.getElementById("crop").click();
}
// used jQuery.fn.extend() method to provide new methods that can be chained to the jQuery() function
// in our case - $(element).cropzee()
jQuery.fn.extend({
	cropzee: function (options = {
	// croppr.js options
	// see https://jamesooi.design/Croppr.js/
	aspectRatio: null,
			maxSize: null,
			minSize: null,
			startSize: [100, 100, '%'],
			onCropStart: null,
			onCropMove: null,
			onCropEnd: null,
			onInitialize: null,
			// lightmodal options
			// see https://hunzaboy.github.io/Light-Modal/#
			modalAnimation: '',
			// cropzee options
			allowedInputs: ['jpg'], // input extensions supported
			imageExtension: 'image/jpeg', // cropped image/blob file-type 'image/jpeg' | 'image/png' | any other supported by browser
			returnImageMode: 'data-url', // image data mode, 'blob' for blob object or 'data-url' for dataURL
	}) {
		if (options.aspectRatio <= 0) {
			options.aspectRatio = null;
		}
		if (!options.allowedInputs) {
			options.allowedInputs = ['jpg', 'jpeg'];
		}
		if (!options.imageExtension) {
			options.imageExtension = 'image/jpeg';
		}
		if (!options.returnImageMode) {
			options.returnImageMode = 'data-url';
		}
		// function to reset input (value) of input, taking in input id
		// resets input value of cropzee input type=file so that same file can be selected twice
		function resetFileInput(id) {
			$('#' + id).val(null);
		}
		
		window.cropzeeGetImage = function (id) {
			return cropzeeReturnImage[id];
		};
		window.cropzeeRotateImage = function (degree) {
			var image = document.getElementsByClassName('croppr-image')[0];
			var imageClipped = document.getElementsByClassName('croppr-imageClipped')[0];
			var rotateValue = document.getElementById('upload-photo-image-rotate-value');
			var rotateValueText = document.getElementById('upload-photo-image-rotate-value-text');
			
			image.style.transform = 'rotate(' + degree + 'deg)';
			imageClipped.style.transform = 'rotate(' + degree + 'deg)';
			rotateValue.value = degree;
			rotateValueText.value = degree.replace('.', ',') + '°';
		};
		// function to crop the modal-image and display it on the hidden canvas and other dynamic canvases (previewers)
		window.cropzeeCreateImage = function (id) {
			var rotateValueEl = document.getElementById('upload-photo-image-rotate-value');
			var rotateValueRad = rotateValueEl.value * (Math.PI / 180);
			// get croppr.js dimensions
			var dimensions = cropzeeCroppr.getValue();
			// get hidden canvas and draw cropped image onto it
			var img = document.getElementsByClassName('croppr-image')[0];
			//var width = 210;
			//var ratio = img.height / img.width;
			//var height = width * ratio;
			var canvas = document.getElementById(selectedCanvas);
			var x = dimensions.width / 2;
			var y = dimensions.height / 2;
			var ctx = canvas.getContext('2d');

			ctx.canvas.width = dimensions.width;
			ctx.canvas.height = dimensions.height;

			ctx.save();

			ctx.translate(dimensions.width / 2, dimensions.height / 2);
			ctx.rotate(rotateValueRad);

			ctx.translate(-dimensions.width / 2, -dimensions.height / 2);
			ctx.drawImage(img, dimensions.x, dimensions.y, dimensions.width, dimensions.height, 0, 0, dimensions.width, dimensions.height);

			ctx.restore();
			// store image data as blob or dataURL for retrieval
			if (options.returnImageMode == 'blob') {
				canvas.toBlob(function (blob) {
					window.cropzeeReturnImage = [];
					cropzeeReturnImage[id] = blob;
				}, options.imageExtension);
			} else {
				window.cropzeeReturnImage = [];
				
				cropzeeReturnImage[id] = canvas.toDataURL(options.imageExtension);
				document.getElementById(imageDataInput).value = canvas.toDataURL(options.imageExtension);
				document.getElementById(extention).value = document.getElementById("crop").value.split('.').pop();
			}

			// cropping finished, close modal
			closeModal();
		};
		// function to initialize croppr.js on the image inside modal
		// returnMode option is not supported in cropzee
		// see https://jamesooi.design/Croppr.js/
		function cropzeeTriggerCroppr() {
			window.cropzeeCroppr = new Croppr('#cropzee-modal-image', {
				aspectRatio: options.aspectRatio,
				maxSize: options.maxSize,
				minSize: options.minSize,
				startSize: options.startSize,
				onCropStart: options.onCropStart,
				onCropMove: options.onCropMove,
				onCropEnd: options.onCropEnd,
				onInitialize: options.onInitialize
			});
		}
		// function to trigger modal and pass image data to display in the modal
		// function takes in input id and image (to be cropped) data
		function cropzeeTriggerModal(id, src) {
			// take in animation option and add 'animated' before it
			var animation = options.modalAnimation;
			if (animation) {
				if (animation.indexOf('animated') == -1) {
					animation = 'animated ' + animation;
				}
			}
			// modal element with dynamic image data, dynamic animation class as supported by animate.css and dynamic input id
			// lightmodal see https://hunzaboy.github.io/Light-Modal/#
			var lightmodalHTML =
					'<div class="light-modal" id="cropzee-modal" role="dialog" aria-labelledby="light-modal-label" aria-hidden="false" data-lightmodal="close">'
					+ '<div class="light-modal-content ' + animation + '">'
					+ '<!-- light modal header -->'
					+ '<!-- <div class="light-modal-header">'
					+ '<h3 class="light-modal-heading">Cropzee</h3>'
					+ '<a href="#" class="light-modal-close-icon" aria-label="close">&times;</a>'
					+ '</div> -->'
					+ '<!-- light modal body -->'
					+ '<div class="light-modal-body" style="max-height: 500px;">'
					+ '<img id="cropzee-modal-image" src="' + src + '" style="display:none;">'
					+ '</div>'
					+ '<!-- light modal footer -->'
					+ '<div class="light-modal-footer" style="justify-content: space-between;">'
					+ '<div onclick="closeModal()" class="button btn" aria-label="close">Cancel</div>'
					+ '<input id="upload-photo-image-rotate-value" type="hidden" name="rangeInputValue" value="0">'
					+ '<input id="upload-photo-image-rotate-value-text" class="image-rotate-value" type="text" name="rangeInputValueText" value="0°" readonly>'
					+ '<input oninput="cropzeeRotateImage(this.value)" onchange="cropzeeRotateImage(this.value)" id="upload-photo-image-rotate" class="image-rotate" type="range" name="rangeInput" step="1" min="-90" max="90" value="0">'
					+ '<div onclick="cropzeeCreateImage(`' + id + '`);" class="button btn">Done</div>'
					+ '</div>'
					+ '</div>'
					+ '<canvas style="position: absolute; top: -99999px; left: -99999px;" id="' + selectedCanvas + '"></canvas>'
					+ '<a style="display:none;" id="cropzee-link"></a>'
					+ '</div>';
			// modal element is appended to body
			$("body").append(lightmodalHTML);
			// after which the inserted image is drawn onto the hidden canvas within the modal
			setTimeout(function () {
				var canvas = document.getElementById(selectedCanvas);
				var ctx = canvas.getContext('2d');
				ctx.canvas.width = cropzeeCanvasWidth;
				ctx.canvas.height = cropzeeCanvasHeight;
				var img = new Image();
				img.src = src;
				ctx.drawImage(img, 0, 0, cropzeeCanvasWidth, cropzeeCanvasHeight);
				setTimeout(function () {
					// the css-only modal is called via href see https://hunzaboy.github.io/Light-Modal/#
					window.location = window.location.pathname + "#cropzee-modal";
					// function to trigger croppr.js on picture in modal
					cropzeeTriggerCroppr();
				}, 50);
			}, 50);
		}
		// function to capture input and insert it into various elements for previewing and display
		function cropzeeReadURL(input, id) {
			if (input.files && input.files[0]) {
				
				var reader = new FileReader();
	
				reader.onload = function (e) {
					
					if(id != null){
						window.cropzeePreviewersLength = $('[data-cropzee="' + id + '"]').length;	
					}else{
						window.cropzeePreviewersLength = 1;
					}
					
					window.cropzeePreviewCanvas = [];
					window.cropzeePreviewCanvasContext = [];
					if (cropzeePreviewersLength) {
						for (let i = 0; i < cropzeePreviewersLength; i++) {
							var img = new Image();
							img.onload = function () {
								var ratio = img.height / img.width;
								img.width = 210;
								cropzeePreviewCanvas[i] = $('[data-cropzee="' + id + '"]')[i];
								cropzeePreviewCanvasContext[i] = cropzeePreviewCanvas[i].getContext('2d');
								cropzeePreviewCanvasContext[i].canvas.width = $(cropzeePreviewCanvas[i]).width() || (defaultWidth + increment);
								window.cropzeeCanvasWidth = $(cropzeePreviewCanvas[i]).width() || (defaultWidth + increment);
								cropzeePreviewCanvasContext[i].canvas.height = cropzeePreviewCanvasContext[i].canvas.width * ratio;
								window.cropzeeCanvasHeight = $(cropzeePreviewCanvas[i]).height() || 'auto';
								cropzeePreviewCanvasContext[i].drawImage(img, 0, 0, cropzeeCanvasWidth, cropzeeCanvasHeight);
							};
							document.getElementById(imageDataInput).value = e.target.result;
							document.getElementById(extention).value = document.getElementById("crop").value.split('.').pop();
							img.src = e.target.result;
						}
					}
					cropzeeTriggerModal(id, e.target.result);
				};
				reader.readAsDataURL(input.files[0]);
			}
		}
		// function to close modal when user clicks outside modal
		$(document).click(function (e) {
			if ($(e.target).is('#cropzee-modal')) {
				closeModal();
			}
		});
		// function that is called first, when input is triggered
		// it resets input value to enable the reloading of the same image just in case
		$(this).click(function () {
			var cropzeeInputId = $(this).attr('id');
			resetFileInput(selectedCanvas);
			// when image is selected, the image-previewers are transformed to canvases
			// then the input data is passed to be read for previewing
			$(this).one("change", function () {
				var ext = $('#' + cropzeeInputId).val().split('.').pop().toLowerCase();
				if ($.inArray(ext, options.allowedInputs) == -1) {
					alert('invalid extension! Please check your input file and try again.');
					resetFileInput(selectedCanvas);
				} else {
					var previewerId = $('[data-cropzee="' + selectedCropzee + '"]').attr("id");
					var previewerClass = $('[data-cropzee="' + selectedCropzee + '"]').attr("class");
					$('[data-cropzee="' + selectedCropzee + '"]').replaceWith('<canvas id="' + previewerId + '" class="' + previewerClass + '" data-cropzee="' + selectedCropzee + '"></canvas>');
					// input data is passed to be read for previewing
					// function takes in input object and its id
					cropzeeReadURL(this, selectedCropzee);	
				}
			});
		});
	}
});
// function to close modal
function closeModal() {
	$('#cropzee-modal').remove();
	//window.location = window.location.pathname + '#';
}


