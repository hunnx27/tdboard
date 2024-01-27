const headerNode = document.getElementsByClassName('.header')[0];
const media_web  = window.matchMedia("screen and (min-width: 1201px)");
const media_tablet  = window.matchMedia("screen and (max-width: 568px)");
const media_only_tablet  = window.matchMedia("screen and (max-width: 1200px) and (min-width:568px)");
const media_mobile  = window.matchMedia("screen and (max-width: 568px)");

var winWidth = window.innerWidth;
console.log(winWidth);

window.onload = function () {
  
}
function inithead(){

}

if(media_web.matches){
  var nav = document.getElementById("navigation");
  nav.onmouseenter = () =>{
      console.log('on mouseenter')
      nav.classList.add("on")
      // borderBottom 생성
      gnb_bg = document.querySelector("#navigation .gnb_bg");
      gnb_bg.style.cssText = "border-bottom: 1px solid #D3D3D3";

  };
  nav.onmouseleave = () =>{
      console.log('on mouseleave')
      nav.classList.remove("on")
      // borderBottom 삭제
      gnb_bg = document.querySelector("#navigation .gnb_bg");
      gnb_bg.style.cssText = "border-bottom: 0";
  };
}

/* only tablet */
if(media_only_tablet.matches){

}
if(media_mobile.matches){

}


