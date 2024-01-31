const headerNode = document.querySelector('.header');
const media_web  = window.matchMedia("screen and (min-width: 1201px)");
const media_tablet  = window.matchMedia("screen and (max-width: 1200px)");
const media_only_tablet  = window.matchMedia("screen and (max-width: 1200px) and (min-width:769px)");
const media_mobile  = window.matchMedia("screen and (max-width: 768px)");
const nav = document.getElementById("navigation");
const m_nav_btn = document.querySelector('.nav_btn_wrap .btn');

const web_nav_over = () => {
  let gnb_bg = document.querySelector("#navigation .gnb_bg");
  nav.classList.add("on");
  gnb_bg.style.cssText = "border-bottom: 1px solid #D3D3D3";
}
const web_nav_out = () =>{
  let gnb_bg = document.querySelector("#navigation .gnb_bg");
  nav.classList.remove("on");
  gnb_bg.style.cssText = "border-bottom: 0";
}

const mobile_header_on = () => {
  if(headerNode.classList.contains('mobile_on')){
    headerNode.classList.remove('mobile_on');
  }else{
    headerNode.classList.add('mobile_on');
  }
}
const mobile_depth_on = (use) =>{
  if(headerNode.classList.contains('mobile_header')){
    if(use.classList.contains('on')){
      use.classList.remove('on');
    }else{
      use.classList.add('on');
    }
  }
}

const mobile_header_off = () => {
  headerNode.classList.remove('mobile_on');
  document.querySelectorAll('.gnb-depth-01').forEach((depth01)=>{
    depth01.classList.remove('on');
  })
}

const smHeaderOn = () => {
  headerNode.classList.add('mobile_header');
}
const smHeaderOff = () => {
  headerNode.classList.remove('mobile_header');
}

function headerEvent(){
  var winWidth = window.innerWidth;
  if(winWidth > 1200){
    /* PC 처리 */
    smHeaderOff();
    mobile_header_off();
    nav.addEventListener('mouseover',web_nav_over);
    nav.addEventListener('mouseout',web_nav_out);
  }else if(winWidth > 768){
    /* 태블릿 처리 */
    smHeaderOff();
    mobile_header_off();
    nav.addEventListener('mouseover',web_nav_over);
    nav.addEventListener('mouseout',web_nav_out);
  }else if(winWidth <= 768){
    /* 모바일 처리 */
    smHeaderOn();
    nav.removeEventListener('mouseover',web_nav_over);
    nav.removeEventListener('mouseout',web_nav_out);
    m_nav_btn.addEventListener('click',mobile_header_on);
    document.querySelectorAll('.gnb-depth-01').forEach((depth01)=>{
      depth01.addEventListener('click',function(){
        mobile_depth_on(this);
      });
    })
  }
}


headerEvent();
window.onresize = () => {
  setTimeout(function(){headerEvent()},300);
}


