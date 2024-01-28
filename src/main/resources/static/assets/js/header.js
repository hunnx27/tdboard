const headerNode = document.getElementsByClassName('.header')[0];
const media_web  = window.matchMedia("screen and (min-width: 1201px)");
const media_tablet  = window.matchMedia("screen and (max-width: 1200px)");
const media_only_tablet  = window.matchMedia("screen and (max-width: 1200px) and (min-width:769px)");
const media_mobile  = window.matchMedia("screen and (max-width: 768px)");
const nav = document.getElementById("navigation");
const web_nav_over = () => {
  let navBody = document.getElementById("navigation");
  let gnb_bg = document.querySelector("#navigation .gnb_bg");
  nav.classList.add("on");
  gnb_bg.style.cssText = "border-bottom: 1px solid #D3D3D3";
}
const web_nav_out = () =>{
  let navBody = document.getElementById("navigation");
  let gnb_bg = document.querySelector("#navigation .gnb_bg");
  nav.classList.remove("on");
  gnb_bg.style.cssText = "border-bottom: 0";
}
const web_headerEventInit = () =>{
  nav.classList.remove("on");
}
function headerEvent(){
  var winWidth = window.innerWidth;
  
  if(winWidth > 1200){
    nav.addEventListener('mouseover',web_nav_over);
    nav.addEventListener('mouseout',web_nav_out);
  }else if(winWidth > 768){

  }else if(winWidth <= 768){
    web_headerEventInit();
    nav.removeEventListener('mouseover',web_nav_over);
    nav.removeEventListener('mouseout',web_nav_out);
  }
  
}


headerEvent();
window.onresize = () => {
  setTimeout(function(){headerEvent()},300);
}


