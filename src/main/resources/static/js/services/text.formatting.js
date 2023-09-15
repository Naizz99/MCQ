var selectedText = "";
var preActiveEl = "";
var activeEl = "";
var selectedParagraph = "";

function getSelectionText() {
    var text = "";
    activeEl = document.activeElement;
    var activeElTagName = activeEl ? activeEl.tagName.toLowerCase() : null;
    if (
      (activeElTagName == "textarea") || (activeElTagName == "input" &&
      /^(?:text|search|password|tel|url)$/i.test(activeEl.type)) &&
      (typeof activeEl.selectionStart == "number")
    ) {
        text = activeEl.value.slice(activeEl.selectionStart, activeEl.selectionEnd);
    } else if (window.getSelection) {
        text = window.getSelection().toString();
    }
    return text;
   
}


document.onmouseup = document.onkeyup = document.onselectionchange = function() {
	
	if(getSelectionText() != ""){
		selectedText = getSelectionText();
		preActiveEl = activeEl;
	}
};

function selectParagraph(id){
	selectedParagraph = document.getElementById(id);
}

function bold1(){
	let result = selectedText.bold();	
	console.log(selectedParagraph.innerText.slice(0,preActiveEl.selectionStart) + " | " + selectedParagraph.innerText.slice(preActiveEl.selectionEnd,selectedParagraph.innerText.length));
	//var newtext = selectedParagraph.innerText.slice(0,preActiveEl.selectionStart) + result + document.getElementById(selectedParagraph).innerText.slice(preActiveEl.selectionEnd,10);
	selectedParagraph.innerHTML = result;
	
}
