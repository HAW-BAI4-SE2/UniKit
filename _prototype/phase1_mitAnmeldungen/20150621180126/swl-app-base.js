/*
 * Some global stuff 
 * variable popUpWin for opening a popup Window
 */

var popUpWin;

/*
 * Function openWin opens a popup window centered in the parent window.
 * Prams:
 *  url:	the page that should be displayed in the popup window
 *  width:	width of the window
 *	height:	height of the window
 *	name:	the name of the popup window (important if multiple windows are on clients desktop)
 */

function openWin(url,width,height,name)
{
	if (popUpWin && popUpWin.closed!=true) popUpWin.close();
	var posx = screen.width/2 - width/2;		
	var posy = screen.height/2 - height/2;
	var propsZ=(document.all)?('top=' + posy + ',left=' + posx):('screenX=' + posx + ',screenY=' + posy);
	props = 'width=' + width + ',height=' + height + ',' + propsZ;
	popUpWin = window.open('',name,props);
	popUpWin.location.href = url;
}

function show(objName)
{
	//var ob_show = new Object();objName + '_show';
	//var ob_hide = objName + '_hide';
	if (document.all){
		document.all.first_part_show.style.visibility = "visible";
		document.all.first_part_hide.style.visibility = "hidden";
	}
	//else if{
	//	(document.documentElement) document.getElementById('first_part_show').innerHTML = rAn;
	//}
	//else if (document.layers){
	//	document.layers.first_part_show.document.open();
	//	document.layers.first_part_show.document.write(rAn);
	//	document.layers.first_part_show.document.close();
	//}
}

function hide(objName)
{
	//var ob_show = objName + '_show';
	//var ob_hide = objName + '_hide';
	if (document.all){
		document.all.first_part_show.style.visibility = "hidden";
		document.all.first_part_hide.style.visibility = "visible";
	}
	//else if{
	//	(document.documentElement) document.getElementById('first_part_show').innerHTML = rAn;
	//}
	//else if (document.layers){
	//	alert('b');
	//	document.layers.first_part_show.document.open();
	//	document.layers.first_part_hide.document.write(rAn);
	//	document.layers.first_part_show.document.close();
	//}
}

function submitChooserForm(){
 	document.forms.ChooserForm.submit();
}

function submitNewChooserForm(x){
	document.typform.newType.value=x.toString();
 	document.typform.submit();
}

function submitNeueVertiefung(vertiefung){
	document.vertief.vertiefung.value=vertiefung.toString();
 	document.vertief.submit();
}

function submitVertiefungsFaecher(){
 	document.neu.submit();
}

function submitListenLehrveranstaltung(){
 	
 	document.listenLeherveranstaltungButton.submit()
}